package entities;

import sharedRegions.AssaultParty;
import sharedRegions.CollectionSite;
import sharedRegions.ConcentrationSite;
import sharedRegions.Museum;
import utils.MemException;

public class MasterThief extends Thief {
    private AssaultParty assaultParties[];

    public AssaultParty[] getAssaultParties() {
        return assaultParties;
    }

    public MasterThief(String threadName, int thiefID, Museum museum, ConcentrationSite concentrationSite, CollectionSite collectionSite, AssaultParty[] assaultParties)  {
        super(threadName, thiefID, museum, concentrationSite, collectionSite);
        this.assaultParties = assaultParties;
        this.setThiefState(MasterThiefStates.PLANNING_HEIST);
    }

    @Override
    public void run() {
        startOperations();
        try {
            this.concentrationSite.prepareAssaultParty();
        } catch (MemException e) {
            throw new RuntimeException(e);
        }
    }

    public void takeARest() {

    }

    public void appraiseSit() {

    }

    public void startOperations() {
        this.setThiefState(MasterThiefStates.DECIDING_WHAT_TO_DO);
    }
}
