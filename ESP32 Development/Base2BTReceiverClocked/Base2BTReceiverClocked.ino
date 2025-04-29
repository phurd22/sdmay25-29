#include <BLEDevice.h>
#include <BLEUtils.h>
#include <BLEServer.h>

#define SERVICE_UUID        "a5f08588-fdb1-4785-b1aa-c21acec22158"
#define CHARACTERISTIC_UUID "9317847f-cc24-4005-bb74-78b06b9757b0"

const int bitPins[4] = {1, 2, 42, 41}; // Pins where bits are output
const int goPin = 14;
const int counterSixBitPins[6] = {46, 9, 10, 11, 12, 13};
long long parsedNumbers[4]; // Array to store parsed long values
String binaryStrings[4] = {"", "", "", ""}; // String array that holds 50 but numbers
int newData = 0;
int skipLoop = 0;
int counterValue = 63; // TODO: change to 59 when circuit setup
int displaying = 0;
int endOfCycle = 0;

BLEServer *pServer = nullptr;

String longToBinaryString(long long num);
void parseBluetoothData(String data);
int readInputCounter();

// Callback class for handling incoming BLE writes
class MyCallbacks : public BLECharacteristicCallbacks {
  String receivedData = ""; // Buffer for incoming data
  void onWrite(BLECharacteristic *pCharacteristic) {
    String rxValue = pCharacteristic->getValue();

    if (rxValue.length() > 0) {
      receivedData += rxValue;

      // Check if 'd' (end of message) is received
      if (receivedData.indexOf('d') != -1) {
        Serial.println("Received: " + receivedData); // Print the received value
        parseBluetoothData(receivedData);
        newData = 1;

        // Reset buffer
        receivedData = "";
      }
      
    }
  }
};

class MyServerCallbacks : public BLEServerCallbacks {
  void onConnect(BLEServer* pServer) {
    Serial.println("Client connected");
  }

  void onDisconnect(BLEServer* pServer) {
    Serial.println("Client disconnected, restarting advertising...");
    BLEDevice::startAdvertising(); // Restart advertising so Android can reconnect
  }
};

void setup() {
  Serial.begin(115200);

  for (int i = 0; i < 4; i++) {
    pinMode(bitPins[i], OUTPUT);
    digitalWrite(bitPins[i], LOW);
  }
  for (int i = 0; i < 6; i++) {
    pinMode(counterSixBitPins[i], INPUT);
  }
  pinMode(goPin, INPUT);

  // Setup for BLE
  BLEDevice::init("Base2ReceiverESP32");
  BLEDevice::setMTU(100);
  pServer = BLEDevice::createServer();
  pServer->setCallbacks(new MyServerCallbacks());
  BLEService *pService = pServer->createService(SERVICE_UUID);
  BLECharacteristic *pCharacteristic =
    pService->createCharacteristic(CHARACTERISTIC_UUID, BLECharacteristic::PROPERTY_READ | BLECharacteristic::PROPERTY_WRITE);
  pCharacteristic->setCallbacks(new MyCallbacks()); // Attach the write callback to handle incoming data
  pService->start();
  BLEAdvertising *pAdvertising = BLEDevice::getAdvertising();
  pAdvertising->addServiceUUID(SERVICE_UUID);
  pAdvertising->setScanResponse(true);
  BLEDevice::startAdvertising();

  Serial.println("BLE is ready. Waiting for data...");
}

void loop() {
  // counter goes 0 to 63
  skipLoop = 0;
  if (counterValue != 63 && counterValue != readInputCounter() - 1) { // TODO: will need to change 63 to 59
    skipLoop = 1;
  }
  if (counterValue == 63 && readInputCounter() != 0) { // TODO: will need to change 63 to 59
    skipLoop = 1;
  }

  // Skip the loop if getting erroneous read from inputs
  // Also, only perform the loop if on the next counter value
  if (!skipLoop) {
    counterValue = readInputCounter();
    Serial.print("Counter: ");
    Serial.println(counterValue);

    // Reset when cycle is finished
    if (endOfCycle && counterValue == 50) {
      endOfCycle = 0;
      displaying = 0;
      for (int i = 0; i < 4; i++) {
        digitalWrite(bitPins[i], LOW);
      }
    }

    // Set boolean variables when at start of output cycle
    if (digitalRead(goPin) == HIGH && counterValue == 0 && newData) {
      displaying = 1;
      newData = 0;
    }

    // Display the bit outputs
    if (displaying) {
      for (int i = 0; i < 4; i++) {
        if (binaryStrings[i][49 - counterValue] == '1') {
          digitalWrite(bitPins[i], HIGH);
        }
        else {
          digitalWrite(bitPins[i], LOW);
        }
      }
      
      if (counterValue == 49) {
        endOfCycle = 1;
      }
    }
  }
}

// Convert long to 50-character two’s complement binary string
String longToBinaryString(long long num) {
  String binaryString = "";
  for (int i = 49; i >= 0; i--) {
    binaryString += ((num >> i) & 1) ? '1' : '0';
  }
  return binaryString;
}

// Takes the 6-bit counter input and returns an int
int readInputCounter() {
  int value = (digitalRead(counterSixBitPins[0]) << 5) | (digitalRead(counterSixBitPins[1]) << 4) | 
          (digitalRead(counterSixBitPins[2]) << 3) | (digitalRead(counterSixBitPins[3]) << 2) | 
          (digitalRead(counterSixBitPins[4]) << 1) | (digitalRead(counterSixBitPins[5]));
  return value;
}

// Parse received data into an array of four long values
void parseBluetoothData(String data) {
  data.remove(data.length() - 1); // Remove trailing 'd'
  
  int firstX = data.indexOf('x');
  int secondX = data.indexOf('x', firstX + 1);
  int thirdX = data.indexOf('x', secondX + 1);
  parsedNumbers[0] = atoll(data.substring(0, firstX).c_str());
  parsedNumbers[1] = atoll(data.substring(firstX + 1, secondX).c_str());
  parsedNumbers[2] = atoll(data.substring(secondX + 1, thirdX).c_str());
  parsedNumbers[3] = atoll(data.substring(thirdX + 1).c_str());

  // Print the binary representation of each number
  for (int i = 0; i < 4; i++) {
    binaryStrings[i] = longToBinaryString(parsedNumbers[i]);
    Serial.print("Long: ");
    Serial.print(parsedNumbers[i]);
    Serial.print(" -> Binary: ");
    Serial.println(binaryStrings[i]);
  }
}