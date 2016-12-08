package edu.swen342;

import java.util.ArrayList;

import akka.actor.UntypedActor;
import akka.actor.ActorRef;

/**
 * @project: SWEN-342 | TSA Airport
 *
 * @author: Benjamin S. Meyers
 * @author: Asma Sattar
 */

public class QueueActor extends UntypedActor{

    private final int LINE_NUMBER;
    private ActorRef bodyChecker;
    private ActorRef bagChecker;

    public QueueActor(int lineNum, ActorRef bodyChecker, ActorRef bagChecker) {
        this.LINE_NUMBER = lineNum;
        this.bodyChecker = bodyChecker;
        this.bagChecker = bagChecker;
    }



    @Override
    public void onReceive(Object message) throws Exception{
    	
    	if(message instanceof Passenger){
    		System.out.println("Queue Actor received Passenger " + ((Passenger) message).getID() + ".");
    		
    		/*  
			1) Passengers can go to the body scanner only when it is ready.
			2) Passengers place their baggage in the baggage scanner as soon as they enter a queue.
			3) If a passenger and his or her baggage are both passed, the passenger leaves the system. Otherwise, the passenger goes to jail.
    		*/
    		
    		/* #1 */
    		bodyChecker.tell(message, getSelf());
    		/* #2 */ 
    		Bag passengerBag = ((Passenger) message).getBag();
    		bagChecker.tell(passengerBag, getSelf());
    	}
    }
}
