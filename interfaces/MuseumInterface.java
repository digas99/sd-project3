package interfaces;

import java.rmi.*;

public interface MuseumInterface extends Remote {
    public ReturnBoolean rollACanvas(int ordinaryId, int roomID) throws RemoteException;

    public RoomInterface getRoom(int roomID) throws RemoteException;

    public int getRoomDistance(int roomID) throws RemoteException;

    public void shutdown() throws RemoteException;
}
