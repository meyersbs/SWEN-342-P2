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
    private ActorRef securityActor;
    private int passengersSeen = 0;
    private boolean docCheckerDone = false;

    public QueueActor(int lineNum, ActorRef bodyChecker, ActorRef bagChecker, ActorRef securityActor) {
        this.LINE_NUMBER = lineNum;
        this.bodyChecker = bodyChecker;
        this.bagChecker = bagChecker;
        this.securityActor = securityActor;
    }



    @Override
    public void onReceive(Object message) throws Exception{
    	
    	if(message instanceof Passenger){
    	    this.passengersSeen++;
    		System.out.println("Queue Actor " + this.LINE_NUMBER + " received Passenger " + ((Passenger) message).getID() + ".");
    		
    		/*  
			1) Passengers can go to the body scanner only when it is ready.
			2) Passengers place their baggage in the baggage scanner as soon as they enter a queue.
			3) If a passenger and his or her baggage are both passed, the passenger leaves the system. Otherwise, the passenger goes to jail.
    		*/
    		
    		/* #1 */
    		bodyChecker.tell(message, getSelf());
    		/* #2 */
    		bagChecker.tell(message, getSelf());
    	}
    	else if(message instanceof DocumentCheckerShuttingDown) {
    	    System.out.println("QueueActor " + this.LINE_NUMBER + " received DocumentCheckerShuttingDown Signal.");
    	    bodyChecker.tell(new ShutDown(), getSelf());
            bagChecker.tell(new ShutDown(), getSelf());
        }
    }
}
