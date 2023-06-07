package client.main;

import client.entities.MasterThief;
import genclass.GenericIO;
import interfaces.AssaultPartyInterface;
import interfaces.CollectionSiteInterface;
import interfaces.ConcentrationSiteInterface;
import interfaces.MuseumInterface;

import static utils.Parameters.*;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *    Client side of the Master Thief.
 *
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
 
public class ClientMasterThief
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
        AssaultPartyInterface[] partiesStub = null;
        String nameEntryMuseum = "Museum";
        MuseumInterface museumStub = null;
        String nameEntryCollectionSite = "CollectionSite";
        CollectionSiteInterface collectionSiteStub = null;
        String nameEntryConcentrationSite = "ConcentrationSite";
        ConcentrationSiteInterface concentrationSiteStub = null;
        Registry registry = null;
        MasterThief[] masters = new MasterThief[N_THIEVES_MASTER];

        try {
            registry = LocateRegistry.getRegistry (rmiRegHostName, rmiRegPortNumb);
        } catch (Exception e) {
            GenericIO.writelnString ("RMI registry creation exception: " + e.getMessage ());
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

        for (int i = 0; i < N_THIEVES_MASTER; i++)
            masters[i] = new MasterThief("MasterThief", i, museumStub, concentrationSiteStub, collectionSiteStub, partiesStub);

        /* start master thief threads */

        for (int i = 0; i < N_THIEVES_MASTER; i++)
            masters[i].start();

        /* wait for master thief threads to finish */

        for (int i = 0; i < N_THIEVES_MASTER; i++)
        {
            while (masters[i].isAlive())
            {
                try {
                    collectionSiteStub.endOperation(i);
                } catch (RemoteException e) {
                    GenericIO.writelnString("Collection Site remote exception: " + e.getMessage());
                    e.printStackTrace();
                    System.exit(1);
                }

                try {
                    concentrationSiteStub.endOperation(i);
                } catch (RemoteException e) {
                    GenericIO.writelnString("Concentration Site remote exception: " + e.getMessage());
                    e.printStackTrace();
                    System.exit(1);
                }

                for (int j = 0; j < N_ASSAULT_PARTIES; j++)
                {
                    try {
                        partiesStub[j].endOperation(i);
                    } catch (RemoteException e) {
                        GenericIO.writelnString("Assault Party " + i + " remote exception: " + e.getMessage());
                        e.printStackTrace();
                        System.exit(1);
                    }
                }

                Thread.yield();
            }

            try {
                masters[i].join();
            } catch (InterruptedException e) {}
            GenericIO.writelnString("The master thief " + i + " has terminated.");
        }
        GenericIO.writelnString();

        try {
            museumStub.shutdown();
        } catch (RemoteException e) {
            GenericIO.writelnString("Museum remote exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        try {
            collectionSiteStub.shutdown();
        } catch (RemoteException e) {
            GenericIO.writelnString("Collection Site remote exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        try {
            concentrationSiteStub.shutdown();
        } catch (RemoteException e) {
            GenericIO.writelnString("Concentration Site remote exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        for (int i = 0; i < N_ASSAULT_PARTIES; i++)
        {
            try {
                partiesStub[i].shutdown();
            } catch (RemoteException e) {
                GenericIO.writelnString("Assault Party " + i + " remote exception: " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

}
