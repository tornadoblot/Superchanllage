#include <SoftwareSerial.h>

#define LEDR 9
#define LEDG 10
#define LEDB 11

int adc_key_val[5] = {30, 150, 360, 535, 760};
int NUM_KEYS = 5;
int adc_key_in;
int key = -1;
int oldkey = -1;

void setup()
{
  Serial.begin(9600);
}
void loop() {
  adc_key_in = analogRead(0);      // read the value from the sensor
  key = get_key(adc_key_in);       // convert into key press
  
  if (key != oldkey) {      // if keypress is detected
    delay(50);               // wait for debounce time
    adc_key_in = analogRead(0);    // read the value from the sensor
    key = get_key(adc_key_in);     // convert into key press
    if (key != oldkey) {
      oldkey = key;
      
      if (key >= 0) {
        Serial.println("key input");
        switch (key)
        {
          case 0:
              Serial.println("case 0");
            break;
            
          case 1:
              Serial.println("case 1");
            break;
            
          case 2:
              Serial.println("case 2");
            break;
            
          case 3:
              Serial.println("case 3");
            break;
            
          case 4:
              Serial.println("case 4");
            break;
        }
      }
    }
  }
  delay(10);
}

// Convert ADC value to key number
int get_key(unsigned int input) {
  int k;
  for (k = 0; k < NUM_KEYS; k++) {
    if (input < adc_key_val[k]) {
      return k;
    }
  }
  if (k >= NUM_KEYS) k = -1;  // No valid key pressed
  return k;
}
