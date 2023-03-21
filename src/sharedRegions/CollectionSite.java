package sharedRegions;

import entities.MasterThief;
import entities.MasterThiefStates;

import static utils.Parameters.*;

public class CollectionSite {
    private int collectedCanvas;
    private boolean endHeist;

    public CollectionSite() {
        collectedCanvas = 0;
        endHeist = false;
    }

    public synchronized int appraiseSit() {
        // check if the heist should end
        if (endHeist) return END_HEIST;

        // check if it should wait for canvas
        // TODO

        // otherwise, make more assault parties
        return CREATE_ASSAULT_PARTY;
    }

    public synchronized void takeARest() {
        MasterThief master = (MasterThief) Thread.currentThread();
        master.setThiefState(MasterThiefStates.WAITING_ARRIVAL);

        //while (arrivedThieves == 0) {
        try {
            wait();
        } catch (InterruptedException e) {}
        //}
    }

    public static void handACanvas(int assaultID) {

    }

    public static void collectACanvas() {

    }

    public static void sumUpResults() {

    }
}
