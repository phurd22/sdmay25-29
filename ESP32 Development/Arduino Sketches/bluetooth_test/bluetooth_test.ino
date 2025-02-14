#include "BluetoothSerial.h"

BluetoothSerial SerialBT;

const int redLED = 21;
const int yellowLED = 19;
const int greenLED = 18;
const int buttonLED = 33;
const int buttonInput = 32;

void setup() {
  Serial.begin(115200);
  if (SerialBT.begin("ESP32_LED_Control")) {
    Serial.println("Bluetooth started successfully");
  } else {
    Serial.println("Bluetooth failed to start");
  }

  pinMode(redLED, OUTPUT);
  pinMode(yellowLED, OUTPUT);
  pinMode(greenLED, OUTPUT);
  pinMode(buttonInput, INPUT);
  pinMode(buttonLED, OUTPUT);

  digitalWrite(redLED, LOW);
  digitalWrite(yellowLED, LOW);
  digitalWrite(greenLED, LOW);
  digitalWrite(buttonLED, LOW);
}

void loop() {
  // Input pin testing
  if (digitalRead(buttonInput) == HIGH) {
    digitalWrite(buttonLED, HIGH);
  }
  else {
    digitalWrite(buttonLED, LOW);
  }

  // Bluetooth testing
  if (SerialBT.available() >= 4) {  // An int is 4 bytes
    int receivedValue = 0;
    byte buffer[4];

    // Read 4 bytes from the Bluetooth buffer
    for (int i = 0; i < 4; i++) {
      buffer[i] = SerialBT.read();
    }

    // Reconstruct the int from the byte array
    receivedValue = (buffer[0] << 24) | (buffer[1] << 16) | (buffer[2] << 8) | buffer[3];

    Serial.print("Received int: ");
    Serial.println(receivedValue);

    // Separate ints from encoding
    int whichLED = receivedValue / 10;
    int onOrOff = receivedValue % 10;

    // Turn LED on or off
    if (whichLED == 1) {
      if (onOrOff == 0) {
        digitalWrite(redLED, LOW);
      }
      else if (onOrOff == 1) {
        digitalWrite(redLED, HIGH);
      }
    }
    else if (whichLED == 2) {
      if (onOrOff == 0) {
        digitalWrite(yellowLED, LOW);
      }
      else if (onOrOff == 1) {
        digitalWrite(yellowLED, HIGH);
      }
    }
    else if (whichLED == 3) {
      if (onOrOff == 0) {
        digitalWrite(greenLED, LOW);
      }
      else if (onOrOff == 1) {
        digitalWrite(greenLED, HIGH);
      }
    }
  }
}
