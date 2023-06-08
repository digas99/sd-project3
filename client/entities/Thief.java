package client.entities;

import java.rmi.*;
import interfaces.*;

public abstract class Thief extends Thread {

    /**
     * Concentration Site object of the thief
     */
    protected final ConcentrationSiteInterface concentrationSite;

    /**
     * Collection Site object of the thief
     */
    protected final CollectionSiteInterface collectionSite;

    /**
     * Museum object of the thief
     */
    protected final MuseumInterface museum;

    /**
     * Assault Party array that stores the assault parties of the thief
     */
    protected AssaultPartyInterface[] assaultParties;

    /**
     * Thief ID
     */
    protected int thiefID;

    /**
     * Thief state
     */
    protected int thiefState;



    public Thief(String threadName, int id, MuseumInterface museum, ConcentrationSiteInterface concentrationSite, CollectionSiteInterface collectionSite, AssaultPartyInterface[] assaultParties) {
        super(threadName);

        this.thiefID = id;
        this.museum = museum;
        this.concentrationSite = concentrationSite;
        this.collectionSite = collectionSite;
        this.assaultParties = assaultParties;
    }

    @Override
    public String toString() {
        return getName();
    }

}
