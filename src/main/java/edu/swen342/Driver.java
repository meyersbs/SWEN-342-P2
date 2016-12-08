package edu.swen342;

/**
 * @project: SWEN-342 | TSA Airport
 *
 * @author: Benjamin S. Meyers
 * @author: Asma Sattar
 */

/*

	1) At the beginning of the day, the system will initialize and turn on all of the scanners.

	2) At the end of the day, the system closes by turning off all the scanners, and sending 
	prisoners in jail to permanent detention facilities. As passengers and their bags clear 
	through the system, it will shutdown, i.e. the queue must be empty and the body scanner 
	must be ready before the body scanner is turned off.
*/

import akka.actor.*;
import java.util.ArrayList;
import akka.actor.UntypedActor;
import akka.actor.Actor;
import akka.actor.ActorRef;


public class Driver {
  		/* Number of passengers */
		private static final int PASSENGER_COUNT = 20;
		/* Number of stations */
		public static final int STATION_COUNT = 4;
		

	  public static void main(String[] args) {
	  		final ActorSystem system = ActorSystem.create("MySystem");
	     	System.out.println("Jail opens");
	   
	     	ArrayList<ActorRef> queues = new ArrayList<ActorRef>();

	     	/* 
			 * Create one Jail and start it, 
			 * and send the appropriate Configure message to the Jail.
			 */
			final ActorRef jail = system.actorOf(Props.create(JailActor.class, STATION_COUNT), "jail");

			for (int lineNumber = 1; lineNumber < STATION_COUNT; lineNumber++) {
				System.out.println("Stations setting up");
				/* turning on scanners */

				System.out.println("Security Station " + lineNumber +  " is on.");
				final ActorRef security = system.actorOf(Props.create(SecurityActor.class, lineNumber, jail), "SecurityStation" + lineNumber);

	   			System.out.println("Body Scanner " + lineNumber +  " on.");
	   			final ActorRef bodyChecker = system.actorOf(Props.create(BodyCheckerActor.class, lineNumber, security), "BodyScanner" + lineNumber);

				System.out.println("Bag Scanner " + lineNumber +  " on.");
				final ActorRef bagChecker = system.actorOf(Props.create(BagCheckerActor.class, lineNumber, security), "BagScanner" + lineNumber);

				System.out.println("Queue " + lineNumber +  " is on.");
				final ActorRef queue = system.actorOf(Props.create(QueueActor.class, lineNumber, bodyChecker, bagChecker, security), "Queue" + lineNumber);

				/* Add the queue to the queues array so you can pass it to the documentChecker*/
				queues.add(queue);
			}
				
				System.out.println("Document Checker opens");
				final ActorRef docChecker = system.actorOf(Props.create(DocumentCheckerActor.class, queues), "DocumentChecker");

				/* OPENING THE AIRPORT AND GENERATING PASSENGERS */
				System.out.println("************ Airport is open ************");

				 /* Create passengers and pass them to the DocumentCheckerActor */
				System.out.println("Creating passengers and bags...");
				for (int id = 1; id <= PASSENGER_COUNT; id++) {
					Passenger passenger = new Passenger(id);
					/* NOTE: Driver is not an actor so we user noSender() */
					docChecker.tell(passenger, ActorRef.noSender());
				}
	  }
}