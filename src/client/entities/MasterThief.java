package client.entities;

import java.*;
import interfaces.*;
import genclass.GenericIO;
import server.objects.CollectionSite;
import utils.MemException;
import utils.MemFIFO;

import static utils.Parameters.*;
import static utils.Utils.count;
import static utils.Utils.logger;

/**
 * Thread that represents the Master Thief
 */

public class MasterThief extends Thief {

    /**
     * Reference to Master Thief threads
     */
    private final Thread[] master;

    private int masterID;

    private boolean endHeist;

    private boolean makingParty;
    private final boolean[] thieves;

    private final boolean[] emptiedRooms;

    private final boolean[] partyActive;

    private final boolean[] partiesInSite;

    private int[] roomState;

    /**
     * State of the canvas of each thief
     */
    private final int[] thiefCanvasState;

    /**
     * Queue of thieves waiting to be appraised
     */
    private MemFIFO<AppraisedThief> thiefQueue;

    private int nJoinedParty;
    private int nextParty;

    /**
     * Thief that is being appraised
     */
    private int appraisedThief;

    /**
     * Party that is ending a run
     */
    private boolean[] closingParty;

    /**
     * Number of thieves in the collection site for each party
     */
    private final int[] registeredThievesPerParty;

    /**
     * Number of canvas collected so far
     */
    private int canvas;

    /**
     * Begin the crawl movement
     */
    private boolean begin;

    /**
     * MasterThief constructor
     * @param threadName Thread name
     * @param thiefID Thief ID
     * @param museum Museum
     * @param concentrationSite ConcentrationSite
     * @param collectionSite CollectionSite
     * @param assaultParties AssaultParty array
     */

    public MasterThief(String threadName, int thiefID, MuseumInterface museum, ConcentrationSiteInterface concentrationSite, CollectionSiteInterface collectionSite, AssaultPartyInterface[] assaultParties) {
        super(threadName, thiefID, museum, concentrationSite, collectionSite, assaultParties);
        setThiefState(MasterThiefStates.PLANNING_HEIST);
        master = new Thread[N_THIEVES_MASTER];
        for (int i = 0; i < N_THIEVES_MASTER; i++)
            master[i] = null;
        thieves = new boolean[N_THIEVES_ORDINARY];
        partyActive = new boolean[N_ASSAULT_PARTIES];
        endHeist = makingParty = false;
        emptiedRooms = new boolean[N_ROOMS];
        partiesInSite = new boolean[N_ASSAULT_PARTIES];
        appraisedThief = -1;
        nextParty = nJoinedParty = 0;
        closingParty = new boolean[N_ASSAULT_PARTIES];
        thiefCanvasState = new int[N_THIEVES_ORDINARY];
        registeredThievesPerParty = new int[N_ASSAULT_PARTIES];
        canvas = 0;
        try {
            thiefQueue = new MemFIFO<>(new AppraisedThief[N_THIEVES_ORDINARY]);
        } catch(MemException e) {
            e.printStackTrace();
        }
    }

