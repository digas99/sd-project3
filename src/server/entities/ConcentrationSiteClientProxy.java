package server.entities;

import genclass.GenericIO;
import server.sharedRegions.ConcentrationSiteInterface;
import utils.Message;
import utils.MessageException;
import utils.ServerCom;

public class ConcentrationSiteClientProxy extends ClientProxy {
    private ConcentrationSiteInterface concentrationSiteInter;

    public ConcentrationSiteClientProxy(ServerCom sconi, ConcentrationSiteInterface inter) {
        super(sconi, "ConcentrationSiteClientProxy");
        this.concentrationSiteInter = inter;
    }

    @Override
    public void run() {
        Message inMessage = null, outMessage = null;

        /* service providing */

        inMessage = (Message) sconi.readObject();
        try {
            outMessage = concentrationSiteInter.processAndReply(inMessage);
        } catch (MessageException e) {
            GenericIO.writelnString("Thread " + getName() + ": " + e.getMessage());
            GenericIO.writelnString(e.getMessageVal().toString());
            System.exit(1);
        }
        sconi.writeObject(outMessage);
        sconi.close();
    }
}
