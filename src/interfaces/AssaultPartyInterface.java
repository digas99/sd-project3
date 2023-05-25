package interfaces;

import java.rmi.*;

public interface AssaultPartyInterface extends Remote {

    public int getID() throws RemoteException;

    public void addThief(int thiefID, int displacement) throws RemoteException;

    public void resetAssaultParty() throws RemoteException;

    public void sendAssaultParty() throws RemoteException;

    public void reverseDirection() throws RemoteException;

    public void crawlIn(int roomDistance, int displacement) throws RemoteException;

    public void crawlOut(int roomDistance, int displacement) throws RemoteException;

    public void shutDown() throws RemoteException;
}
