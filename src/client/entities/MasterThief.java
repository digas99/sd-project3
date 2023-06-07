package client.entities;

import interfaces.*;
import genclass.GenericIO;

import static client.entities.MasterThiefStates.PLANNING_HEIST;
import static utils.Parameters.*;
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

    private int masterState;

    private final AssaultPartyInterface[] assaultPartiesStub;

    private final CollectionSiteInterface collectionSiteStub;

    private final ConcentrationSiteInterface concentrationSiteStub;

    private final MuseumInterface museumStub;

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
        master = new Thread[N_THIEVES_MASTER];
        for (int i = 0; i < N_THIEVES_MASTER; i++)
            master[i] = null;

        this.museumStub = museum;
        this.concentrationSiteStub = concentrationSite;
        this.collectionSiteStub = collectionSite;
        this.assaultPartiesStub = assaultParties;

        this.masterState = PLANNING_HEIST;
    }

    /**
     * Life cycle of the Master Thief
     */
    @Override
    public void run() {
        masterID = this.thiefID;

        startOperations();
        lifecycle: while(true) {
            switch (appraiseSit(occupancy(), getFreeParty(), peekFreeRoom())) {
                case CREATE_ASSAULT_PARTY:
                    logger(this, "CREATE_ASSAULT_PARTY");
                    int[] prepareReturn = prepareAssaultParty();
                    int assaultPartyID = prepareReturn[0];
                    this.masterState = prepareReturn[1];
                    GenericIO.writelnString("Assault Party ID: " + assaultPartyID);
                    if (assaultPartyID >= 0) {
                        setPartyActive(assaultPartyID, true);
                        sendAssaultParty(assaultPartyID);
                    }
                    break;
                case WAIT_FOR_CANVAS:
                    logger(this, "WAIT_FOR_CANVAS");
                    takeARest();

                    int[] partyState = collectACanvas();
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
                        resetAssaultParty(partyID);
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

    void startOperations() {
        try {
            concentrationSiteStub.startOperations(masterID);
        } catch (Exception e) {
            GenericIO.writelnString("Exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    int appraiseSit(int occupancy, int freeParty, int freeRoom) {
        int ret = -1;
        try {
            ret = collectionSiteStub.appraiseSit(masterID, occupancy, freeParty, freeRoom);
        } catch (Exception e) {
            GenericIO.writelnString("Exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        return ret;
    }

    int occupancy() {
        int ret = -1;
        try {
            ret = concentrationSiteStub.occupancy();
        } catch (Exception e) {
            GenericIO.writelnString("Exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        return ret;
    }

    int getFreeParty() {
        int ret = -1;
        try {
            ret = concentrationSiteStub.getFreeParty();
        } catch (Exception e) {
            GenericIO.writelnString("Exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        return ret;
    }

    int peekFreeRoom() {
        int ret = -1;
        try {
            ret = concentrationSiteStub.peekFreeRoom();
        } catch (Exception e) {
            GenericIO.writelnString("Exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        return ret;
    }

    int[] prepareAssaultParty() {
        int[] ret = new int[2];
        try {
            ret = concentrationSiteStub.prepareAssaultParty(masterID);
        } catch (Exception e) {
            GenericIO.writelnString("Exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        return ret;
    }

    void setPartyActive(int partyID, boolean state) {
        try {
            concentrationSiteStub.setPartyActive(partyID, state);
        } catch (Exception e) {
            GenericIO.writelnString("Exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    void sendAssaultParty(int partyID) {
        try {
            assaultPartiesStub[partyID].sendAssaultParty(masterID);
        } catch (Exception e) {
            GenericIO.writelnString("Exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    void takeARest() {
        try {
            collectionSiteStub.takeARest(masterID);
        } catch (Exception e) {
            GenericIO.writelnString("Exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    int[] collectACanvas() {
        int[] ret = new int[4];
        try {
            ret = collectionSiteStub.collectACanvas(masterID);
            this.masterState = ret[4];
        } catch (Exception e) {
            GenericIO.writelnString("Exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        return ret;
    }

    void resetAssaultParty(int partyID) {
        try {
            assaultPartiesStub[partyID].resetAssaultParty();
        } catch (Exception e) {
            GenericIO.writelnString("Exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    int getRoomState(int roomID) {
        int ret = -1;
        try {
            ret = concentrationSiteStub.getRoomState(roomID);
        } catch (Exception e) {
            GenericIO.writelnString("Exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        return ret;
    }

    void setRoomState(int roomID, int state) {
        try {
            concentrationSiteStub.setRoomState(roomID, state);
        } catch (Exception e) {
            GenericIO.writelnString("Exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    void sumUpResults() {
        try {
            collectionSiteStub.sumUpResults();
        } catch (Exception e) {
            GenericIO.writelnString("Exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    void endOperations() {
        try {
            concentrationSiteStub.endOperations();
        } catch (Exception e) {
            GenericIO.writelnString("Exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
