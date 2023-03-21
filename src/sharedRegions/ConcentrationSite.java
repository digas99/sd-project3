package sharedRegions;

import entities.*;
import genclass.GenericIO;
import utils.MemException;
import utils.MemFIFO;

import static utils.Parameters.*;
import static utils.Utils.logger;

public class ConcentrationSite {
    private final MemFIFO<Integer> thieves;
    private final MemFIFO<Integer> pickedThieves;
    private int assaultPartyID;
    private int nAssaultParties;
    private boolean allowIn;

    public boolean hasEnoughThieves() {
        return thieves.size() >= N_THIEVES_PER_PARTY;
    }

    public ConcentrationSite(int nThieves, int nAssaultParties) throws MemException {
        // init thieves
        thieves = new MemFIFO<>(new Integer[nThieves]);
        pickedThieves = new MemFIFO<>(new Integer[N_THIEVES_PER_PARTY]);
        this.nAssaultParties = nAssaultParties;
        assaultPartyID = 0;
        allowIn = true;
    }

    public synchronized void startOperations() {
        MasterThief master = (MasterThief) Thread.currentThread();
        master.setThiefState(MasterThiefStates.DECIDING_WHAT_TO_DO);
    }

    public synchronized boolean amINeeded() {
        if (!allowIn) return false;

        OrdinaryThief thief = (OrdinaryThief) Thread.currentThread();
        try {
            thieves.write(thief.getThiefID());
            logger(thief, "entered Concentration Site");
        } catch (MemException e) {
        }

        // wake up master if party can be made
        if (hasEnoughThieves()) {
            allowIn = false;
            notifyAll();
        }

        if (thief.getThiefState() == OrdinaryThiefStates.COLLECTION_SITE)
            thief.setThiefState(OrdinaryThiefStates.CONCENTRATION_SITE);

        return true;
    }

    public synchronized void prepareAssaultParty() {
        MasterThief master = (MasterThief) Thread.currentThread();
        master.setThiefState(MasterThiefStates.ASSEMBLING_GROUP);

        // sleep until there are enough thieves for an assault party
        while (!hasEnoughThieves()) {
            try {
                logger(master, "waiting for thieves to enter concentration site");
                wait();
            } catch (InterruptedException e) {}
        }

        logger(master, "woke up");
        logger(master, "assembling Assault Party " + assaultPartyID);
        try {
            for (int i = 0; i < N_THIEVES_PER_PARTY; i++) {
                int thiefID = thieves.read();
                pickedThieves.write(thiefID);
            }

        } catch (MemException e) {}

        // party is decided so wake up all thieves from that party
        notifyAll();

        // sleep and wait until all thieves have joined the party
        while (pickedThieves.size() > 0) {
            try {
                logger(master, "waiting for thieves to join party");
                wait();
            } catch (InterruptedException e) {}
        }

        logger(master, "woke up");
    }

    public synchronized int prepareExcursion() {
        OrdinaryThief thief = (OrdinaryThief) Thread.currentThread();

        while (pickedThieves.size() == 0) {
            try {
                logger(thief, "waiting to be assigned a party");
                wait();
            } catch (InterruptedException e) {}
        }

        logger(thief, "woke up");

        thief.setThiefState(OrdinaryThiefStates.CRAWLING_INWARDS);
        try {
            pickedThieves.read();
        } catch (MemException e) {}

        // if thief is the last to enter the party, notify master
        if (pickedThieves.size() == 0)
            notifyAll();

        return assaultPartyID;
    }

    public synchronized void sendAssaultParty() {
        MasterThief master = (MasterThief) Thread.currentThread();
        master.setThiefState(MasterThiefStates.DECIDING_WHAT_TO_DO);
        GenericIO.writelnString("Sending Assault Party " + assaultPartyID);

        // setup id for next assault party
        assaultPartyID = (assaultPartyID + 1) % nAssaultParties;
        allowIn = true;
    }
}
