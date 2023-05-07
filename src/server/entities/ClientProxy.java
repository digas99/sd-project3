package server.entities;

import utils.ServerCom;

public abstract class ClientProxy extends Thread {
    /**
     * Number of instantiated threads.
     */
    private static int nProxy = 0;
    /**
     * Communication channel.
     */
    protected ServerCom sconi;
    private int masterId;
    private int masterState;
    private int ordinaryId;
    private int ordinaryState;

    public ClientProxy(ServerCom sconi, String className) {
        super(className + "_" + ClientProxy.getProxyId(className));
        this.sconi = sconi;
    }

    private static int getProxyId(String className) {
        Class<?> cl = null;
        int proxyId;

        try {
            cl = Class.forName("server.entities."+className);
        } catch (ClassNotFoundException e) {
            System.out.println("Data type "+ className +" was not found!");
            e.printStackTrace();
            System.exit(1);
        }

        synchronized(cl) {
            proxyId = nProxy;
            nProxy += 1;
        }
        return proxyId;
    }

    @Override
    public String toString() {
        return masterId != -1 ? "Master_"+masterId : "Ordinary_"+ordinaryId;
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
}
