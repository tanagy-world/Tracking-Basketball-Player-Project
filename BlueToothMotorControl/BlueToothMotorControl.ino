#include<SoftwareSerial.h>
#include <Servo.h> 

#define BLUETOOTH_TX 2
#define BLUETOOTH_RX 3
#define PC_BAUD_RATE 115200
#define BL_BAUD_RATE 9600
#define SERVO_PIN 9

#define DEBUG 1

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

  // M(각도)/ 형태로 전송.
  if (blueToothSerial.available()) {

    String str="";
    int start=0;
    int last=0;
    str=blueToothSerial.readString();
    start=str.indexOf("M")+1;
    last=str.indexOf("/");
    if(start!=-1 && last!=-1)
      servo.write(str.substring(start,last).toInt());
    #if DEBUG
      Serial.println("blt msg : "+str);
    #endif
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
