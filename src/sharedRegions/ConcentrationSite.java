package sharedRegions;

import entities.*;
import utils.MemException;
import utils.MemStack;
import utils.Monitor;

import static utils.Parameters.*;

public class ConcentrationSite {
    private final MemStack<OrdinaryThief> thieves;
    private MasterThief master;

    public boolean hasEnoughThieves() {
        return thieves.size() >= N_THIEVES_PER_PARTY;
    }

    public ConcentrationSite(int nThieves) throws MemException {
        // init thieves
        thieves = new MemStack<>(new OrdinaryThief[nThieves]);
        master = null;
    }

    public void startOperations() throws InterruptedException {
        master = (MasterThief) Thread.currentThread();
        master.setThiefState(MasterThiefStates.DECIDING_WHAT_TO_DO);

        // sleep until there are enough thieves for an assault party
        while (!hasEnoughThieves()) {
            master.getDecisionMonitor().wait();
        }
    }

    public boolean amINeeded() throws MemException {
        OrdinaryThief thief = (OrdinaryThief) Thread.currentThread();
        thieves.write(thief);

        // wake up master if party can be made
        if (hasEnoughThieves())
            master.getDecisionMonitor().notify();

        if (thief.getThiefState() == OrdinaryThiefStates.COLLECTION_SITE)
            thief.setThiefState(OrdinaryThiefStates.CONCENTRATION_SITE);

        while (!thief.isInAssaultParty()) {
            try {
                thief.getPartyMonitor().wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return thief.isInAssaultParty();
    }

    public void prepareAssaultParty() throws MemException {
        master.setThiefState(MasterThiefStates.ASSEMBLING_GROUP);

        AssaultParty assaultParty = master.getAssaultParties().read();
        Monitor[] thievesMonitors = new Monitor[N_THIEVES_PER_PARTY];
        for (int i = 0; i < N_THIEVES_PER_PARTY; i++) {
           OrdinaryThief thief = thieves.read();
           if (!thief.isInAssaultParty()) {
               assaultParty.thieves.write(thief);
               thief.setInAssaultParty(true);
               thievesMonitors[i] = thief.getPartyMonitor();
           }
        }

        // party is filled up and wake up all thieves from that party
        for (Monitor thiefMonitor : thievesMonitors)
            thiefMonitor.notify();
    }

    public void sendAssaultParty() {
        MasterThief master = (MasterThief) Thread.currentThread();
        master.setThiefState(MasterThiefStates.DECIDING_WHAT_TO_DO);
    }
}
