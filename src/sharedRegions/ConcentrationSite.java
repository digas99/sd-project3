package sharedRegions;

import entities.*;

public class ConcentrationSite {
    private final OrdinaryThief thieves[];

    public ConcentrationSite(int n_thieves) {
        // init thieves
        thieves = new OrdinaryThief[n_thieves];
        for (int i = 0; i < n_thieves; i++) {
            thieves[i] = null;
        }
    }

    public boolean amINeeded() {
        OrdinaryThief thief = (OrdinaryThief) Thread.currentThread();
        thieves[thief.getThiefID()] = thief;

        if (thief.getThiefState() == OrdinaryThiefStates.COLLECTION_SITE)
            thief.setThiefState(OrdinaryThiefStates.CONCENTRATION_SITE);
    }

    public void prepareAssaultParty() {
        MasterThief master = (MasterThief) Thread.currentThread();
        master.setThiefState(MasterThiefStates.ASSEMBLING_GROUP);

        AssaultParty[] assaultParties = master.getAssaultParties();
        // get first not full party

    }

    public void sendAssaultParty() {
        MasterThief master = (MasterThief) Thread.currentThread();
        master.setThiefState(MasterThiefStates.DECIDING_WHAT_TO_DO);
    }
}
