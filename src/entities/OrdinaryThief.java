package entities;

import sharedRegions.AssaultParty;
import sharedRegions.CollectionSite;
import sharedRegions.ConcentrationSite;
import sharedRegions.Museum;

import static utils.Utils.logger;

public class OrdinaryThief extends Thief {
    private AssaultParty[] assaultParties;

    public OrdinaryThief(String threadName, int thiefID, Museum museum, ConcentrationSite concentrationSite, CollectionSite collectionSite, AssaultParty[] assaultParties) {
        super(threadName, thiefID, museum, concentrationSite, collectionSite);
        thiefState = OrdinaryThiefStates.CONCENTRATION_SITE;
        this.assaultParties = assaultParties;
    }

    @Override
    public void run() {
        while (true) {
            while (concentrationSite.amINeeded()) {
                int assaultID = concentrationSite.prepareExcursion();
                logger(this, "entered Assault Party " + assaultID);
                AssaultParty party = assaultParties[assaultID];
                party.crawlIn();
                museum.rollACanvas(assaultID);
                party.reverseDirection();
                party.crawlOut();
                collectionSite.handACanvas(assaultID);
                /*
                // simulating assault, to be deleted
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {}
                */
            }
        }
    }
}
