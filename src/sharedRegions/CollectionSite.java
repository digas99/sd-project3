package sharedRegions;

import entities.MasterThief;
import entities.MasterThiefStates;
import entities.OrdinaryThief;
import genclass.GenericIO;
import utils.MemException;
import utils.MemFIFO;

import static utils.Parameters.*;
import static utils.Utils.all;

public class CollectionSite {
    private MemFIFO<Integer> arrivedThieves;
    private final boolean[] roomEmpty;

    public CollectionSite(GeneralRepos repos) {
        try {
            arrivedThieves = new MemFIFO<>(new Integer[N_THIEVES_ORDINARY]);
        } catch (MemException e) {}
        roomEmpty = new boolean[N_ROOMS];
        for (int i = 0; i < N_ROOMS; i++)
            roomEmpty[i] = false;
    }

    public synchronized int appraiseSit() {
        MasterThief master = (MasterThief) Thread.currentThread();

        // check if the heist should end
        if (all(roomEmpty)) {
            // update concentration site
            master.getConcentrationSite().endHeist(true);
            return END_HEIST;
        }

        // check if it should wait for canvas
        GenericIO.writelnString("Master: " + master.getConcentrationSite().getJoinedThieves());
        if (master.sentAnyAssaultParty() && master.getConcentrationSite().getJoinedThieves() < N_THIEVES_PER_PARTY)
            return WAIT_FOR_CANVAS;

        // otherwise, make more assault parties
        return CREATE_ASSAULT_PARTY;
    }

    public synchronized void takeARest() {
        MasterThief master = (MasterThief) Thread.currentThread();
        master.setThiefState(MasterThiefStates.WAITING_ARRIVAL);

        while (arrivedThieves.size() == 0) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }
    }

    public synchronized void handACanvas() {
        OrdinaryThief thief = (OrdinaryThief) Thread.currentThread();
        roomEmpty[thief.getParty().getRoomID()] = !thief.hasCanvas();

        // wake up master
        try {
            arrivedThieves.write(thief.getThiefID());
            notifyAll();
        } catch (MemException e) {}

        // wait for master to collect canvas
        while (arrivedThieves.has(thief.getThiefID())) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }

        thief.hasCanvas(false);
    }

    public void collectACanvas() {
        try {
            // collect a canvas by taking a thief from the queue
            arrivedThieves.read();
        } catch (MemException e) {}

        // update rooms states
        for (int i = 0; i < N_ROOMS; i++) {
            if (roomEmpty[i])
                ((MasterThief) Thread.currentThread()).getConcentrationSite().setRoomState(i, EMPTY_ROOM);
        }
    }

    public void sumUpResults() {
        MasterThief master = (MasterThief) Thread.currentThread();
        master.setThiefState(MasterThiefStates.PRESENTING_REPORT);

    }
}
