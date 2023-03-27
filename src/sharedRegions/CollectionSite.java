package sharedRegions;

import entities.MasterThief;
import entities.MasterThiefStates;
import entities.OrdinaryThief;
import entities.OrdinaryThiefStates;
import utils.MemException;
import utils.MemFIFO;

import java.util.Arrays;

import static utils.Parameters.*;
import static utils.Utils.logger;
import static utils.Utils.min;

public class CollectionSite {
    private final boolean[] inside;
    private int canvas;
    private boolean endHeist;
    private final int[] roomState;
    private final int[] thiefCanvasState;
    private MemFIFO<Integer> thiefQueue;
    private int appraisedThief;
    private final boolean[] partiesInSite;
    private final boolean[] registeredThieves;

    @Override
    public String toString() {
        return "Collection Site";
    }

    public int occupancy() {
        int count = 0;
        for (int i = 0; i < N_THIEVES_ORDINARY; i++) {
            if (inside[i]) count++;
        }
        return count;
    }

    private int canvasToCollect() {
        int count = 0;
        for (int i = 0; i < N_THIEVES_ORDINARY; i++)
            if (thiefCanvasState[i] != UNKNOWN) count++;
        return count;
    }

    private int numberPartiesInSite() {
        int count = 0;
        for (int i = 0; i < N_ASSAULT_PARTIES; i++)
            if (partiesInSite[i]) count++;
        return count;
    }

    public CollectionSite(GeneralRepos repos) {
        canvas = 0;
        appraisedThief = -1;
        registeredThieves = new boolean[N_THIEVES_ORDINARY];
        inside = new boolean[N_THIEVES_ORDINARY];
        roomState = new int[N_ROOMS];
        for (int i = 0; i < N_ROOMS; i++)
            roomState[i] = FREE_ROOM;
        partiesInSite = new boolean[N_ASSAULT_PARTIES];
        thiefCanvasState = new int[N_THIEVES_ORDINARY];
        try { thiefQueue = new MemFIFO<>(new Integer[N_THIEVES_ORDINARY]); } catch (MemException e) {e.printStackTrace();}
    }

    public synchronized int appraiseSit() {
        MasterThief masterThief = (MasterThief) Thread.currentThread();

        if (endHeist) {
            masterThief.getConcentrationSite().endHeist(true);
            return END_HEIST;
        }

        logger(masterThief, "Appraising situation. Concentration Site Occupancy: " + masterThief.getConcentrationSite().occupancy() + "/" + N_THIEVES_ORDINARY);
        logger(masterThief, "Appraising situation. Active Assault Parties: " + masterThief.getActiveAssaultParties() + "/" + N_ASSAULT_PARTIES);
        logger(masterThief, "Appraising situation. Thief Queue Size: " + thiefQueue.size() + "/" + N_ASSAULT_PARTIES);
        if ((masterThief.getActiveAssaultParties() > 0 && masterThief.getConcentrationSite().occupancy() < N_THIEVES_PER_PARTY) || thiefQueue.size() > 0)
            return WAIT_FOR_CANVAS;

        return CREATE_ASSAULT_PARTY;
    }

    public synchronized void takeARest() {
        MasterThief masterThief = (MasterThief) Thread.currentThread();
        masterThief.setThiefState(MasterThiefStates.WAITING_ARRIVAL);
        // sleep until an ordinary thief reaches the collection site and hands a canvas
        while (canvasToCollect() == 0) {
            try { wait(); } catch (InterruptedException e) {e.printStackTrace();}
        }
    }

    public synchronized void handACanvas() {
        OrdinaryThief ordinaryThief = (OrdinaryThief) Thread.currentThread();
        ordinaryThief.setThiefState(OrdinaryThiefStates.COLLECTION_SITE);
        try { thiefQueue.write(ordinaryThief.getThiefID()); } catch (MemException e) {e.printStackTrace();}
        inside[ordinaryThief.getThiefID()] = true;
        registeredThieves[ordinaryThief.getThiefID()] = true;
        partiesInSite[ordinaryThief.getParty().getID()] = true;
        logger(ordinaryThief, "Entered collection site. Collection Site Occupancy: " + occupancy() + "/" + N_THIEVES_ORDINARY);

        // register canvas
        thiefCanvasState[ordinaryThief.getThiefID()] = ordinaryThief.hasCanvas() ? WITH_CANVAS : WITHOUT_CANVAS;
        if (thiefCanvasState[ordinaryThief.getThiefID()] == WITHOUT_CANVAS)
            roomState[ordinaryThief.getRoomID()] = EMPTY_ROOM;

        // wake up master thief
        notifyAll();

        // wait until master thief appraises the situation
        while (appraisedThief != ordinaryThief.getThiefID() && thiefQueue.has(ordinaryThief.getThiefID())) {
            try { wait(); } catch (InterruptedException e) {e.printStackTrace();}
        }

        // leave collection site
        inside[ordinaryThief.getThiefID()] = false;

        // if last thief of party
        int[] thievesOfParty = ordinaryThief.getParty().getThieves();
        int nThievesFromParty = 0;
        for (int thiefFromParty : thievesOfParty)
            if (registeredThieves[thiefFromParty]) nThievesFromParty++;
        if (nThievesFromParty == N_THIEVES_PER_PARTY) {
            logger(ordinaryThief, "Last thief from party leaving Collection Site.");
            // clear registered thieves from his party
            partiesInSite[ordinaryThief.getParty().getID()] = false;
            for (int thiefFromParty : thievesOfParty)
                registeredThieves[thiefFromParty] = false;
            ordinaryThief.getConcentrationSite().setRoomState(ordinaryThief.getRoomID(), ordinaryThief.hasCanvas() ? FREE_ROOM : EMPTY_ROOM);
        }

        logger(ordinaryThief, "Left collection site. Collection Site Occupancy: " + occupancy() + "/" + N_THIEVES_ORDINARY);
    }

    public synchronized void collectACanvas() {
        MasterThief masterThief = (MasterThief) Thread.currentThread();

        int nextThiefID = -1;
        try { nextThiefID = thiefQueue.read(); } catch (MemException e) {e.printStackTrace();}

        switch (thiefCanvasState[nextThiefID]) {
            case WITH_CANVAS:
                canvas++;
                logger(masterThief, "Collected a canvas from Ordinary "+ nextThiefID +". Total canvases collected so far: " + canvas);
                break;
            case WITHOUT_CANVAS:
                logger(masterThief, "Ordinary " + nextThiefID + " had no canvas to collect.");
                break;
            case UNKNOWN:
                logger(masterThief, "Something went wrong when trying to collect canvas from Ordinary " + nextThiefID + ".");
                break;
        }

        thiefCanvasState[nextThiefID] = UNKNOWN;
        endHeist = Arrays.stream(roomState).allMatch(roomState -> roomState == EMPTY_ROOM);
        masterThief.setRoomState(roomState);
        masterThief.setActiveAssaultParties(min(masterThief.getActiveAssaultParties(), numberPartiesInSite()));

        // wake up thief
        appraisedThief = nextThiefID;

        logger(masterThief, "Waking up Ordinary " + nextThiefID + " to leave the collection site.");
        notifyAll();

        if (thiefQueue.size() == 0)
            appraisedThief = -1;

        masterThief.setThiefState(MasterThiefStates.DECIDING_WHAT_TO_DO);
    }

    public synchronized void sumUpResults() {
        MasterThief masterThief = (MasterThief) Thread.currentThread();
        masterThief.setThiefState(MasterThiefStates.PRESENTING_REPORT);

        logger(this, "The heist is over! Were collected " + canvas + " canvases.");
    }
}
