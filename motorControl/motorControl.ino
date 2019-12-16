#include <Servo.h> 

Servo servo; 
const int servoPin = 9; 

void setup() {
  servo.attach(servoPin); 
  Serial.begin(115200);
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

  if(Serial.available()){
    char tmp[100];
    String str="";
    byte len=Serial.readBytes(tmp, 20);

    for(int i = 0; i < len; i++){
      Serial.print(tmp[i]);
      str.concat(tmp[i]); 
    }

    if(str=="r"){
      servo.write(0);
    }
    else if(str=="l"){
      servo.write(90);
    }
    
  }

  
  
  //while(Serial.available() > 0){
    
      //servo.write(0);    //0도로 이동             
      //delay(1000);       //1초 대기                 
      //servo.write(90);   //90도로 이동            
      //delay(1000);       //1초 대기
      //servo.write(180);  //180도로 이동             
      //delay(1000);       //1초 대기 다시 처음으로 돌아감     
  //}
}
