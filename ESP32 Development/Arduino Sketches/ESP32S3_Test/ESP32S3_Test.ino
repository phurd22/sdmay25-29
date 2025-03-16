/*
    Based on Neil Kolban example for IDF: https://github.com/nkolban/esp32-snippets/blob/master/cpp_utils/tests/BLE%20Tests/SampleServer.cpp
    Ported to Arduino ESP32 by Evandro Copercini
    updates by chegewara
*/

//   _____    _   _              ____                        U  ___ u   ____      _  __   ____     _    
//  |_ " _|  |'| |'|     ___    / __"| u      __        __    \/"_ \/U |  _"\ u  |"|/ /  / __"| uU|"|u  
//    | |   /| |_| |\   |_"_|  <\___ \/       \"\      /"/    | | | | \| |_) |/  | ' /  <\___ \/ \| |/  
//   /| |\  U|  _  |u    | |    u___) |       /\ \ /\ / /\.-,_| |_| |  |  _ <  U/| . \\u u___) |  |_|   
//  u |_|U   |_| |_|   U/| |\u  |____/>>     U  \ V  V /  U\_)-\___/   |_| \_\   |_|\_\  |____/>> (_)   
//  _// \\_  //   \\.-,_|___|_,-.)(  (__)    .-,_\ /\ /_,-.     \\     //   \\_,-,>> \\,-.)(  (__)|||_  
// (__) (__)(_") ("_)\_)-' '-(_/(__)          \_)-'  '-(_/     (__)   (__)  (__)\.)   (_/(__)    (__)_) 

#include <BLEDevice.h>
#include <BLEUtils.h>
#include <BLEServer.h>

// See the following for generating UUIDs:
// https://www.uuidgenerator.net/

#define SERVICE_UUID        "4fafc201-1fb5-459e-8fcc-c5c9c331914b"
#define CHARACTERISTIC_UUID "beb5483e-36e1-4688-b7f5-ea07361b26a8"

// Callback class for handling incoming BLE writes
class MyCallbacks : public BLECharacteristicCallbacks {
  void onWrite(BLECharacteristic *pCharacteristic) {
    String rxValue = pCharacteristic->getValue();

    if (rxValue.length() > 0) {
      // Serial.print("Received: ");
      // Serial.flush();
      Serial.println("Received: " + rxValue); // Print the received value
      Serial.flush();
    }
  }
};

void setup() {
  delay(5000);
  Serial.begin(115200);
  Serial.println("Starting BLE work!");

  BLEDevice::init("MyESP32");
  BLEDevice::setMTU(100);
  BLEServer *pServer = BLEDevice::createServer();
  BLEService *pService = pServer->createService(SERVICE_UUID);
  BLECharacteristic *pCharacteristic =
    pService->createCharacteristic(CHARACTERISTIC_UUID, BLECharacteristic::PROPERTY_READ | BLECharacteristic::PROPERTY_WRITE);

  pCharacteristic->setValue("Hello World says Neil");

  // Attach the write callback to handle incoming data
  pCharacteristic->setCallbacks(new MyCallbacks());

  pService->start();

  // BLEAdvertising *pAdvertising = pServer->getAdvertising();  // this still is working for backward compatibility
  BLEAdvertising *pAdvertising = BLEDevice::getAdvertising();
  pAdvertising->addServiceUUID(SERVICE_UUID);
  pAdvertising->setScanResponse(true);
  pAdvertising->setMinPreferred(0x06);  // functions that help with iPhone connections issue
  pAdvertising->setMinPreferred(0x12);
  BLEDevice::startAdvertising();
  Serial.println("Characteristic defined! Now you can read it in your phone!");
}

void loop() {
  // put your main code here, to run repeatedly:
  delay(2000);
}
