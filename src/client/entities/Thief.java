package client.entities;

import client.stubs.AssaultPartyStub;
import client.stubs.CollectionSiteStub;
import client.stubs.ConcentrationSiteStub;
import client.stubs.MuseumStub;
import server.sharedRegions.*;

public abstract class Thief extends Thread {

    /**
     * Concentration Site object of the thief
     */
    protected final ConcentrationSiteStub concentrationSite;

    /**
     * Collection Site object of the thief
     */
    protected final CollectionSiteStub collectionSite;

    /**
     * Museum object of the thief
     */
    protected final MuseumStub museum;

    /**
     * Assault Party array that stores the assault parties of the thief
     */
    protected AssaultPartyStub[] assaultParties;

    /**
     * Thief ID
     */
    protected int thiefID;

    /**
     * Thief state
     */
    protected int thiefState;


    /**
     * Set the thief state
     * @param thiefState Thief state
     */
    public void setThiefState(int thiefState) {
        this.thiefState = thiefState;
    }

    /**
     * Get the thief ID
     * @return Thief ID
     */
    public int getThiefID() {
        return thiefID;
    }

    /**
     * Set the thief ID
     * @param thiefID Thief ID
     */
    public void setThiefID(int thiefID) {
        this.thiefID = thiefID;
    }

    /**
     * Get the Museum
     * @return Museum object
     */
    public MuseumStub getMuseum() {
        return museum;
    }

    /**
     * THief constructor
     * @param threadName Thread name
     * @param id Thief ID
     * @param museum Museum
     * @param concentrationSite Concentration Site
     * @param collectionSite Collection Site
     * @param assaultParties Assault Parties
     */
    public Thief(String threadName, int id, MuseumStub museum, ConcentrationSiteStub concentrationSite, CollectionSiteStub collectionSite, AssaultPartyStub[] assaultParties) {
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
