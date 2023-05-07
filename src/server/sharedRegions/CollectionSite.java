package server.sharedRegions;

import client.entities.MasterThief;
import client.entities.MasterThiefStates;
import client.entities.OrdinaryThief;
import client.entities.OrdinaryThiefStates;
import genclass.GenericIO;
import server.entities.CollectionSiteClientProxy;
import server.main.ServerAssaultParty;
import server.main.ServerCollectionSite;
import utils.MemException;
import utils.MemFIFO;

import static utils.Parameters.*;
import static utils.Utils.*;

/**
 * Shared Region with methods used by the Master Thief and the Ordinary Thieves.
 * This class is responsible for gathering canvas and shaping the lifecycle of the Master Thief.
 */

public class CollectionSite {
    /**
     * Reference to Master Thief threads
     */
    private final CollectionSiteClientProxy[] master;
    /**
     * Reference to Ordinary Thief threads
     */
    private final CollectionSiteClientProxy[] ordinary;
    /**
     * Queue of thieves waiting to be appraised
     */
    private MemFIFO<AppraisedThief> thiefQueue;
    /**
     * Thieves inside the collection site
     */
    private final boolean[] thieves;
    /**
     * Parties inside the collection site
     */
    private final boolean[] partiesInSite;
    /**
     * Number of emptied rooms
     */
    private final boolean[] emptiedRooms;
    /**
     * State of the canvas of each thief
     */
    private final int[] thiefCanvasState;
    /**
     * Number of thieves in the collection site for each party
     */
    private final int[] registeredThievesPerParty;
    /**
     * Number of canvas collected so far
     */
    private int canvas;
    /**
     * Thief that is being appraised
     */
    private int appraisedThief;
    /**
     * Party that is ending a run
     */
    private boolean[] closingParty;
    /**
     * Number of entity groups requesting the shutdown
     */
    private int nEntities;

    /**
     * Checks the number of thieves inside the collection site
     * @return the number of thieves
     */
    public int occupancy() {
        return count(thieves);
    }

    /**
     * Returns the number of parties in the Collection Site
     * @return number of parties in the Collection Site
     */
    private int numberOfPartiesInSite() {
        return count(partiesInSite);
    }

    /**
     * Returns the number of canvas to collect
     * @return number of canvas to collect
     */
    private int canvasToCollect() {
        int count = 0;
        for (int i = 0; i < N_THIEVES_ORDINARY; i++)
            if (thiefCanvasState[i] != UNKNOWN) count++;
        return count;
    }

    @Override
    public String toString() {
        return "Collection Site";
    }

    /**
     * Collection on Site Constructor
     */
    public CollectionSite() {
        master = new CollectionSiteClientProxy[N_THIEVES_MASTER];
        for (int i = 0; i < N_THIEVES_MASTER; i++)
            master[i] = null;
        ordinary = new CollectionSiteClientProxy[N_THIEVES_ORDINARY];
        for (int i = 0; i < N_THIEVES_ORDINARY; i++)
            ordinary[i] = null;
        thieves = new boolean[N_THIEVES_ORDINARY];
        partiesInSite = new boolean[N_ASSAULT_PARTIES];
        closingParty = new boolean[N_ASSAULT_PARTIES];
        emptiedRooms = new boolean[N_ROOMS];
        thiefCanvasState = new int[N_THIEVES_ORDINARY];
        registeredThievesPerParty = new int[N_ASSAULT_PARTIES];
        canvas = 0;
        appraisedThief = -1;
        try {
            thiefQueue = new MemFIFO<>(new AppraisedThief[N_THIEVES_ORDINARY]);
        } catch(MemException e) {
            e.printStackTrace();
        }
    }

    /**
     * Master Thief Appraises the Situation
     * @return Action to be taken
     */
    public synchronized int appraiseSit(int concentrationSiteOccupancy, int freeParty, int freeRoom) {
        int masterId;
        masterId = ((CollectionSiteClientProxy) Thread.currentThread()).getMasterId();
        master[masterId] = (CollectionSiteClientProxy) Thread.currentThread();

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
     * Master Thief takes a rest while waiting for an ordinary thief to hand a canvas
     */

    public synchronized void takeARest() {
        int masterId;
        masterId = ((CollectionSiteClientProxy) Thread.currentThread()).getMasterId();
        master[masterId].setMasterState(MasterThiefStates.WAITING_ARRIVAL);

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
    }

    /**
     * Ordinary Thief hands a canvas and waits for the master thief to appraise the situation
     */
    public synchronized void handACanvas(int partyID, int roomID, boolean hasCanvas) {
        int ordinaryId;
        ordinaryId = ((CollectionSiteClientProxy) Thread.currentThread()).getOrdinaryId();
        ordinary[ordinaryId] = (CollectionSiteClientProxy) Thread.currentThread();
        ordinary[ordinaryId].setOrdinaryState(OrdinaryThiefStates.COLLECTION_SITE);
        int ordinaryThiefID = ordinaryId;
        GenericIO.writelnString("Ordinary Thief " + ordinaryThiefID + " is handing a canvas");

        AppraisedThief thief = new AppraisedThief(ordinaryThiefID, roomID, partyID, hasCanvas);
        try {
            thiefQueue.write(thief);
        } catch (MemException e) {
            e.printStackTrace();
        }

        thieves[ordinaryThiefID] = true;
        registeredThievesPerParty[partyID]++;
        partiesInSite[partyID] = true;
        thiefCanvasState[ordinaryThiefID] = hasCanvas ? WITH_CANVAS : WITHOUT_CANVAS;
        //logger(ordinaryThief, "Entered collection site. Collection Site Occupancy: " + occupancy() + "/" + N_THIEVES_ORDINARY);

        // if last thief of party
        if (registeredThievesPerParty[partyID] == N_THIEVES_PER_PARTY)
            closingParty[partyID] = true;

        // wake up master thief
        notifyAll();

        // wait until the master thief appraises the situation
        while(appraisedThief != ordinaryThiefID) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // leave collection site
        thieves[ordinaryThiefID] = false;
        notifyAll();
        GenericIO.writelnString("Ordinary_" + ordinaryId + " left collection site. Collection Site Occupancy: " + occupancy() + "/" + N_THIEVES_ORDINARY);
    }

    /**
     * Master Thief collects a canvas from an ordinary thief
     * @return array with the room id and state
     */
    public synchronized int[] collectACanvas() {
        int masterId;
        masterId = ((CollectionSiteClientProxy) Thread.currentThread()).getMasterId();
        master[masterId].setMasterState(MasterThiefStates.DECIDING_WHAT_TO_DO);

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

        return new int[]{nextThief.partyID, nextThief.roomID, roomState, lastThief ? 1 : 0};
    }

    /**
     * Master Thief presents the report of the heist
     */
    public synchronized void sumUpResults() {
        int masterId;
        masterId = ((CollectionSiteClientProxy) Thread.currentThread()).getMasterId();
        master[masterId].setMasterState(MasterThiefStates.PRESENTING_REPORT);

        notifyAll();
        logger(this, "The heist is over! Were collected " + canvas + " canvas.");
    }

    /**
     * Operation server shutdown
     */
    public synchronized void shutdown() {
        nEntities++;
        if (nEntities >= N_ENTITIES_SHUTDOWN)
            ServerCollectionSite.waitConnection = false;

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
