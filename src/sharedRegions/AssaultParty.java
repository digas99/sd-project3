package sharedRegions;

import entities.MasterThief;
import entities.MasterThiefStates;
import entities.OrdinaryThief;
import entities.OrdinaryThiefStates;
import genclass.GenericIO;

import java.util.Arrays;
import java.util.Comparator;

import static utils.Parameters.MAX_SEPARATION_LIMIT;
import static utils.Parameters.N_THIEVES_PER_PARTY;
import static utils.Utils.*;

/**
 * Shared Region with methods used by the Master Thief and the Ordinary Thieves.
 * This class is responsible for the management of the Assault Parties and the crawling movements of its thieves.
 */

public class AssaultParty {
   private int id;
   private final GeneralRepos repos;
   private boolean begin;
   private int nextThiefID;
   private int inRoom;
   private Mapping[] thieves;
   private Museum.Room room;

   /**
    * Get Array of thieves IDs in the Assault Party.
    *
    * @return Array of thieves IDs
    */
   public int[] getThieves() {
        int[] thieves = new int[N_THIEVES_PER_PARTY];
        for (int i = 0; i < N_THIEVES_PER_PARTY; i++) {
             thieves[i] = this.thieves[i].getThiefID();
        }
        return thieves;
   }

   /**
    * Get the ID of the Assault Party.
    *
    * @return ID of the Assault Party
    */
   public int getID() {
      return id;
   }

   /**
    * Add a thief to the Assault Party.
    * This method adds a thief to the thieves array, in the first empty position.
    * The thief is added as a Mapping.
    *
    * @param thiefID ID of the thief to be added
    */
   public void addThief(int thiefID) {
      for (int i = 0; i < N_THIEVES_PER_PARTY; i++) {
         if (thieves[i] == null) {
            thieves[i] = new Mapping(thiefID, 0);
            logger(this, "Added Ordinary " + thiefID);
            break;
         }
      }
   }

   /**
    * Get the room assigned to the Assault Party.
    *
    * @return Room object
    */
   public Museum.Room getRoom() {
      return room;
   }

   /**
    * Assign a room to the Assault Party.
    * This method assigns a room to the Assault Party, and sets the Assault Party ID in the room.
    *
    * @param room Room object
    */
   public void setRoom(Museum.Room room) {
      logger(this, "Was given room " + room);
      GenericIO.writelnString(room + " has distance " + room.getDistance() + " and paintings " + room.getPaintings());
      this.room = room;
      room.setAssaultPartyID(id);
      inRoom = 0;
   }

   /**
    * Reset the Assault Party.
    * This method resets the Assault Party, setting all the variables to their initial values.
    */
   public void resetAssaultParty() {
      begin = false;
      nextThiefID = -1;
      for (int i = 0; i < N_THIEVES_PER_PARTY; i++)
         thieves[i] = null;
      logger(this, "RESETED!");
   }

