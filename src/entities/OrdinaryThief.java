package entities;

import sharedRegions.AssaultParty;
import sharedRegions.CollectionSite;
import sharedRegions.ConcentrationSite;
import sharedRegions.Museum;

public class OrdinaryThief extends Thief {
    private AssaultParty party;

    public void joinParty(int partyID) {
        this.party = assaultParties[partyID];
    }

    public AssaultParty getParty() {
        return party;
    }

    public OrdinaryThief(String threadName, int thiefID, Museum museum, ConcentrationSite concentrationSite, CollectionSite collectionSite, AssaultParty[] assaultParties) {
        super(threadName, thiefID, museum, concentrationSite, collectionSite, assaultParties);
        thiefState = OrdinaryThiefStates.CONCENTRATION_SITE;
    }

    @Override
    public void run() {
        while (true) {
            while (concentrationSite.amINeeded()) {
                concentrationSite.prepareExcursion();
                party.crawlIn();
                //museum.rollACanvas(assaultID);
                //party.reverseDirection();
                //party.crawlOut();
                //collectionSite.handACanvas(assaultID);

                // simulating assault, to be deleted
                try {
                    Thread.sleep(100000);
                } catch (InterruptedException e) {}
            }
        }
    }
}
