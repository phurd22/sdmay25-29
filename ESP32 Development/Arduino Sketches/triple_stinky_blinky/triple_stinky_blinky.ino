const int redLED = 21;
const int yellowLED = 19;
const int greenLED = 18;
const int buttonLED = 33;
const int buttonInput = 32;
unsigned long startTime;
unsigned long currentTime;
const int period = 300;
int currentLED = 1;

void setup() {
  pinMode(redLED, OUTPUT);
  pinMode(yellowLED, OUTPUT);
  pinMode(greenLED, OUTPUT);
  pinMode(buttonInput, INPUT);
  pinMode(buttonLED, OUTPUT);
  digitalWrite(redLED, HIGH);
  startTime = millis();
  currentTime = millis();
}

void loop() {
  currentTime = millis();
  if (currentTime - startTime >= period) {
    switchLED();
    startTime = currentTime;
  }
  checkButton();
}

// Switches lit LED based on what previous one was
void switchLED() {
  if (currentLED == 1) {
    digitalWrite(redLED, LOW);
    digitalWrite(yellowLED, HIGH);
    currentLED = 2;
  }
  else if (currentLED == 2) {
    digitalWrite(yellowLED, LOW);
    digitalWrite(greenLED, HIGH);
    currentLED = 3;
  }
  else {
    digitalWrite(greenLED, LOW);
    digitalWrite(redLED, HIGH);
    currentLED = 1;
  }
}

// Checks if button is depressed and turns on LED
void checkButton() {
  if (digitalRead(buttonInput) == HIGH) {
    digitalWrite(buttonLED, HIGH);
  }
  else {
    digitalWrite(buttonLED, LOW);
  }
}