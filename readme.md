# Evoluting Life
This application is an experiment to train robots to travel without colliding. To achieve this, it uses 
a Genetic Algorithm and a basic neural network. The fitness of a robot is determined by large distance 
and few collisions. The application van run as a simulation as well as on a raspberry-pi.
## Build
To build this application using a Terminal, navigate to the root of this project. Make sure you have 
installed Maven and type:
```bash
mvn clean package
```
## Running the simulation
To run this application as a simulation using a Terminal, navigate to the target
sub folder and type:
```bash
java -jar evoluting-life-java-<version>-jar-with-dependencies.jar
```
Where the <version>-placeholder needs to be replaced by the current version.
## Running on a Raspberry-Pi
### Prerequisites
#### pi4j
To control the pins on your Raspberry-Pi, this application uses the
[pi4j library](http://pi4j.com/index.html). The library needs to be installed on
your Raspberry-Pi. You can find instructions here: [Install pi4j](http://pi4j.com/install.html)
#### ServoBlaster
To control the servo that holds and turns the sensor, the application uses
[ServoBlaster](https://github.com/richardghirst/PiBits/tree/master/ServoBlaster).
This repository contains a servoBlaster.sh which should be started on the Raspberry-Pi before 
starting the application.
#### runRobot
To start the application in a convenient way, it is recommended to copy the runRobot.sh file
in this repository to your Raspberry-Pi.
#### The application
Also copy the application 'evoluting-life-java-<version>-jar-with-dependencies.jar' that
was previously build to your Raspberry-Pi.
### Pin setup
The pin numbering of wiringpi wil be used in this description and in code. Visit
[Raspberry Pi Pinout](http://pinout.xyz/pinout/wiringpi) for reference.
The following pins are used for running the application on the Raspberry-Pi:
* GPIO_07 : Servo               (output)
* GPIO_10 : Sensor echo         (input)
* GPIO_14 : Sensor trigger      (output)
* GPIO_23 : Left motor forward  (output, hard PWM)
* GPIO_25 : Left motor reverse  (output, soft PWM)
* GPIO_26 : Right motor forward (output, hard PWM)
* GPIO_28 : Right motor reverse (output, soft PWM)
* GPIO_29 : Led                 (output)  

## Structure of the code
The green colored boxes represent classes that are part of the core and are used in both simulation
and on the Raspberry-Pi. The orange colored boxed represent classes that are used for the simulation only.
The grey colored boxes represent classes that are used on the Raspberry-Pi only. The red lines represent
a dependency that is used to report velocity, position, collision etc. to the SimRobot class.
 
![Class Diagram](https://docs.google.com/drawings/d/18I7Fg6CTmE0s5LimI8FwSKXlY61ioaHY7mvGooyhNbY/pub?w=1119&h=640)