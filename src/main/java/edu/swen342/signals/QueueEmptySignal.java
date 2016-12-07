package edu.swen342.signals;

/**
 * @project: SWEN-342 | TSA Airport
 *
 * @author: Benjamin S. Meyers
 * @author: Asma Sattar
 */

public class QueueEmptySignal {

    private final int LINE_NUMBER;

    /**
     * Constructor.
     */
    public QueueEmptySignal(int lineNum) {
        this.LINE_NUMBER = lineNum;
    }

    public int getQueueNumber() { return this.LINE_NUMBER; }
}
