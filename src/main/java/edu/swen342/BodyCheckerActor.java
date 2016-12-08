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
	1) Each body scanner is identified with the line it is in.
	2) Passengers randomly fail inspection with a probability of 20%.
*/
public class BodyCheckerActor extends UntypedActor {

    //random number generator for whether a passenger is turned away or allowed to proceed.
    private Random random = new Random();

	private final int LINE_NUMBER;
	private ActorRef securityActor;

    /**
     * Constructor.
     */
    public BodyCheckerActor(int lineNum, ActorRef securityActor) {
    	this.LINE_NUMBER = lineNum;
    	this.securityActor = securityActor;
    }


    @Override
    public void onReceive(Object message) throws Exception{
        if(message instanceof Passenger){
            System.out.println("Body Checker is checking Passenger " + ((Passenger) message).getID() + "'s body.");

            /*
            1) Each body scanner is identified with the line it is in.
            2) Passengers randomly fails inspection with a probability of 20%.
            */

            if(random.nextDouble() > 0.2) {
                System.out.println("Body inspection passed for Passenger " + ((Passenger) message).getID() + ".");
                BodyCheckReport b = new BodyCheckReport(((Passenger) message), false);
                securityActor.tell(b, getSelf());
            }
            else {
                System.out.println("Body inspection failed for Passenger " + ((Passenger) message).getID() + "! Notifying airport security.");
                BodyCheckReport b = new BodyCheckReport(((Passenger) message), true);
                securityActor.tell(b, getSelf());
            }
        }
    }
}
