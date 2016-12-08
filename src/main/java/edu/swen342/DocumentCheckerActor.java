package edu.swen342;

/**
 * @project: SWEN-342 | TSA Airport
 *
 * @author: Benjamin S. Meyers
 * @author: Asma Sattar
 */

import akka.actor.UntypedActor;
import akka.actor.ActorRef;
import java.util.ArrayList;
import java.util.Random;


import akka.actor.*;
import akka.actor.UntypedActor;
import akka.actor.Actor;

public class DocumentCheckerActor extends UntypedActor{

	/* Variables */

	//passneger queue
	private ArrayList<ActorRef> queues;
	private int passengersChecked = 0;
	private int passengersSentToQueue = 0;
	
	//random number generator for whether a passenger is turned away or allowed to proceed. 
	private Random random = new Random();

	//Queue tracker
	private int queueCount = 0;
    /**
     * Constructor.
     */
    public DocumentCheckerActor(ArrayList<ActorRef> queues) {
    	this.queues = queues;
    }

    @Override
    public void onReceive(Object message) throws Exception{
    	
    	if(message instanceof Passenger){
    		passengersChecked++;
    		System.out.println("Document Checker received Passenger " + ((Passenger) message).getID() + ".");
    		
    		/*  
			1) Passengers enter the system from a main driver program.
			2) Passengers are randomly turned away for document problems at a probability of 20%.
			3) Passengers not turned away enter the queue for one of the lines in a cyclic fashion.
    		*/
    		

    		/* #2 IF -> Passenger’s with proper documents are assigned to the queue for one of the lines. */
    		if (random.nextDouble() > 0.2) {
				
				/* Add Passenger to one of the lines */

				queues.get(queueCount).tell(message, getSelf());
				passengersSentToQueue++;
				queueCount = ((queueCount + 1)  % queues.size());
				System.out.println("Documents approved for passenger " + ((Passenger) message).getID() + ". Passenger moving to the Security Queue .");
    		}
    		/* ELSE -> Passenger’s with document problems are turned away..*/
    		else{
    			System.out.println("Document Checker turns passenger " + ((Passenger) message).getID() + " away.");
    		}

    		if(passengersChecked == 20) {
    			System.out.println("Document Checker has no more passengers to check. Notifying Queues.");
    			for(ActorRef q : queues) {
    				q.tell(new DocumentCheckerShuttingDown(passengersSentToQueue, queueCount), getSelf());
				}
			}

    		
    	}
    }
}
