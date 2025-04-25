#include <BLEDevice.h>
#include <BLEUtils.h>
#include <BLEServer.h>

#define SERVICE_UUID        "246ccada-310c-46a4-9881-3d2feb165646"
#define CHARACTERISTIC_UUID "9d5a6f5c-e32c-429f-9faa-8209e6f2dda4"

BLECharacteristic *pCharacteristic;
const int sendPin = 14;
int send;
int count;
unsigned long lastNewValue;
unsigned long lastSent;
unsigned long timer;
String binaryBuffer = "";

BLEServer *pServer = nullptr;

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

  send = 0;
  count = 0;
  lastNewValue = 0;
  lastSent = 0;
  timer = 0;

  pinMode(sendPin, INPUT);

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
  //pCharacteristic->setCallbacks(new MyCallbacks()); // Attach the write callback to handle incoming data
  pService->start();
  BLEAdvertising *pAdvertising = BLEDevice::getAdvertising();
  pAdvertising->addServiceUUID(SERVICE_UUID);
  pAdvertising->setScanResponse(true);
  BLEDevice::startAdvertising();

  Serial.println("BLE is ready. Waiting for data...");
}

// TODO: Going to need some logic to hold onto bits and only send every ~100 ms or else it gets 
// too fast for the BLE connection. Only send when you have new buffered data.
void loop() {
  if (digitalRead(sendPin) == HIGH) {
    send = 1;
  }

  // if (send == 1) {
  //   for (int i = 0; i < 50; i++) {
  //     String binString = generateRandomBinaryString(4);
  //     pCharacteristic->setValue(binString.c_str());
  //     pCharacteristic->notify();
  //     Serial.print("Sent: ");
  //     Serial.println(binString);
  //     Serial.print("Index: ");
  //     Serial.println(i);
  //     Serial.println("-------------------------------------");
  //     delay(100);
  //   }
  //   send = 0;
  // }

  timer = millis();
  if (timer - lastNewValue > 20 && send == 1 && count < 50) {
    lastNewValue = timer;
    binaryBuffer += generateRandomBinaryString(4);
    count++;
  }
  if (timer - lastSent > 100 && send == 1) {
    lastSent = timer;
    // String binString = generateRandomBinaryString(4);
    pCharacteristic->setValue(binaryBuffer.c_str());
    pCharacteristic->notify();
    Serial.print("Sent: ");
    Serial.println(binaryBuffer);
    Serial.print("Count: ");
    Serial.println(count);
    Serial.println("---------------------------------------");
    binaryBuffer = "";
    // count++;
    if (count == 50) {
      count = 0;
      send = 0;
    }
  }
}
