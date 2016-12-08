package edu.swen342;

/**
 * @project: SWEN-342 | TSA Airport
 *
 * @author: Benjamin S. Meyers
 * @author: Asma Sattar
 */

import akka.actor.UntypedActor;

/*
	1) Each baggage scanner is identified with the line it is in.
	2) Baggage randomly fails inspection with a probability of 20%.
*/
public class BagCheckerActor extends UntypedActor {

	private final int LINE_NUMBER;

    /**
     * Constructor.
     */
    public BagCheckerActor(int lineNum) {
    	this.LINE_NUMBER = lineNum;
    }


    @Override
    public void onReceive(Object message) throws Exception{
    	/* TODO: add the onReceive info here */
    }
}
