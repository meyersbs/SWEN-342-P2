package edu.swen342;

/**
 * @project: SWEN-342 | TSA Airport
 *
 * @author: Benjamin S. Meyers
 * @author: Asma Sattar
 */

public class Passenger {

    private final int ID;
    private int currentLine = -1;

    /**
     * Constructor.
     */
    public Passenger(int id) {
        this.ID = id;
    }

    public int getID() { return this.ID; }

    public int getCurrentLine() { return this.currentLine; }

    public void setCurrentLine(int lineNum) { this.currentLine = lineNum; }
}
