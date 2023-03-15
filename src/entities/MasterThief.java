package entities;

import sharedRegions.AssaultParty;
import sharedRegions.CollectionSite;
import sharedRegions.ConcentrationSite;
import sharedRegions.Museum;

public class MasterThief extends Thief {
    private AssaultParty assaultParties[];

    public AssaultParty[] getAssaultParties() {
        return assaultParties;
    }

    public MasterThief(String threadName, Museum museum, ConcentrationSite concentrationSite, CollectionSite collectionSite, AssaultParty[] assaultParties)  {
        super(threadName, museum, concentrationSite, collectionSite);
        this.assaultParties = assaultParties;
        this.setThiefState(MasterThiefStates.PLANNING_HEIST);
    }

    @Override
    public void run() {
        startOperations();
    }

    public void takeARest() {

    }

    public void appraiseSit() {

    }

    public void startOperations() {
        this.setThiefState(MasterThiefStates.DECIDING_WHAT_TO_DO);
    }
}
