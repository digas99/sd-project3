package sharedRegions;

import entities.*;
import genclass.GenericIO;
import utils.*;

import java.util.Arrays;

import static utils.Parameters.*;
import static utils.Utils.logger;


/**
 * Shared Region with methods used by the Master Thief and the Ordinary Thieves.
 * This class is responsible for gathering thieves and sending them to an Assault Party.
 */

public class ConcentrationSite {
    private final boolean[] inside;
    private boolean endHeist;
    private int nextPartyID;
    private boolean makeParty;
    private int joinedParty;
    private int[] roomState;

    private GeneralRepos repos;

    /**
     * Function to peek the next free room
     * @return roomID or -1 if no free room
     */
    public int peekFreeRoom() {
        for (int i = 0; i < N_ROOMS; i++) {
            if (roomState[i] == FREE_ROOM)
                return i;
        }
        return -1;
    }

    /**
     * Get the next free room and set it to busy state
     * if roomID == -1, no free room was found
     * @return roomID
     */
    public int getFreeRoom() {
        int roomID = peekFreeRoom();
        if (roomID != -1)
            roomState[roomID] = BUSY_ROOM;
        return roomID;
    }

    /**
     * Function to get the occupancy of the concentration site
     * @return number of thieves inside the concentration site
     */

    public int occupancy() {
        int count = 0;
        for (int i = 0; i < N_THIEVES_ORDINARY; i++) {
            if (inside[i]) count++;
        }
        return count;
    }

   /**
    * ConcentrationSite constructor
    * No relevant parameters are passed to the constructor but many of the variables are initialized
    *
    * @param repos General Repository
    */
    public ConcentrationSite(GeneralRepos repos) {
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

    /**
     * Returns the number of thieves inside the Concentration Site
     * @return number of thieves inside the Concentration Site
     */
    public synchronized int getOccupancy() {
        return occupancy();
    }

    /**
     * Function to be called by the Master Thief to start the operations
     */
    public synchronized void startOperations() {
        MasterThief master = (MasterThief) Thread.currentThread();
        master.setThiefState(MasterThiefStates.DECIDING_WHAT_TO_DO);
        //repos.updateMasterThiefState(MasterThiefStates.DECIDING_WHAT_TO_DO);
    }

    /**
     * Checks if the Ordinary Thief that called it is still needed
     *
     * @return true if the thief is still needed, false if the heist is over
     */

    public synchronized boolean amINeeded() {
        OrdinaryThief ordinaryThief = (OrdinaryThief) Thread.currentThread();
        ordinaryThief.setThiefState(OrdinaryThiefStates.CONCENTRATION_SITE);


        ordinaryThief.setParty(null);

        if (endHeist) {
            notifyAll();
            return false;
        }

        // register in concentration site
        inside[ordinaryThief.getThiefID()] = true;
        logger(ordinaryThief, "Entered concentration site. Concentration Site Occupancy: " + occupancy() + "/" + N_THIEVES_ORDINARY);

        // wakeup master to check if there are enough thieves to make a party
        notifyAll();

        return true;
    }

    /**
     * Function to be called by the Master Thief to prepare a party
     * @return the party ID or -1 if there are no more rooms available
     *
     */
    public synchronized int prepareAssaultParty() {
        MasterThief master = (MasterThief) Thread.currentThread();
        master.setThiefState(MasterThiefStates.ASSEMBLING_GROUP);
        //repos.updateMasterThiefState(MasterThiefStates.ASSEMBLING_GROUP);

        // update rooms state
        roomState = master.getRoomState();

        if (peekFreeRoom() == -1) {
            notifyAll();
            return -1;
        }

        // wait until there are enough thieves to make a party
        while (occupancy() < N_THIEVES_PER_PARTY && !endHeist ) {
            try { wait(); } catch (InterruptedException e) {e.printStackTrace();}
        }

        // if enough thieves
        makeParty = true;
        logger(master, "Enough thieves to make a party. Preparing excursion");

        // wakeup thieves to prepare excursion
        notifyAll();
        nextPartyID = master.getFreeParty();

        // wait until thieves are ready
        while (makeParty) {
            try { wait(); } catch (InterruptedException e) {e.printStackTrace();}
        }

        master.setPartyActive(nextPartyID, true);

        // if thieves are ready
        return nextPartyID;
    }

    /**
     * Function to be called by the Master Thief to prepare an Excursion
     * @return false if there are no more rooms available or if the heist is over
     */
    public synchronized boolean prepareExcursion() {
        OrdinaryThief ordinaryThief = (OrdinaryThief) Thread.currentThread();
        ordinaryThief.setThiefState(OrdinaryThiefStates.CRAWLING_INWARDS);

        // wait until master says to prepare excursion
        while (!endHeist && (!makeParty || joinedParty >= N_THIEVES_PER_PARTY)) {
            //logger(ordinaryThief, "Waiting for master to prepare excursion");
            try { wait(); } catch (InterruptedException e) {e.printStackTrace();}
            /*GenericIO.writelnString("end heist: "+endHeist+", make party: "+makeParty+", joined party: "+joinedParty);*/
        }

        if (endHeist || peekFreeRoom() == -1) {
            inside[ordinaryThief.getThiefID()] = false;
            makeParty = false;
            notifyAll();
            return false;
        }

        joinedParty++;
        ordinaryThief.setAssaultParty(nextPartyID, joinedParty == 1);
        logger(ordinaryThief, "Joined Party " + nextPartyID + ". Party Occupancy: " + joinedParty + "/" + N_THIEVES_PER_PARTY);
        GenericIO.writelnString(Arrays.toString(roomState));

        // if last thief joining, reset variables and wakeup master
        if (joinedParty == N_THIEVES_PER_PARTY) {
            makeParty = false;
            joinedParty = 0;
            notifyAll();

            int roomID = getFreeRoom();
            if (roomID == -1) {
                GenericIO.writelnString("No more free rooms for " + ordinaryThief);
                return false;
            }

            ordinaryThief.setRoomOfParty(roomID);
        }

        // leave concentration site
        inside[ordinaryThief.getThiefID()] = false;
        notifyAll();

        return true;
    }

    /**
     * Function to be called by the Master Thief to end the heist and wake up all thieves
     */
    public synchronized void endOperations() {
        endHeist = true;
        notifyAll();
    }
}
