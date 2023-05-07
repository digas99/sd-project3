package server.entities;

import genclass.GenericIO;
import server.sharedRegions.CollectionSiteInterface;
import utils.Message;
import utils.MessageException;
import utils.ServerCom;

public class CollectionSiteClientProxy extends ClientProxy {
    private CollectionSiteInterface collectionSiteInter;
    public CollectionSiteClientProxy(ServerCom sconi, CollectionSiteInterface inter) {
        super(sconi, "CollectionSiteClientProxy");
        this.collectionSiteInter = inter;
    }

    @Override
    public void run() {
        Message inMessage = null, outMessage = null;

        /* service providing */

        inMessage = (Message) sconi.readObject();
        try {
            outMessage = collectionSiteInter.processAndReply(inMessage);
        } catch (MessageException e) {
            GenericIO.writelnString("Thread " + getName() + ": " + e.getMessage());
            GenericIO.writelnString(e.getMessageVal().toString());
            System.exit(1);
        }
        sconi.writeObject(outMessage);
        sconi.close();
    }
}
