package server.entities;

import genclass.GenericIO;
import server.sharedRegions.AssaultPartyInterface;
import server.sharedRegions.MuseumInterface;
import utils.Message;
import utils.MessageException;
import utils.ServerCom;

public class MuseumClientProxy extends ClientProxy {
    private MuseumInterface museumInter;

    public MuseumClientProxy(ServerCom sconi, MuseumInterface museumInter) {
        super(sconi, "MuseumProxy");
        this.museumInter = museumInter;
    }

    @Override
    public void run() {
        Message inMessage = null, outMessage = null;

        /* service providing */

        inMessage = (Message) sconi.readObject();
        try {
            outMessage = museumInter.processAndReply(inMessage);
        } catch (MessageException e) {
            GenericIO.writelnString("Thread " + getName() + ": " + e.getMessage());
            GenericIO.writelnString(e.getMessageVal().toString());
            System.exit(1);
        }
        sconi.writeObject(outMessage);
        sconi.close();
    }
}
