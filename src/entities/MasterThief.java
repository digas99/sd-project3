package entities;

import sharedRegions.AssaultParty;
import sharedRegions.CollectionSite;
import sharedRegions.ConcentrationSite;
import sharedRegions.Museum;
import utils.MemException;

import static utils.Parameters.*;

public class MasterThief extends Thief {
    public MasterThief(String threadName, int thiefID, Museum museum, ConcentrationSite concentrationSite, CollectionSite collectionSite, AssaultParty[] assaultParties) throws MemException {
        super(threadName, thiefID, museum, concentrationSite, collectionSite, assaultParties);
        setThiefState(MasterThiefStates.PLANNING_HEIST);
    }

    @Override
    public void run() {
        concentrationSite.startOperations();
        lifecycle: for (int i = 0; i < 4; i++) {
        //lifecycle: while(true) {
            switch (collectionSite.appraiseSit()) {
                case CREATE_ASSAULT_PARTY:
                    int assaultPartyID = concentrationSite.prepareAssaultParty();
                    assaultParties[assaultPartyID].sendAssaultParty();
                    break;
                case WAIT_FOR_CANVAS:
                    break;
                case END_HEIST:
                    break lifecycle;
            }
        }
    }
}
