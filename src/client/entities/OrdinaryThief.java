package client.entities;

import client.stubs.AssaultPartyStub;
import client.stubs.CollectionSiteStub;
import client.stubs.ConcentrationSiteStub;
import client.stubs.MuseumStub;
import genclass.GenericIO;
import server.sharedRegions.*;

import static utils.Parameters.*;
import static utils.Utils.*;

/**
 * Thread that represents the Ordinary Thief
 */

public class OrdinaryThief extends Thief {
    private int partyID;
    private int roomID;
    /**
     * Displacement of the thief
     */
    private final int displacement;

    /**
     * Ordinary Thief Constructor
     * @param threadName Thread name
     * @param thiefID Thief ID
     * @param museum Museum
     * @param concentrationSite Concentration Site
     * @param collectionSite Collection Site
     * @param assaultParties Assault Parties
     */

    public OrdinaryThief(String threadName, int thiefID, MuseumStub museum, ConcentrationSiteStub concentrationSite, CollectionSiteStub collectionSite, AssaultPartyStub[] assaultParties){
        super(threadName, thiefID, museum, concentrationSite, collectionSite, assaultParties);
        thiefState = OrdinaryThiefStates.CONCENTRATION_SITE;
        displacement = random(MIN_DISPLACEMENT, MAX_DISPLACEMENT);
        partyID = roomID = -1;
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
        while (concentrationSite.amINeeded()) {
            int[] assaultData = concentrationSite.prepareExcursion();
            if (assaultData != null && assaultData[0] != -1) {
                partyID = assaultData[0];
                roomID = assaultData[1];
                AssaultPartyStub party = assaultParties[partyID];

                party.crawlIn(museum.getRoomDistance(roomID), displacement);
                boolean hasCanvas = museum.rollACanvas(roomID);
                party.reverseDirection();
                party.crawlOut(museum.getRoomDistance(roomID), displacement);
                GenericIO.writelnString("Thief " + thiefID + " has a canvas: " + hasCanvas);
                collectionSite.handACanvas(partyID, roomID, hasCanvas);
            } else break;
        }
    }
}
