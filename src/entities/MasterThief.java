package entities;

import genclass.GenericIO;
import sharedRegions.AssaultParty;
import sharedRegions.CollectionSite;
import sharedRegions.ConcentrationSite;
import sharedRegions.Museum;
import utils.MemException;
import utils.MemFIFO;

public class MasterThief extends Thief {
    private MemFIFO<AssaultParty> assaultParties;

    public MemFIFO<AssaultParty> getAssaultParties() {
        return assaultParties;
    }

    public MasterThief(String threadName, int thiefID, Museum museum, ConcentrationSite concentrationSite, CollectionSite collectionSite, AssaultParty[] assaultParties) throws MemException {
        super(threadName, thiefID, museum, concentrationSite, collectionSite);

        // fill up assault parties queue
        this.assaultParties = new MemFIFO<>(assaultParties);
        for (AssaultParty party : assaultParties) {
            this.assaultParties.write(party);
        }

        setThiefState(MasterThiefStates.PLANNING_HEIST);
    }

    @Override
    public void run() {
        while(true) {
            concentrationSite.startOperations();
            concentrationSite.prepareAssaultParty();
        }
    }

    public void takeARest() {

    }

    public void appraiseSit() {

    }

}
