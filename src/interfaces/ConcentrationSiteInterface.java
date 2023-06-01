package interfaces;

import java.rmi.*;

public interface ConcentrationSiteInterface extends Remote {
    public int occupancy() throws RemoteException;

    public void setRoomState(int roomID, int state) throws RemoteException;

    public int peekFreeRoom() throws RemoteException;

    public int getRoomState(int roomID) throws RemoteException;

    public int getFreeParty() throws RemoteException;

    public void setPartyActive(int partyID, boolean state) throws RemoteException;

    public int startOperations(int masterId) throws RemoteException;

    public ReturnBoolean amINeeded(int ordinaryId) throws RemoteException;

    public int[] prepareAssaultParty(int masterId) throws RemoteException;

    public int[] prepareExcursion(int ordinaryId) throws RemoteException;

    public void endOperations() throws RemoteException;

    public void shutdown() throws RemoteException;
}
