#include <BLEDevice.h>
#include <BLEUtils.h>
#include <BLEServer.h>

#define SERVICE_UUID        "246ccada-310c-46a4-9881-3d2feb165646"
#define CHARACTERISTIC_UUID "9d5a6f5c-e32c-429f-9faa-8209e6f2dda4"

BLECharacteristic *pCharacteristic;
const int sendPin = 14;
int send = 0;
int count = 0;

// Callback class for handling incoming BLE writes
// class MyCallbacks : public BLECharacteristicCallbacks {

// };

String generateRandomBinaryString(int length) {
  String result = "";
  for (int i = 0; i < length; i++) {
    result += random(2); // either 0 or 1
  }
  return result;
}

void setup() {
  Serial.begin(115200);

  pinMode(sendPin, INPUT);

  // Setup for BLE
  BLEDevice::init("Base2SenderESP32");
  BLEDevice::setMTU(100);
  BLEServer *pServer = BLEDevice::createServer();
  BLEService *pService = pServer->createService(SERVICE_UUID);
  pCharacteristic = pService->createCharacteristic(
    CHARACTERISTIC_UUID,
    BLECharacteristic::PROPERTY_READ |
    BLECharacteristic::PROPERTY_WRITE |
    BLECharacteristic::PROPERTY_NOTIFY
  );
  //pCharacteristic->setCallbacks(new MyCallbacks()); // Attach the write callback to handle incoming data
  pService->start();
  BLEAdvertising *pAdvertising = BLEDevice::getAdvertising();
  pAdvertising->addServiceUUID(SERVICE_UUID);
  pAdvertising->setScanResponse(true);
  BLEDevice::startAdvertising();

  Serial.println("BLE is ready. Waiting for data...");
}


void loop() {
  unsigned long lastSent = 0;
  unsigned long time = 0;

  if (digitalRead(sendPin) == HIGH) {
    send = 1;
  }

  if (send == 1) {
    for (int i = 0; i < 50; i++) {
      String binString = generateRandomBinaryString(4);
      pCharacteristic->setValue(binString.c_str());
      pCharacteristic->notify();
      Serial.print("Sent: ");
      Serial.println(binString);
      delay(250);
    }
    send = 0;
  }

  // time = millis();
  // if (time - lastSent > 250 && send == 1) {
  //   lastSent = time;
  //   String binString = generateRandomBinaryString(4);
  //   pCharacteristic->setValue(binString.c_str());
  //   pCharacteristic->notify();
  //   Serial.print("Sent: ");
  //   Serial.println(binString);
  //   count++;
  //   if (count == 50) {
  //     count = 0;
  //     send = 0;
  //   }
  // }
}
