package edu.swen342;

/*
 * @project: SWEN-342 | TSA Airport
 *
 * @author: Benjamin S. Meyers
 * @author: Asma Sattar
 */

/* IMPORTS ************************************************************************************************************/
import java.util.ArrayList;
import akka.actor.UntypedActor;

/**
 * 1) The jail knows how many security stations feed it passengers as prisoners.
 * 2) The jail will hold all prisoners until the end of the day when all prisoners in holding are sent to permanent
 * 		detention.
 */
public class JailActor extends UntypedActor{

	/* GLOBAL VARIABLES ***********************************************************************************************/
    private ArrayList<Passenger> prisoners = new ArrayList<Passenger>();
    private int numOfSecurityStations;
    private int stationEndDayCounter = 0;

    /** Constructor. */
    public JailActor(int n) {
    	this.numOfSecurityStations = n;
    }

	/**
	 * Message handling.
	 * Receives:
	 * 		Passenger from SecurityActors
	 * 		EndOfDay from SecurityActors
	 */
	@Override
    public void onReceive(Object message) throws Exception{
    	/* If JailActor receives Passenger, put them in holding */
    	if(message instanceof Passenger) {
    		System.out.println("\tJailActor received Passenger " + ((Passenger) message).getID() + ".");

    		prisoners.add(((Passenger) message));
			System.out.println("\tPassenger " + ((Passenger) message).getID() + " is placed in holding.");
    	}
    	/* Else if JailActor receives EndOfDay... */
    	else if(message instanceof EndOfDay ){
			System.out.println("\tJailActor received EndOfDay signal from SecurityActor " + ((EndOfDay) message).getID() + ".");

			/* Keep track of how many EndOfDay Signals have been received */
    		stationEndDayCounter++;

    		/* If all SecurityActors have sent an EndOfDay Signal */
    		if(stationEndDayCounter == numOfSecurityStations){
    			System.out.println("#### CLOSING TIME ####");

    			/* Send all of the prisoners to permanent detention */
				for (Passenger prisoner : prisoners) {
					System.out.println("\tJailActor is sending Passenger " + prisoner.getID() + " to permanent detention.");
				}
    			System.out.println("\tJailActor is locking up the airport.");
    			System.out.println("#### AIRPORT IS CLOSED ####");
    			/* Terminate all Actors */
				getContext().system().terminate();
    		}
    	}
    }
}
