package sharedRegions;

import entities.OrdinaryThief;
import entities.OrdinaryThiefStates;
import utils.MemException;

public class AssaultParty {
   private int id;
   private int[] thieves;
   private int nextThiefPos;
   private int nextThiefID;
   private int nThieves;

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public AssaultParty(int id, int size) throws MemException {
      this.id = id;
      this.thieves = new int[size];
      nThieves = nextThiefPos = 0;
      nextThiefID = -1;
   }

   @Override
   public String toString() {
      return "AssaultParty_"+id;
   }

   public synchronized void crawlIn() {
      OrdinaryThief thief = (OrdinaryThief) Thread.currentThread();
      thieves[nThieves++] = thief.getThiefID();
      thief.setThiefState(OrdinaryThiefStates.CRAWLING_INWARDS);

      // first thief to arrive sets next thief as himself
      if (nextThiefID == -1) nextThiefID = thief.getThiefID();

      // wait for turn
      while (true) {
         while (nextThiefID != thief.getThiefID()) {
            try {
               wait();
            } catch (InterruptedException e) {}
         }

         //while (canIMove) {
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
      }
   }

   public synchronized void reverseDirection() {
        ((OrdinaryThief) Thread.currentThread()).setThiefState(OrdinaryThiefStates.CRAWLING_OUTWARDS);
   }

   public synchronized void crawlOut() {
      OrdinaryThief thief = (OrdinaryThief) Thread.currentThread();
      thief.setThiefState(OrdinaryThiefStates.CRAWLING_OUTWARDS);
   }
}
