#include <Wire.h>
#include <PinChangeInterrupt.h> 
 
byte TxByte = 0xAA;
uint8_t trig = 11;
uint8_t echo = 12;

// since c optimizes out things it thinks can't change in a function, the ISR change wasn't seen by loop() 
volatile bool sawEcho = false; 
volatile bool sawRising = false; 
volatile unsigned long echoTime = 0; 

unsigned long duration = 0;

 
void I2C_TxHandler(void)
{
  unsigned long x = duration;

  Wire.write((unsigned byte) x & 0xFF);
  Wire.write((unsigned byte) (x / 256));

  //Serial.println(x);
}

unsigned long distance_test() {
  digitalWrite(trig, HIGH);  
  delayMicroseconds(10);
  digitalWrite(trig, LOW);   
  unsigned long val = pulseIn(echo, HIGH, 25000);  
  //val = val / 58;       
  return val;
}  

ISR (PCINT0_vect){
  int x = digitalRead(echo);

// if it has not gone high yet but now is  
  if (!sawRising && x){
    sawRising = true; 
    digitalWrite(LED_BUILTIN, HIGH);
  }

// if it has gone high before 
  if (sawRising && !x){
    echoTime = micros();
    sawEcho = true; 
    digitalWrite(LED_BUILTIN, LOW);
  }
}

void setup() {
  Serial.begin(9600);

  PCIFR |= B00000001;
  PCICR |= B00000001;	
  PCMSK0 |= B00010000;			


  pinMode(trig, OUTPUT);
  digitalWrite(trig, LOW);   
  pinMode(echo, INPUT_PULLUP); 

  pinMode(LED_BUILTIN, OUTPUT);
  digitalWrite(LED_BUILTIN, LOW); 

  Wire.begin(0x55); // Initialize I2C (Slave Mode: address=0x55 )
  Wire.onRequest(I2C_TxHandler);
}
 
void loop() {  
  sawRising = false; 
  sawEcho = false; 
  echoTime = 0; 
  digitalWrite(trig, HIGH);  
  delayMicroseconds(10);
  digitalWrite(trig, LOW);   
  unsigned long startTime = micros(); 

  while (!sawEcho && ((micros() - startTime) < 100000)){   
  }

  if (sawEcho){
    duration = echoTime - startTime;
    Serial.println(duration);
  }
  else{
    duration = 0; 
    Serial.print("timed out ");
    Serial.println(echoTime - startTime);
 } 
}

