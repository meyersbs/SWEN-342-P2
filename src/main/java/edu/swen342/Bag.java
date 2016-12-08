package edu.swen342;

/**
 * @project: SWEN-342 | TSA Airport
 *
 * @author: Benjamin S. Meyers
 * @author: Asma Sattar
 */

public class Bag {

    /* Bag id should not change*/
    private final int ID;

    /**
     * Constructor.
     */
    public Bag(int id) {
        this.ID = id;
    }


    /*
    * Returns Bag id
    */
    public int getID() { 
        return this.ID; 
    }
}
