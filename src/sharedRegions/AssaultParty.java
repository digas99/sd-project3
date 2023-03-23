package sharedRegions;

import entities.MasterThief;
import entities.MasterThiefStates;
import entities.OrdinaryThief;
import entities.OrdinaryThiefStates;
import genclass.GenericIO;
import utils.MemException;

import static utils.Utils.logger;
import static utils.Utils.min;
import static utils.Parameters.*;

public class AssaultParty {
   private int id;
   private OrdinaryThief[] thieves;
   private int thievesPerParty;
   private int roomID;
   private int nextThiefPos;
   private int nextThiefID;
   private int nThieves;
   private int max_separation;
   private Museum.Room room;

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public int getRoomID() {
      return roomID;
   }

   public void setRoomID(int roomID) {
      this.roomID = roomID;
   }

   public int size() {
      return nThieves;
   }

   public Museum.Room getRoom() {
      return room;
   }

   public void setRoom(Museum.Room room) {
      this.room = room;
   }

   public AssaultParty(int id, int size, int thievesPerParty, int max_separation, GeneralRepos repos) throws MemException {
      this.id = id;
      this.thieves = new OrdinaryThief[size];
      nThieves = nextThiefPos = 0;
      nextThiefID = roomID = -1;
      this.thievesPerParty = thievesPerParty;
      this.max_separation = max_separation;
   }

   @Override
   public String toString() {
      return "AssaultParty_"+id;
   }

   /**
    * Send Assault Party.
    * The master thief calls this method to wake up the first thief of the Assault Party and trigger his crawling.
    * It also sets up the ID for the next assault party and resets other variables.
    */

   public synchronized void sendAssaultParty() {
      MasterThief master = (MasterThief) Thread.currentThread();
      master.setThiefState(MasterThiefStates.DECIDING_WHAT_TO_DO);
      GenericIO.writelnString("Sending " + this + " to room " + roomID);

      // wake up first thief
      notifyAll();
   }

   public synchronized void crawlIn() {
      OrdinaryThief thief = (OrdinaryThief) Thread.currentThread();
      thieves[nThieves++] = thief;
      thief.setThiefState(OrdinaryThiefStates.CRAWLING_INWARDS);

      // first thief to arrive sets next thief as himself
      if (nextThiefID == -1) {
         nextThiefID = thief.getThiefID();
         room = thief.getMuseum().getRoom(roomID);
      }

      // wait for turn
      while (true) {
         // sleep if not his turn
         while (nextThiefID != thief.getThiefID() || nThieves < thievesPerParty) {
            try {
               logger(this, thief, "waiting for turn");
               wait();
            } catch (InterruptedException e) {}
         }

         logger(this, thief, "woke up");
         while (thief.canIMove())
            move(thief);

         // wake up next thief
         OrdinaryThief nextThief = lowerThief(thief);
         nextThiefID = nextThief.getThiefID();
         nextThief.setMovesLeft(MAX_SEPARATION_LIMIT);
         nextThief.canIMove(true);
         notifyAll();

         // if end of path, break
         if (thief.getPosition() == room.getDistance()) {
            thief.setThiefState(OrdinaryThiefStates.AT_A_ROOM);
            break;
         }
      }
   }

   private void move(OrdinaryThief thief) {
      if (thief.getMovesLeft() == 0) {
         thief.canIMove(false);
         return;
      }

      OrdinaryThief lowerThief = lowerThief(thief);
      int distanceToNextThief = lowerThief.getPosition() - thief.getPosition();
      int distanceToGoal = room.getDistance() - thief.getPosition();
      int distanceToMove = min(thief.getDisplacement(), distanceToGoal, thief.getMovesLeft());

      // is first thief
      if (isFirstThief(thief)) {
         distanceToMove = min(distanceToMove, distanceToNextThief);

         thief.setPosition(thief.getPosition() + distanceToMove);
         thief.setMovesLeft(thief.getMovesLeft() - distanceToMove);
         return;
      }

      // if middle or last thief
      if (!isLastThief(thief))
         distanceToMove = min(distanceToMove, distanceToNextThief);

      thief.setPosition(thief.getPosition() + distanceToMove);
      thief.setMovesLeft(thief.getMovesLeft() - distanceToMove);

      // fix excess moves or if went to a occupied position
      if (checkExcessMove(thief, distanceToMove) || checkOccupiedMove(thief)) thief.setMovesLeft(0);
   }

