package interfaces;

import java.rmi.*;

public interface CollectionSiteInterface extends Remote {
    public int occupancy() throws RemoteException;

    public int appraiseSit(int masterId, int concentrationSiteOccupancy, int freeParty, int freeRoom) throws RemoteException;

    public int takeARest(int masterId) throws RemoteException;

    public int handACanvas(int ordinaryId, int partyID, int roomID, boolean hasCanvas) throws RemoteException;

    public int[] collectACanvas(int masterId) throws RemoteException;

    public int sumUpResults() throws RemoteException;

    public void endOperation(int masterId) throws RemoteException;

    public void shutdown() throws RemoteException;
}
