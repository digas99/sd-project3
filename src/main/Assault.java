package main;

import entities.MasterThief;
import entities.OrdinaryThief;
import genclass.FileOp;
import genclass.GenericIO;
import sharedRegions.*;
import utils.MemException;

import static utils.Parameters.*;

public class Assault {

    public static void main(String[] args) throws MemException {
        GenericIO.writelnString("Starting program with " + N_THIEVES_ORDINARY + " Ordinary Thieves");

        MasterThief masters[];
        OrdinaryThief thieves[];

        AssaultParty assaultParties[];
        CollectionSite collectionSite;
        ConcentrationSite concentrationSite;
        Museum museum;
        GeneralRepos repos;
        String logFile;
        char opt;
        boolean success;

        GenericIO.writelnString ("\n" + "      Heist to the Museum\n");
        do { GenericIO.writeString ("Logging file name? ");
            logFile = GenericIO.readlnString ();
            if (FileOp.exists (".", logFile)){
                do {
                GenericIO.writeString ("There is already a file with this name. Delete it (y - yes; n - no)? ");
                opt = GenericIO.readlnChar ();
            } while ((opt != 'y') && (opt != 'n'));
                if (opt == 'y')
                    success = true;
                else success = false;
            }
            else success = true;
        } while (!success);

        // init shared regions
        repos = new GeneralRepos(logFile);
        collectionSite = new CollectionSite();
        concentrationSite = new ConcentrationSite(N_THIEVES_ORDINARY, N_ASSAULT_PARTIES);
        museum = new Museum();
        assaultParties = new AssaultParty[N_ASSAULT_PARTIES];
        for (int i = 0; i < N_ASSAULT_PARTIES; i++)
            assaultParties[i] = new AssaultParty(i, N_THIEVES_ORDINARY);

        // init masters and thieves
        masters = new MasterThief[N_THIEVES_MASTER];
        thieves = new OrdinaryThief[N_THIEVES_ORDINARY];
        for (int i = 0; i < N_THIEVES_MASTER; i++)
            masters[i] = new MasterThief("Master_" + (i + 1), i, museum, concentrationSite, collectionSite) ;
        for (int i = 0; i < N_THIEVES_ORDINARY; i++)
            thieves[i] = new OrdinaryThief("Ordinary_"+(i+1), i, museum, concentrationSite, collectionSite, assaultParties);

        // start threads
        for (int i = 0; i < N_THIEVES_MASTER; i++)
            masters[i].start();
        for (int i = 0; i < N_THIEVES_ORDINARY; i++)
            thieves[i].start();

        // join threads
        for (int i = 0; i < N_THIEVES_MASTER; i++) {
            try {
                masters[i].join();
            } catch (InterruptedException e) {}
            GenericIO.writelnString("[" + masters[i].getName() + "]: has terminated!");
        }
        for (int i = 0; i < N_THIEVES_ORDINARY; i++) {
            try {
                thieves[i].join();
            } catch (InterruptedException e) {}
            GenericIO.writelnString("[" + thieves[i].getName() + "]: has terminated!");
        }
    }
}
