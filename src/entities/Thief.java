package entities;

import sharedRegions.AssaultParty;
import sharedRegions.CollectionSite;
import sharedRegions.ConcentrationSite;
import sharedRegions.Museum;

import static utils.Parameters.*;
import static utils.Utils.random;

public abstract class Thief extends Thread {
    private final ConcentrationSite concentrationSite;
    private final CollectionSite collectionSite;
    private final Museum museum;

    private int thiefState;
    private int displacement;

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

    public Thief(String threadName, Museum museum, ConcentrationSite concentrationSite, CollectionSite collectionSite) {
        super(threadName);

        this.displacement = random(MIN_DISPLACEMENT, MAX_DISPLACEMENT);
        this.museum = museum;
        this.concentrationSite = concentrationSite;
        this.collectionSite = collectionSite;
    }
}
