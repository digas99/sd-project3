package server.entities;

import genclass.GenericIO;
import server.sharedRegions.AssaultPartyInterface;
import utils.Message;
import utils.MessageException;
import utils.ServerCom;

public class AssaultPartyClientProxy extends Thread {
    /**
     * Number of instantiated threads.
     */
    private static int nProxy = 0;
    /**
     * Communication channel.
     */
    private ServerCom sconi;
    private int masterId;
    private int masterState;
    private int ordinaryId;
    private int ordinaryState;
    private AssaultPartyInterface assaultPartyInter;

    public AssaultPartyClientProxy(ServerCom sconi, AssaultPartyInterface inter) {
        super("AssaultPartyProxy_" + AssaultPartyClientProxy.getProxyId());
        this.sconi = sconi;
        this.assaultPartyInter = inter;
    }

    private static int getProxyId() {
        Class<?> cl = null;
        int proxyId;

        try {
            cl = Class.forName("server.entities.AssaultPartyClientProxy");
        } catch (ClassNotFoundException e) {
            GenericIO.writeString("Data type AssaultPartyClientProxy was not found!");
            e.printStackTrace();
            System.exit(1);
        }

        synchronized(cl) {
            proxyId = nProxy;
            nProxy += 1;
        }
        return proxyId;
    }

    public int getMasterId() {
        return masterId;
    }

    public void setMasterId(int masterId) {
        this.masterId = masterId;
    }

    public int getMasterState() {
        return masterState;
    }

    public void setMasterState(int masterState) {
        this.masterState = masterState;
    }

    public int getOrdinaryId() {
        return ordinaryId;
    }

    public void setOrdinaryId(int ordinaryId) {
        this.ordinaryId = ordinaryId;
    }

    public int getOrdinaryState() {
        return ordinaryState;
    }

    public void setOrdinaryState(int ordinaryState) {
        this.ordinaryState = ordinaryState;
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
