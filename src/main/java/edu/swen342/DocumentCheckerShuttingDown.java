package edu.swen342;

/**
 * @project: SWEN-342 | TSA Airport
 *
 * @author: Benjamin S. Meyers
 * @author: Asma Sattar
 */

/*

*/
public class DocumentCheckerShuttingDown {

    private final int PASSENGERS_SENT_TO_QUEUE;
    private final int NUM_QUEUES;
    /**
     * Constructor.
     */
    public DocumentCheckerShuttingDown(int p, int q) {
        this.PASSENGERS_SENT_TO_QUEUE = p;
        this.NUM_QUEUES = q;
    }

    public int getPass2Queue() { return this.PASSENGERS_SENT_TO_QUEUE; }

    public int getNumQueues() { return this.NUM_QUEUES; }
}
