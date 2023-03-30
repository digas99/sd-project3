package entities;

import sharedRegions.*;
import utils.MemException;

import static utils.Parameters.*;
import static utils.Utils.logger;

public class MasterThief extends Thief {

    private int activeAssaultParties;
    private int[] roomState;

    public MasterThief(String threadName, int thiefID, Museum museum, ConcentrationSite concentrationSite, CollectionSite collectionSite, AssaultParty[] assaultParties) throws MemException {
        super(threadName, thiefID, museum, concentrationSite, collectionSite, assaultParties);
        setThiefState(MasterThiefStates.PLANNING_HEIST);
        activeAssaultParties = 0;
        roomState = new int[N_ROOMS];
        for (int i = 0; i < N_ROOMS; i++)
            roomState[i] = FREE_ROOM;
    }

    public int getActiveAssaultParties() {
        return activeAssaultParties;
    }

    public void setActiveAssaultParties(int activeAssaultParties) {
        this.activeAssaultParties = activeAssaultParties;
    }

    public int[] getRoomState() {
        return roomState;
    }

    public void setRoomState(int[] roomState) {
        this.roomState = roomState;
    }

    @Override
    public void run() {
        int concentrationSiteOccupancy;
        concentrationSite.startOperations();
        lifecycle: while(true) {
            concentrationSiteOccupancy = concentrationSite.getOccupancy();
            switch (collectionSite.appraiseSit(concentrationSiteOccupancy)) {
                case CREATE_ASSAULT_PARTY:
                    logger(this, "CREATE_ASSAULT_PARTY");
                    int assaultPartyID = concentrationSite.prepareAssaultParty();
                    if (assaultPartyID == -1) break;
                    assaultParties[assaultPartyID].sendAssaultParty();
                    break;
                case WAIT_FOR_CANVAS:
                    logger(this, "WAIT_FOR_CANVAS");
                    collectionSite.takeARest();
                    collectionSite.collectACanvas();
                    break;
                case END_HEIST:
                    logger(this, "END_HEIST");
                    collectionSite.sumUpResults();
                    break lifecycle;
            }
        }
        concentrationSite.endOperations();
    }
}
