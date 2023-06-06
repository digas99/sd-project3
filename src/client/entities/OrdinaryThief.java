package client.entities;

import java.rmi.*;
import interfaces.*;
import genclass.GenericIO;
import server.objects.CollectionSite;
import server.objects.Museum;
import utils.MemException;
import utils.MemFIFO;

import static utils.Parameters.*;
import static utils.Utils.*;

/**
 * Thread that represents the Ordinary Thief
 */

public class OrdinaryThief extends Thief {
    private int partyID;
    private int roomID;

    private int ordinaryID;
    /**
     * Displacement of the thief
     */
    private final int displacement;

    private final boolean[] partyActive;
    private int[] roomState;
    private boolean endHeist;
    private boolean makingParty;
    private int nJoinedParty;
    private int nextParty;

    private final boolean[] thieves;

    /**
     * State of the canvas of each thief
     */
    private final int[] thiefCanvasState;

    /**
     * Number of thieves in the collection site for each party
     */
    private final int[] registeredThievesPerParty;

    /**
     * Parties inside the collection site
     */
    private final boolean[] partiesInSite;

    /**
     * Party that is ending a run
     */
    private boolean[] closingParty;
    /**
     * Thief that is being appraised
     */
    private int appraisedThief;



    /**
     * Queue of thieves waiting to be appraised
     */
    private MemFIFO<AppraisedThief> thiefQueue;

    private final Thread[] ordinary;
    /**
     * Number of entity groups requesting the shutdown
     */
    private int nEntities;

    /**
     * Ordinary Thief Constructor
     * @param threadName Thread name
     * @param thiefID Thief ID
     * @param museum Museum
     * @param concentrationSite Concentration Site
     * @param collectionSite Collection Site
     * @param assaultParties Assault Parties
     */

    public OrdinaryThief(String threadName, int thiefID, MuseumInterface museum, ConcentrationSiteInterface concentrationSite, CollectionSiteInterface collectionSite, AssaultPartyInterface[] assaultParties){
        super(threadName, thiefID, museum, concentrationSite, collectionSite, assaultParties);
        thiefState = OrdinaryThiefStates.CONCENTRATION_SITE;
        displacement = random(MIN_DISPLACEMENT, MAX_DISPLACEMENT);
        ordinary = new Thread[N_THIEVES_ORDINARY];
        for (int i = 0; i < N_THIEVES_ORDINARY; i++)
            ordinary[i] = null;
        partyID = roomID = -1;
        thieves = new boolean[N_THIEVES_ORDINARY];
        partyActive = new boolean[N_ASSAULT_PARTIES];
        roomState = new int[N_ROOMS];
        endHeist = makingParty = false;
        nextParty = nJoinedParty = 0;
        thiefCanvasState = new int[N_THIEVES_ORDINARY];
        registeredThievesPerParty = new int[N_ASSAULT_PARTIES];
        partiesInSite = new boolean[N_ASSAULT_PARTIES];
    }

    @Override
    public String toString() {
        return super.toString() + " [Party: " + partyID + ", Room: " + roomID + "]";
    }

    /**
     * Life cycle of the Ordinary Thief
     */
    @Override
    public void run() {
        ordinaryID = this.thiefID;
        while (amINeeded(ordinaryID).getBooleanVal()) {
            int[] assaultData = prepareExcursion(ordinaryID);
            if (assaultData != null && assaultData[0] != -1) {
                partyID = assaultData[0];
                roomID = assaultData[1];
                AssaultPartyInterface party = assaultParties[partyID];

                party.crawlIn(museum.getRoomDistance(roomID), displacement);
                hasCanvas = rollACanvas(ordinaryID,roomID).getBooleanVal();
                party.reverseDirection();
                party.crawlOut(museum.getRoomDistance(roomID), displacement);
                GenericIO.writelnString("Thief " + thiefID + " has a canvas: " + hasCanvas);
                handACanvas(partyID, roomID, hasCanvas);
            } else break;
        }
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
     * Method used by the setnPaintings
     * Ordinary Thieves to roll a canvas from a room
     */
    public synchronized ReturnBoolean rollACanvas(int ordinaryId, int roomID) {
        ordinary[ordinaryId] = Thread.currentThread();

        GenericIO.writelnString(("Ordinary_" + ordinaryId + " is rolling a canvas from Room_" + roomID));
        Museum.Room room = getRoom(roomID);

        boolean hasCanvas = room.getPaintings() > 0;


        if (room.getPaintings() == 0)
            GenericIO.writelnString("Ordinary_" + ordinaryId + "left empty handed from Room_" + roomID);
        else
            GenericIO.writelnString("Ordinary_" + ordinaryId + " rolled a canvas from Room_" + roomID + ". Paintings: " + room.getPaintings() + "/" + room.getTotalPaintings());

        if (hasCanvas)
            room.setPaintings(room.getPaintings() - 1);

        return new ReturnBoolean(hasCanvas, OrdinaryThiefStates.AT_A_ROOM);
    }

    /**
     * Ordinary Thief hands a canvas and waits for the master thief to appraise the situation
     */
    public synchronized int handACanvas(int ordinaryId, int partyID, int roomID, boolean hasCanvas) {
        ordinary[ordinaryId] = Thread.currentThread();
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

        return OrdinaryThiefStates.COLLECTION_SITE;
    }

    public synchronized int peekFreeRoom() {
        for (int i = 0; i < N_ROOMS; i++) {
            if (roomState[i] == FREE_ROOM)
                return i;
        }
        return -1;
    }

    private int getFreeRoom() {
        int roomID = peekFreeRoom();
        if (roomID != -1)
            roomState[roomID] = BUSY_ROOM;
        return roomID;
    }


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

    public synchronized int occupancy() {
        return count(thieves);
    }


}
