package edu.swen342;

/*
 * @project: SWEN-342 | TSA Airport
 *
 * @author: Benjamin S. Meyers
 * @author: Asma Sattar
 */

/**
 * Passenger with ID. Passed between Actors.
 */
public class Passenger {
    /* Immutable ID */
    private final int ID;

    /** Constructor. */
    public Passenger(int id) { this.ID = id; }

    /** Get Passenger's ID */
    public int getID() { return this.ID; }
}
