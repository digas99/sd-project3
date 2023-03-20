package entities;

import sharedRegions.AssaultParty;
import sharedRegions.CollectionSite;
import sharedRegions.ConcentrationSite;
import sharedRegions.Museum;

import static utils.Parameters.*;
import static utils.Utils.random;

public abstract class Thief extends Thread {
    protected final ConcentrationSite concentrationSite;
    protected final CollectionSite collectionSite;
    protected final Museum museum;

    protected int thiefID;
    protected int thiefState;
    protected int displacement;

    public int getThiefState() {
        return thiefState;
    }

    public void setThiefState(int thiefState) {
        this.thiefState = thiefState;
    }

    public int getDisplacement() {
        return displacement;
    }

    public void setDisplacement(int displacement) {
        this.displacement = displacement;
    }

    public int getThiefID() {
        return thiefID;
    }

    public void setThiefID(int thiefID) {
        this.thiefID = thiefID;
    }

    public Thief(String threadName, int id, Museum museum, ConcentrationSite concentrationSite, CollectionSite collectionSite) {
        super(threadName);

        this.thiefID = id;
        this.displacement = random(MIN_DISPLACEMENT, MAX_DISPLACEMENT);
        this.museum = museum;
        this.concentrationSite = concentrationSite;
        this.collectionSite = collectionSite;
    }

    @Override
    public String toString() {
        return thiefID + " - " + getName();
    }

}
