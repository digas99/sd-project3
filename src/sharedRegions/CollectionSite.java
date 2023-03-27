package sharedRegions;

import entities.MasterThief;
import entities.MasterThiefStates;

import static utils.Parameters.*;
import static utils.Utils.logger;

public class CollectionSite {

    private int canvas;
    private int canvasToCollect;

    @Override
    public String toString() {
        return "Collection Site";
    }

    public CollectionSite(GeneralRepos repos) {
        canvas = canvasToCollect = 0;
    }

    public synchronized int appraiseSit() {
        MasterThief masterThief = (MasterThief) Thread.currentThread();

        if (masterThief.getActiveAssaultParties() > 0 && masterThief.getConcentrationSite().occupancy() < N_THIEVES_PER_PARTY)
            return WAIT_FOR_CANVAS;

        return CREATE_ASSAULT_PARTY;
    }

    public synchronized void takeARest() {
        // sleep until an ordinary thief reaches the collection site and hands a canvas
        while (canvasToCollect == 0) {
            try {
                wait();
            } catch (InterruptedException e) {e.printStackTrace();}
        }
    }

    public synchronized void handACanvas() {
        canvasToCollect++;
    }

    public synchronized void collectACanvas() {
        canvas++;
        canvasToCollect--;
    }

    public synchronized void sumUpResults() {
        MasterThief masterThief = (MasterThief) Thread.currentThread();
        masterThief.setThiefState(MasterThiefStates.PRESENTING_REPORT);
        
        logger(this, "The heist is over! Were collected " + canvas + " canvases.");
    }
}
