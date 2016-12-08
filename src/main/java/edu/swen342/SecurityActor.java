package edu.swen342;

import akka.actor.UntypedActor;

/**
 * @project: SWEN-342 | TSA Airport
 *
 * @author: Benjamin S. Meyers
 * @author: Asma Sattar
 */

/*
1) Each security station is identified with the line it is in.

2) The security station must be prepared to have either the 
	body scanner or the baggage scanner get ahead of the other by an arbitrary amount 
	- that is, it must remember results until both scan reports for a passenger arrive.

3) The security station cannot close until both of the scanners attached have turned off.
*/

public class SecurityActor extends UntypedActor {

	private final int LINE_NUMBER;

    /**
     * Constructor.
     */
    public SecurityActor(int lineNum) {
    	this.LINE_NUMBER = lineNum;
    }


    @Override
    public void onReceive(Object message) throws Exception{
    	/* TODO: add the onReceive info here */
    }
}
