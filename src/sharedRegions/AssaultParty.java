package sharedRegions;

import entities.MasterThief;
import entities.OrdinaryThief;
import utils.MemException;
import utils.MemFIFO;

public class AssaultParty {
   MasterThief master;

   MemFIFO crawlInQueue;

   public MasterThief getMaster() {
      return master;
   }

   public void setMaster(MasterThief master) {
      this.master = master;
   }

   public MemFIFO getCrawlInQueue() {
      return crawlInQueue;
   }

   public AssaultParty(int n_thieves) throws MemException {
      this.crawlInQueue = new MemFIFO<>(new OrdinaryThief[n_thieves]);
   }

   public static void prepareExcursion() {

   }

   public static void reverseDirection() {

   }
}
