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
    }

    public void takeARest() {

    }

    public void appraiseSit() {

    }

    public void startOperations() {

    }

    @Override
    public void run() {

    }
}
