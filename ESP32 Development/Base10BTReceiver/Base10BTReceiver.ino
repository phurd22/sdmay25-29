#include "BluetoothSerial.h"

BluetoothSerial SerialBT;

// Define the pins where the LEDs are connected
const int ledPins[5][4] = {
  {13, 12, 14, 27}, // Group 1
  {26, 25, 33, 32}, // Group 2
  {15, 2, 4, 16},   // Group 3
  {17, 5, 18, 19},  // Group 4
  {0, 0, 0, 0}   // Group 5
};

String numbers[5] = {"42", "173", "9", "543210", "0"}; // Default values

void setup() {
  Serial.begin(115200);  // Debugging
  SerialBT.begin("ESP32_BT");  // Start Bluetooth with a custom name

  for (int g = 0; g < 5; g++) {
    for (int i = 0; i < 4; i++) {
      pinMode(ledPins[g][i], OUTPUT);
    }
  }

  Serial.println("Bluetooth is ready. Waiting for data...");
}

void displayDigit(int group, int digit) {
  int value = digit - '0';  // Convert char to integer

  for (int i = 0; i < 4; i++) {
    digitalWrite(ledPins[group][i], (value >> i) & 1);
  }
}

void turnOnGroupLEDs(int group) {
  for (int i = 0; i < 4; i++) {
    digitalWrite(ledPins[group][i], HIGH);
  }
}

void clearAllLEDs() {
  for (int g = 0; g < 5; g++) {
    for (int i = 0; i < 4; i++) {
      digitalWrite(ledPins[g][i], LOW);
    }
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
    index++;
  }

  Serial.println("Updated Numbers:");
  for (int i = 0; i < 5; i++) {
    Serial.println(numbers[i]);
  }
}

void loop() {
  if (SerialBT.available()) {
    String receivedData = SerialBT.readStringUntil('d');
    Serial.println(receivedData);
    parseBluetoothData(receivedData);
  }

  int maxLen = 0;
  for (int i = 0; i < 5; i++) {
    if (numbers[i].length() > maxLen) {
      maxLen = numbers[i].length();
    }
  }

  for (int pos = 0; pos < maxLen; pos++) { // Start from the rightmost and move left
    clearAllLEDs(); // Clear previous state
    for (int i = 0; i < 5; i++) {
      int digitPos = numbers[i].length() - 1 - pos;
      if (digitPos >= 0) {
        displayDigit(i, numbers[i][digitPos]);
      } 
      else {
        turnOnGroupLEDs(i);
      }
    }
    delay(1000);
  }
}