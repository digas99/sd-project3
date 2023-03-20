package sharedRegions;

import entities.*;
import genclass.GenericIO;
import utils.MemException;
import utils.MemStack;

import static utils.Parameters.*;

public class ConcentrationSite {
    private final MemStack<OrdinaryThief> thieves;

    public boolean hasEnoughThieves() {
        return thieves.size() >= N_THIEVES_PER_PARTY;
    }

    public ConcentrationSite(int nThieves) throws MemException {
        // init thieves
        thieves = new MemStack<>(new OrdinaryThief[nThieves]);
    }

    public synchronized void startOperations() {
        MasterThief master = (MasterThief) Thread.currentThread();
        master.setThiefState(MasterThiefStates.DECIDING_WHAT_TO_DO);
    }

    public synchronized void amINeeded() {
        OrdinaryThief thief = (OrdinaryThief) Thread.currentThread();
        try {
            thieves.write(thief);
        } catch (MemException e) {}

        // wake up master if party can be made
        if (hasEnoughThieves())
            notifyAll();

        if (thief.getThiefState() == OrdinaryThiefStates.COLLECTION_SITE)
            thief.setThiefState(OrdinaryThiefStates.CONCENTRATION_SITE);

        while (!thief.isInAssaultParty()) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }
    }

    public synchronized void prepareAssaultParty() {
        MasterThief master = (MasterThief) Thread.currentThread();
        master.setThiefState(MasterThiefStates.ASSEMBLING_GROUP);

        // sleep until there are enough thieves for an assault party
        while (!hasEnoughThieves()) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }

        try {
            AssaultParty assaultParty = master.getAssaultParties().read();
            for (int i = 0; i < N_THIEVES_PER_PARTY; i++) {
               OrdinaryThief thief = thieves.read();
               if (!thief.isInAssaultParty()) {
                   assaultParty.getThieves().write(thief);
                   thief.setInAssaultParty(true);
               }
            }

            GenericIO.writelnString(assaultParty.toString());
            assaultParty.getThieves().println();
        } catch (MemException e) {}

        // party is filled up and wake up all thieves from that party
        notifyAll();
    }

    public synchronized void sendAssaultParty() {
        MasterThief master = (MasterThief) Thread.currentThread();
        master.setThiefState(MasterThiefStates.DECIDING_WHAT_TO_DO);
    }
}
