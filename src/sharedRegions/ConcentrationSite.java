package sharedRegions;

import entities.*;
import utils.MemException;
import utils.MemStack;
import utils.Monitor;

import static utils.Parameters.*;

public class ConcentrationSite {
    private final MemStack<OrdinaryThief> thieves;

    public ConcentrationSite(int nThieves) throws MemException {
        // init thieves
        thieves = new MemStack<>(new OrdinaryThief[nThieves]);
    }

    public boolean amINeeded() throws MemException {
        OrdinaryThief thief = (OrdinaryThief) Thread.currentThread();
        thieves.write(thief);

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
        MasterThief master = (MasterThief) Thread.currentThread();
        master.setThiefState(MasterThiefStates.ASSEMBLING_GROUP);

        AssaultParty[] assaultParties = master.getAssaultParties();
        AssaultParty party;
        OrdinaryThief thief;
        Monitor[] thievesMonitors;
        for (int i = 0; i < assaultParties.length; i++) {
            party = assaultParties[i];
            thievesMonitors = new Monitor[N_THIEVES_PER_PARTY];
            for (int j = 0; j < N_THIEVES_PER_PARTY; j++) {
                thief = thieves.read();
                if (!thief.isInAssaultParty()) {
                    party.thieves.write(thief);
                    thief.setInAssaultParty(true);
                    thievesMonitors[j] = thief.getPartyMonitor();
                }
            }

            // party is filled up and wake up all thieves from that party
            for (Monitor thiefMonitor : thievesMonitors)
                thiefMonitor.notify();
        }

    }

    public void sendAssaultParty() {
        MasterThief master = (MasterThief) Thread.currentThread();
        master.setThiefState(MasterThiefStates.DECIDING_WHAT_TO_DO);
    }
}
