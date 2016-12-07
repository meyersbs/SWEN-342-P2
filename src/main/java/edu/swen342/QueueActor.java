package edu.swen342;

import java.util.ArrayList;

/**
 * @project: SWEN-342 | TSA Airport
 *
 * @author: Benjamin S. Meyers
 * @author: Asma Sattar
 */

public class QueueActor {

    private final int LINE_NUMBER;
    private ArrayList<Passenger> PASSENGERS_IN_LINE = new ArrayList<Passenger>();

    public QueueActor(int lineNum) {
        this.LINE_NUMBER = lineNum;
    }

    public void addPassenger(Passenger p) { this.PASSENGERS_IN_LINE.add(p); }

    public Passenger getNextInLine() { return this.PASSENGERS_IN_LINE.remove(0); }
}
