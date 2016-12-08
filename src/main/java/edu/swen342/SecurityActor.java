package edu.swen342;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

import java.util.ArrayList;

/**
 * @project: SWEN-342 | TSA Airport
 *
 * @author: Benjamin S. Meyers
 * @author: Asma Sattar
 */

/*
1) Each security station is identified with the line it is in.

2) The security station must be prepared to have either the 
	body scanner or the baggage scanner get ahead of the other by an arbitrary amount 
	- that is, it must remember results until both scan reports for a passenger arrive.

3) The security station cannot close until both of the scanners attached have turned off.
*/

public class SecurityActor extends UntypedActor {

	private final int LINE_NUMBER;
    private ArrayList<SecuritySummary> passengers = new ArrayList<SecuritySummary>();
    private ActorRef jailActor;

    /**
     * Constructor.
     */
    public SecurityActor(int lineNum, ActorRef jailActor) {
    	this.LINE_NUMBER = lineNum;
    	this.jailActor = jailActor;
    }


    @Override
    public void onReceive(Object message) throws Exception{
        int tempPassengerID = -999;
        boolean summaryExists = false;
        if(message instanceof BagCheckReport) {
            System.out.println("Security Actor " + this.LINE_NUMBER + " received BagCheckReport for Passenger " + ((BagCheckReport) message).getPassenger().getID() + ".");
            tempPassengerID = ((BagCheckReport) message).getPassenger().getID();
            summaryExists = false;
            for(SecuritySummary s : this.passengers) {
                if(s.getPassenger().getID() == tempPassengerID) {
                    s.setBag(((BagCheckReport) message));
                    summaryExists = true;
                    break;
                }
            }
            if(!summaryExists) {
                SecuritySummary s = new SecuritySummary(((BagCheckReport) message).getPassenger());
                s.setBag(((BagCheckReport) message));
                this.passengers.add(s);
            }
        }
        else if(message instanceof BodyCheckReport) {
            System.out.println("Security Actor " + this.LINE_NUMBER + " received BodyCheckReport for Passenger " + ((BodyCheckReport) message).getPassenger().getID() + ".");
            tempPassengerID = ((BodyCheckReport) message).getPassenger().getID();
            summaryExists = false;
            for(SecuritySummary s : this.passengers) {
                if(s.getPassenger().getID() == tempPassengerID) {
                    s.setBody(((BodyCheckReport) message));
                    summaryExists = true;
                    break;
                }
            }
            if(!summaryExists) {
                SecuritySummary s = new SecuritySummary(((BodyCheckReport) message).getPassenger());
                s.setBody(((BodyCheckReport) message));
                this.passengers.add(s);
            }
        }

        for(SecuritySummary s : this.passengers) {
            if(s.getPassenger().getID() == tempPassengerID && summaryExists) {
                try {
                    if (!s.getBag() || !s.getBody()) {
                        System.out.println("Passenger " + s.getPassenger().getID() + " has failed security inspection. Notifying Jail Actor.");
                        this.jailActor.tell(s.getPassenger(), getSelf());
                    }
                } catch(NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class SecuritySummary {
        private BagCheckReport bag;
        private BodyCheckReport body;
        private final Passenger PASSENGER;

        SecuritySummary(Passenger p) { PASSENGER = p; }

        void setBag(BagCheckReport b) { this.bag = b; }
        void setBody(BodyCheckReport b) { this.body = b; }

        boolean getBag() { return this.bag.sendToJail(); }
        boolean getBody() { return this.body.sendToJail(); }
        Passenger getPassenger() { return PASSENGER; }
    }
}
