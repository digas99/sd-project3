package interfaces;

import java.rmi.*;

public interface ConcentrationSiteInterface extends Remote {
    public int occupancy() throws RemoteException;

    public void setRoomState(int roomID, int state) throws RemoteException;

    public int peekFreeRoom() throws RemoteException;

    public int getRoomState(int roomID) throws RemoteException;

    public int getFreeParty() throws RemoteException;

    public void setPartyActive(int partyID, boolean state) throws RemoteException;

    public void startOperations() throws RemoteException;

    public boolean amINeeded() throws RemoteException;

    public int prepareAssaultParty() throws RemoteException;

    public int[] prepareExcursion() throws RemoteException;

    public void endOperations() throws RemoteException;

    public void shutDown() throws RemoteException;
}
