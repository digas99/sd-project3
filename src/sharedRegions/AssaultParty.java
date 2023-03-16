package sharedRegions;

import com.sun.org.apache.xpath.internal.operations.Or;
import entities.MasterThief;
import entities.OrdinaryThief;
import entities.OrdinaryThiefStates;
import utils.MemException;
import utils.MemFIFO;

public class AssaultParty {
   MasterThief master;
   MemFIFO<OrdinaryThief> thieves;
   MemFIFO<OrdinaryThief> crawlInQueue;

   public MasterThief getMaster() {
      return master;
   }

   public void setMaster(MasterThief master) {
      this.master = master;
   }

   public MemFIFO<OrdinaryThief> getCrawlInQueue() {
      return crawlInQueue;
   }

   public boolean isFull() {
      return crawlInQueue.full();
   }

   public AssaultParty(int n_thieves) throws MemException {
      this.thieves = new MemFIFO<>(new OrdinaryThief[n_thieves]);
      this.crawlInQueue = new MemFIFO<>(new OrdinaryThief[n_thieves]);
   }

   public static void prepareExcursion() {

   }

   public static void reverseDirection() {

   }
}
