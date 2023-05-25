package interfaces;

import java.rmi.*;

public interface CollectionSiteInterface extends Remote {
    public int occupancy() throws RemoteException;

    public int appraiseSit(int concentrationSiteOccupancy, int freeParty, int freeRoom) throws RemoteException;

    public void takeARest() throws RemoteException;

    public void handACanvas(int partyID, int roomID, boolean hasCanvas) throws RemoteException;

    public int[] collectACanvas() throws RemoteException;

    public void sumUpResults() throws RemoteException;

    public void shutDown() throws RemoteException;
}
