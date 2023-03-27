package entities;

import sharedRegions.*;
import utils.MemException;

import static utils.Parameters.*;
import static utils.Utils.logger;

public class MasterThief extends Thief {

    private int activeAssaultParties;
    private boolean[] hasCanvas;

    public MasterThief(String threadName, int thiefID, Museum museum, ConcentrationSite concentrationSite, CollectionSite collectionSite, AssaultParty[] assaultParties) throws MemException {
        super(threadName, thiefID, museum, concentrationSite, collectionSite, assaultParties);
        setThiefState(MasterThiefStates.PLANNING_HEIST);
        activeAssaultParties = 0;
        hasCanvas = new boolean[N_ASSAULT_PARTIES];
        for (int i = 0; i < N_ASSAULT_PARTIES; i++)
            hasCanvas[i] = true;
    }

    public int getActiveAssaultParties() {
        return activeAssaultParties;
    }

    public void setActiveAssaultParties(int activeAssaultParties) {
        this.activeAssaultParties = activeAssaultParties;
    }

    public boolean hasCanvas(int partyID) {
        return hasCanvas[partyID];
    }

    public void hasCanvas(int partyID, boolean hasCanvas) {
        this.hasCanvas[partyID] = hasCanvas;
    }

    @Override
    public void run() {
        concentrationSite.startOperations();
        lifecycle: while(true) {
            switch (collectionSite.appraiseSit()) {
                case CREATE_ASSAULT_PARTY:
                    logger(this, "CREATE_ASSAULT_PARTY");
                    int assaultPartyID = concentrationSite.prepareAssaultParty();
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
    }
}
