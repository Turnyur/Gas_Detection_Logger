## Microcontroller Based Toxic Gas Detection and Data Logging System

I developed this system during my undergraduate days (December 2016).

The system has two parts

1. Software part
2. Hardware part


## Software Part
The software part entails development of an Android application capable of receiving data remotely and then processing and showing the information gleaned in a structured and easily understable way.
In addiction, The Android app is capable of storing the information an SQLite Database on the android platform together with the date and time of the arrival of each data obtained remotely from the sensor.

Back in 2016, Android ADT in Eclipse was still supported, so i used ADT in developing the Android Application.
Early stages of the application development through the advance stages can be found below

![alt text](https://github.com/Turnyur/Gas_Detection_Logger/blob/master/shots/Screenshot_2017-01-02-18-26-20.png)
![alt text](https://github.com/Turnyur/Gas_Detection_Logger/blob/master/shots/Screenshot_2017-01-04-22-52-42.png)
![alt text](https://github.com/Turnyur/Gas_Detection_Logger/blob/master/shots/adt_capture1.PNG)
![alt text](https://github.com/Turnyur/Gas_Detection_Logger/blob/master/shots/adt_capture3.PNG)
![alt text](https://github.com/Turnyur/Gas_Detection_Logger/blob/master/shots/Screenshot_2017-01-03-05-56-32.png)
![alt text](https://github.com/Turnyur/Gas_Detection_Logger/blob/master/shots/Screenshot_2017-01-03-18-41-40.png)
![alt text](https://github.com/Turnyur/Gas_Detection_Logger/blob/master/shots/Screenshot_2017-01-05-09-37-18.png)
![alt text](https://github.com/Turnyur/Gas_Detection_Logger/blob/master/shots/Screenshot_2017-01-04-06-58-16.png)
![alt text](https://github.com/Turnyur/Gas_Detection_Logger/blob/master/shots/Screenshot_2017-01-04-12-08-51.png)
![alt text](https://github.com/Turnyur/Gas_Detection_Logger/blob/master/shots/Screenshot_2017-01-06-10-20-25.png)
![alt text](https://github.com/Turnyur/Gas_Detection_Logger/blob/master/shots/Screenshot_2017-01-07-11-50-26.png)


The completed Android application application can be downloaded below;

* APK [Gas Detection & Data Logger](https://github.com/Turnyur/Gas_Detection_Logger/blob/master/gaslogger_app.apk) 
* Source Code [Gas Detection & Data Logger (Eclipse ADT files )](https://github.com/Turnyur/Gas_Detection_Logger/blob/master/android-app)



PS: Although you can download the APK file and install on your device, it can only streams Gas and Temperature Status of your environment when the hardware device is available and must have been paired to the Android app through Bluetooth.




## Hardware Part
The Hardware part consist of the Design using Proteus of the circuit diagram and the construction of the electronic components of the system such as;

* The Gas Sensor (MQ2)
* The Temperature Sensor (LM35)
* The Bluetooth Sensor (HC-O5)
* The Microcontroller (Atmega 328p)
* *Discrete components* such as resistors, transistors, capacitors diodes etc 

* The Proteus Simulation can be downloaded [here](https://github.com/Turnyur/Gas_Detection_Logger/blob/master/shots/Gas%20data%20logging.pdsprj) ![alt text](https://github.com/Turnyur/Gas_Detection_Logger/blob/master/shots/data%20log.png)
* The Microcontroller employed was programmed in Arduino Programming environment. The code snippet can be found [here](https://github.com/Turnyur/Gas_Detection_Logger/blob/master/shots/declare_code_full.PNG)
* The completed Hardware sub-system is shown below ![alt text](https://github.com/Turnyur/Gas_Detection_Logger/blob/master/shots/hard_shots.jpg)

