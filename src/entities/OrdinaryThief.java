package entities;

import sharedRegions.AssaultParty;
import sharedRegions.CollectionSite;
import sharedRegions.ConcentrationSite;
import sharedRegions.Museum;
import utils.MemException;
import utils.Monitor;

public class OrdinaryThief extends Thief {
    private AssaultParty assaultParty;
    private boolean inAssaultParty;
    private Monitor partyMonitor;
    public AssaultParty getAssaultParty() {
        return assaultParty;
    }

    public void setAssaultParty(AssaultParty assaultParty) {
        this.assaultParty = assaultParty;
    }

    public boolean isInAssaultParty() {
        return inAssaultParty;
    }

    public void setInAssaultParty(boolean inAssaultParty) {
        this.inAssaultParty = inAssaultParty;
    }

    public Monitor getPartyMonitor() {
        return partyMonitor;
    }

    public void setPartyMonitor(Monitor partyMonitor) {
        this.partyMonitor = partyMonitor;
    }

    public OrdinaryThief(String threadName, int thiefID, Museum museum, ConcentrationSite concentrationSite, CollectionSite collectionSite) {
        super(threadName, thiefID, museum, concentrationSite, collectionSite);
        thiefState = OrdinaryThiefStates.CONCENTRATION_SITE;
        inAssaultParty = false;
        partyMonitor = new Monitor();
    }

    @Override
    public void run() {
        try {
            concentrationSite.amINeeded();
        } catch (MemException e) {
            throw new RuntimeException(e);
        }

        assaultParty.prepareExcursion();
    }
}
