package edu.swen342;

/**
 * @project: SWEN-342 | TSA Airport
 *
 * @author: Benjamin S. Meyers
 * @author: Asma Sattar
 */

public class BagCheckReport {

    private final Passenger PASSENGER;
    private final boolean SEND_TO_JAIL;

    public BagCheckReport(Passenger p, boolean status) {
        this.PASSENGER = p;
        this.SEND_TO_JAIL = status;
    }

    public Passenger getPassenger() { return this.PASSENGER; }

    public boolean sendToJail() { return this.SEND_TO_JAIL; }
}
