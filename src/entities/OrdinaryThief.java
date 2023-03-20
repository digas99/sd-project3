package entities;

import sharedRegions.AssaultParty;
import sharedRegions.CollectionSite;
import sharedRegions.ConcentrationSite;
import sharedRegions.Museum;

public class OrdinaryThief extends Thief {
    private AssaultParty assaultParty;
    private boolean inAssaultParty;

    public boolean isInAssaultParty() {
        return inAssaultParty;
    }

    public void setInAssaultParty(boolean inAssaultParty) {
        this.inAssaultParty = inAssaultParty;
    }

    public OrdinaryThief(String threadName, int thiefID, Museum museum, ConcentrationSite concentrationSite, CollectionSite collectionSite) {
        super(threadName, thiefID, museum, concentrationSite, collectionSite);
        thiefState = OrdinaryThiefStates.CONCENTRATION_SITE;
        assaultParty = null;
        inAssaultParty = false;
    }

    @Override
    public void run() {
        concentrationSite.amINeeded();
        // assaultParty.prepareExcursion();
    }
}
