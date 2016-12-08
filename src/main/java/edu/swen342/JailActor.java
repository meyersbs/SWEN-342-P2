package edu.swen342;

import java.util.ArrayList;

import akka.actor.UntypedActor;
//import static akka.actor.Actors.*;

/**
 * @project: SWEN-342 | TSA Airport
 *
 * @author: Benjamin S. Meyers
 * @author: Asma Sattar
 */

/*
	The jail knows how many security stations feed it passengers as prisoners.
	
	The jail will hold all prisoners until the end of the day when all prisoners 
	in holding are sent to permanent detention.
*/
public class JailActor extends UntypedActor{

    private ArrayList<Passenger> prisoners = new ArrayList<Passenger>();
    private int numOfSecurityStations;
    private int station_EndDay_counter =0;
    /**
     * Constructor.
     */
    public JailActor(int numOfSecuretyStations) {
    	this.numOfSecurityStations = numOfSecuretyStations;
    }

    @Override
    public void onReceive(Object message) throws Exception{
    	
    	if(message instanceof Passenger){
    		System.out.println("Jail Actor received Passenger " + ((Passenger) message).getID() + ".");
    		
    		//Add Person to prisoners array
    		prisoners.add(((Passenger) message));
			System.out.println("Passenger " + ((Passenger) message).getID() + " is in holding.");

    	}else if (message instanceof EndOfDay){ /*TODO: make EndOfDay class */
    		//increment the station_EndDay_counter counter each time the EndOfDay is sent
    		station_EndDay_counter++;
    		/* If EndOfDay == numOFSecretyStation */
    		if(station_EndDay_counter == numOfSecurityStations){
    			System.out.println("Closing Time");
    			//Loop over the prisoners and send them to permanent detention
				for(int i = 0; i < prisoners.size(); i++) {
					System.out.println("JailActor is sending Passenger " + prisoners.get(i).getID() + " to permanent detention.");
				}
    			for(int i = 0; i < prisoners.size(); i++) {
    				/* TODO: Kill all remaining prisoners*/
    				//prisoners.get(i).tell(poisonPill());
    			}
    			System.out.println("The Jail is shutting down for the night.");
    			/* TODO: shut down everything s*/
    			// Actors.registry().shutdownAll();
    		}
    	}
    }

}
