package entities;

import sharedRegions.AssaultParty;
import sharedRegions.CollectionSite;
import sharedRegions.ConcentrationSite;
import sharedRegions.Museum;

import static utils.Parameters.*;
import static utils.Utils.random;
import static utils.Utils.max;

public class OrdinaryThief extends Thief {
    private AssaultParty party;
    private boolean canIMove;
    private int position;
    private int displacement;
    private int movesLeft;

    public void joinParty(int partyID) {
        this.party = assaultParties[partyID];
    }

    public AssaultParty getParty() {
        return party;
    }

    public boolean canIMove() {
        return canIMove;
    }

    public void canIMove(boolean canIMove) {
        this.canIMove = canIMove;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getDisplacement() {
        return displacement;
    }

    public void setDisplacement(int displacement) {
        this.displacement = displacement;
    }

    public int getMovesLeft() {
        return movesLeft;
    }

    public void setMovesLeft(int movesLeft) {
        this.movesLeft = movesLeft;
    }

    public OrdinaryThief(String threadName, int thiefID, Museum museum, ConcentrationSite concentrationSite, CollectionSite collectionSite, AssaultParty[] assaultParties) {
        super(threadName, thiefID, museum, concentrationSite, collectionSite, assaultParties);
        thiefState = OrdinaryThiefStates.CONCENTRATION_SITE;
        displacement = random(MIN_DISPLACEMENT, MAX_DISPLACEMENT);
        movesLeft = max(displacement, MAX_SEPARATION_LIMIT);
        canIMove = false;
    }

    @Override
    public void run() {
        while (true) {
            while (concentrationSite.amINeeded()) {
                concentrationSite.prepareExcursion();
                party.crawlIn();
                //museum.rollACanvas(assaultID);
                //party.reverseDirection();
                //party.crawlOut();
                //collectionSite.handACanvas(assaultID);

                // simulating assault, to be deleted
                try {
                    Thread.sleep(100000);
                } catch (InterruptedException e) {}
            }
        }
    }
}
