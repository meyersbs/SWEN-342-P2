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
 * 1) Each body scanner is identified with the line it is in.
 * 2) Passengers randomly fail inspection with a probability of 20%.
*/
public class BodyCheckerActor extends UntypedActor {

    /* GLOBAL VARIABLES ***********************************************************************************************/
    private Random random = new Random();
	private final int LINE_NUMBER;
	private ActorRef securityActor;

    /** Constructor. */
    public BodyCheckerActor(int lineNum, ActorRef securityActor) {
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
     *      BodyCheckReport to SecurityActor
     *      BodyCheckerOff to SecurityActor
     */
    @Override
    public void onReceive(Object message) throws Exception{
        /* If BodyCheckerActor receives Passenger... */
        if(message instanceof Passenger){
            System.out.println("\tBodyCheckerActor is checking Passenger " + ((Passenger) message).getID() + "'s body.");

            /* If Passenger is not carrying prohibited items, give the SecurityActor an 'all-clear' */
            if(random.nextDouble() > 0.2) {
                System.out.println("\tBody inspection passed for Passenger " + ((Passenger) message).getID() + ".");
                BodyCheckReport b = new BodyCheckReport(((Passenger) message), false);
                securityActor.tell(b, getSelf());
            }
            /* If Passenger is carrying prohibited items, alert the SecurityActor */
            else {
                System.out.println("\tBody inspection failed for Passenger " + ((Passenger) message).getID() + "! Notifying airport security.");
                BodyCheckReport b = new BodyCheckReport(((Passenger) message), true);
                securityActor.tell(b, getSelf());
            }
        }
        /* Else if BodyCheckerActor receives ShutDown Signal, shutdown and notify SecurityActor */
        else if(message instanceof ShutDown) {
            System.out.println("\tBodyCheckerActor " + this.LINE_NUMBER + " is shutting down. Notifying SecurityActor " + this.LINE_NUMBER + ".");
            securityActor.tell(new BodyCheckerOff(), getSelf());
        }
    }
}
