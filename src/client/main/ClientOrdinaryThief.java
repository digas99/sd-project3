package client.main;

import client.entities.OrdinaryThief;
import client.stubs.AssaultPartyStub;
import client.stubs.CollectionSiteStub;
import client.stubs.ConcentrationSiteStub;
import client.stubs.MuseumStub;
import genclass.GenericIO;

import static client.main.ConnectionData.*;
import static utils.Parameters.*;

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
        OrdinaryThief[] ordinary = new OrdinaryThief[N_THIEVES_ORDINARY];
        AssaultPartyStub[] assaultPartyStub = new AssaultPartyStub[N_ASSAULT_PARTIES];
        CollectionSiteStub collectionSiteStub;
        ConcentrationSiteStub concentrationSiteStub;
        MuseumStub museumStub;

        /* problem initialization */

        assaultPartyStub[0] = new AssaultPartyStub(ASSAULT_PARTY_0_MACHINE, ASSAULT_PARTY_0_PORT);
        assaultPartyStub[1] = new AssaultPartyStub(ASSAULT_PARTY_1_MACHINE, ASSAULT_PARTY_1_PORT);
        collectionSiteStub = new CollectionSiteStub(COLLECTION_SITE_MACHINE, COLLECTION_SITE_PORT);
        concentrationSiteStub = new ConcentrationSiteStub(CONCENTRATION_SITE_MACHINE, CONCENTRATION_SITE_PORT);
        museumStub = new MuseumStub(MUSEUM_MACHINE, MUSEUM_PORT);

        /* init master thieves */
        for (int i = 0; i < N_THIEVES_ORDINARY; i++)
            ordinary[i] = new OrdinaryThief("Ordinary_"+i, i, museumStub, concentrationSiteStub, collectionSiteStub, assaultPartyStub);

        /* start of the simulation */

        for (int i = 0; i < N_THIEVES_ORDINARY; i++)
            ordinary[i].start ();

        /* waiting for the end of the simulation */

        GenericIO.writelnString ();
        for (int i = 0; i < N_THIEVES_ORDINARY; i++)
        { while (ordinary[i].isAlive ())
        {
            Thread.yield ();
        }
            try
            { ordinary[i].join ();
            }
            catch (InterruptedException e) {}
            GenericIO.writelnString ("Ordinary_" + (i+1) + " has terminated.");
        }
        GenericIO.writelnString ();
        assaultPartyStub[0].shutDown();
        assaultPartyStub[1].shutDown();
        collectionSiteStub.shutDown();
        concentrationSiteStub.shutDown();
        museumStub.shutDown();
    }
}
