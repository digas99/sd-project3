package server.main;

import genclass.GenericIO;
import interfaces.AssaultPartyInterface;
import interfaces.Register;
import server.objects.AssaultParty;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Server that instantiates the AssaultParty.
 */
public class ServerAssaultParty {
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

        if (args.length != 5) {
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

        AssaultParty party = new AssaultParty(Integer.parseInt(args[3]));
        AssaultPartyInterface partyStub = null;

        try {
            partyStub = (AssaultPartyInterface) UnicastRemoteObject.exportObject(party, portNumber);
        } catch (RemoteException e) {
            GenericIO.writelnString("AssaultParty stub generation exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        GenericIO.writelnString("Stub was generated!");

        /* register it with the general registry service */
        String nameEntryObject = "AssaultParty"+args[4];
        String nameEntryBase = "RegisterHandler";
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
            reg.bind(nameEntryObject, partyStub);
        } catch (RemoteException e) {
            GenericIO.writelnString("AssaultParty registration exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (AlreadyBoundException e) {
            GenericIO.writelnString("AssaultParty already bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        GenericIO.writelnString("AssaultParty object was registered!");

        /* wait for the end of operations */
        GenericIO.writelnString("AssaultParty is in operation!");
        try {
            while (!end)
                synchronized (Class.forName("server.main.ServerAssaultParty")) {
                    try {
                        (Class.forName("server.main.ServerAssaultParty")).wait();
                    } catch (InterruptedException e) {
                        GenericIO.writelnString("AssaultParty main thread was interrupted!");
                    }
                }
        } catch (ClassNotFoundException e) {
            GenericIO.writelnString("The data type ServerAssaultParty was not found (blocking)!");
            e.printStackTrace();
            System.exit(1);
        }


        /* server shutdown */
        boolean shutdownDone = false;

        try {
            reg.unbind(nameEntryObject);
        } catch (RemoteException e) {
            GenericIO.writelnString("AssaultParty deregistration exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            GenericIO.writelnString("AssaultParty not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        GenericIO.writelnString("AssaultParty was deregistered!");

        try {
            shutdownDone = UnicastRemoteObject.unexportObject(party, true);
        } catch (NoSuchObjectException e) {
            GenericIO.writelnString("AssaultParty unexport exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        if (shutdownDone)
            GenericIO.writelnString("AssaultParty was shutdown!");
    }

    /**
     * Close of operations.
     */

    public static void shutdown() {
        end = true;
        try {
            synchronized (Class.forName("server.main.ServerAssaultParty")) {
                (Class.forName("server.main.ServerAssaultParty")).notify();
            }
        } catch (ClassNotFoundException e) {
            GenericIO.writelnString("The data type ServerAssaultParty was not found (waking up)!");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
