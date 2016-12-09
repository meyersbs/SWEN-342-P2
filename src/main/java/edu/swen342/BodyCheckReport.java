package edu.swen342;

/*
 * @project: SWEN-342 | TSA Airport
 *
 * @author: Benjamin S. Meyers
 * @author: Asma Sattar
 */

/**
 * BagCheckReport with reference to Passenger and alert status. Passed between Actors.
 */
public class BodyCheckReport {

    /* Immutable variables */
    private final Passenger PASSENGER;
    private final boolean SEND_TO_JAIL;

    /** Constructor. */
    public BodyCheckReport(Passenger p, boolean status) {
        this.PASSENGER = p;
        this.SEND_TO_JAIL = status;
    }

    /** Get this.PASSENGER */
    public Passenger getPassenger() { return this.PASSENGER; }
    /** Get this.SEND_TO_JAIL */
    public boolean sendToJail() { return this.SEND_TO_JAIL; }
}
