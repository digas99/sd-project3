package entities;

import sharedRegions.*;

import static utils.Parameters.*;
import static utils.Utils.*;

/**
 * Thread that represents the Ordinary Thief
 */

public class OrdinaryThief extends Thief {

    /**
     * Object the represents the party of the thief
     */
    private AssaultParty party;

    /**
     * Displacement of the thief
     */
    private int displacement;

    /**
     * True if the thief has a canvas
     */
    private boolean hasCanvas;

    /**
     * Array of Museum Rooms
     */
    private Museum.Room[] rooms;

    /**
     * General repository
     */
    private GeneralRepos repos;


    /**
     * Set AssaultParty array
     * @param partyID AssaultParty ID
     * @param reset True if the party should be reset
     */
    public void setAssaultParty(int partyID, boolean reset) {
        logger(this, "Assault Party: "+partyID);
        party = assaultParties[partyID];
        if (reset) party.resetAssaultParty();
    }

    /**
     * Set the room of the party
     * @param roomID Room ID
     */
    public void setRoomOfParty(int roomID) {
        party.setRoom(rooms[roomID]);
    }

    /**
     * Get the displacement of the thief
     * @return Displacement
     */
    public int getDisplacement() {
        return displacement;
    }

    /**
     * Get the room of the thief
     * @return True if the thief has a canvas
     */
    public boolean hasCanvas() {
        return hasCanvas;
    }

    /**
     * Set if the thief has a canvas
     * @param hasCanvas True if the thief has a canvas
     */
    public void hasCanvas(boolean hasCanvas) {
        this.hasCanvas = hasCanvas;
    }

    /**
     * Set the party of the thief
     * @param party Party
     */
    public void setParty(AssaultParty party) {
        this.party = party;
    }

    /**
     * Get the party of the thief
     * @return Party
     */
    public AssaultParty getParty() {
        return party;
    }

    /**
     * Get the party ID of the thief
     * @return Party ID
     */
    public int getPartyID() {
        return party.getID();

    }

    /**
     * Get the thieves from the party
     * @return Array of thieves
     */
    public int[] getThievesFromParty() {
        return party.getThieves();
    }
    /**
     * Get the room ID of the party
     * @return Room ID
     */
    public int getRoomID() {
        return party.getRoom().getID();
    }

    /**
     * Ordinary Thief Constructor
     * @param threadName Thread name
     * @param thiefID Thief ID
     * @param museum Museum
     * @param concentrationSite Concentration Site
     * @param collectionSite Collection Site
     * @param assaultParties Assault Parties
     * @param repos General Repository
     */

    public OrdinaryThief(String threadName, int thiefID, Museum museum, ConcentrationSite concentrationSite, CollectionSite collectionSite, AssaultParty[] assaultParties, GeneralRepos repos){
        super(threadName, thiefID, museum, concentrationSite, collectionSite, assaultParties,repos);
        thiefState = OrdinaryThiefStates.CONCENTRATION_SITE;
        displacement = random(MIN_DISPLACEMENT, MAX_DISPLACEMENT);
        hasCanvas = false;
        rooms = museum.getRooms();
        this.repos = repos;
    }

    /**
     * Life cycle of the Ordinary Thief
     */
    @Override
    public void run() {
        while (concentrationSite.amINeeded()) {
            if (concentrationSite.prepareExcursion()) {
                party.crawlIn();
                museum.rollACanvas();
                party.reverseDirection();
                party.crawlOut();
                collectionSite.handACanvas();
            } else break;
        }
    }
}
