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

BLECharacteristic *pCharacteristic;
BLEServer *pServer = nullptr;


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
  // put your main code here, to run repeatedly:

}
