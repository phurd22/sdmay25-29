#include "BluetoothSerial.h"

BluetoothSerial SerialBT;

// Define the pins where the LEDs are connected
const int ledPins[5][5] = {
  {15, 2, 4, 5, 18}, // Group 1
  {0, 0, 0, 0, 0}, // Group 2
  {0, 0, 0, 0, 0},   // Group 3
  {0, 0, 0, 0, 0},  // Group 4
  {0, 0, 0, 0, 0}   // Group 5
};

String numbers[5] = {"0", "0", "0", "0", "0"}; // Default values

void setup() {
  Serial.begin(115200);  // Debugging
  SerialBT.begin("ESP32_BT");  // Start Bluetooth with a custom name

  for (int i = 0; i < 5; i++) {
    for (int g = 0; g < 5; g++) {
      pinMode(ledPins[i][g], OUTPUT);
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

void clearNumLEDs() {
  for (int i = 0; i < 5; i++) {
    for (int g = 0; g < 4; g++) {
      digitalWrite(ledPins[i][g], LOW);
    }
  }
}

void setSignLEDs() {
  for (int i = 0; i < 5; i++) {
    if (numbers[i].charAt(0) == '-') {
      numbers[i].remove(0, 1);
      digitalWrite(ledPins[i][4], HIGH);
    }
    else {
      digitalWrite(ledPins[i][4], LOW);
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
    setSignLEDs();
  }

  int maxLen = 0;
  for (int i = 0; i < 5; i++) {
    if (numbers[i].length() > maxLen) {
      maxLen = numbers[i].length();
    }
  }

  for (int pos = 0; pos < maxLen; pos++) { // Start from the leftmost and move right
    clearNumLEDs(); // Clear previous state
    for (int i = 0; i < 5; i++) {
      int numLen = numbers[i].length();
      if (pos < numLen) {
        displayDigit(i, numbers[i][pos]);
      } 
      else {
        turnOnGroupLEDs(i);
      }
    }
    delay(1000);
  }
}