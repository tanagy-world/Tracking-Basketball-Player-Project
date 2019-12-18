#include<SoftwareSerial.h>
#include <Servo.h> 

#define BLUETOOTH_TX 2
#define BLUETOOTH_RX 3
#define PC_BAUD_RATE 115200
#define BL_BAUD_RATE 9600
#define SERVO_PIN 9

Servo servo; 
SoftwareSerial blueToothSerial(BLUETOOTH_TX,BLUETOOTH_RX);

void setup() {
  // put your setup code here, to run once:
  Serial.begin(PC_BAUD_RATE);
  blueToothSerial.begin(BL_BAUD_RATE);
  servo.attach(SERVO_PIN);   
}

String readSerial(){
  String str="";
  char ch;

  while( Serial.available() > 0 )   
  {   
    ch = Serial.read();   
    str.concat(ch);   
    delay(10);  
  }   

  return str;     
}

void loop() {
  // put your main code here, to run repeatedly:

  if (blueToothSerial.available()) {       
    Serial.write(blueToothSerial.read());  //블루투스측 내용을 시리얼모니터에 출력
  }
  
  if(Serial.available()){
    char tmp[100]={0,};
    String str="";
    byte len=Serial.readBytes(tmp, 20);

    str=tmp;
    Serial.println(str);
    
    if(tmp[0]=='M'){
      if(str.length()>1){
        servo.write(str.substring(2).toInt());
        Serial.println(str.substring(2).toInt());
      }
    }
    else if(tmp[0]=='B'){
      if(str.length()>1){
        str.substring(2).toCharArray(tmp,str.length());
        blueToothSerial.write(tmp);
      }
    }
    
  }
}
