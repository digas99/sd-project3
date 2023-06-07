package interfaces;

import server.objects.Museum;

import java.rmi.*;

public interface MuseumInterface extends Remote {
    public ReturnBoolean rollACanvas(int ordinaryId, int roomID) throws RemoteException;

    public Museum.Room getRoom(int roomID) throws RemoteException;
    public void shutdown() throws RemoteException;
}
