package entities;

import sharedRegions.*;
import utils.MemException;

import static utils.Parameters.*;
import static utils.Utils.logger;

/**
 * Thread that represents the Master Thief
 */

public class MasterThief extends Thief {

    /**
     * Number of active assault parties
     */
    private int activeAssaultParties;
    private boolean[] partyActive;

    /**
     * Array of room states
     */
    private int[] roomState;

    /**
     * General repository
     */
    private GeneralRepos repos;

    /**
     * MasterThief constructor
     * @param threadName Thread name
     * @param thiefID Thief ID
     * @param museum Museum
     * @param concentrationSite ConcentrationSite
     * @param collectionSite CollectionSite
     * @param assaultParties AssaultParty array
     * @param repos GeneralRepos
     */

    public MasterThief(String threadName, int thiefID, Museum museum, ConcentrationSite concentrationSite, CollectionSite collectionSite, AssaultParty[] assaultParties, GeneralRepos repos) throws MemException {
        super(threadName, thiefID, museum, concentrationSite, collectionSite, assaultParties,repos);
        setThiefState(MasterThiefStates.PLANNING_HEIST);
        activeAssaultParties = 0;
        roomState = new int[N_ROOMS];
        for (int i = 0; i < N_ROOMS; i++)
            roomState[i] = FREE_ROOM;
        this.repos = repos;
        partyActive = new boolean[N_ASSAULT_PARTIES];
    }

    /**
     * Get the number of active assault parties
     * @return Number of active assault parties
     */
    public int getActiveAssaultParties() {
        return activeAssaultParties;
    }

    /**
     * Set the number of active assault parties
     * @param activeAssaultParties Number of active assault parties
     */
    public void setActiveAssaultParties(int activeAssaultParties) {
        this.activeAssaultParties = activeAssaultParties;
    }

    /**
     * Set a party as active or inactive
     * @param partyID
     * @param active
     */
    public void setPartyActive(int partyID, boolean active) {
        partyActive[partyID] = active;
    }

    /**
     * Get the first free party
     * @return Free party ID
     */
    public int getFreeParty() {
        for (int i = 0; i < N_ASSAULT_PARTIES; i++) {
            if (!partyActive[i])
                return i;
        }
        return -1;
    }

    /**
     * Get the room state
     * @return Room state
     */
    public int[] getRoomState() {
        return roomState;
    }

    /**
     * Set the room state
     * @param roomState Room state
     */
    public void setRoomState(int[] roomState) {
        this.roomState = roomState;
    }


    /**
     * Life cycle of the Master Thief
     */
    @Override
    public void run() {
        int concentrationSiteOccupancy;
        concentrationSite.startOperations();
        lifecycle: while(true) {
            concentrationSiteOccupancy = concentrationSite.getOccupancy();
            switch (collectionSite.appraiseSit(concentrationSiteOccupancy)) {
                case CREATE_ASSAULT_PARTY:
                    //logger(this, "CREATE_ASSAULT_PARTY");
                    repos.updateMasterThiefState(MasterThiefStates.PLANNING_HEIST);
                    int assaultPartyID = concentrationSite.prepareAssaultParty();
                    if (assaultPartyID >= 0)
                        assaultParties[assaultPartyID].sendAssaultParty();
                    break;
                case WAIT_FOR_CANVAS:
                    //logger(this, "WAIT_FOR_CANVAS");
                    repos.updateMasterThiefState(MasterThiefStates.WAITING_ARRIVAL);
                    collectionSite.takeARest();
                    collectionSite.collectACanvas();
                    break;
                case END_HEIST:
                    logger(this, "END_HEIST");
                    repos.updateMasterThiefState(MasterThiefStates.PRESENTING_REPORT);
                    collectionSite.sumUpResults();
                    break lifecycle;
            }
        }
        concentrationSite.endOperations();
    }
}
