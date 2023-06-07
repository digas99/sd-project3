package client.entities;

import java.rmi.*;
import interfaces.*;
import genclass.GenericIO;
import server.objects.CollectionSite;
import server.objects.Museum;
import utils.MemException;
import utils.MemFIFO;

import static utils.Parameters.*;
import static utils.Utils.*;

/**
 * Thread that represents the Ordinary Thief
 */

public class OrdinaryThief extends Thief {

    private int ordinaryID;

    private int ordinaryState;

    private final Thread[] ordinary;
    /**
     * Number of entity groups requesting the shutdown
     */

    private final AssaultPartyInterface[] assaultPartiesStub;

    private final MuseumInterface museumStub;

    private final ConcentrationSiteInterface concentrationSiteStub;

    private final CollectionSiteInterface collectionSiteStub;

    private int partyID;

    private int roomID;




    private int nEntities;

    /**
     * Ordinary Thief Constructor
     * @param threadName Thread name
     * @param thiefID Thief ID
     * @param museum Museum
     * @param concentrationSite Concentration Site
     * @param collectionSite Collection Site
     * @param assaultParties Assault Parties
     */

    public OrdinaryThief(String threadName, int thiefID, MuseumInterface museum, ConcentrationSiteInterface concentrationSite, CollectionSiteInterface collectionSite, AssaultPartyInterface[] assaultParties){
        super(threadName, thiefID, museum, concentrationSite, collectionSite, assaultParties);
        thiefState = OrdinaryThiefStates.CONCENTRATION_SITE;
        ordinary = new Thread[N_THIEVES_ORDINARY];
        for (int i = 0; i < N_THIEVES_ORDINARY; i++)
            ordinary[i] = null;

        this.assaultPartiesStub = assaultParties;
        this.museumStub = museum;
        this.concentrationSiteStub = concentrationSite;
        this.collectionSiteStub = collectionSite;



    }

    @Override
    public String toString() {
        return super.toString() + " [Party: " + partyID + ", Room: " + roomID + "]";
    }

    /**
     * Life cycle of the Ordinary Thief
     */
    @Override
    public void run() {
        ordinaryID = this.thiefID;
        while (amINeeded()) {
            int[] assaultData = prepareExcursion();
            if (assaultData != null && assaultData[0] != -1) {
                int partyID = assaultData[0];
                int roomID = assaultData[1];
                AssaultPartyInterface party = assaultParties[partyID];

                crawlIn(partyID, getRoomDistance(roomID), displacement);
                boolean hasCanvas = rollACanvas(roomID);
                reverseDirection(partyID);
                crawlOut(partyID,getRoomDistance(roomID), displacement);
                GenericIO.writelnString("Thief " + thiefID + " has a canvas: " + hasCanvas);
                handACanvas(partyID, roomID, hasCanvas);
            } else break;
        }
    }

    boolean amINeeded(){
        ReturnBoolean ret = null;
        try {
            ret = concentrationSiteStub.amINeeded(ordinaryID);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        this.ordinaryState = ret.getIntStateVal();
        return ret.getBooleanVal();
    }

    int [] prepareExcursion(){
        int [] ret = null;
        try {
            ret = concentrationSiteStub.prepareExcursion(ordinaryID);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return ret;

    }

    int crawlIn(int partyID,int roomID,int displacement){
        int ret = -1;
        try {
            ret = assaultPartiesStub[partyID].crawlIn(ordinaryID,roomID, displacement);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return ret;
    }

    boolean rollACanvas(int roomID){
        ReturnBoolean ret = null;
        try {
            ret = museumStub.rollACanvas(ordinaryID,roomID);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        this.ordinaryState = ret.getIntStateVal();
        return ret.getBooleanVal();
    }

    int handACanvas (int partyID,int roomID,boolean hasCanvas){
        int ret = -1;
        try {
            ret = collectionSiteStub.handACanvas(ordinaryID,partyID,roomID,hasCanvas);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return ret;
    }

    int reverseDirection(int partyID){
        int ret = -1;
        try {
            ret = assaultPartiesStub[partyID].reverseDirection(ordinaryID);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return ret;
    }

    int crawlOut(int roomID,int displacement){
        int ret = -1;
        try {
            ret = assaultPartiesStub[partyID].crawlOut(ordinaryID,roomID,displacement);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return ret;
    }







}
