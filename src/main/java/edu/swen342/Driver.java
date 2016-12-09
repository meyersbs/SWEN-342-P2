package edu.swen342;

/*
 * @project: SWEN-342 | TSA Airport
 *
 * @author: Benjamin S. Meyers
 * @author: Asma Sattar
 */

/* IMPORTS ************************************************************************************************************/
import akka.actor.*;
import java.util.ArrayList;
import akka.actor.ActorRef;

/**
 * 1) At the beginning of the day, the system will initialize and turn on all of the scanners.
 * 2) At the end of the day, the system closes by turning off all the scanners, and sending prisoners in jail to
 * 		permanent detention facilities. As passengers and their bags clear through the system, it will shutdown, i.e.
 * 		the queue must be empty and the body scanner must be ready before the body scanner is turned off.
 */
public class Driver {

	/* GLOBAL VARIABLES ***********************************************************************************************/
	private static final int PASSENGER_COUNT = 20;
	public static final int STATION_COUNT = 4;


	/**
	 * MAIN method.
	 */
	public static void main(String[] args) {
		final ActorSystem system = ActorSystem.create("MySystem");
		System.out.println("#### JAIL IS OPENING ####");

		/* List of QueueActors */
		ArrayList<ActorRef> queues = new ArrayList<ActorRef>();

		/* Create a single JailActor */
		final ActorRef jail = system.actorOf(Props.create(JailActor.class, STATION_COUNT), "Jail");

		/* Setup security stations */
		for(int lineNumber = 1; lineNumber < STATION_COUNT+1; lineNumber++) {
			System.out.println("#### SETTING UP STATION " + lineNumber + " ####");

			/* Turn on SecurityActor, BodyScanner, and BagScanner */
			System.out.println("\tSecurityActor " + lineNumber +  " is on.");
			final ActorRef security = system.actorOf(Props.create(SecurityActor.class, lineNumber, jail), "SecurityStation" + lineNumber);

			System.out.println("\tBodyScanner " + lineNumber +  " is on.");
			final ActorRef bodyChecker = system.actorOf(Props.create(BodyCheckerActor.class, lineNumber, security), "BodyScanner" + lineNumber);

			System.out.println("\tBagScanner " + lineNumber +  " is on.");
			final ActorRef bagChecker = system.actorOf(Props.create(BagCheckerActor.class, lineNumber, security), "BagScanner" + lineNumber);

			/* Assign SecurityActor, BodyScanner, and BagScanner to a QueueActor */
			System.out.println("\tQueueActor " + lineNumber +  " is on.");
			final ActorRef queue = system.actorOf(Props.create(QueueActor.class, lineNumber, bodyChecker, bagChecker, security), "Queue" + lineNumber);

			/* Keep a list of QueueActors */
			queues.add(queue);
		}

		System.out.println("#### DocumentCheckerActor IS ON ####");
		final ActorRef docChecker = system.actorOf(Props.create(DocumentCheckerActor.class, queues), "DocumentChecker");

		/* OPENING THE AIRPORT AND GENERATING PASSENGERS */
		System.out.println("#### AIRPORT IS OPEN FOR BUSINESS ####");

		/* Create passengers and pass them to the DocumentCheckerActor */
		for (int id = 1; id <= PASSENGER_COUNT; id++) {
			Passenger passenger = new Passenger(id);
			System.out.println("\tPassenger " + id + " enters the airport.");
			/* NOTE: Driver is not an actor so we user noSender() */
			docChecker.tell(passenger, ActorRef.noSender());
		}
	}
}