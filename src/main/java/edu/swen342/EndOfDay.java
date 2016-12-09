package edu.swen342;

/*
 * @project: SWEN-342 | TSA Airport
 *
 * @author: Benjamin S. Meyers
 * @author: Asma Sattar
 */

/**
 * Signal sent from SecurityActors to JailActor.
 */
public class EndOfDay {

    /* Immutable ID */
    private final int ID;

    /** Constructor. */
    public EndOfDay(int id) {
        this.ID = id;
    }

    /** Get this.ID */
    public int getID() { return this.ID; }
}
