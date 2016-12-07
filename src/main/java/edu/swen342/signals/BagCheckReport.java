package edu.swen342.signals;

/**
 * @project: SWEN-342 | TSA Airport
 *
 * @author: Benjamin S. Meyers
 * @author: Asma Sattar
 */

public class BagCheckReport {

    private final int PASSENGER_ID;
    private final boolean SEND_TO_JAIL;

    public BagCheckReport(int id, boolean status) {
        this.PASSENGER_ID = id;
        this.SEND_TO_JAIL = status;
    }

    public int getPassengerID() { return this.PASSENGER_ID; }

    public boolean sendToJail() { return this.SEND_TO_JAIL; }
}
