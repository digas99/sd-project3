package server.main;

import genclass.GenericIO;
import server.entities.ConcentrationSiteClientProxy;
import server.objects.ConcentrationSite;
import utils.ServerCom;

import java.net.SocketTimeoutException;

/**
 * Server that instantiates the CollectionSite.
 */
public class ServerConcentrationSite {
    /**
     * Flag signaling the service is active.
     */
    public static boolean waitConnection;

    /**
     * Main method.
     *
     * @param args runtime arguments
     *             args[0] - port nunber for listening to service requests
     *             args[1] - name of the platform where is located the server for the general repository
     *             args[2] - port nunber where the server for the general repository is listening to service requests
     */
    public static void main(String[] args) {
        ConcentrationSite concentrationSite;
        ConcentrationSiteInterface concentrationSiteInter;
        // GeneralReposStub reposStub;
        ServerCom scon, sconi;
        int portNumber = -1;
        String reposServerName;
        int reposPortNumber = -1;

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
        reposServerName = args[1];
        try {
            reposPortNumber = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString("args[2] is not a number!");
            System.exit(1);
        }
        if ((reposPortNumber < 4000) || (reposPortNumber >= 65536)) {
            GenericIO.writelnString("args[2] is not a valid port number!");
            System.exit(1);
        }

        concentrationSite = new ConcentrationSite();
        concentrationSiteInter = new ConcentrationSiteInterface(concentrationSite);
        scon = new ServerCom(portNumber);
        scon.start();
        GenericIO.writelnString("Service ConcentrationSite has been established!");
        GenericIO.writelnString("Server is listening or service requests.");

        ConcentrationSiteClientProxy proxy;

        waitConnection = true;
        while (waitConnection) {
            try {
                sconi = scon.accept();
                proxy = new ConcentrationSiteClientProxy(sconi, concentrationSiteInter);
                proxy.start();
            } catch (SocketTimeoutException e) {}
        }
        scon.end();
        GenericIO.writelnString("Server ConcentrationSite was shutdown.");
    }
}
