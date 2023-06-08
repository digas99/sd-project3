package interfaces;

import java.rmi.*;

public interface AssaultPartyInterface extends Remote {

    public int getID() throws RemoteException;

    public void addThief(int thiefID, int displacement) throws RemoteException;

    public void resetAssaultParty() throws RemoteException;

    public int sendAssaultParty(int masterId) throws RemoteException;

    public int reverseDirection(int ordinaryId) throws RemoteException;

    public int crawlIn(int thiefID, int roomDistance, int displacement) throws RemoteException;

    public int crawlOut(int thiefID, int roomDistance, int displacement) throws RemoteException;

    public void endOperation(int masterId) throws RemoteException;

    public void shutdown() throws RemoteException;
}