    /**
     * Life cycle of the Master Thief
     */
    @Override
    public void run() {
        masterID = this.thiefID;

        startOperations(masterID);
        lifecycle: while(true) {
            switch (appraiseSit(masterID,occupancy(), getFreeParty(), peekFreeRoom())) {
                case CREATE_ASSAULT_PARTY:
                    logger(this, "CREATE_ASSAULT_PARTY");
                    int assaultPartyID = concentrationSite.prepareAssaultParty();
                    GenericIO.writelnString("Assault Party ID: " + assaultPartyID);
                    if (assaultPartyID >= 0) {
                        setPartyActive(assaultPartyID, true);
                        assaultParties[assaultPartyID].sendAssaultParty();
                    }
                    break;
                case WAIT_FOR_CANVAS:
                    logger(this, "WAIT_FOR_CANVAS");
                    takeARest(masterID);

                    int[] partyState =collectACanvas(masterID);
                    int partyID = partyState[0];
                    int roomID = partyState[1];
                    int roomState = partyState[2];
                    boolean lastThief = partyState[3] == 1;
                    GenericIO.writelnString("Party ID: " + partyID);
                    GenericIO.writelnString("Room ID: " + roomID);
                    GenericIO.writelnString("Room State: " + roomState);
                    GenericIO.writelnString("Last Thief: " + lastThief);

                    if (lastThief) {
                        setPartyActive(partyID, false);
                        assaultParties[partyID].resetAssaultParty();
                    }

                    // must only update if room not busy
                    if (roomState != BUSY_ROOM) {
                        // only need to update room state if it's not empty already
                        if (getRoomState(roomID) != EMPTY_ROOM)
                            setRoomState(roomID, roomState);
                    }

                    break;
                case END_HEIST:
                    logger(this, "END_HEIST");
                    sumUpResults();
                    break lifecycle;
            }
        }
        endOperations();
    }

    /**
     * Send an Assault Party.
     * This method is called by the Master Thief, and tells the Ordinary Thieves to start crawling.
     * The Master Thief goes to the DECIDING_WHAT_TO_DO state.
     */
    public synchronized int sendAssaultParty(int masterId) {
        master[masterId] = Thread.currentThread();

        GenericIO.writelnString("MasterThief_" + masterId + " is sending AssaultParty_" + id + ".");
        begin = true;
        notifyAll();

        return MasterThiefStates.DECIDING_WHAT_TO_DO;
    }

    /**
     * Master Thief collects a canvas from an ordinary thief
     * @return array with the room id and state
     */
    public synchronized int[] collectACanvas(int masterId) {
        AppraisedThief nextThief = null;
        try {
            nextThief = thiefQueue.read();
        } catch (MemException e) {
            e.printStackTrace();
        }

        int roomState = BUSY_ROOM;

        if (nextThief.hasCanvas) {
            canvas++;
            GenericIO.writelnString("Collected a canvas from Ordinary " + nextThief.thiefID + ". Total canvases collected so far: " + canvas);
        }
        else {
            roomState = EMPTY_ROOM;
            emptiedRooms[nextThief.roomID] = true;
            GenericIO.writelnString("Ordinary " + nextThief.thiefID + " had no canvas to collect.");
        }

        thiefCanvasState[nextThief.thiefID] = UNKNOWN;

        boolean lastThief = closingParty[nextThief.partyID];
        if (lastThief) {
            logger(this, "Last thief of party " + nextThief.partyID + " is " + nextThief.thiefID);
            if (!emptiedRooms[nextThief.roomID])
                roomState = FREE_ROOM;

            closingParty[nextThief.partyID] = false;
            partiesInSite[nextThief.partyID] = false;
            registeredThievesPerParty[nextThief.partyID] = 0;
        }

        //logger(masterThief, "Parties in site: " + numberOfPartiesInSite());

        GenericIO.writelnString("Room " + nextThief.roomID);
        switch (roomState) {
            case BUSY_ROOM:
                GenericIO.writelnString("BUSY");
                break;
            case FREE_ROOM:
                GenericIO.writelnString("FREE");
                break;
            case EMPTY_ROOM:
                GenericIO.writelnString("EMPTY");
                break;
        }

        // wake up next thief
        appraisedThief = nextThief.thiefID;
        GenericIO.writelnString("Waking up Ordinary " + nextThief.thiefID + " to leave the collection site.");
        notifyAll();

        return new int[]{nextThief.partyID, nextThief.roomID, roomState, lastThief ? 1 : 0, MasterThiefStates.DECIDING_WHAT_TO_DO};
    }

    /**
     * Function to be called by the Master Thief to start the operations
     */
    public synchronized int startOperations(int masterId) {
        master[masterId] = Thread.currentThread();
        return MasterThiefStates.DECIDING_WHAT_TO_DO;
    }

