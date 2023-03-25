package sharedRegions;
import java.util.Objects;
import genclass.GenericIO;
import genclass.TextFile;
import entities.*;
import static utils.Parameters.*;

public class GeneralRepos {

    private final String logFile;

    private int[] ordinaryThiefStates;

    private int masterThiefStates;

    private int nPaintings;

    private int nRooms;

    private int id;

    private int distance;

    private int position;

    private int roomID;

    private int canvas;

    private int paintings;


    public GeneralRepos(String logFile) {
        ordinaryThiefStates = new int[N_THIEVES_ORDINARY];
        if ((logFile == null) || Objects.equals(logFile, ""))
            this.logFile = "logging";
        else this.logFile = logFile;
        for (int i = 0; i < N_THIEVES_ORDINARY; i++)
            ordinaryThiefStates[i] = OrdinaryThiefStates.CRAWLING_INWARDS;
        masterThiefStates = MasterThiefStates.PLANNING_HEIST;
        nPaintings = 0;
        id = 0;
        distance = 0;
        position = 0;
        roomID = 0;
        canvas = 0;
        reportInitialStatus();


    }

    private void reportInitialStatus() {
        TextFile log = new TextFile();
        if (!log.openForWriting("./logs/", logFile)) {
            GenericIO.writelnString("The operation of creating the file " + logFile + " failed!");
            System.exit(1);
        }
        log.writelnString("                                    Heist to the Museum - Description of the internal state");
        log.writelnString("MstT      Thief 1        Thief 2        Thief 3          Thief 4         Thief 5         Thief 6");
        log.writelnString("Stat     Stat S MD       Stat S MD      Stat S MD        Stat S MD       Stat S MD       Stat S MD");
        log.writelnString("                Assault party 1                                  Assault party 2                                                 Museum");
        log.writelnString("      Elem 1         Elem 2        Elem 3                    Elem 1        Elem 2         Elem 3          Room 1      Room 2      Room 3      Room 4      Room 5");
        log.writelnString("Rid  Id Pos Cv     Id Pos Cv     Id Pos Cv             Rid  Id Pos Cv     Id Pos Cv     Id Pos Cv         NP DT       NP DT       NP DT       NP DT       NP DT");
        if (!log.close()) {
            GenericIO.writelnString("The operation of closing the file " + logFile + " failed!");
            System.exit(1);
        }
        reportStatus();
    }

    private void reportStatus() {
        TextFile log = new TextFile();

        String lineStatus = "";

        if (!log.openForAppending("./logs/", logFile)) {
            GenericIO.writelnString("The operation of opening for appending the file " + logFile + " failed!");
            System.exit(1);
        }
        switch (masterThiefStates) {
            case MasterThiefStates.PLANNING_HEIST:
                lineStatus += "PLHT";
                break;
            case MasterThiefStates.DECIDING_WHAT_TO_DO:
                lineStatus += "DWTD";
                break;
            case MasterThiefStates.ASSEMBLING_GROUP:
                lineStatus += "ASGR";
                break;
            case MasterThiefStates.WAITING_ARRIVAL:
                lineStatus += "WTAR";
                break;
            case MasterThiefStates.PRESENTING_REPORT:
                lineStatus += "PRRP";
                break;
        }
        for (int i = 0; i < N_THIEVES_ORDINARY; i++) {
            switch (ordinaryThiefStates[i]) {
                case OrdinaryThiefStates.CONCENTRATION_SITE:
                    lineStatus += "COlS";
                    break;
                case OrdinaryThiefStates.CRAWLING_INWARDS:
                    lineStatus += "CRIN";
                    break;
                case OrdinaryThiefStates.AT_A_ROOM:
                    lineStatus += "ATAR";
                    break;
                case OrdinaryThiefStates.CRAWLING_OUTWARDS:
                    lineStatus += "CROT";
                    break;
                case OrdinaryThiefStates.COLLECTION_SITE:
                    lineStatus += "CONS";
                    break;
            }
        }
        lineStatus += "  " + String.format("%2d", roomID) + "  " + String.format("%2d", id) + "  " + String.format("%2d", position) + "  " + String.format("%2d", canvas) + "  " + String.format("%2d", roomID) + "  " + String.format("%2d", id) + "  " + String.format("%2d", position) + "  " + String.format("%2d", canvas) +
                "  " + String.format("%2d",nPaintings ) + "  " + String.format("%2d", distance);
        log.writelnString(lineStatus);

        if (!log.close()) {
            GenericIO.writelnString("The operation of closing the file " + logFile + " failed!");
            System.exit(1);
        }
    }

    public void reportLegend() {
        TextFile log = new TextFile();                    // instantiation of a text file handler
        String lineStatus = "";                                    // state line to be printed
        if (!log.openForAppending("./logs/", logFile)) {
            GenericIO.writelnString("The operation of opening for appending the file " + logFile + " failed!");
            System.exit(1);
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
        ;

        log.writelnString(lineStatus);
        if (!log.close()) {
            GenericIO.writelnString("The operation of closing the file " + logFile + " failed!");
            System.exit(1);
        }
    }

    public synchronized void setOrdinaryThiefState(int id, int state) {
        ordinaryThiefStates[id] = state;
        reportStatus();
    }

    public synchronized void setMasterThiefState(int state) {
        masterThiefStates = state;
        reportStatus();
    }

    public synchronized void setOrdinaryThiefPosition(int id, int position) {
        this.position = position;
        reportStatus();
    }

    public synchronized void setOrdinaryThiefId(int id) {
        this.id = id;
        reportStatus();
    }

    public synchronized void setnPaintings(int nPaintings) {
        this.nPaintings = nPaintings;
    }

    public synchronized void setDistance(int distance) {
        this.distance = distance;
    }

    public synchronized void setRoomID(int nRooms) {
        this.nRooms = nRooms;
    }

    public synchronized void getCanvas(int canvas) {
        this.canvas = canvas;
        reportStatus();
    }

    public synchronized void setAssaultPartyID(int id) {
        this.roomID = id;
        reportStatus();
    }

    public synchronized void setPaintings(int nPaintings) {
        this.paintings = nPaintings;
        reportStatus();
    }
}

