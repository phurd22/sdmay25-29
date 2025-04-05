#include <BLEDevice.h>
#include <BLEUtils.h>
#include <BLEServer.h>

#define SERVICE_UUID        "a5f08588-fdb1-4785-b1aa-c21acec22158"
#define CHARACTERISTIC_UUID "9317847f-cc24-4005-bb74-78b06b9757b0"

String longToBinaryString(long long num);
void parseBluetoothData(String data);

const int bitPins[4] = {1, 2, 42, 41}; // Pins where bits are output
long long parsedNumbers[4]; // Array to store parsed long values
String binaryStrings[4] = {"", "", "", ""}; // String array that holds 50 but numbers
int newData = 0;

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

        // Reset buffer
        receivedData = "";
      }
      
    }
  }
};

void setup() {
  Serial.begin(115200);

  for (int i = 0; i < 4; i++) {
    pinMode(bitPins[i], OUTPUT);
  }

  // Setup for BLE
  BLEDevice::init("Base2ReceiverESP32");
  BLEDevice::setMTU(100);
  BLEServer *pServer = BLEDevice::createServer();
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
  if (newData) {
    newData = 0;
    for (int i = 49; i >= 0; i--) {
      for (int j = 0; j < 4; j++) {
        if (binaryStrings[j][i] == '1') {
          digitalWrite(bitPins[j], HIGH);
        }
        else {
          digitalWrite(bitPins[j], LOW);
        }
      }
      delay(250);
    }
    for (int i = 0; i < 4; i++) {
      digitalWrite(bitPins[i], LOW);
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
  newData = 1;
}