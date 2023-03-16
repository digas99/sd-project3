package sharedRegions;
import java.util.Objects;
import genclass.GenericIO;
import genclass.TextFile;

public class GeneralRepos {

    private final String logFile;


    public GeneralRepos(String logFile) {
        if ((logFile == null) || Objects.equals (logFile, ""))
        this.logFile = "logging";
        else this.logFile = logFile;
    }

    private void reporInitialStatus(){
        TextFile log = new TextFile();
        if(!log.openForWriting(".", logFile)){
            GenericIO.writelnString ("The operation of creating the file " + logFile + " failed!");
            System.exit (1);
        }
        log.writelnString ("Heist to the Museum - Description of the internal state");
        log.writelnString (" MstT  Thief 1  Thief 2  Thief 3  Thief 4   Thief 5  Thief 6");
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
        log.writelnString (lineStatus);
        if (!log.close ()){
            GenericIO.writelnString ("The operation of closing the file " + logFile + " failed!");
            System.exit (1);
        }
    }
}
