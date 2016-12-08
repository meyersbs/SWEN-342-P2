package edu.swen342;

/**
 * @project: SWEN-342 | TSA Airport
 *
 * @author: Benjamin S. Meyers
 * @author: Asma Sattar
 */

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

import java.util.Random;

/*
	1) Each baggage scanner is identified with the line it is in.
	2) Baggage randomly fails inspection with a probability of 20%.
*/
public class BagCheckerActor extends UntypedActor {

    //random number generator for whether a passenger is turned away or allowed to proceed.
    private Random random = new Random();

	private final int LINE_NUMBER;
	private ActorRef securityActor;

    /**
     * Constructor.
     */
    public BagCheckerActor(int lineNum, ActorRef securityActor) {
    	this.LINE_NUMBER = lineNum;
    	this.securityActor = securityActor;
    }


    @Override
    public void onReceive(Object message) throws Exception{

        if(message instanceof Passenger){
            System.out.println("Bag Checker received Passenger " + ((Passenger) message).getID() + "'s bag.");

            /*
            1) Each baggage scanner is identified with the line it is in.
            2) Baggage randomly fails inspection with a probability of 20%.
            */

            if(random.nextDouble() > 0.2) {
                System.out.println("Bag passed inspection for Passenger " + ((Passenger) message).getID() + ".");
                BagCheckReport b = new BagCheckReport(((Passenger) message), false);
                securityActor.tell(b, getSelf());
            }
            else {
                System.out.println("Bag failed inspection for Passenger " + ((Passenger) message).getID() + "! Notifying airport security.");
                BagCheckReport b = new BagCheckReport(((Passenger) message), true);
                securityActor.tell(b, getSelf());
            }
        }
        else if(message instanceof ShutDown) {
            System.out.println("BagCheckerActor " + this.LINE_NUMBER + " received ShutDown Signal.");
            securityActor.tell(new BagScannerOff(), getSelf());
        }
    }

}