   /**
    * Assault Party initialization.
    * An id is given to the Assault Party, the array of thieves is initialized, and all the variables are set to their initial values.
    *
    * @param id ID of the Assault Party
    * @param repos GeneralRepos object for logging
    */
   public AssaultParty(int id, GeneralRepos repos) {
      this.id = id;
      this.repos = repos;
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
   public synchronized void sendAssaultParty() {
      MasterThief masterThief = (MasterThief) Thread.currentThread();
      masterThief.setActiveAssaultParties(masterThief.getActiveAssaultParties() + 1);
      logger(this, "PARTY SENT");
      begin = true;
      notifyAll();

      masterThief.setThiefState(MasterThiefStates.DECIDING_WHAT_TO_DO);
   }

   /**
    * Reverse the direction of the crawling.
    * This method is called by the Ordinary Thieves and, when called by the last thief, it triggers the crawling out movement.
    * The Ordinary Thief goes to the CRAWLING_OUTWARDS state.
    */
   public synchronized void reverseDirection() {
      OrdinaryThief ordinaryThief = (OrdinaryThief) Thread.currentThread();
      ordinaryThief.setThiefState(OrdinaryThiefStates.CRAWLING_OUTWARDS);
      getThief(ordinaryThief.getThiefID()).isAtGoal(false);
      inRoom++;

      // if last thief reaching room, set begin to true and notify all
      if (inRoom == N_THIEVES_PER_PARTY) {
         begin = true;
         notifyAll();
      }
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

      return !getThief(thiefID).isAtGoal() && (!begin || nextThiefID != thiefID || size(thieves) < N_THIEVES_PER_PARTY);
   }

   /**
    * Crawl towards the room of the Museum.
    * This method is called by the Ordinary Thieves, and makes them crawl from position 0 to the room's distance.
    * The Ordinary Thief goes to the CRAWLING_INWARDS state.
    */
   public synchronized void crawlIn() {
      OrdinaryThief ordinaryThief = (OrdinaryThief) Thread.currentThread();
      ordinaryThief.setThiefState(OrdinaryThiefStates.CRAWLING_INWARDS);
      int thiefID = ordinaryThief.getThiefID();

      if (nextThiefID == -1) {
         resetAssaultParty();
         nextThiefID = thiefID;
      }

      addThief(thiefID);

      do {
         // wait until master says to begin
         while (sleep(thiefID)) {
            try {
               wait();
            } catch (InterruptedException e) {e.printStackTrace();}
         }
      } while(crawl(ordinaryThief, 0, room.getDistance()));

      ordinaryThief.setThiefState(OrdinaryThiefStates.AT_A_ROOM);
      loggerCrawl(ordinaryThief, "ARRIVED AT " + room);
   }

   /**
    * Crawl towards the Collection Site and leave the Museum.
    * This method is called by the Ordinary Thieves, and makes them crawl from the room's distance to position 0.
    * The Ordinary Thief goes to the CRAWLING_OUTWARDS state.
    */
   public synchronized void crawlOut() {
      OrdinaryThief ordinaryThief = (OrdinaryThief) Thread.currentThread();
      ordinaryThief.setThiefState(OrdinaryThiefStates.CRAWLING_OUTWARDS);
      int thiefID = ordinaryThief.getThiefID();

      do {
         // wait until master says to begin
         while (sleep(thiefID)) {
            try {
               wait();
            } catch (InterruptedException e) {e.printStackTrace();}
         }
      } while(crawl(ordinaryThief, room.getDistance(), 0));

      inRoom--;
      getThief(thiefID).isAtGoal(false);
      ordinaryThief.setThiefState(OrdinaryThiefStates.COLLECTION_SITE);
      loggerCrawl(ordinaryThief, "ARRIVED AT COLLECTION SITE");
   }

   private boolean crawl(OrdinaryThief ordinaryThief, int beginning, int goal) {
      //printPositions();

      int thiefID = ordinaryThief.getThiefID();
      boolean backwards = goal < beginning;

      while(move(ordinaryThief, beginning, goal, backwards));

      // wake up next thief
      nextThiefID = getNextThief(thiefID, backwards);
      //GenericIO.writelnString("Next Thief: " + nextThiefID);

      notifyAll();

      if (getThief(thiefID).isAtGoal()) return false;
      return true;
   }

   private boolean move(OrdinaryThief ordinaryThief, int beginning, int goal, boolean backwards) {
      boolean validMove;
      int distanceToGoal;
      int thiefID = ordinaryThief.getThiefID();
      int move = ordinaryThief.getDisplacement();
      do {
         distanceToGoal = Math.abs(goal - getThiefPosition(thiefID));
         move = min(move, distanceToGoal);

         updateThiefPosition(thiefID, move, backwards);

         // check thieves separation from each other
         validMove = !wrongSeparation(backwards) && !checkOverlay(beginning, goal);
         if (!validMove) {
            updateThiefPosition(thiefID, move, !backwards);
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

   private void updateThiefPosition(int thiefID, int move, boolean backwards) {
      int thiefPos = getThiefPosition(thiefID);
      int newPos = !backwards ? thiefPos + move : thiefPos - move;
      setThiefPosition(thiefID, newPos);
   }

   // make sure thieves are only MAX_SEPARATION_LIMIT apart from each other (not circular)
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

   // make sure thieves are not overlapping
   private boolean checkOverlay(int beginning, int goal) {
      for (int i = 0; i < N_THIEVES_PER_PARTY; i++) {
         for (int j = i+1; j < N_THIEVES_PER_PARTY; j++) {
            int currentPos = thieves[i].getPosition();
            int nextPos = thieves[j].getPosition();
            if (currentPos != beginning && currentPos != goal && currentPos == nextPos) {
               //GenericIO.writelnString("OVERLAY");
               return true;
            }
         }
      }
      return false;
   }

   // get next thief
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

   // set thief position
   private void setThiefPosition(int thiefID, int position) {
      for (int i = 0; i < N_THIEVES_PER_PARTY; i++) {
         if (thieves[i].getThiefID() == thiefID) {
            thieves[i].setPosition(position);
            break;
         }
      }
   }

   // get thief position
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

   // order thieves by position
   private void orderThieves(boolean backwards) {
      if (backwards)
         Arrays.sort(thieves, Comparator.comparingInt(Mapping::getPosition).reversed());
      else
         Arrays.sort(thieves, Comparator.comparingInt(Mapping::getPosition));
   }

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
      String print = "POS " + this + "\n";
      for (int i = 0; i < N_THIEVES_PER_PARTY; i++) {
         print += thieves[i].getThiefID() + " ";
      }
      print+="\n";
      for (int i = 0; i < N_THIEVES_PER_PARTY; i++) {
         print += thieves[i].getPosition() + " ";
      }
      GenericIO.writelnString(print);
   }

   private Mapping getThief(int thiefID) {
      for (int i = 0; i < thieves.length; i++) {
         if (thieves[i] != null && thieves[i].getThiefID() == thiefID)
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
      private boolean isAtGoal;

      /**
       * Mapping initialization.
       * The variable isAtGoal is initialized to false.
       *
       * @param thiefID Thief ID
       * @param position Thief position
       */
      public Mapping(int thiefID, int position) {
         this.thiefID = thiefID;
         this.position = position;
         this.isAtGoal = false;
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
   }
}