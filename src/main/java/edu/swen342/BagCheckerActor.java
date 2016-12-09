package edu.swen342;

/*
 * @project: SWEN-342 | TSA Airport
 *
 * @author: Benjamin S. Meyers
 * @author: Asma Sattar
 */

/* IMPORTS ************************************************************************************************************/
import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import java.util.Random;

/**
 * 1) Each baggage scanner is identified with the line it is in.
 * 2) Baggage randomly fails inspection with a probability of 20%.
 */
public class BagCheckerActor extends UntypedActor {

    /* GLOBAL VARIABLES ***********************************************************************************************/
    private Random random = new Random();
	private final int LINE_NUMBER;
	private ActorRef securityActor;

    /** Constructor. */
    public BagCheckerActor(int lineNum, ActorRef securityActor) {
    	this.LINE_NUMBER = lineNum;
    	this.securityActor = securityActor;
    }

    /**
     * Message handling.
     * Receives:
     *      Passenger from QueueActor
     *      ShutDown from QueueActor
     *
     * Sends:
     *      BagCheckReport to SecurityActor
     *      BagScannerOff to SecurityActor
     */
    @Override
    public void onReceive(Object message) throws Exception{
        /* If BagCheckerActor receives Passenger... */
        if(message instanceof Passenger){
            System.out.println("\tBagCheckerActor received Passenger " + ((Passenger) message).getID() + "'s bag.");

            /* If Passenger's Bag does not have prohibited items, give the SecurityActor an 'all-clear' */
            if(random.nextDouble() > 0.2) {
                System.out.println("\tBag passed inspection for Passenger " + ((Passenger) message).getID() + ".");
                BagCheckReport b = new BagCheckReport(((Passenger) message), false);
                securityActor.tell(b, getSelf());
            }
            /* If Passenger's Bag does have prohibited items, alert the SecurityActor */
            else {
                System.out.println("\tBag failed inspection for Passenger " + ((Passenger) message).getID() + "! Notifying airport security.");
                BagCheckReport b = new BagCheckReport(((Passenger) message), true);
                securityActor.tell(b, getSelf());
            }
        }
        /* Else if BagCheckerActor receives ShutDown Signal, shutdown and notify SecurityActor */
        else if(message instanceof ShutDown) {
            System.out.println("\tBagCheckerActor " + this.LINE_NUMBER + " is shutting down. Notifying SecurityActor " + this.LINE_NUMBER + ".");
            securityActor.tell(new BagScannerOff(), getSelf());
        }
    }
}
