package server.main;

import genclass.GenericIO;
import interfaces.MuseumInterface;
import interfaces.Register;
import server.objects.Museum;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Server that instantiates the Museum.
 */
public class ServerMuseum {
    /**
     *  Flag signaling the end of operations.
     */
    private static boolean end = false;

    /**
     * Main method.
     *
     * @param args runtime arguments
     *             args[0] - port nunber for listening to service requests
     *             args[1] - name of the platform where is located the server for the general repository
     *             args[2] - port nmnber where the server for the general repository is listening to service requests
     */
    public static void main(String[] args) {
        int portNumber = -1;
        String rmiRegHostName;
        int rmiRegPortNumb = -1;

        if (args.length != 3) {
            GenericIO.writelnString("Wrong number of parameters!");
            System.exit(1);
        }

        try {
            portNumber = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString("args[0] is not a number!");
            System.exit(1);
        }
        if ((portNumber < 4000) || (portNumber >= 65536)) {
            GenericIO.writelnString("args[0] is not a valid port number!");
            System.exit(1);
        }
        rmiRegHostName = args[1];
        try {
            rmiRegPortNumb = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString("args[2] is not a number!");
            System.exit(1);
        }
        if ((rmiRegPortNumb < 4000) || (rmiRegPortNumb >= 65536)) {
            GenericIO.writelnString("args[2] is not a valid port number!");
            System.exit(1);
        }

        /* create and install the security manager */
        if (System.getSecurityManager() == null)
            System.setSecurityManager(new SecurityManager());
        GenericIO.writelnString("Security manager was installed!");

        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException e) {
            GenericIO.writelnString("RMI registry creation exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        GenericIO.writelnString("RMI registry was created!");

        Museum museum = new Museum();
        MuseumInterface museumStub = null;

        try {
            museumStub = (MuseumInterface) UnicastRemoteObject.exportObject(museum, portNumber);
        } catch (RemoteException e) {
            GenericIO.writelnString("Museum stub generation exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        GenericIO.writelnString("Stub was generated!");

        /* register it with the general registry service */
        String nameEntryBase = "RegisterHandler";
        String nameEntryObject = "Museum";
        Register reg = null;

        try {
            reg = (Register) registry.lookup(nameEntryBase);
        } catch (RemoteException e) {
            GenericIO.writelnString("RegisterRemoteObject lookup exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            GenericIO.writelnString("RegisterRemoteObject not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        try {
            reg.bind(nameEntryObject, museumStub);
        } catch (RemoteException e) {
            GenericIO.writelnString("Museum registration exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (AlreadyBoundException e) {
            GenericIO.writelnString("Museum already bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        GenericIO.writelnString("Museum object was registered!");

        /* wait for the end of operations */
        GenericIO.writelnString("Museum is in operation!");
        try {
            while (!end)
                synchronized (Class.forName("server.main.ServerMuseum")) {
                    try {
                        (Class.forName("server.main.ServerMuseum")).wait();
                    } catch (InterruptedException e) {
                        GenericIO.writelnString("Museum main thread was interrupted!");
                    }
                }
        } catch (ClassNotFoundException e) {
            GenericIO.writelnString("The data type ServerMuseum was not found (blocking)!");
            e.printStackTrace();
            System.exit(1);
        }

        /* server shutdown */
        boolean shutdownDone = false;

        try {
            reg.unbind(nameEntryObject);
        } catch (RemoteException e) {
            GenericIO.writelnString("Museum deregistration exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            GenericIO.writelnString("Museum not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        GenericIO.writelnString("Museum was deregistered!");

        try {
            shutdownDone = UnicastRemoteObject.unexportObject(museum, true);
        } catch (NoSuchObjectException e) {
            GenericIO.writelnString("Museum unexport exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        if (shutdownDone)
            GenericIO.writelnString("Museum was shutdown!");
    }

    /**
     * Close of operations.
     */

    public static void shutdown() {
        end = true;
        try {
            synchronized (Class.forName("server.main.ServerMuseum")) {
                (Class.forName("server.main.ServerMuseum")).notify();
            }
        } catch (ClassNotFoundException e) {
            GenericIO.writelnString("The data type ServerMuseum was not found (waking up)!");
            e.printStackTrace();
            System.exit(1);
        }
    }
}