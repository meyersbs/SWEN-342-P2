package edu.swen342;

import java.util.ArrayList;

/**
 * @project: SWEN-342 | TSA Airport
 *
 * @author: Benjamin S. Meyers
 * @author: Asma Sattar
 */

public class JailActor {

    private ArrayList<Passenger> prisoners = new ArrayList<Passenger>();

    /**
     * Constructor.
     */
    public JailActor() {}

    public void addPrisoner(Passenger p) { this.prisoners.add(p); }
}
