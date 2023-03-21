package sharedRegions;

import entities.MasterThief;
import entities.MasterThiefStates;

public class CollectionSite {

    public CollectionSite() {

    }

    public void takeARest() {
        MasterThief master = (MasterThief) Thread.currentThread();
        master.setThiefState(MasterThiefStates.WAITING_ARRIVAL);

        //while (arrivedThieves == 0) {
        try {
            wait();
        } catch (InterruptedException e) {}
        //}
    }

    public static void handACanvas() {

    }

    public static void collectACanvas() {

    }

    public static void sumUpResults() {

    }
}
