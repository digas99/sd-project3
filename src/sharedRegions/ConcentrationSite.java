package sharedRegions;

import entities.*;
import utils.MemException;
import utils.MemFIFO;

import static utils.Parameters.*;
import static utils.Utils.logger;

public class ConcentrationSite {
    private final MemFIFO<Integer> thieves;
    private final int[] rooms;
    private int assaultPartyID;
    private int joinedThieves;
    private boolean endHeist;
    private boolean makeParty;
    private AssaultParty assaultParty;

    private boolean hasEnoughThieves() {
        return thieves.size() >= N_THIEVES_PER_PARTY;
    }

    public int[] getRooms() {
        return rooms;
    }

    public void setRoomState(int room, int state) {
        rooms[room] = state;
    }

    public int getJoinedThieves() {
        return joinedThieves;
    }

    public void endHeist(boolean endHeist) {
        this.endHeist = endHeist;
    }

    public ConcentrationSite(GeneralRepos repos) throws MemException {
        thieves = new MemFIFO<>(new Integer[N_THIEVES_PER_PARTY]);
        rooms = new int[N_ROOMS];
        for (int i = 0; i < N_ROOMS; i++)
            rooms[i] = FREE_ROOM;
        assaultPartyID = joinedThieves = 0;
        endHeist = makeParty = false;
        assaultParty = null;
    }

    public String toString() {
        return "Concentration Site";
    }

    /**
     * Start Operations.
     * The master thief calls this method to start the heist.
     */

    public synchronized void startOperations() {
        MasterThief master = (MasterThief) Thread.currentThread();
        master.setThiefState(MasterThiefStates.DECIDING_WHAT_TO_DO);
    }

    /**
     * Is Ordinary Thief needed.
     * The thief calls this method to check if he sould enter the concentration site or not (if the heist has ended).
     *
     * @return returns true if the thief should enter the concentration site, false otherwise
     */

    public synchronized boolean amINeeded() {
        if (endHeist) return false;

        OrdinaryThief thief = (OrdinaryThief) Thread.currentThread();
        try {
            thieves.write(thief.getThiefID());
            logger(this, thief, "entered Concentration Site");
        } catch (MemException e) {
        }

        // wake up master for her to decide if she needs more thieves
        notifyAll();

        if (thief.getThiefState() == OrdinaryThiefStates.COLLECTION_SITE)
            thief.setThiefState(OrdinaryThiefStates.CONCENTRATION_SITE);

        return true;
    }

    /**
     * Prepare Assault Party.
     * The master thief calls this method to wait until there are enough thieves to form an assault party and then
     * wakes them up to join the party.
     *
     * @return returns the id of the assault party
     */

    public synchronized int prepareAssaultParty() {
        MasterThief master = (MasterThief) Thread.currentThread();
        master.setThiefState(MasterThiefStates.ASSEMBLING_GROUP);

        // sleep until a thief joins concentration site
        while (!hasEnoughThieves()) {
            try {
                logger(this, master, "waiting for thieves to enter concentration site");
                wait();
            } catch (InterruptedException e) {}
        }

        logger(this, master, "woke up");
        logger(this, master, "assembling Assault Party " + assaultPartyID);

        // wake up all thieves and give order to thieves to join party
        makeParty = true;
        notifyAll();

        // sleep and wait until all thieves have joined the party
        while (joinedThieves < N_THIEVES_PER_PARTY) {
            try {
                logger(this, master, " waiting for thieves to join party");
                wait();
            } catch (InterruptedException e) {}
        }

        logger(this, master, "woke up");

        // setup id for next assault party
        assaultPartyID = (assaultPartyID + 1) % N_ASSAULT_PARTIES;
        joinedThieves -= N_THIEVES_PER_PARTY;
        makeParty = false;

        return assaultParty.getId();
    }

    /**
     * Prepare Excursion.
     * The thief calls this method to be ready to join an assault party when the master thief wakes him up.
     */

    public synchronized void prepareExcursion() {
        OrdinaryThief thief = (OrdinaryThief) Thread.currentThread();

        // make sure thieves wake up in order
        try {
            while (!makeParty || thief.getThiefID() != thieves.peek() || joinedThieves >= N_THIEVES_PER_PARTY) {
                logger(this, thief, "waiting to be able to join party");
                wait();
            }
        } catch (InterruptedException | MemException e) {}

        logger(this, thief, "woke up");

        thief.setThiefState(OrdinaryThiefStates.CRAWLING_INWARDS);
        // remove itself from queue
        try {
            thieves.read();
            thief.joinParty(assaultPartyID);
            joinedThieves++;

            // only run this code once per party
            if (joinedThieves == 1) {
                assaultParty = thief.getParty();
                assaultParty.setRoomID(getFreeRoom());
            }
            logger(this, thief, "joined "+ assaultParty);
        } catch (MemException e) {}

        // wake up next thief to join party or master if all thieves have joined
        notifyAll();
    }

    private int getFreeRoom() {
        for (int i = 0; i < N_ROOMS; i++) {
            if (rooms[i] == FREE_ROOM) {
                rooms[i] = BUSY_ROOM;
                return i;
            }
        }
        return -1;
    }
}
