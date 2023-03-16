package main;

import entities.MasterThief;
import entities.OrdinaryThief;
import genclass.FileOp;
import genclass.GenericIO;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import sharedRegions.*;
import utils.MemException;

import static utils.Parameters.*;

@Command(name = "Assault", mixinStandardHelpOptions = true, description = "Project 1 for Sistemas Distribuídos")
public class Assault {

    private int n_thieves_master = MIN_THIEVES_MASTER;
    @Option(names = {"--ordinary", "-o"}, description = "Number of thieves Ordinary")
    private int n_thieves_ordinary = MIN_THIEVES_ORDINARY;
    private int n_assault_parties = MIN_ASSAULT_PARTIES;

    public static void main(String[] args) throws MemException {
        Assault assault = new Assault();
        handleArgsParser(new CommandLine(assault), args);

        GenericIO.writelnString("Starting program with " + assault.n_thieves_ordinary + " Ordinary Thieves");



        // set number of assault parties
        assault.n_assault_parties = (int) Math.floor((double) assault.n_thieves_ordinary/3);

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
        concentrationSite = new ConcentrationSite(assault.n_thieves_ordinary);
        museum = new Museum();
        assaultParties = new AssaultParty[assault.n_assault_parties];
        for (int i = 0; i < assault.n_assault_parties; i++)
            assaultParties[i] = new AssaultParty(assault.n_thieves_ordinary);

        // init masters and thieves
        masters = new MasterThief[assault.n_thieves_master];
        thieves = new OrdinaryThief[assault.n_thieves_ordinary];
        for (int i = 0; i < assault.n_thieves_master; i++)
            masters[i] = new MasterThief("Master_" + (i + 1), i, museum, concentrationSite, collectionSite, assaultParties);
        for (int i = 0; i < assault.n_thieves_ordinary; i++)
            thieves[i] = new OrdinaryThief("Ordinary_"+(i+1), i, museum, concentrationSite, collectionSite);

        // start threads
        for (int i = 0; i < assault.n_thieves_master; i++)
            masters[i].start();
        for (int i = 0; i < assault.n_thieves_ordinary; i++)
            thieves[i].start();

        // join threads
        for (int i = 0; i < assault.n_thieves_master; i++) {
            try {
                masters[i].join();
            } catch (InterruptedException e) {}
            GenericIO.writelnString(masters[i].getName() + "has terminated!");
        }
        for (int i = 0; i < assault.n_thieves_ordinary; i++) {
            try {
                thieves[i].join();
            } catch (InterruptedException e) {}
            GenericIO.writelnString(thieves[i].getName() + "has terminated!");
        }
    }

    private static void handleArgsParser(CommandLine cl, String[] args) {
        // handle wrong arguments
        try {
            cl.parseArgs(args);
        } catch (CommandLine.ParameterException e) {
            cl.usage(System.err);
            e.printStackTrace();
            System.exit(1);
        }

        // print help if arg is --help
        if (cl.isUsageHelpRequested()) {
            cl.usage(System.out);
            System.exit(0);
        }
    }
}
