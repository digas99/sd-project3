package server.objects;

import client.entities.*;
import genclass.GenericIO;
import interfaces.ConcentrationSiteInterface;
import interfaces.ReturnBoolean;
import server.main.ServerConcentrationSite;

import static utils.Parameters.*;
import static utils.Utils.count;

/**
 * Shared Region with methods used by the Master Thief and the Ordinary Thieves.
 * This class is responsible for gathering thieves and sending them to an Assault Party.
 */

public class ConcentrationSite implements ConcentrationSiteInterface {
    /**
     * Reference to Master Thief threads
     */
    private final Thread[] master;
    /**
     * Reference to Ordinary Thief threads
     */
    private final Thread[] ordinary;
    private final boolean[] thieves;
    private final boolean[] partyActive;
    private int[] roomState;
    private boolean endHeist;
    private boolean makingParty;
    private int nJoinedParty;
    private int nextParty;
    /**
     * Number of entity groups requesting the shutdown
     */
    private int nEntities;

    /**
     * Checks the number of thieves inside the concentration site
     * @return the number of thieves
     */
    public synchronized int occupancy() {
        return count(thieves);
    }

    /**
     * Set the state of a room
     * @param roomID id of the room
     * @param state state of the room
     */
    public synchronized void setRoomState(int roomID, int state) {
        roomState[roomID] = state;
    }

    /**
     * Get the state of a room
     * @param roomID id of the room
     * @return state of the room
     */
    public synchronized int getRoomState(int roomID) {
        return roomState[roomID];
    }

    /**
     * Function to peek the next free room
     * @return roomID or -1 if no free room
     */
    public synchronized int peekFreeRoom() {
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
    private int getFreeRoom() {
        int roomID = peekFreeRoom();
        if (roomID != -1)
            roomState[roomID] = BUSY_ROOM;
        return roomID;
    }

    /**
     * Searches for a party that is not active at the moment
     * @return the index of the party that is free, -1 if all active
     */
    public synchronized int getFreeParty() {
        for (int i = 0; i < N_ASSAULT_PARTIES; i++) {
            if (!partyActive[i]) {
                GenericIO.writelnString("Party " + i + " is free");
                return i;
            }
        }
        GenericIO.writelnString("No free parties");
        return -1;
    }

    /**
     * Set the active state of a party
     * @param partyID ID of the party
     * @param state state to set
     */
    public synchronized void setPartyActive(int partyID, boolean state) {
        GenericIO.writelnString("Party " + partyID + " is now " + (state ? "active" : "inactive"));
        partyActive[partyID] = state;
    }

   /**
    * ConcentrationSite constructor
    * No relevant parameters are passed to the constructor but many of the variables are initialized
    */
    public ConcentrationSite() {
        master = new Thread[N_THIEVES_MASTER];
        for (int i = 0; i < N_THIEVES_MASTER; i++)
            master[i] = null;
        ordinary = new Thread[N_THIEVES_ORDINARY];
        for (int i = 0; i < N_THIEVES_ORDINARY; i++)
            ordinary[i] = null;
        thieves = new boolean[N_THIEVES_ORDINARY];
        partyActive = new boolean[N_ASSAULT_PARTIES];
        roomState = new int[N_ROOMS];
        endHeist = makingParty = false;
        nextParty = nJoinedParty = 0;
    }

    @Override
    public String toString() {
        return "Concentration Site";
    }

    /**
     * Function to be called by the Master Thief to start the operations
     */
    public synchronized int startOperations(int masterId) {
        master[masterId] = Thread.currentThread();
        return MasterThiefStates.DECIDING_WHAT_TO_DO;
    }

    /**
     * Checks if the Ordinary Thief that called it is still needed
     *
     * @return true if the thief is still needed, false if the heist is over
     */

    public synchronized ReturnBoolean amINeeded(int ordinaryId) {
        ordinary[ordinaryId] = Thread.currentThread();

        if (endHeist) {
            notifyAll();
            return new ReturnBoolean(false, OrdinaryThiefStates.CONCENTRATION_SITE);
        }

        thieves[ordinaryId] = true;
        GenericIO.writelnString("Ordinary_"+ordinaryId+" entered concentration site. Concentration Site Occupancy: " + occupancy() + "/" + N_THIEVES_ORDINARY);

        // wake up master to check if there are enough thieves to form a party
        notifyAll();

        return new ReturnBoolean(true, OrdinaryThiefStates.CONCENTRATION_SITE);
    }

    /**
     * Function to be called by the Master Thief to prepare a party
     * @return the party ID or -1 if there are no more rooms available
     *
     */
    public synchronized int[] prepareAssaultParty(int masterId) {
        // make sure there are free rooms
        if (peekFreeRoom() == -1) {
            notifyAll();
            return new int[]{-1, MasterThiefStates.ASSEMBLING_GROUP};
        }

        // wait until there are enough thieves to form a party
        while (occupancy() < N_THIEVES_PER_PARTY && !endHeist) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        makingParty = true;
        GenericIO.writelnString("Enough thieves to make a party. Preparing excursion");
        nextParty = getFreeParty();
        notifyAll();

        // wait while thieves are being assigned to a party
        while (makingParty) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // set the party as active
        if (nextParty > -1 && nextParty < N_ASSAULT_PARTIES) partyActive[nextParty] = true;
        GenericIO.writelnString("Setting Party " + nextParty);
        return new int[]{nextParty, MasterThiefStates.ASSEMBLING_GROUP};
    }

    /**
     * Function to be called by the Ordinary Thief enroll in a party
     * @return an array with the party ID and the room ID or null if there are no more rooms available
     */
    public synchronized int[] prepareExcursion(int ordinaryId) {
        //logger(ordinaryThief, "End Heist: " + endHeist + ". Making Party: " + makingParty + ". N Joined Party: " + nJoinedParty);

        // wait until master says it's time to form a party
        while (!endHeist && (!makingParty || nJoinedParty >= N_THIEVES_PER_PARTY)) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // leave concentration site if there are no more rooms available
        if (endHeist || peekFreeRoom() == -1) {
            thieves[ordinaryId] = false;
            makingParty = false;
            notifyAll();
            return null;
        }

        // if rooms are available, join the party
        nJoinedParty++;
        int nextRoom = peekFreeRoom();
        GenericIO.writelnString("Ordinary_"+ordinaryId+" joined Party " + nextParty + ". Party Occupancy: " + nJoinedParty + "/" + N_THIEVES_PER_PARTY);
        GenericIO.writelnString("Room: " + nextRoom);

        // if last thief to join the party, reset variables and notify master
        if (nJoinedParty == N_THIEVES_PER_PARTY) {
            nJoinedParty = 0;
            makingParty = false;
            notifyAll();

            // set room as busy
            getFreeRoom();
        }

        // leave concentration site
        thieves[ordinaryId] = false;

        // wake up next thief
        notifyAll();

        return new int[]{nextParty, nextRoom, OrdinaryThiefStates.CRAWLING_INWARDS};
    }

    /**
     * Function to be called by the Master Thief to end the heist and wake up all thieves
     */
    public synchronized void endOperations() {
        endHeist = true;
        notifyAll();
    }

    /**
     * Operation server shutdown
     */
    public synchronized void shutdown() {
        nEntities++;
        if (nEntities >= N_ENTITIES_SHUTDOWN)
            ServerConcentrationSite.shutdown();

        notifyAll();
    }
}
