package server.entities;

import genclass.GenericIO;
import server.sharedRegions.AssaultPartyInterface;
import utils.Message;
import utils.MessageException;
import utils.ServerCom;

public class AssaultPartyClientProxy extends ClientProxy {
    private AssaultPartyInterface assaultPartyInter;

    public AssaultPartyClientProxy(ServerCom sconi, AssaultPartyInterface inter) {
        super(sconi, "AssaultPartyProxy");
        this.assaultPartyInter = inter;
    }

    @Override
    public void run() {
        Message inMessage = null, outMessage = null;

        /* service providing */

        inMessage = (Message) sconi.readObject();
        try {
            outMessage = assaultPartyInter.processAndReply(inMessage);
        } catch (MessageException e) {
            GenericIO.writelnString("Thread " + getName() + ": " + e.getMessage());
            GenericIO.writelnString(e.getMessageVal().toString());
            System.exit(1);
        }
        sconi.writeObject(outMessage);
        sconi.close();
    }
}
