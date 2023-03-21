package entities;

import genclass.GenericIO;
import sharedRegions.AssaultParty;
import sharedRegions.CollectionSite;
import sharedRegions.ConcentrationSite;
import sharedRegions.Museum;
import utils.MemException;
import utils.MemFIFO;
import utils.Utils;

public class MasterThief extends Thief {
    public MasterThief(String threadName, int thiefID, Museum museum, ConcentrationSite concentrationSite, CollectionSite collectionSite) throws MemException {
        super(threadName, thiefID, museum, concentrationSite, collectionSite);

        setThiefState(MasterThiefStates.PLANNING_HEIST);
    }

    @Override
    public void run() {
        for (int i = 0; i < 4; i++) {
        //while(true) {
            concentrationSite.startOperations();
            concentrationSite.prepareAssaultParty();
            concentrationSite.sendAssaultParty();
        }
    }

    public void appraiseSit() {

    }

}
