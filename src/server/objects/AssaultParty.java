package server.objects;

import client.entities.MasterThief;
import client.entities.MasterThiefStates;
import client.entities.OrdinaryThief;
import client.entities.OrdinaryThiefStates;
import genclass.GenericIO;
import interfaces.AssaultPartyInterface;
import server.main.ServerAssaultParty;

import java.util.Arrays;
import java.util.Comparator;

import static utils.Parameters.*;
import static utils.Utils.*;

/**
 * Shared Region with methods used by the Master Thief and the Ordinary Thieves.
 * This class is responsible for the management of the Assault Parties and the crawling movements of its thieves.
 */

public class AssaultParty implements AssaultPartyInterface {
   /**
    * ID of the Assault Party
    */
   private int id;
   /**
    * Reference to Master Thief threads
    */
   private final Thread[] master;
   /**
    * Reference to Ordinary Thief threads
    */
   private final Thread[] ordinary;
   /**
    * Begin the crawl movement
    */
   private boolean begin;
   /**
    * Id of the next Thief to move
    */
   private int nextThiefID;
   /**
    * Party reached the room
    */
   private int inRoom;
   /**
    * Thieves progress in the Assault Party
    */
   private final Mapping[] thieves;
   /**
    * Number of entity groups requesting the shutdown
    */
   private int nEntities;

   /**
    * Get the ID of the Assault Party.
    *
    * @return ID of the Assault Party
    */
   public synchronized int getID() {
      return id;
   }

   /**
    * Add a thief to the Assault Party.
    * This method adds a thief to the thieves array, in the first empty position.
    * The thief is added as a Mapping.
    *
    * @param thiefID ID of the thief to be added
    * @param displacement Displacement of the thief to be added
    */
   public synchronized void addThief(int thiefID, int displacement) {
      for (int i = 0; i < N_THIEVES_PER_PARTY; i++) {
         if (thieves[i] == null) {
            thieves[i] = new Mapping(thiefID, 0, displacement);
            break;
         }
      }
   }

   /**
    * Reset the Assault Party.
    * This method resets the Assault Party, setting all the variables to their initial values.
    */
   public synchronized void resetAssaultParty() {
      GenericIO.writelnString("AssaultParty_" + id + " is resetting.");
      nextThiefID = -1;
      for (int i = 0; i < N_THIEVES_PER_PARTY; i++)
         thieves[i] = null;
   }

   public synchronized boolean allEnded() {
      for (int i = 0; i < thieves.length; i++) {
         if (!thieves[i].ended())
            return false;
      }
      return true;
   }

   /**
    * Assault Party initialization.
    * An id is given to the Assault Party, the array of thieves is initialized, and all the variables are set to their initial values.
    *
    * @param id ID of the Assault Party
    */
   public AssaultParty(int id) {
      this.id = id;
      master = new Thread[N_THIEVES_MASTER];
      for (int i = 0; i < N_THIEVES_MASTER; i++)
         master[i] = null;
      ordinary = new Thread[N_THIEVES_ORDINARY];
      for (int i = 0; i < N_THIEVES_ORDINARY; i++)
         ordinary[i] = null;
      thieves = new Mapping[N_THIEVES_PER_PARTY];
      resetAssaultParty();
   }

   @Override
   public String toString() {
      return "AssaultParty_"+id;
   }

   /**
    * Send an Assault Party.
    * This method is called by the Master Thief, and tells the Ordinary Thieves to start crawling.
    * The Master Thief goes to the DECIDING_WHAT_TO_DO state.
    */
   public synchronized int sendAssaultParty(int masterId) {
      master[masterId] = Thread.currentThread();

      GenericIO.writelnString("MasterThief_" + masterId + " is sending AssaultParty_" + id + ".");
      begin = true;
      notifyAll();

      return MasterThiefStates.DECIDING_WHAT_TO_DO;
   }

   /**
    * Reverse the direction of the crawling.
    * This method is called by the Ordinary Thieves and, when called by the last thief, it triggers the crawling out movement.
    * The Ordinary Thief goes to the CRAWLING_OUTWARDS state.
    */
   public synchronized int reverseDirection(int ordinaryId) {
      getThief(ordinaryId).isAtGoal(false);
      inRoom++;

      GenericIO.writelnString("Thief_" + ordinaryId + " is reversing direction.");

      // if last thief reaching room, set begin to true and notify all
      if (inRoom == N_THIEVES_PER_PARTY) {
         begin = true;
         notifyAll();
      }

      return OrdinaryThiefStates.CRAWLING_OUTWARDS;
   }

   private boolean sleep(int thiefID) {
      // \/\/  DEBUGING PURPOSES ONLY \/\/
      try {
         getThief(thiefID).isAtGoal();
      } catch (NullPointerException e) {
         e.printStackTrace();
         GenericIO.writelnString("ERROR: "+ this + " Ordinary_" + thiefID + " thieves " + Arrays.toString(thieves));
         System.exit(1);
      }
      // /\/\ DEBUGING PURPOSES ONLY /\/\

      //GenericIO.writelnString("Thief_" + thiefID + ":");
      //GenericIO.writelnString("isAtGoal: " + getThief(thiefID).isAtGoal() + " begin: " + begin + " nextThiefID: " + nextThiefID + " size: " + size(thieves));
      return !getThief(thiefID).isAtGoal() && (!begin || nextThiefID != thiefID || size(thieves) < N_THIEVES_PER_PARTY);
   }

