package sharedRegions;

import entities.MasterThief;
import entities.MasterThiefStates;
import entities.OrdinaryThief;
import entities.OrdinaryThiefStates;
import genclass.GenericIO;
import utils.MemException;
import utils.MemFIFO;

import java.util.Arrays;

import static utils.Parameters.*;
import static utils.Utils.logger;

public class CollectionSite {
    private final boolean[] inside;
    private int canvas;
    private boolean endHeist;
    private final int[] roomState;
    private final int[] thiefCanvasState;
    private MemFIFO<AppraisedThief> thiefQueue;
    private int appraisedThief;
    private final boolean[] partiesInSite;
    private final boolean[] registeredThieves;
    private boolean closingParty;

    @Override
    public String toString() {
        return "Collection Site";
    }

    public void printRoomState() {
        String print = "Room State:\n";
        for (int i = 0; i < N_ROOMS; i++) {
            int state = roomState[i];
            print += i+": ";
            switch (state) {
                case FREE_ROOM:
                    print += "FREE, ";
                    break;
                case BUSY_ROOM:
                    print += "BUSY, ";
                    break;
                case EMPTY_ROOM:
                    print += "EMPTY, ";
                    break;
            }
        }
        print += "\n";
        GenericIO.writelnString(print);
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
        closingParty = false;
        registeredThieves = new boolean[N_THIEVES_ORDINARY];
        inside = new boolean[N_THIEVES_ORDINARY];
        roomState = new int[N_ROOMS];
        for (int i = 0; i < N_ROOMS; i++)
            roomState[i] = FREE_ROOM;
        partiesInSite = new boolean[N_ASSAULT_PARTIES];
        thiefCanvasState = new int[N_THIEVES_ORDINARY];
        try { thiefQueue = new MemFIFO<>(new AppraisedThief[N_THIEVES_ORDINARY]); } catch (MemException e) {e.printStackTrace();}
    }

    public synchronized int appraiseSit(int concentrationSiteOccupancy) {
        MasterThief masterThief = (MasterThief) Thread.currentThread();


        if (endHeist && occupancy() == 0 && concentrationSiteOccupancy == N_THIEVES_ORDINARY)
            return END_HEIST;

        logger(this, "Active Parties: "+masterThief.getActiveAssaultParties()+", Concentration Occupancy: "+concentrationSiteOccupancy+", Parties in Site: "+numberPartiesInSite()+", Thief Queue: "+thiefQueue.size());
        if (masterThief.getActiveAssaultParties() > 0 || (concentrationSiteOccupancy < N_THIEVES_PER_PARTY && numberPartiesInSite() > 0)
                    || thiefQueue.size() > 0)
            return WAIT_FOR_CANVAS;

        while (occupancy() > 0) {
            try { wait(); } catch (InterruptedException e) {e.printStackTrace();}
        }

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
        AppraisedThief thief = new AppraisedThief(ordinaryThief.getThiefID(), ordinaryThief.getRoomID(), ordinaryThief.getPartyID(), ordinaryThief.hasCanvas());
        try {
            thiefQueue.write(thief);
        } catch (MemException e) {
            e.printStackTrace();
        }
        inside[ordinaryThief.getThiefID()] = true;
        registeredThieves[ordinaryThief.getThiefID()] = true;
        partiesInSite[ordinaryThief.getPartyID()] = true;
        thiefCanvasState[ordinaryThief.getThiefID()] = ordinaryThief.hasCanvas() ? WITH_CANVAS : WITHOUT_CANVAS;
        //logger(ordinaryThief, "Entered collection site. Collection Site Occupancy: " + occupancy() + "/" + N_THIEVES_ORDINARY);

        int[] thievesOfParty = ordinaryThief.getThievesFromParty();
        int nThievesFromParty = 0;
        for (int thiefFromParty : thievesOfParty) {
            if (registeredThieves[thiefFromParty])
                nThievesFromParty++;
        }

        // wake up master thief
        notifyAll();

        // wait until master thief appraises the situation
        while (appraisedThief != ordinaryThief.getThiefID() && thiefQueue.has(thief)) {
            try { wait(); } catch (InterruptedException e) {e.printStackTrace();}
        }

        // if last thief of party
        if (nThievesFromParty == N_THIEVES_PER_PARTY) {
            //logger(ordinaryThief, "Last thief from party leaving Collection Site.");
            // clear registered thieves from his party
            partiesInSite[ordinaryThief.getPartyID()] = false;
            for (int thiefFromParty : thievesOfParty)
                registeredThieves[thiefFromParty] = false;
            printRoomState();
            closingParty = true;
            if (roomState[ordinaryThief.getRoomID()] == BUSY_ROOM)
                roomState[ordinaryThief.getRoomID()] = FREE_ROOM;
        }

        // leave collection site
        inside[ordinaryThief.getThiefID()] = false;
        notifyAll();
        //logger(ordinaryThief, "Left collection site. Collection Site Occupancy: " + occupancy() + "/" + N_THIEVES_ORDINARY);
    }

    public synchronized void collectACanvas() {
        MasterThief masterThief = (MasterThief) Thread.currentThread();

        AppraisedThief nextThief = null;
        try { nextThief = thiefQueue.read(); } catch (MemException e) {e.printStackTrace();}

        if (nextThief.hasCanvas) {
            canvas++;
            logger(masterThief, "Collected a canvas from Ordinary " + nextThief.thiefID + ". Total canvases collected so far: " + canvas);
        }
        else {
            logger(masterThief, "Ordinary " + nextThief.thiefID + " had no canvas to collect.");
            roomState[nextThief.roomID] = EMPTY_ROOM;
        }

        endHeist = Arrays.stream(roomState).allMatch(roomState -> roomState == EMPTY_ROOM);
        masterThief.setRoomState(roomState);
        logger(masterThief, "Parties in site: " + numberPartiesInSite());
        thiefCanvasState[nextThief.thiefID] = UNKNOWN;

        // wake up thief
        appraisedThief = nextThief.thiefID;

        logger(masterThief, "Waking up Ordinary " + nextThief.thiefID + " to leave the collection site.");

        notifyAll();

        if (closingParty) {
            closingParty = false;
            masterThief.setActiveAssaultParties(masterThief.getActiveAssaultParties() - 1);
        }

        if (thiefQueue.size() == 0)
            appraisedThief = -1;

        masterThief.setThiefState(MasterThiefStates.DECIDING_WHAT_TO_DO);
    }

    public synchronized void sumUpResults() {
        MasterThief masterThief = (MasterThief) Thread.currentThread();
        masterThief.setThiefState(MasterThiefStates.PRESENTING_REPORT);

        notifyAll();
        logger(this, "The heist is over! Were collected " + canvas + " canvas.");
    }

    class AppraisedThief {
        int thiefID;
        int roomID;
        int partyID;
        boolean hasCanvas;

        public AppraisedThief(int thiefID, int roomID, int partyID, boolean hasCanvas) {
            this.thiefID = thiefID;
            this.roomID = roomID;
            this.partyID = partyID;
            this.hasCanvas = hasCanvas;
        }
    }
}
