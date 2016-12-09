package edu.swen342;

/*
 * @project: SWEN-342 | TSA Airport
 *
 * @author: Benjamin S. Meyers
 * @author: Asma Sattar
 */

/* IMPORTS ************************************************************************************************************/
import akka.actor.UntypedActor;
import akka.actor.ActorRef;
import java.util.ArrayList;
import java.util.Random;

/**
 * 1) Passengers enter the system from a main driver program.
 * 2) Passengers are randomly turned away for document problems at a probability of 20%.
 * 3) Passengers not turned away enter the queue for one of the lines in a cyclic fashion.
 */
public class DocumentCheckerActor extends UntypedActor{

	/* GLOBAL VARIABLES ***********************************************************************************************/
	private ArrayList<ActorRef> queues;
	private int passengersChecked = 0;
	private Random random = new Random();
	private int queueCount = 0;

	/**
	 * Constructor.
	 * @param queues - an ArrayList of QueueActors.
	 */
	public DocumentCheckerActor(ArrayList<ActorRef> queues) {
    	this.queues = queues;
    }

	/**
	 * Message handling.
	 * Receives:
	 * 		Passenger
	 *
	 * Sends:
	 * 		Passenger to QueueActors
	 * 		ShutDown to QueueActors
	 */
	@Override
    public void onReceive(Object message) throws Exception {

		/* If DocumentCheckerActor receives Passenger... */
    	if(message instanceof Passenger) {
    		passengersChecked++;
    		System.out.println("\tDocumentCheckerActor received Passenger " + ((Passenger) message).getID() + ".");

    		/* If the Passenger has proper documentation, assign them to the next QueueActor in the cycle */
    		if(random.nextDouble() > 0.2) {
				queues.get(queueCount).tell(message, getSelf());
				queueCount = ((queueCount + 1)  % queues.size());
				System.out.println("\tDocumentCheckerActor approves documents for Passenger " + ((Passenger) message).getID() + ". Passenger moving to the SecurityQueue.");
    		}
    		/* Else if the Passenger has forged documentation, send them away */
    		else {
    			System.out.println("\tDocumentCheckerActor turns Passenger " + ((Passenger) message).getID() + " away.");
    		}

    		/* If all of the Passengers have been processed... */
    		if(passengersChecked == 20) {
    			/* Shut down and notify each SecurityActor */
    			System.out.println("\tDocumentCheckerActor is shutting down. Notifying SecurityQueues.");
    			for(ActorRef q : queues) {
    				q.tell(new ShutDown(), getSelf());
				}
			}
    	}
    }
}
