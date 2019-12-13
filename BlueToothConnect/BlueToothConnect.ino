#include<SoftwareSerial.h>

#define BLUETOOTH_TX 2
#define BLUETOOTH_RX 3
#define BAUD_RATE 9600

SoftwareSerial blueToothSerial(BLUETOOTH_TX,BLUETOOTH_RX);

void setup() {
  // put your setup code here, to run once:

  Serial.begin(BAUD_RATE);
  blueToothSerial.begin(BAUD_RATE);

}

void loop() {
  // put your main code here, to run repeatedly:

  if (blueToothSerial.available()) {       
    Serial.write(blueToothSerial.read());  //블루투스측 내용을 시리얼모니터에 출력
  }
  if (Serial.available()) {         
    blueToothSerial.write(Serial.read());  //시리얼 모니터 내용을 블루추스 측에 WRITE
  }

}
