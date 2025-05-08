#include <BLEDevice.h>
#include <BLEUtils.h>
#include <BLEServer.h>

#define SERVICE_UUID        "4fafc201-1fb5-459e-8fcc-c5c9c331914b"
#define CHARACTERISTIC_UUID "beb5483e-36e1-4688-b7f5-ea07361b26a8"

// Define the pins where the Bits are connected
const int bitInputPins[4][5] = {
  {39, 40, 41, 42, 2}, // Group 1
  {0, 35, 36, 37, 38}, // Group 2
  {19, 21, 47, 48, 45},   // Group 3 // changed 20 to 19
  {15, 7, 6, 5, 4}  // Group 4
};

String numbers[4] = {"0", "0", "0", "0"}; // Default values
String receivedData = ""; // Buffer for incoming data
const int numberLength = 15;
const int goPin = 14;
const int counterFourBitPins[4] = {10, 11, 12, 13};  // b3, b2, b1, b0
int counterValue = 0;
int unofficialCounter = 0;
int newData = 0;
int displaying = 0;
int initialNum = 1;
int endOfCycle = 0;
int skipLoop = 0;

void parseBluetoothData(String data);
void setSignBits();
void clearSignBits();
void padData();
void clearNumBits();
void displayDigit(int group, int digit);
int readInputCounter();

// Callback class for handling incoming BLE writes
class MyCallbacks : public BLECharacteristicCallbacks {
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

void setup() {
  Serial.begin(115200);

  for (int i = 0; i < 4; i++) {
    for (int g = 0; g < 5; g++) {
      pinMode(bitInputPins[i][g], OUTPUT);
    }
  }
  for (int i = 0; i < 4; i++) {
    pinMode(counterFourBitPins[i], INPUT);
  }
  pinMode(goPin, INPUT);
  clearNumBits();
  clearSignBits();

  // Setup for BLE
  BLEDevice::init("Base10ESP32");
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
  unofficialCounter = readInputCounter();

  // counter goes 14 to 0 (it actually does 15 to 0 but should be 14)
  skipLoop = 0;
  if (counterValue != 0 && counterValue != unofficialCounter + 1) {
    skipLoop = 1;
  }
  if (counterValue == 0 && unofficialCounter != 15) {
    skipLoop = 1;
  }

  // Skip the loop if getting erroneous read from inputs
  // Also, only perform loop if on next counter value
  if (!skipLoop) {
    counterValue = unofficialCounter;
    Serial.print("Counter: ");
    Serial.println(counterValue);

    // Reset when cycle is finished
    if (endOfCycle && counterValue == 15) {
      endOfCycle = 0;
      displaying = 0;
      clearNumBits();
      clearSignBits();
    }

    // Set boolean variables when at start of output cycle
    if (digitalRead(goPin) == HIGH && counterValue == 14 && newData) {
      displaying = 1;
      newData = 0;
    }

    // At start of cycle display first digit
    if (counterValue == 14 && displaying) {
      setSignBits();
      padData();

      for (int i = 0; i < 4; i++) {
        displayDigit(i, numbers[i][14 - counterValue]);
      }
    }

    // On next counter value display new digit
    if (displaying && counterValue != 14) {
      clearNumBits();

      for (int i = 0; i < 4; i++) {
        displayDigit(i, numbers[i][14 - counterValue]);
      }

      if (counterValue == 0) {
        endOfCycle = 1;
      }
    }
  }
}

// Takes the 4-bit counter input and returns as int
int readInputCounter() {
  int value = (digitalRead(counterFourBitPins[0]) << 3) | (digitalRead(counterFourBitPins[1]) << 2) | 
         (digitalRead(counterFourBitPins[2]) << 1) | (digitalRead(counterFourBitPins[3]));
  return value;
}

// Parse received data into numbers String array
void parseBluetoothData(String data) {
  int index = 0;
  int startPos = 0;
  while (index < 4 && startPos < data.length()) {
    int endPos = data.indexOf('x', startPos);
    if (endPos == -1) { // Last number
      endPos = data.length(); 
    }  
    numbers[index] = data.substring(startPos, endPos);
    startPos = endPos + 1;
    if (index == 4 && numbers[index].endsWith("d")) {  // Remove trailing 'd'
      numbers[index].remove(numbers[index].length() - 1);
    }
    index++;
  }

  Serial.println("Updated Numbers:");
  for (int i = 0; i < 4; i++) {
    Serial.println(numbers[i]);
  }
}

// Add 0s to front of numbers Strings until they are 'numberLength' long
void padData() {
  for (int i = 0; i < 4; i++) {
    while (numbers[i].length() < numberLength) {
      numbers[i] = "0" + numbers[i];
    }
  }
  Serial.println("Padded Numbers:");
  for (int i = 0; i < 4; i++) {
    Serial.println(numbers[i]);
  }
  Serial.println();
}

// Write the specified to the output pins speecified by pin group number
void displayDigit(int group, int digit) {
  int value = digit - '0';  // Convert char to integer

  for (int i = 0; i < 4; i++) {
    digitalWrite(bitInputPins[group][i], (value >> i) & 1);
  }
}

// Write to all 4 sign bits
void setSignBits() {
  for (int i = 0; i < 4; i++) {
    if (numbers[i].charAt(0) == '-') {
      numbers[i].remove(0, 1);
      digitalWrite(bitInputPins[i][4], HIGH);
    }
    else {
      digitalWrite(bitInputPins[i][4], LOW);
    }
  }
}

// Clear all the digit bit outputs
void clearNumBits() {
  for (int i = 0; i < 4; i++) {
    for (int g = 0; g < 4; g++) {
      digitalWrite(bitInputPins[i][g], LOW);
    }
  }
}

// Clear sign bit outputs
void clearSignBits() {
  for (int i = 0; i < 4; i++) {
    digitalWrite(bitInputPins[i][4], LOW);
  }
}