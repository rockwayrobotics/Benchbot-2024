#include <Wire.h>
 
byte TxByte = 0xAA;
int trig = 11;
int echo = 12; 
 
void I2C_TxHandler(void)
{
  unsigned long x = distance_test();

  Wire.write((unsigned byte) x & 0xFF);
  Wire.write((unsigned byte) (x / 256));
  digitalWrite(LED_BUILTIN, HIGH);
}

unsigned long distance_test() {
  digitalWrite(trig, LOW);   
  delayMicroseconds(2);
  digitalWrite(trig, HIGH);  
  delayMicroseconds(20);
  digitalWrite(trig, LOW);   
  unsigned long val = pulseIn(echo, HIGH);  
  //val = val / 58;       
  return val;
}  
 
void setup() {
  pinMode(trig, OUTPUT);
  pinMode(echo, INPUT); 
  pinMode(LED_BUILTIN, OUTPUT);
  digitalWrite(LED_BUILTIN, LOW); 
  Wire.begin(0x55); // Initialize I2C (Slave Mode: address=0x55 )
  Wire.onRequest(I2C_TxHandler);
}
 
void loop() {
  // Nothing To Be Done Here
}
