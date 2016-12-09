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

/**
 * 1) Passengers can go to the body scanner only when it is ready.
 * 2) Passengers place their baggage in the baggage scanner as soon as they enter a queue.
 * 3) If a passenger and his or her baggage are both passed, the passenger leaves the system. Otherwise, the passenger
 *      goes to jail.
 */
public class QueueActor extends UntypedActor{

    /* GLOBAL VARIABLES ***********************************************************************************************/
    private final int LINE_NUMBER;
    private ActorRef bodyChecker;
    private ActorRef bagChecker;
    private ActorRef securityActor;

    /** Constructor. */
    public QueueActor(int lineNum, ActorRef bodyChecker, ActorRef bagChecker, ActorRef securityActor) {
        this.LINE_NUMBER = lineNum;
        this.bodyChecker = bodyChecker;
        this.bagChecker = bagChecker;
        this.securityActor = securityActor;
    }

    /**
     * Message handling.
     * Receives:
     *      Passenger from DocumentCheckerActor
     *      DocumentCheckerShuttingDown from DocumentCheckerActor
     *
     * Sends:
     *      Passenger to BagCheckerActor
     *      Passenger to BodyCheckerActor
     *      ShutDown to BagCheckerActor
     *      ShutDown to BodyCheckerActor
     */
    @Override
    public void onReceive(Object message) throws Exception{
    	/* If QueueActor receives Passenger... */
    	if(message instanceof Passenger){
    		System.out.println("\tQueueActor " + this.LINE_NUMBER + " received Passenger " + ((Passenger) message).getID() + ".");
    		/* Alert the BagCheckerActor and BodyCheckerActor */
    		bodyChecker.tell(message, getSelf());
    		bagChecker.tell(message, getSelf());
    	}
    	/* Else if QueueActor receives DocumentCheckerShuttingDown */
    	else if(message instanceof DocumentCheckerShuttingDown) {
    	    System.out.println("\tQueueActor " + this.LINE_NUMBER + " received DocumentCheckerShuttingDown Signal.");
    	    /* Send ShutDown Signal to BagCheckerActor and BodyCheckerActor */
    	    bodyChecker.tell(new ShutDown(), getSelf());
            bagChecker.tell(new ShutDown(), getSelf());
        }
    }
}
