package entities;

import sharedRegions.AssaultParty;
import sharedRegions.CollectionSite;
import sharedRegions.ConcentrationSite;
import sharedRegions.Museum;

public class OrdinaryThief extends Thief {
    AssaultParty assaultParty;

    public AssaultParty getAssaultParty() {
        return assaultParty;
    }

    public void setAssaultParty(AssaultParty assaultParty) {
        this.assaultParty = assaultParty;
    }

    public OrdinaryThief(String threadName, Museum museum, ConcentrationSite concentrationSite, CollectionSite collectionSite) {
        super(threadName, museum, concentrationSite, collectionSite);
        this.thiefState = OrdinaryThiefStates.CONCENTRATION_SITE;
    }

    @Override
    public void run() {
        while(amINeeded()) {
            this.assaultParty.prepareExcursion();
        }
    }

    private boolean amINeeded() {
        return false;
    }

}
