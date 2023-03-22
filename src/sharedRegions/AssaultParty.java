package sharedRegions;

import entities.MasterThief;
import entities.MasterThiefStates;
import entities.OrdinaryThief;
import entities.OrdinaryThiefStates;
import genclass.GenericIO;
import utils.MemException;

import static utils.Utils.logger;
import static utils.Parameters.*;

public class AssaultParty {
   private int id;
   private int[] thieves;
   private int roomID;
   private int nextThiefPos;
   private int nextThiefID;
   private int nThieves;

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

   public AssaultParty(int id, int size,GeneralRepos repos) throws MemException {
      this.id = id;
      this.thieves = new int[size];
      nThieves = nextThiefPos = 0;
      nextThiefID = roomID = -1;
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
      thieves[nThieves++] = thief.getThiefID();
      thief.setThiefState(OrdinaryThiefStates.CRAWLING_INWARDS);

      // first thief to arrive sets next thief as himself
      if (nextThiefID == -1) nextThiefID = thief.getThiefID();

      // wait for turn
      while (true) {
         while (nextThiefID != thief.getThiefID() || nThieves < N_THIEVES_PER_PARTY) {
            try {
               logger(this, thief, "waiting for turn");
               wait();
            } catch (InterruptedException e) {
            }
         }

         logger(this, thief, "woke up");
         //while (canIMove()) {
         // TODO: move the most
         //}

         // wake up next thief
         nextThiefID = thieves[(++nextThiefPos) % nThieves];
         notifyAll();

         // TODO: if end of path, break
         // if (endOfPath) {
         //   thief.setThiefState(OrdinaryThiefStates.AT_A_ROOM);
         //   break;
         // }
         break;
      }
   }

   private boolean canIMove(OrdinaryThief thief) {
      return false;
   }

   public synchronized void reverseDirection() {
        ((OrdinaryThief) Thread.currentThread()).setThiefState(OrdinaryThiefStates.CRAWLING_OUTWARDS);
   }

   public synchronized void crawlOut() {
      OrdinaryThief thief = (OrdinaryThief) Thread.currentThread();
      thief.setThiefState(OrdinaryThiefStates.CRAWLING_OUTWARDS);
   }
}
