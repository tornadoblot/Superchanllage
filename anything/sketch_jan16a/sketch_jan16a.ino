#include <SoftwareSerial.h> // 시리얼 통신
// RFID 라이브러리
#include <SPI.h>
#include <MFRC522.h>

//블루투스
int Tx = 6; //전송
int Rx = 7; //수신
SoftwareSerial btSerial(Tx, Rx);

//RFID 설정
#define RST_PIN 9
#define SS_PIN 10
MFRC522 mfrc(SS_PIN, RST_PIN);

//키패드
int  adc_key_val[5] = {30, 150, 360, 535, 760 };
int NUM_KEYS = 5;
int adc_key_in;
int key = -1;
int oldkey = -1;

String cUID = "";

void setup()
{
  Serial.begin(9600);
  btSerial.begin(9600);

  //RFID
  SPI.begin(); // SPI 초기화
  mfrc.PCD_Init(); // (SPI : 하나의 마스터와 다수의 SLAVE(종속적인 역활)간의 통신 방식)
}

void loop()
{
  if (btSerial.available()) {
    Serial.write(btSerial.read());
  }

  if (Serial.available()) {
    btSerial.write(Serial.read());
  }

  adc_key_in = analogRead(0);      // read the value from the sensor
  key = get_key(adc_key_in);       // convert into key press
  if (key != oldkey) {      // if keypress is detected
    delay(50);               // wait for debounce time
    adc_key_in = analogRead(0);    // read the value from the sensor
    key = get_key(adc_key_in);
    keyip();
  }

  // rfid
  if (!mfrc.PICC_IsNewCardPresent() || !mfrc.PICC_ReadCardSerial()) {
    // 태그 접촉이 되지 않았을때 또는 ID가 읽혀지지 않았을때
    return; // return
  }

  Serial.print("Card UID:");                  // 태그의 ID출력

  for (byte i = 0; i < 4; i++) {               // 태그의 ID출력하는 반복문.태그의 ID사이즈(4)까지
    Serial.print(mfrc.uid.uidByte[i]);        // mfrc.uid.uidByte[0] ~ mfrc.uid.uidByte[3]까지 출력
    Serial.print(" ");                        // id 사이의 간격 출력
  }
  btSerial.println("5");
  Serial.println();
  delay(100);
}

int get_key(unsigned int input) {
  int k;
  for (k = 0; k < NUM_KEYS; k++) {
    if (input < adc_key_val[k]) {
      return k;
    }
  }
  if (k >= NUM_KEYS)k = -1;  // No valid key pressed
  return k;
}

void keyip()
{

  if (key != oldkey) {
    oldkey = key;
    if (key >= 0) {
      Serial.println("key input");
      switch (key)
      {
        case 0:
          Serial.println("case 0");
          btSerial.println("0");
          break;
        case 1:
          Serial.println("case 1");
          btSerial.println("1");
          break;
        case 2:
          Serial.println("case 2");
          btSerial.println("2");
          break;

        case 3:
          Serial.println("case 3");
          btSerial.println("3");
          break;
        case 4:
          Serial.println("case 4");
          btSerial.println("4");
          break;
      }
    }
  }
}
