package sharedRegions;

import entities.MasterThief;
import entities.OrdinaryThief;
import utils.MemException;
import utils.MemFIFO;

public class AssaultParty {
   private MasterThief master;
   private MemFIFO<OrdinaryThief> thieves;
   private MemFIFO<OrdinaryThief> crawlInQueue;
   private int id;

   public MasterThief getMaster() {
      return master;
   }

   public void setMaster(MasterThief master) {
      this.master = master;
   }

   public MemFIFO<OrdinaryThief> getCrawlInQueue() {
      return crawlInQueue;
   }

   public MemFIFO<OrdinaryThief> getThieves() {
      return thieves;
   }

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public boolean isFull() {
      return crawlInQueue.isFull();
   }

   public AssaultParty(int id, int n_thieves) throws MemException {
      thieves = new MemFIFO<>(new OrdinaryThief[n_thieves]);
      crawlInQueue = new MemFIFO<>(new OrdinaryThief[n_thieves]);
      this.id = id;
   }

   @Override
   public String toString() {
      return "Assault Party "+id;
   }

   public void prepareExcursion() {

   }

   public void reverseDirection() {

   }
}
