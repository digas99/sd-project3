package entities;

import sharedRegions.AssaultParty;
import sharedRegions.CollectionSite;
import sharedRegions.ConcentrationSite;
import sharedRegions.Museum;

public class OrdinaryThief extends Thief {
    private AssaultParty assaultParty;

    public AssaultParty getAssaultParty() {
        return assaultParty;
    }

    public void setAssaultParty(AssaultParty assaultParty) {
        this.assaultParty = assaultParty;
    }

    public OrdinaryThief(String threadName, int thiefID, Museum museum, ConcentrationSite concentrationSite, CollectionSite collectionSite) {
        super(threadName, thiefID, museum, concentrationSite, collectionSite);
        this.thiefState = OrdinaryThiefStates.CONCENTRATION_SITE;
    }

    @Override
    public void run() {
        while(this.concentrationSite.amINeeded()) {
            this.assaultParty.prepareExcursion();
        }
    }
}
