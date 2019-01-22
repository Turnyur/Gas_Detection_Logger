
//If youre not using a BTBee connect set the pin connected to the KEY pin high
#include <SoftwareSerial.h>
float sensor0=A0;
float sensor1=A1;
int gas_value;
int temp_value;
float final_gas_value;
float temp;
SoftwareSerial BTSerial(4,5); 
void setup() {
    Serial.begin(9600);
   BTSerial.begin(9600);
  }
void loop() {
gas_value=(int)analogRead(sensor0);
delay(500);
temp_value=(int)analogRead(sensor1);
temp=temp_value * 0.48/*828125*/; //convert analog reading to temperature in degree Celsious
 final_gas_value = gas_value * (5.0 / 1023.0);
 //convert to milli volts
 final_gas_value*=1000.0;
 
 delay(2);
//Serial.println(gas_value);
  
 Serial.println(temp);
 Serial.println(final_gas_value);
 /*
 BTSerial.print('#');
 BTSerial.print(temp);
 BTSerial.print('+');
 BTSerial.print(gas_value);
 BTSerial.print('+');
 BTSerial.print('~');
 */
        BTSerial.print('#');
	BTSerial.print(temp);
	BTSerial.print('+');
	delay(5);
	BTSerial.print(final_gas_value);
	BTSerial.print('+');
	BTSerial.print('~');
	BTSerial.println();

  delay(10);

  while (BTSerial.available()) {
      Serial.write(BTSerial.read());
       delay(9);
    }

}
