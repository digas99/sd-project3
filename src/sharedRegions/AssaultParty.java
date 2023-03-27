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
import static utils.Utils.loggerCrawl;
import static utils.Utils.min;

public class AssaultParty {
   private int id;
   private final GeneralRepos repos;
   private boolean begin;
   private int nextThiefID;
   private int inRoom;
   private Mapping[] thieves;
   private Museum.Room room;

   public int getID() {
      return id;
   }

   public void setID(int id) {
      this.id = id;
   }

   public void addThief(int thiefID) {
      for (int i = 0; i < N_THIEVES_PER_PARTY; i++) {
         if (thieves[i] == null) {
            GenericIO.writelnString("Thief " + thiefID + " is in party " + id);
            thieves[i] = new Mapping(thiefID, 0);
            break;
         }
      }
   }

   public Museum.Room getRoom() {
      return room;
   }

   public void setRoom(Museum.Room room) {
      GenericIO.writelnString("Party " + id + " is in room " + room);
      GenericIO.writelnString(room + " has distance " + room.getDistance() + " and paintings " + room.getPaintings());
      this.room = room;
      room.setAssaultPartyID(id);
      inRoom = 0;
   }

   public AssaultParty(int id, GeneralRepos repos) {
      this.id = id;
      this.repos = repos;
      begin = false;
      thieves = new Mapping[N_THIEVES_PER_PARTY];
      for (int i = 0; i < N_THIEVES_PER_PARTY; i++) {
         thieves[i] = null;
      }
      room = null;
      nextThiefID = -1;
   }

   @Override
   public String toString() {
      return "AssaultParty_"+id;
   }

   public synchronized void sendAssaultParty() {
      MasterThief masterThief = (MasterThief) Thread.currentThread();
      masterThief.setThiefState(MasterThiefStates.DECIDING_WHAT_TO_DO);
      masterThief.setActiveAssaultParties(masterThief.getActiveAssaultParties() + 1);
      begin = true;
      notifyAll();
   }

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
      return !getThief(thiefID).isAtGoal() && (!begin || nextThiefID != thiefID || size(thieves) < N_THIEVES_PER_PARTY);
   }

   public synchronized void crawlIn() {
      OrdinaryThief ordinaryThief = (OrdinaryThief) Thread.currentThread();
      ordinaryThief.setThiefState(OrdinaryThiefStates.CRAWLING_INWARDS);
      int thiefID = ordinaryThief.getThiefID();
      addThief(thiefID);

      if (nextThiefID == -1) nextThiefID = thiefID;

      do {
         // wait until master says to begin
         while (sleep(thiefID)) {
            try {
               wait();
            } catch (InterruptedException e) {e.printStackTrace();}
         }
      } while(crawl(ordinaryThief, 0, room.getDistance()));

      ordinaryThief.setThiefState(OrdinaryThiefStates.AT_A_ROOM);
      loggerCrawl(ordinaryThief, "ARRIVED AT ROOM");
   }

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
      ordinaryThief.setThiefState(OrdinaryThiefStates.COLLECTION_SITE);
      loggerCrawl(ordinaryThief, "ARRIVED AT COLLECTION SITE");
   }

   private boolean crawl(OrdinaryThief ordinaryThief, int beginning, int goal) {
      printPositions();

      int thiefID = ordinaryThief.getThiefID();
      boolean backwards = goal < beginning;

      while(move(ordinaryThief, beginning, goal, backwards));

      // wake up next thief
      nextThiefID = getNextThief(thiefID, backwards);
      GenericIO.writelnString("Next Thief: " + nextThiefID);

      notifyAll();

      // if next thief is at goal, then this thief was the last one to reach goal
      if (getThiefPosition(nextThiefID) == goal) begin = false;

      if (getThief(thiefID).isAtGoal()) return false;

      return true;
   }

   private boolean move(OrdinaryThief ordinaryThief, int beginning, int goal, boolean backwards) {
      boolean validMove;
      int thiefID = ordinaryThief.getThiefID();
      int distanceToGoal;
      int move = ordinaryThief.getDisplacement();
      do {
         distanceToGoal = Math.abs(goal - getThiefPosition(thiefID));
         move = min(move, distanceToGoal);

         updateThiefPosition(thiefID, move, backwards);
         loggerCrawl(ordinaryThief, "MOVE " + (backwards ? "-" : "+") + move + " TO POS " + getThiefPosition(thiefID));

         // check thieves separation from each other
         validMove = !wrongSeparation(backwards) && !checkOverlay(beginning, goal);
         if (!validMove) {
            updateThiefPosition(thiefID, move, !backwards);
            loggerCrawl(ordinaryThief, "REVERTED TO POS "+getThiefPosition(thiefID));
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

      boolean valid0 = Math.abs(thieves[0].getPosition() - thieves[1].getPosition()) <= MAX_SEPARATION_LIMIT;
      boolean valid1 = Math.abs(thieves[1].getPosition() - thieves[2].getPosition()) <= MAX_SEPARATION_LIMIT;
      if (!valid0 || !valid1) GenericIO.writelnString("WRONG SEPARATION");
      return !valid0 || !valid1;
   }

   // make sure thieves are not overlapping
   private boolean checkOverlay(int beginning, int goal) {
      for (int i = 0; i < N_THIEVES_PER_PARTY; i++) {
         for (int j = i+1; j < N_THIEVES_PER_PARTY; j++) {
            int currentPos = thieves[i].getPosition();
            int nextPos = thieves[j].getPosition();
            if (currentPos != beginning && currentPos != goal && currentPos == nextPos) {
               GenericIO.writelnString("OVERLAY");
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
      for (int i = 0; i < N_THIEVES_PER_PARTY; i++) {
         if (thieves[i].getThiefID() == thiefID)
            return thieves[i];
      }
      return null;
   }

   class Mapping {
      private int thiefID;
      private int position;
      private boolean isAtGoal;

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
      public void setThiefID(int thiefID) {
          this.thiefID = thiefID;
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