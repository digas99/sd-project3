package entities;

import sharedRegions.AssaultParty;
import sharedRegions.CollectionSite;
import sharedRegions.ConcentrationSite;
import sharedRegions.Museum;
import utils.MemException;
import utils.MemFIFO;
import utils.Monitor;

public class MasterThief extends Thief {
    private MemFIFO<AssaultParty> assaultParties;
    private Monitor decisionMonitor;

    public MemFIFO<AssaultParty> getAssaultParties() {
        return assaultParties;
    }

    public Monitor getDecisionMonitor() {
        return decisionMonitor;
    }

    public void setDecisionMonitor(Monitor decisionMonitor) {
        this.decisionMonitor = decisionMonitor;
    }

    public MasterThief(String threadName, int thiefID, Museum museum, ConcentrationSite concentrationSite, CollectionSite collectionSite, AssaultParty[] assaultParties) throws MemException {
        super(threadName, thiefID, museum, concentrationSite, collectionSite);

        // fill up assault parties queue
        for (AssaultParty party : assaultParties)
            this.assaultParties.write(party);

        setThiefState(MasterThiefStates.PLANNING_HEIST);
    }

    @Override
    public void run() {
        while(true) {
            try {
                concentrationSite.startOperations();
                concentrationSite.prepareAssaultParty();
            } catch (MemException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void takeARest() {

    }

    public void appraiseSit() {

    }

}