   /**
    * Check if thief moved in excess.
    * Fix thief position if it moved past another thief and went too far related to that new lower thief.
    *
    * @param thief
    * @param distanceToMove
    * @return true if a fix in the position was made.
    */
   private boolean checkExcessMove(OrdinaryThief thief, int distanceToMove) {
      OrdinaryThief newLowerThief = lowerThief(thief);
      int distanceToNextThief = newLowerThief.getPosition() - thief.getPosition();
      if (distanceToNextThief > max_separation) {
         int excessMove = distanceToMove - distanceToNextThief;
         // fix position
         thief.setPosition(thief.getPosition() - excessMove);
         return true;
      }
      return false;
   }

   /**
    * Check if thief move went to an occupied position.
    * The thief's position is fixed (decreased) until it is no longer in an occupied position.
    *
    * @param thief
    * @return true if a fix in the position was made.
    */

   private boolean checkOccupiedMove(OrdinaryThief thief) {
      boolean wrongMove = false;
      // fix if it got to occupied position
      while (inOccupiedPos(thief)) {
         wrongMove = true;
         thief.setPosition(thief.getPosition() - 1);
      }
      return wrongMove;
   }

   /**
    * Check if thief is in occupied position.
    *
    * @param thief
    * @return true if it is in occupied position, false if not
    */

   private boolean inOccupiedPos(OrdinaryThief thief) {
      int thiefPos = thief.getPosition();
      for (OrdinaryThief t : thieves) {
         if (thief != t) {
            if (thiefPos == t.getPosition())
               return true;
         }
      }
      return false;
   }

   /**
    * Get the thief that is in a lower position that the one given.
    * This behaves in a circular way.
    *
    * @param thief
    * @return thief in a lower position
    */

   private OrdinaryThief lowerThief(OrdinaryThief thief) {
      int thiefPos = thief.getPosition();
      int nextPos = 0;
      OrdinaryThief nextThief = null;
      for (OrdinaryThief t : thieves) {
         if (t.getPosition() < thiefPos && t.getPosition() > nextPos) {
            nextPos = t.getPosition();
            nextThief = t;
         }
      }

      // make it circular
      if (nextThief == null) {
         // TODO
      }

      return nextThief;
   }

   /**
    * Get the thief that is in a higher position that the one given.
    * This behaves in a circular way.
    *
    * @param thief
    * @return thief in a higher position
    */

   private OrdinaryThief higherThief(OrdinaryThief thief) {
      int thiefPos = thief.getPosition();
      int prevPos = Integer.MAX_VALUE;
      OrdinaryThief prevThief = null;
      for (OrdinaryThief t : thieves) {
         if (t.getPosition() > thiefPos && t.getPosition() < prevPos) {
            prevPos = t.getPosition();
            prevThief = t;
         }
      }

      // make it circular
      if (prevThief == null) {
         // TODO
      }

      return prevThief;
   }

   /**
    * Check if given thief is the first thief (the one in the highest position)
    *
    * @param thief
    * @return true if it is the first, false if not
    */

   private boolean isFirstThief(OrdinaryThief thief) {
      int highestPos = 0;
      OrdinaryThief firstThief = null;
      for (OrdinaryThief t : thieves) {
         if (t.getPosition() > highestPos) {
             highestPos = t.getPosition();
             firstThief = t;
         }
      }
      return thief == firstThief;
   }

   /**
    * Check if given thief is the last thief (the one in the lowest position)
    *
    * @param thief
    * @return true if it is the last, false if not
    */

   private boolean isLastThief(OrdinaryThief thief) {
      int leastPos = Integer.MAX_VALUE;
      OrdinaryThief lastThief = null;
      for (OrdinaryThief t : thieves) {
         if (t.getPosition() < leastPos) {
            leastPos = t.getPosition();
            lastThief = t;
         }
      }
      return thief == lastThief;
   }

   public synchronized void reverseDirection() {
        ((OrdinaryThief) Thread.currentThread()).setThiefState(OrdinaryThiefStates.CRAWLING_OUTWARDS);
   }

   public synchronized void crawlOut() {
      OrdinaryThief thief = (OrdinaryThief) Thread.currentThread();
      thief.setThiefState(OrdinaryThiefStates.CRAWLING_OUTWARDS);
   }
}