   /**
    * Crawl towards the room of the Museum.
    * This method is called by the Ordinary Thieves, and makes them crawl from position 0 to the room's distance.
    * The Ordinary Thief goes to the CRAWLING_INW repos.setAssaultPartyID(id);
      repos.setOrdinaryThiefAssaultPartyID(thiefID, id);ARDS state.
    */
   public synchronized int crawlIn(int thiefID, int roomDistance, int displacement) {
      ordinary[thiefID] = Thread.currentThread();

       if (nextThiefID == -1) {
           resetAssaultParty();
           nextThiefID = thiefID;
       }

       addThief(thiefID, displacement);

       GenericIO.writelnString("Thief " + thiefID + " is crawling inwards.");

       notifyAll();

       do {
           // wait until master says to begin
           while (sleep(thiefID)) {
               try {
                   wait();
               } catch (InterruptedException e) {e.printStackTrace();}
           }
           //GenericIO.writelnString(getThiefPosition(thiefID)+"is the position of thief "+thiefID);
       } while(crawl(thiefID, displacement, 0, roomDistance));

       return OrdinaryThiefStates.AT_A_ROOM;
   }

   /**
    * Crawl towards the Collection Site and leave the Museum.
    * This method is called by the Ordinary Thieves, and makes them crawl from the room's distance to position 0.
    * The Ordinary Thief goes to the CRAWLING_OUTWARDS state.
    */
   public synchronized int crawlOut(int thiefID, int roomDistance, int displacement) {
      GenericIO.writelnString("Thief " + thiefID + " is crawling outwards.");

       do {
           // wait until master says to begin
           while (sleep(thiefID)){
               try {
                   wait();
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
       } while(crawl(thiefID, displacement, roomDistance, 0));

       GenericIO.writelnString("Thief " + thiefID + " is out of the Museum.");

       inRoom--;
       getThief(thiefID).isAtGoal(false);
       getThief(thiefID).ended(true);

       if (allEnded())
          begin = false;

       return OrdinaryThiefStates.COLLECTION_SITE;
   }

   /**
    * Crawl the thief to the goal.
    * @param thiefID thief id
    * @param beginning position to start from
    * @param goal position to crawl to
    * @return false if the thief has reached the goal
    */
   private boolean crawl(int thiefID, int displacement, int beginning, int goal) {
      boolean backwards = goal < beginning;

      while(move(thiefID, displacement, beginning, goal, backwards));

      // wake up next thief
      nextThiefID = getNextThief(thiefID, backwards);
      GenericIO.writelnString("Thief " + thiefID + " is waking up thief " + nextThiefID + " to crawl.");
      notifyAll();

      return !getThief(thiefID).isAtGoal();
   }
   /**
    * Calculate the next move of the thief towards the goal.
    * @param thiefID thief id
    * @param beginning position to start from
    * @param goal position to move to
    * @param backwards true if the thief is moving backwards
    * @return true if the thief has reached the goal
    */

   private boolean move(int thiefID, int displacement, int beginning, int goal, boolean backwards) {
      boolean validMove;
      int distanceToGoal;
      int move = displacement;
      do {
         distanceToGoal = Math.abs(goal - getThiefPosition(thiefID));
         move = min(move, distanceToGoal);

         updateThiefPosition(thiefID, move, backwards);

         // check thieves separation from each other
         validMove = !wrongSeparation(backwards) && !checkOverlay(beginning, goal);
         if (!validMove) {
            updateThiefPosition(thiefID, move, !backwards);
            //GenericIO.writelnString("Thief_" + thiefID + " is moving: " + move);
            move--;
         } else {
            if (getThiefPosition(thiefID) == goal) {
               getThief(thiefID).isAtGoal(true);
               return false;
            }
         }

         if (move == 0) return false;

      } while (!validMove);
      return true;
   }

   /**
    * update the position of the thief
    * @param thiefID id of the thief
    * @param move distance to move
    * @param backwards true if the thief is moving backwards
   */

   private void updateThiefPosition(int thiefID, int move, boolean backwards) {
      int thiefPos = getThiefPosition(thiefID);
      int newPos = !backwards ? thiefPos + move : thiefPos - move;
      setThiefPosition(thiefID, newPos);
   }

   /**
    * Check if the thieves are separated by more than MAX_SEPARATION_LIMIT
    * @param backwards true if the thieves are moving backwards
    * @return true if the thieves are separated by more than MAX_SEPARATION_LIMIT
    */
   private boolean wrongSeparation(boolean backwards) {
      orderThieves(backwards);

      for (int i = 0; i < N_THIEVES_PER_PARTY-1; i++) {
            int currentPos = thieves[i].getPosition();
            int nextPos = thieves[i+1].getPosition();
            if (Math.abs(currentPos - nextPos) > MAX_SEPARATION_LIMIT)
               return true;
      }
      return false;
   }


   /**
    * Check if the thieves are  not overlapping
    * @param beginning position to start from
    * @param goal position to move to
    * @retur true if the thieves are not overlapping
    */
   private boolean checkOverlay(int beginning, int goal) {
      for (int i = 0; i < N_THIEVES_PER_PARTY; i++) {
         for (int j = i+1; j < N_THIEVES_PER_PARTY; j++) {
            int currentPos = thieves[i].getPosition();
            int nextPos = thieves[j].getPosition();
            if (currentPos != beginning && currentPos != goal && currentPos == nextPos) {
               return true;
            }
         }
      }
      return false;
   }

   // get next thief

   /**
    * Get nextThiefID
    * @param thiefID id of the thief
    * @param backwards true if the thief is moving backwards
    * @return id of the next thief
    */
   private int getNextThief(int thiefID, boolean backwards) {
      orderThieves(backwards);
      int nextThiefID = -1;
      for (int i = 0; i < N_THIEVES_PER_PARTY; i++) {
         if (thieves[i].getThiefID() == thiefID) {
            int nextPos = i > 0 ? i-1 : N_THIEVES_PER_PARTY-1;
            nextThiefID = thieves[nextPos].getThiefID();
            break;
         }
      }
      return nextThiefID;
   }


   /**
    * Set the position of the thief
    * @param thiefID id of the thief
    * @param position position to set
    */
   private void setThiefPosition(int thiefID, int position) {
      for (int i = 0; i < N_THIEVES_PER_PARTY; i++) {
         if (thieves[i].getThiefID() == thiefID) {
            thieves[i].setPosition(position);
            break;
         }
      }
   }

   /**
    * Get the position of the thief
    * @param thiefID id of the thief
    * @return position of the thief
    */
   private int getThiefPosition(int thiefID) {
      int position = -1;
      for (int i = 0; i < N_THIEVES_PER_PARTY; i++) {
         if (thieves[i].getThiefID() == thiefID) {
            position = thieves[i].getPosition();
            break;
         }
      }
      return position;
   }

   /**
    * Order the thieves by position
    * @param backwards true if the thieves are moving backwards
    *
    */
   private void orderThieves(boolean backwards) {
      if (backwards)
         Arrays.sort(thieves, Comparator.comparingInt(Mapping::getPosition).reversed());
      else
         Arrays.sort(thieves, Comparator.comparingInt(Mapping::getPosition));
   }

   /**
    * Operation server shutdown
    */
   public synchronized void shutdown() {
      nEntities++;
      if (nEntities >= N_ENTITIES_SHUTDOWN)
         ServerAssaultParty.shutdown();

      notifyAll();
   }

   /**
     * Get the size of the party
     * @param thieves array of thieves
     * @return size of the party
     */
   private int size(Mapping[] thieves) {
      int size = 0;
      for (int i = 0; i < N_THIEVES_PER_PARTY; i++) {
         if (thieves[i] != null)
            size++;
      }
      return size;
   }

   // print positions
   private void printPositions() {
      StringBuilder print = new StringBuilder("POS " + this + "\n");
      for (int i = 0; i < N_THIEVES_PER_PARTY; i++) {
         print.append(thieves[i].getThiefID()).append(" ");
      }
      print.append("\n");
      for (int i = 0; i < N_THIEVES_PER_PARTY; i++) {
         print.append(thieves[i].getPosition()).append(" ");
      }
   }

   /**
    * Get the thief with the given thiefID
    * @param thiefID id of the thief
    * @return thief with the given thiefID
    */
   private Mapping getThief(int thiefID) {
      for (int i = 0; i < thieves.length; i++) {
         if (thieves[i].getThiefID() == thiefID)
            return thieves[i];
      }
      return null;
   }

   /**
    * Data structure to help mapping thieves to their positions.
    * Each object has a thiefID, a position and a boolean to check if the thief is at the goal.
    */
   class Mapping {
      private int thiefID;
      private int position;
      private int displacement;
      private boolean isAtGoal;
      private boolean ended;

      /**
       * Mapping initialization.
       * The variable isAtGoal is initialized to false.
       *
       * @param thiefID Thief ID
       * @param position Thief position
       */
      public Mapping(int thiefID, int position, int displacement) {
         this.thiefID = thiefID;
         this.position = position;
         this.displacement = displacement;
         this.isAtGoal = this.ended = false;
      }

      @Override
      public String toString() {
         return thiefID + " " + position + " " + isAtGoal;
      }

      public int getThiefID() {
          return thiefID;
      }
      public int getPosition() {
          return position;
      }
      public void setPosition(int position) {
          this.position = position;
      }
      public boolean isAtGoal() {
         return isAtGoal;
      }
      public void isAtGoal(boolean atGoal) {
         isAtGoal = atGoal;
      }
      public boolean ended() {
         return ended;
      }
      public void ended(boolean ended) {
         this.ended = ended;
      }
   }
}