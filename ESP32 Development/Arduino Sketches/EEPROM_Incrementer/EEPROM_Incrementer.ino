// A0 - pin 27
// A1 - pin 26
// A2 - pin 25
// A3 - pin 33

const int addressPins[] = {27, 26, 25, 33}; // Array to hold the address pin numbers
const int numPins = 4; // Number of address bits

void setup() {
  for (int i = 0; i < numPins; i++) {
    pinMode(addressPins[i], OUTPUT); // Set each pin as output
  }
}

void loop() {
  for (int address = 0; address < 16; address++) { // Cycle through 0 to 15
    for (int bit = 0; bit < numPins; bit++) {
      digitalWrite(addressPins[bit], (address >> bit) & 1); // Set each pin according to the address bits
    }
    delay(500); // Small delay before incrementing to the next address
  }
}