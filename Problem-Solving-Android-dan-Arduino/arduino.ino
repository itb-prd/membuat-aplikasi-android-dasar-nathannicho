/*
   Tobias Andy Rusman - 16516091
   Nicholas Nathanael - 16516149
   Samuel Sandi Kristianto Lim - 16516307
   Dengan rumus persamaan termistor Steinhart-Hart:
   Suhu dalam Kelvin = 1 / {A + B[ln(R)] + C[ln(R)]3}
   A = 0.001129148, B = 0.000234125 dan C = 8.76741E-08
*/
#include <SoftwareSerial.h>
SoftwareSerial BT (0,1);
#include <math.h>
#define PinTermistor A0                                                 
float Termistor(int A)
{
long R;  
float Suhu; 
  R=9850*((1024.0 / A) - 1);
  Suhu = log(R);
  Suhu = 1 / (0.001129148 + (0.000234125 * Suhu) + (0.0000000876741 * Suhu * Suhu * Suhu));
  Suhu = Suhu - 273;                      
  return Suhu;                                   
}

void setup() {
  Serial.begin(115200);
  BT.begin(9600);
}

void loop() {
  float Suhu;
  Suhu=Termistor(analogRead(PinTermistor));   
  Serial.print(Suhu);
  if (Serial.available()>0) {
    BT.println(Suhu);
    delay(50);                                                           
}}
