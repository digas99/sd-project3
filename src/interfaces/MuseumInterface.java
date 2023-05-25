package interfaces;

import java.rmi.*;

public interface MuseumInterface extends Remote {
    public boolean rollACanvas(int roomID) throws RemoteException;

    public void shutDown() throws RemoteException;

    public int getRoomDistance(int roomID) throws RemoteException;

    public int getRoomPaintings(int roomID) throws RemoteException;

    public int setRomPaintings(int roomID, int paintings) throws RemoteException;
}
