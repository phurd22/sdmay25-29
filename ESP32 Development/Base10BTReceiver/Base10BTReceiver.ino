// Define the pins where the LEDs are connected
const int ledPins[5][4] = {
    {13, 12, 14, 27}, // Group 1
    {26, 25, 33, 32}, // Group 2
    {15, 2, 4, 16},   // Group 3
    {17, 5, 18, 19},  // Group 4
    {21, 1, 22, 23}   // Group 5
};

const char *numbers[5] = {
    "42",   // Example numbers
    "173",
    "9",
    "56",
    "1024"
};

void setup() {
    for (int g = 0; g < 5; g++) {
        for (int i = 0; i < 4; i++) {
            pinMode(ledPins[g][i], OUTPUT);
        }
    }
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

void loop() {
  int maxLen = 0;
  for (int i = 0; i < 5; i++) {
    int len = strlen(numbers[i]);
    if (len > maxLen) {
      maxLen = len;
    }
  }

  for (int pos = 0; pos < maxLen; pos++) { // Start from the rightmost and move left
    clearAllLEDs(); // Clear previous state
    for (int i = 0; i < 5; i++) {  // Iterate through groups
      int len = strlen(numbers[i]);
      int digitPos = len - 1 - pos; // Get the digit from right to left

      if (digitPos >= 0) {
        displayDigit(i, numbers[i][digitPos]);  // Display current digit
      } 
      else {
        turnOnGroupLEDs(i); // If out of digits, turn all LEDs in the group on
      }
    }
    delay(1000);
  }
}