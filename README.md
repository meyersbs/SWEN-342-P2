# SWEN-342-P2

[Project Description](http://www.se.rit.edu/~swen-342/projects/TSA-ActorProject.html)

### Dependencies

* Java `1.8`
* Maven `3.3.9`
  * akka `2.4.14`
  * akka_actor `2.11`

### Build

Command: `mvn package`

### Run

Command: `java -cp target/tsa-airport-1.0-SNAPSHOT.jar:lib/akka-actor_2.11-2.4.12.jar:lib/scala-library-2.11.8.jar:lib/config-1.3.0.jar edu.swen342.Driver`