    /**
     * Checks the number of thieves inside the concentration site
     * @return the number of thieves
     */
    public synchronized int occupancy() {
        return count(thieves);
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
     * Master Thief Appraises the Situation
     * @return Action to be taken
     */
    public synchronized int appraiseSit(int masterId, int concentrationSiteOccupancy, int freeParty, int freeRoom) {
        master[masterId] = Thread.currentThread();

        // end heist if all rooms are empty, if no thieves left in collection site and they are all in concentration site
        logger(this, "Emptied Rooms: " + count(emptiedRooms) + " Occupancy: " + occupancy() + " Concentration Site Occupancy: " + concentrationSiteOccupancy);
        if (freeRoom == -1
                && occupancy() == 0
                && concentrationSiteOccupancy == N_THIEVES_ORDINARY)
            return END_HEIST;

        // wait for a canvas if there are parties active, if all thieves aren't in concentration site and if there are
        // parties in the collection site
        logger(master, "Parties in Site: " + numberOfPartiesInSite() + "\nThief Queue Size: " + thiefQueue.size() + "\nFree Party: " + freeParty);
        if ((concentrationSiteOccupancy < N_THIEVES_PER_PARTY
                && numberOfPartiesInSite() > 0)
                || thiefQueue.size() > 0
                || freeParty == -1)
            return WAIT_FOR_CANVAS;

        return CREATE_ASSAULT_PARTY;
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
     * Returns the number of parties in the Collection Site
     * @return number of parties in the Collection Site
     */
    private int numberOfPartiesInSite() {
        return count(partiesInSite);
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
     * Set the state of a room
     * @param roomID id of the room
     * @param state state of the room
     */
    public synchronized void setRoomState(int roomID, int state) {
        roomState[roomID] = state;
    }

    private int canvasToCollect() {
        int count = 0;
        for (int i = 0; i < N_THIEVES_ORDINARY; i++)
            if (thiefCanvasState[i] != UNKNOWN) count++;
        return count;
    }

    /**
     * Master Thief takes a rest while waiting for an ordinary thief to hand a canvas
     */

    public synchronized int takeARest(int masterId) {
        // sleep until an ordinary thief reaches the collection site and hands a canvas
        GenericIO.writelnString("Master Thief is waiting for a canvas");
        GenericIO.writelnString("Canvas to collect: "+canvasToCollect());
        while (canvasToCollect() == 0) {
            GenericIO.writelnString("Canvas to collect: "+canvasToCollect());
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return MasterThiefStates.WAITING_ARRIVAL;
    }


    /**
     * Master Thief presents the report of the heist
     */
    public synchronized int sumUpResults() {
        notifyAll();
        logger(this, "The heist is over! Were collected " + canvas + " canvas.");

        return MasterThiefStates.PRESENTING_REPORT;
    }

    /**
     * Function to be called by the Master Thief to end the heist and wake up all thieves
     */
    public synchronized void endOperations() {
        endHeist = true;
        notifyAll();
    }
    /**
     * Helper class to store information about the ordinary thieves being appraised
     */
    class AppraisedThief {
        int thiefID;
        int roomID;
        int partyID;
        boolean hasCanvas;

        @Override
        public String toString() {
            return "Appraised Thief " + thiefID + " [Room: " + roomID + ", Party: " + partyID + ", Has Canvas: " + hasCanvas + "]";
        }

        /**
         * Constructor of the AppraisedThief class
         * All class attributes are initialized with the values passed as parameters
         *
         * @param thiefID ID of the Ordinary Thief
         * @param roomID ID of the room that was being targeted by the party
         * @param partyID ID of the party assigned to the Ordinary Thief
         * @param hasCanvas boolean value indicating if the Ordinary Thief has a canvas
         */

        public AppraisedThief(int thiefID, int roomID, int partyID, boolean hasCanvas) {
            this.thiefID = thiefID;
            this.roomID = roomID;
            this.partyID = partyID;
            this.hasCanvas = hasCanvas;
        }
    }


}
