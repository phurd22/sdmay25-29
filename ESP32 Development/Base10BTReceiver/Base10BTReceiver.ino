#include <BLEDevice.h>
#include <BLEUtils.h>
#include <BLEServer.h>

#define SERVICE_UUID        "4fafc201-1fb5-459e-8fcc-c5c9c331914b"
#define CHARACTERISTIC_UUID "beb5483e-36e1-4688-b7f5-ea07361b26a8"

// Define the pins where the Bits are connected
const int bitInputPins[5][5] = {
  {39, 40, 41, 42, 2}, // Group 1
  {0, 35, 36, 37, 38}, // Group 2
  {20, 21, 47, 48, 45},   // Group 3
  {15, 7, 6, 5, 4},  // Group 4
  {3, 8, 18, 17, 16}   // Group 5
};

const int buttonPin = 13;

String numbers[5] = {"0", "0", "0", "0", "0"}; // Default values
String receivedData = ""; // Buffer for incoming data
const int numberLength = 15;
bool buttonPressed;

void parseBluetoothData(String data);
void setSignBits();
void clearSignBits();
void padData();
void clearNumBits();
void displayDigit(int group, int digit);

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

        // Reset buffer
        receivedData = "";
      }
      
    }
  }
};

void setup() {
  Serial.begin(115200);

  buttonPressed = false;
  for (int i = 0; i < 5; i++) {
    for (int g = 0; g < 5; g++) {
      pinMode(bitInputPins[i][g], OUTPUT);
    }
  }
  pinMode(buttonPin, INPUT);

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
  clearNumBits();
  clearSignBits();

  if (digitalRead(buttonPin) == HIGH) {
    buttonPressed = true;
  }

  if (buttonPressed) {
    setSignBits();
    padData();
    for (int pos = 0; pos < numberLength; pos++) {
      clearNumBits();
      for (int i = 0; i < 5; i++) {
        displayDigit(i, numbers[i][pos]);
      }
      delay(500);
    }
    buttonPressed = false;
  }
}

void parseBluetoothData(String data) {
  int index = 0;
  int startPos = 0;
  while (index < 5 && startPos < data.length()) {
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
  for (int i = 0; i < 5; i++) {
    Serial.println(numbers[i]);
  }
}

void padData() {
  for (int i = 0; i < 5; i++) {
    while (numbers[i].length() < numberLength) {
      numbers[i] = "0" + numbers[i];
    }
  }
  Serial.println("Padded Numbers:");
  for (int i = 0; i < 5; i++) {
    Serial.println(numbers[i]);
  }
  Serial.println();
}

void displayDigit(int group, int digit) {
  int value = digit - '0';  // Convert char to integer

  for (int i = 0; i < 4; i++) {
    digitalWrite(bitInputPins[group][i], (value >> i) & 1);
  }
}

void clearNumBits() {
  for (int i = 0; i < 5; i++) {
    for (int g = 0; g < 4; g++) {
      digitalWrite(bitInputPins[i][g], LOW);
    }
  }
}

void setSignBits() {
  for (int i = 0; i < 5; i++) {
    if (numbers[i].charAt(0) == '-') {
      numbers[i].remove(0, 1);
      digitalWrite(bitInputPins[i][4], HIGH);
    }
    else {
      digitalWrite(bitInputPins[i][4], LOW);
    }
  }
}

void clearSignBits() {
  for (int i = 0; i < 5; i++) {
    digitalWrite(bitInputPins[i][4], LOW);
  }
}