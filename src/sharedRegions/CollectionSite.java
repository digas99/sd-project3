package sharedRegions;

import entities.MasterThief;
import entities.MasterThiefStates;
import entities.OrdinaryThief;
import entities.OrdinaryThiefStates;

import static utils.Parameters.*;
import static utils.Utils.all;
import static utils.Utils.logger;

public class CollectionSite {

    private int canvas;
    private int canvasToCollect;
    private boolean[] emptiedRooms;

    @Override
    public String toString() {
        return "Collection Site";
    }

    public CollectionSite(GeneralRepos repos) {
        canvas = canvasToCollect = 0;
        emptiedRooms = new boolean[N_ROOMS];
    }

    public synchronized int appraiseSit() {
        MasterThief masterThief = (MasterThief) Thread.currentThread();

        if (all(emptiedRooms))
            return END_HEIST;

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
        OrdinaryThief ordinaryThief = (OrdinaryThief) Thread.currentThread();
        ordinaryThief.setThiefState(OrdinaryThiefStates.COLLECTION_SITE);

        // if ordinary thief doesn't bring a canvas, then the room is empty
        if (!ordinaryThief.hasCanvas())
            emptiedRooms[ordinaryThief.getRoomID()] = true;
        else
            canvas++;

        canvasToCollect++;
    }

    public synchronized void collectACanvas() {
        canvasToCollect--;
    }

    public synchronized void sumUpResults() {
        MasterThief masterThief = (MasterThief) Thread.currentThread();
        masterThief.setThiefState(MasterThiefStates.PRESENTING_REPORT);

        logger(this, "The heist is over! Were collected " + canvas + " canvases.");
    }
}
