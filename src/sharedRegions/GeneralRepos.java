package sharedRegions;
import java.util.Objects;
import genclass.GenericIO;
import genclass.TextFile;
import entities.*;

public class GeneralRepos {

    private final String logFile;

    private int ordinaryThiefStates;

    private int masterThiefStates;


    public GeneralRepos(String logFile) {
        if ((logFile == null) || Objects.equals (logFile, ""))
        this.logFile = "logging";
        else this.logFile = logFile;
        ordinaryThiefStates = OrdinaryThiefStates.CONCENTRATION_SITE;
        masterThiefStates = MasterThiefStates.PLANNING_HEIST;
    }

    private void reporInitialStatus(){
        TextFile log = new TextFile();
        if(!log.openForWriting("/home/lara/Desktop/sd-project1", logFile)){
            GenericIO.writelnString ("The operation of creating the file " + logFile + " failed!");
            System.exit (1);
        }
        log.writelnString ("            Heist to the Museum - Description of the internal state");
        log.writelnString ("MstT    Thief 1     Thief 2     Thief 3     Thief 4     Thief 5     Thief 6");
        log.writelnString("Stat     Stat S MD   Stat S MD   Stat S MD   Stat S MD   Stat S MD   Stat S MD");
        log.writelnString("                 Assault party 1                     Assault party 2                                                Museum");
        log.writelnString("Elem 1    Elem 2   Elem 3                    Elem 1      Elem 2      Elem 3          Room 1      Room 2      Room 3      Room 4      Room 5");
        log.writelnString("");
        if (!log.close ()){
            GenericIO.writelnString ("The operation of closing the file " + logFile + " failed!");
            System.exit (1);
        }
        reportStatus ();
    }

    private void reportStatus(){
        TextFile log = new TextFile();

        String lineStatus = "";

        if (!log.openForAppending (".", logFile)) { GenericIO.writelnString ("The operation of opening for appending the file " + logFile + " failed!");
            System.exit (1);
        }
        switch(ordinaryThiefStates){
            case OrdinaryThiefStates.CONCENTRATION_SITE: lineStatus += "ColS"; break;
            case OrdinaryThiefStates.CRAWLING_INWARDS: lineStatus += "CI"; break;
            case OrdinaryThiefStates.AT_A_ROOM: lineStatus += "AAR"; break;
            case OrdinaryThiefStates.CRAWLING_OUTWARDS: lineStatus += "CO"; break;
            case OrdinaryThiefStates.COLLECTION_SITE: lineStatus += "ConS"; break;
        }
        switch(masterThiefStates){
            case MasterThiefStates.PLANNING_HEIST: lineStatus += "PH"; break;
            case MasterThiefStates.DECIDING_WHAT_TO_DO: lineStatus += "DWTD"; break;
            case MasterThiefStates.ASSEMBLING_GROUP: lineStatus += "AG"; break;
            case MasterThiefStates.WAITING_ARRIVAL: lineStatus += "WA"; break;
            case MasterThiefStates.PRESENTING_REPORT: lineStatus += "PR"; break;
        }


        log.writelnString (lineStatus);
        if (!log.close ()){
            GenericIO.writelnString ("The operation of closing the file " + logFile + " failed!");
            System.exit (1);
        }
    }

    public void reportLegend()
    {
        TextFile log = new TextFile ();                  	// instantiation of a text file handler
        String lineStatus = "";                              		// state line to be printed
        if (!log.openForAppending ("/home/lara/Desktop/sd-project1", logFile))
        {
            GenericIO.writelnString ("The operation of opening for appending the file " + logFile + " failed!");
            System.exit (1);
        }

        lineStatus += "\n\n";
        lineStatus += "Legend:\n";
        lineStatus += "MstT Stat    – state of the master thief\n";
        lineStatus += "Thief # Stat - state of the ordinary thief # (# - 1 .. 6)\n";
        lineStatus += "Thief # S    – situation of the ordinary thief # (# - 1 .. 6) either 'W' (waiting to join a party) or 'P' (in party)\n";
        lineStatus += "Thief # MD   – maximum displacement of the ordinary thief # (# - 1 .. 6) a random number between 2 and 6\n";
        lineStatus += "Assault party # RId  – assault party # (# - 1,2) elem # (# - 1 .. 3) room identification (1 .. 5)\n";
        lineStatus += "Assault party # Elem # Id – assault party # (# - 1,2) elem # (# - 1 .. 3) member identification (1 .. 6)\n";
        lineStatus += "Assault party # Elem # Pos – assault party # (# - 1,2) elem # (# - 1 .. 3) present position (0 .. DT RId)\n";
        lineStatus += "Assault party # Elem # Cv – assault party # (# - 1,2) elem # (# - 1 .. 3) carrying a canvas (0,1)\n";
        lineStatus += "Museum Room # NP - room identification (1 .. 5) number of paintings presently hanging on the walls\n";
        lineStatus += "Museum Room # DT - room identification (1 .. 5) distance from outside gathering site, a random number between 15 and 30\n";

        log.writelnString (lineStatus);
        if (!log.close ())
        {
            GenericIO.writelnString ("The operation of closing the file " + logFile + " failed!");
            System.exit (1);
        }
    }
}
