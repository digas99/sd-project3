package client.stubs;

import genclass.GenericIO;
import utils.*;
import client.entities.*;
import client.main.*;

public class GeneralReposStub {

    /**
     *  Name of the platform where is located the general repository server.
     */

    private String serverHostName;

    /**
     *  Port number for listening to service requests.
     */

    private int serverPortNumb;

    /**
     *   Instantiation of a stub to the general repository.
     *
     *     @param serverHostName name of the platform where is located the barber shop server
     *     @param serverPortNumb port number for listening to service requests
     */

    public GeneralReposStub (String serverHostName, int serverPortNumb)
    {
        this.serverHostName = serverHostName;
        this.serverPortNumb = serverPortNumb;
    }

    /**
     * Operation to shut down the server (service request).
     */

    public void shutDown(){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(MessageType.SHUT);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();

        if (inMessage.getMsgType() != MessageType.SHUTDONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        con.close();
    }
}
