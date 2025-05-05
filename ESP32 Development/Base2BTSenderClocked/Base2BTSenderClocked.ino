#include <BLEDevice.h>
#include <BLEUtils.h>
#include <BLEServer.h>

#define SERVICE_UUID        "246ccada-310c-46a4-9881-3d2feb165646"
#define CHARACTERISTIC_UUID "9d5a6f5c-e32c-429f-9faa-8209e6f2dda4"

const int goPin = 13;
const int counterSixBitPins[6] = {3, 46, 9, 10, 11, 12};
const int dataPins[4] = {16, 17, 18, 8};
String binaryBuffer = "";
unsigned long timer = 0;
unsigned long lastSent = 0;
int reading = 0;
int skipLoop = 0;
int counterValue = 59;
int unofficialCounter = 0;
int endOfCycle = 0;

BLECharacteristic *pCharacteristic;
BLEServer *pServer = nullptr;

int readInputCounter();
void readToBuffer();

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

  for (int i = 0; i < 6; i++) {
    pinMode(counterSixBitPins[i], INPUT);
  }
  for (int i = 0; i < 4; i++) {
    pinMode(dataPins[i], INPUT);
  }
  pinMode(goPin, INPUT);

  // Setup for BLE
  BLEDevice::init("Base2SenderESP32");
  BLEDevice::setMTU(100);
  pServer = BLEDevice::createServer();
  pServer->setCallbacks(new MyServerCallbacks());
  BLEService *pService = pServer->createService(SERVICE_UUID);
  pCharacteristic = pService->createCharacteristic(
    CHARACTERISTIC_UUID,
    BLECharacteristic::PROPERTY_READ |
    BLECharacteristic::PROPERTY_WRITE |
    BLECharacteristic::PROPERTY_NOTIFY
  );
  pService->start();
  BLEAdvertising *pAdvertising = BLEDevice::getAdvertising();
  pAdvertising->addServiceUUID(SERVICE_UUID);
  pAdvertising->setScanResponse(true);
  BLEDevice::startAdvertising();

  Serial.println("BLE is ready. Waiting for data...");
}

void loop() {
  timer = millis();

  // Send data to tablet every 100 ms (delay for consistent bluetooth communication)
  if (binaryBuffer != "" && timer - lastSent > 100) {
    String toSend = "";
    int dIndex = binaryBuffer.indexOf('d');
    if (dIndex != -1) {
      toSend = binaryBuffer.substring(0, dIndex);
      binaryBuffer = binaryBuffer.substring(dIndex + 1);
      pCharacteristic->setValue(toSend.c_str());
      pCharacteristic->notify();
      Serial.print("Sent: ");
      Serial.println(toSend);
    }
    else {
      pCharacteristic->setValue(binaryBuffer.c_str());
      pCharacteristic->notify();
      Serial.print("Sent: ");
      Serial.println(binaryBuffer);
      binaryBuffer = "";
    }
    lastSent = timer;
  }

  unofficialCounter = readInputCounter();

  // counter goes 0 to 59
  skipLoop = 0;
  if (counterValue != 59 && counterValue != unofficialCounter - 1) {
    skipLoop = 1;
  }
  if (counterValue == 59 && unofficialCounter != 0) {
    skipLoop = 1;
  }

  // Skip the loop if getting erroneous read from inputs
  // Also, only perform the loop if on the next counter value
  if (!skipLoop) {
    counterValue = unofficialCounter;
    Serial.print("Counter: ");
    Serial.println(counterValue);

    // Reset when cycle is finished
    if (endOfCycle && counterValue == 50) {
      endOfCycle = 0;
      reading = 0;
    }

    // Set boolean 'reading' variable when at start of read cycle
    if (digitalRead(goPin) == HIGH && counterValue == 0) {
      reading = 1;
    }

    // Read the inputs to the buffer
    if (reading) {
      // 5 ms delay for propogation
      delay(5); // LOOK AT THIS IF THERE ARE ERRORS, COULD HAVE TO DO WITH THIS
      readToBuffer();

      if (counterValue == 49) {
        endOfCycle = 1;
      }
    }
  }
}

// Takes the 6-bit counter input and returns an int
int readInputCounter() {
  int value = (digitalRead(counterSixBitPins[0]) << 5) | (digitalRead(counterSixBitPins[1]) << 4) | 
          (digitalRead(counterSixBitPins[2]) << 3) | (digitalRead(counterSixBitPins[3]) << 2) | 
          (digitalRead(counterSixBitPins[4]) << 1) | (digitalRead(counterSixBitPins[5]));
  return value;
}

// Read from input data pins and add to the end of the buffer
void readToBuffer() {
  String fourBits = "";
  for (int i = 0; i < 4; i++) {
    if (digitalRead(dataPins[i]) == HIGH) {
      fourBits += "1";
    }
    else {
      fourBits += "0";
    }
  }
  binaryBuffer += fourBits;
  if (counterValue == 49) {
    binaryBuffer += "d";
  }
}