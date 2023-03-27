package sharedRegions;

import entities.*;
import genclass.GenericIO;
import utils.*;

import static utils.Parameters.*;
import static utils.Utils.logger;

public class ConcentrationSite {
    private final boolean[] inside;
    private boolean endHeist;
    private int nextPartyID;
    private boolean makeParty;
    private int joinedParty;
    private int[] roomState;

    public int getFreeRoom() {
        for (int i = 0; i < N_ROOMS; i++) {
            if (roomState[i] == FREE_ROOM) {
                roomState[i] = BUSY_ROOM;
                return i;
            }
        }
        return -1;
    }

    public void setRoomState(int roomID, int state) {
        roomState[roomID] = state;
    }

    public int occupancy() {
        int count = 0;
        for (int i = 0; i < N_THIEVES_ORDINARY; i++) {
            if (inside[i]) count++;
        }
        return count;
    }

    public void endHeist(boolean endHeist) {
        this.endHeist = endHeist;
    }

    public ConcentrationSite(GeneralRepos repos) throws MemException {
        endHeist = makeParty = false;
        inside = new boolean[N_THIEVES_ORDINARY];
        roomState = new int[N_ROOMS];
        for (int i = 0; i < N_ROOMS; i++)
            roomState[i] = FREE_ROOM;
        nextPartyID = joinedParty = 0;
    }

    public String toString() {
        return "Concentration Site";
    }

    public synchronized void startOperations() {
        MasterThief master = (MasterThief) Thread.currentThread();
        master.setThiefState(MasterThiefStates.DECIDING_WHAT_TO_DO);
    }

    public synchronized boolean amINeeded() {
        OrdinaryThief thief = (OrdinaryThief) Thread.currentThread();
        thief.setThiefState(OrdinaryThiefStates.CONCENTRATION_SITE);

        if (endHeist) return false;

        // register in concentration site
        inside[thief.getThiefID()] = true;
        logger(thief, "Entered concentration site. Concentration Site Occupancy: " + occupancy() + "/" + N_THIEVES_ORDINARY);

        // wakeup master to check if there are enough thieves to make a party
        notifyAll();

        return true;
    }

    public synchronized int prepareAssaultParty() {
        MasterThief master = (MasterThief) Thread.currentThread();
        master.setThiefState(MasterThiefStates.ASSEMBLING_GROUP);

        // update rooms state
        roomState = master.getRoomState();

        // wait until there are enough thieves to make a party
        while (occupancy() < N_THIEVES_PER_PARTY) {
            try { wait(); } catch (InterruptedException e) {e.printStackTrace();}
        }

        // if enough thieves
        makeParty = true;
        logger(master, "Enough thieves to make a party. Preparing excursion");

        // wakeup thieves to prepare excursion
        notifyAll();

        // wait until thieves are ready
        while (makeParty) {
            try { wait(); } catch (InterruptedException e) {e.printStackTrace();}
        }

        // setup nextPartyID
        nextPartyID = (nextPartyID + 1) % N_ASSAULT_PARTIES;

        // if thieves are ready
        return nextPartyID;
    }
    public synchronized void prepareExcursion() {
        OrdinaryThief ordinaryThief = (OrdinaryThief) Thread.currentThread();
        ordinaryThief.setThiefState(OrdinaryThiefStates.CRAWLING_INWARDS);

        // wait until master says to prepare excursion
        while (!endHeist && (!makeParty || joinedParty >= N_THIEVES_PER_PARTY)) {
            try { wait(); } catch (InterruptedException e) {e.printStackTrace();}
        }

        if (endHeist) return;

        // setup nextPartyID
        GenericIO.writelnString("NEXT PARTY ID: "+nextPartyID);
        ordinaryThief.setAssaultParty(nextPartyID);
        joinedParty++;
        logger(ordinaryThief, "Joined Party " + nextPartyID + ". Party Occupancy: " + joinedParty + "/" + N_THIEVES_PER_PARTY);

        // if last thief joining, reset variables and wakeup master
        if (joinedParty == N_THIEVES_PER_PARTY) {
            ordinaryThief.setRoomOfParty(getFreeRoom());
            makeParty = false;
            joinedParty = 0;
            notifyAll();
        }

        // leave concentration site
        inside[ordinaryThief.getThiefID()] = false;
        logger(ordinaryThief, "Left concentration site. Concentration Site Occupancy: " + occupancy() + "/" + N_THIEVES_ORDINARY);
    }
}
