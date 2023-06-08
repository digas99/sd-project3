package client.main;

import client.entities.OrdinaryThief;
import genclass.GenericIO;
import interfaces.*;

import static utils.Parameters.*;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *    Client side of the Ordinary Thief.
 *
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */

public class ClientOrdinaryThief
{
    /**
     *  Main method.
     */

    public static void main (String [] args)
    {
        String rmiRegHostName;
        int rmiRegPortNumb = -1;

        if (args.length != 2)
        { GenericIO.writelnString ("Wrong number of parameters!");
          System.exit (1);
        }

        rmiRegHostName = args[0];
        try
        { rmiRegPortNumb = Integer.parseInt (args[1]);
        }
        catch (NumberFormatException e)
        { GenericIO.writelnString ("args[1] is not a number!");
          System.exit (1);
        }

        if ((rmiRegPortNumb < 4000) || (rmiRegPortNumb >= 65536))
        { GenericIO.writelnString ("args[1] is not a valid port number!");
          System.exit (1);
        }

        String nameEntryAssaultPartyA = "AssaultPartyA";
        String nameEntryAssaultPartyB = "AssaultPartyB";
        String nameEntryMuseum = "Museum";
        String nameEntryCollectionSite = "CollectionSite";
        String nameEntryConcentrationSite = "ConcentrationSite";
        AssaultPartyInterface[] partiesStub = null;
        MuseumInterface museumStub = null;
        ConcentrationSiteInterface concentrationSiteStub = null;
        CollectionSiteInterface collectionSiteStub = null;
        Registry registry = null;
        OrdinaryThief[] thieves = new OrdinaryThief[N_THIEVES_ORDINARY];

        try {
            registry = LocateRegistry.getRegistry (rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException e) {
            GenericIO.writelnString("RMI registry creation exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        try {
            museumStub = (MuseumInterface) registry.lookup (nameEntryMuseum);
        } catch (Exception e) {
            GenericIO.writelnString ("Museum stub lookup exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        try {
            collectionSiteStub = (CollectionSiteInterface) registry.lookup (nameEntryCollectionSite);
        } catch (Exception e) {
            GenericIO.writelnString ("Collection Site stub lookup exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        try {
            concentrationSiteStub = (ConcentrationSiteInterface) registry.lookup (nameEntryConcentrationSite);
        } catch (Exception e) {
            GenericIO.writelnString ("Concentration Site stub lookup exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        try {
            partiesStub = new AssaultPartyInterface[2];
            partiesStub[0] = (AssaultPartyInterface) registry.lookup (nameEntryAssaultPartyA);
            partiesStub[1] = (AssaultPartyInterface) registry.lookup (nameEntryAssaultPartyB);
        } catch (Exception e) {
            GenericIO.writelnString ("Assault Party stub lookup exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        for (int i = 0; i < N_THIEVES_ORDINARY; i++)
            thieves[i] = new OrdinaryThief("Ordinary", i, museumStub, concentrationSiteStub, collectionSiteStub, partiesStub);

        for (int i = 0; i < N_THIEVES_ORDINARY; i++)
            thieves[i].start();

        for (int i = 0; i < N_THIEVES_ORDINARY; i++)
        {
            try {
                thieves[i].join();
            } catch (InterruptedException e) {}
            GenericIO.writelnString("The Ordinary Thief " + i + " has terminated.");
        }
        GenericIO.writelnString();

        try {
            concentrationSiteStub.shutdown();
        } catch (RemoteException e) {
            GenericIO.writelnString("Concentration Site shutdown exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        try {
            collectionSiteStub.shutdown();
        } catch (RemoteException e) {
            GenericIO.writelnString("Collection Site shutdown exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        try {
            museumStub.shutdown();
        } catch (RemoteException e) {
            GenericIO.writelnString("Museum shutdown exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        try {
            partiesStub[0].shutdown();
        } catch (RemoteException e) {
            GenericIO.writelnString("Assault Party A shutdown exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        try {
            partiesStub[1].shutdown();
        } catch (RemoteException e) {
            GenericIO.writelnString("Assault Party B shutdown exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }
    }
}
