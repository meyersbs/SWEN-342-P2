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
import java.util.ArrayList;

/**
 * 1) Each SecurityActor is identified by the QueueActor it is associated with.
 * 2) The SecurityActor must be prepared to have either the BodyScanner or the BagScanner get ahead of the other by an
 *      arbitrary amount - that is, it must remember results until both scan reports for a Passenger arrive.
 * 3) The SecurityActor cannot close until both of the scanners attached have turned off.
 */
public class SecurityActor extends UntypedActor {

    /* GLOBAL VARIABLES ***********************************************************************************************/
	private final int LINE_NUMBER;
	private ArrayList<SecuritySummary> passengers = new ArrayList<SecuritySummary>();
	private ActorRef jailActor;
	private boolean bagCheckerOff = false;
	private boolean bodyCheckerOff = false;

	/**
     * Constructor.
     * @param lineNum - the line number that the SecurityActor is part of.
     * @param jailActor - the JailActor to send shady Passengers to.
     */
    public SecurityActor(int lineNum, ActorRef jailActor) {
    	this.LINE_NUMBER = lineNum;
    	this.jailActor = jailActor;
    }

    /**
     * Message handling.
     * Receives:
     *      BagCheckReport from BagCheckerActor
     *      BodyCheckReport from BodyCheckerActor
     *      BagCheckerOff from BagCheckerActor
     *      BodyCheckerOff from BodyCheckerActor
     *
     * Sends:
     *      Passenger to JailActor
     *      EndOfDay to JailActor
     */
    @Override
    public void onReceive(Object message) throws Exception {
        int tempPassengerID = -999;
        boolean summaryExists = false;
        /* If SecurityActor receives a BagCheckReport ... */
        if(message instanceof BagCheckReport) {
            System.out.println("\tSecurityActor " + this.LINE_NUMBER + " received BagCheckReport for Passenger " + ((BagCheckReport) message).getPassenger().getID() + ".");
            tempPassengerID = ((BagCheckReport) message).getPassenger().getID();
            summaryExists = false;
            /* Check and see if other reports have been received for this Passenger */
            for(SecuritySummary s : this.passengers) {
                /* If they have, update the SecuritySummary */
                if(s.getPassenger().getID() == tempPassengerID) {
                    s.setBag(((BagCheckReport) message));
                    summaryExists = true;
                    break;
                }
            }
            /* Otherwise, create a new SecuritySummary and add the BagCheckReport */
            if(!summaryExists) {
                SecuritySummary s = new SecuritySummary(((BagCheckReport) message).getPassenger());
                s.setBag(((BagCheckReport) message));
                this.passengers.add(s);
            }
        }
        /* Else if SecurityActor receives BodyCheckReport... */
        else if(message instanceof BodyCheckReport) {
            System.out.println("\tSecurityActor " + this.LINE_NUMBER + " received BodyCheckReport for Passenger " + ((BodyCheckReport) message).getPassenger().getID() + ".");
            tempPassengerID = ((BodyCheckReport) message).getPassenger().getID();
            summaryExists = false;
            /* Check and see if other reports have been received for this Passenger */
            for(SecuritySummary s : this.passengers) {
                /* If they have, update the SecuritySummary */
                if(s.getPassenger().getID() == tempPassengerID) {
                    s.setBody(((BodyCheckReport) message));
                    summaryExists = true;
                    break;
                }
            }
            /* Otherwise, create a new SecuritySummary and add the BodyCheckReport */
            if(!summaryExists) {
                SecuritySummary s = new SecuritySummary(((BodyCheckReport) message).getPassenger());
                s.setBody(((BodyCheckReport) message));
                this.passengers.add(s);
            }
        }
        /* Else if SecurityActor receives BodyCheckerOff Signal... */
        else if(message instanceof BodyCheckerOff) {
            System.out.println("\tSecurityActor " + this.LINE_NUMBER + " received BodyCheckerOff Signal from BodyCheckerActor " + this.LINE_NUMBER + ".");
            /* Set boolean flag */
            this.bodyCheckerOff = true;
        }
        /* Else if SecurityActor receives BagCheckerOff Signal... */
        else if(message instanceof BagCheckerOff) {
            System.out.println("\tSecurityActor " + this.LINE_NUMBER + " received BagCheckerOff Signal from BagCheckerActor " + this.LINE_NUMBER + ".");
            /* Set boolean flag */
            this.bagCheckerOff = true;
        }

        /* For each SecuritySummary... */
        for(SecuritySummary s : this.passengers) {
            /* If both the BagCheckReport and the BodyCheckReport for a single Passenger have been received... */
            if(s.getPassenger().getID() == tempPassengerID && summaryExists) {
                try {
                    /* If either check has failed, send the Passenger to the JailActor */
                    if (s.sendToJail()) {
                        System.out.println("\tPassenger " + s.getPassenger().getID() + " has failed security inspection. Notifying JailActor.");
                        this.jailActor.tell(s.getPassenger(), getSelf());
                    /* Otherwise, send them off to their flight */
                    } else {
                        System.out.println("\tPassenger " + s.getPassenger().getID() + " has passed security inspection. Passenger has left security area.");
                    }
                } catch(NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }

        /*
         * If both scanner associated with the SecurityActor's QueueActor have shut down, shut down and notify the
         * JailActor
         */
        if(bodyCheckerOff && bagCheckerOff) {
            System.out.println("\tSecurityActor " + this.LINE_NUMBER + " is shutting down. Notifying JailActor.");
            jailActor.tell(new EndOfDay(this.LINE_NUMBER), getSelf());
        }
    }

    /** Internal class for storing reports related to a single Passenger. */
    class SecuritySummary {
        private BagCheckReport bag;
        private BodyCheckReport body;
        private final Passenger PASSENGER;

        /** Constructor. */
        SecuritySummary(Passenger p) { this.PASSENGER = p; }

        /** Set this.bag to b */
        void setBag(BagCheckReport b) { this.bag = b; }
        /** Set this.body to b */
        void setBody(BodyCheckReport b) { this.body = b; }

        /** Get this.bag.sendToJail() */
        boolean getBag() { return this.bag.sendToJail(); }
        /** Get this.body.sendToJail() */
        boolean getBody() { return this.body.sendToJail(); }
        /** Get this.PASSENGER */
        Passenger getPassenger() { return this.PASSENGER; }
        boolean sendToJail() { return getBag() || getBody(); }
    }
}
