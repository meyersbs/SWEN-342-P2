package edu.swen342;

/**
 * @project: SWEN-342 | TSA Airport
 *
 * @author: Benjamin S. Meyers
 * @author: Asma Sattar
 */

import akka.actor.UntypedActor;

/*
	1) Each body scanner is identified with the line it is in.
	2) Passengers randomly fail inspection with a probability of 20%.
*/
public class BodyCheckerActor extends UntypedActor {

	private final int LINE_NUMBER;

    /**
     * Constructor.
     */
    public BodyCheckerActor(int lineNum) {
    	this.LINE_NUMBER = lineNum;
    }


    @Override
    public void onReceive(Object message) throws Exception{
    	/* TODO: add the onReceive info here */
    }
}
