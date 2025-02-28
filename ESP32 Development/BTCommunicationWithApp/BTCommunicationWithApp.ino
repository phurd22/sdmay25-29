#include "BluetoothSerial.h"

BluetoothSerial SerialBT;

const int buttonPin = 23;
bool buttonPressed = false;

void setup() {
  Serial.begin(115200);
  SerialBT.begin("ESP32_BT"); // Bluetooth device name
  pinMode(buttonPin, INPUT_PULLDOWN);
  Serial.println("Bluetooth started");
}

void loop() {
  if (digitalRead(buttonPin) == HIGH && !buttonPressed) {
    delay(100); // Debounce delay
    if (digitalRead(buttonPin) == HIGH) { // Check again after delay
      for (int i = 0; i < 30; i++) {
        // Generate a random number in the range [-562949953421312, 562949953421311]
        int64_t randomNum = (int64_t)random(0, (1LL << 30)) << 20 | random(0, (1 << 20)); // 50-bit signed

        if (random(2) == 0) { // 50% chance of negative number
            randomNum = -randomNum;
        }

        SerialBT.write((uint8_t*)&randomNum, sizeof(randomNum)); // Send as 8 bytes
        delay(10); // Small delay to avoid overflow
      }
      Serial.println("Sent 30 numbers");
    }
  }

  if (digitalRead(buttonPin) == LOW) {
    buttonPressed = false; // Reset when button is released
  }
}

// int64_t randomSigned50Bit() {
//     uint64_t randomNumber = ((uint64_t)random(1UL << 25) << 25) | random(1UL << 25);

//     // Convert to signed 50-bit number using two's complement
//     if (randomNumber & (1ULL << 49)) { // Check if the 50th bit (sign bit) is set
//         randomNumber |= 0xFFFF000000000000; // Extend sign bit to 64-bit
//     }

//     return (int64_t)randomNumber;
// }
