package client.stubs;

import genclass.GenericIO;
import utils.*;
import client.entities.*;

public class MuseumStub {

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

    public MuseumStub (String serverHostName, int serverPortNumb)
    {
        this.serverHostName = serverHostName;
        this.serverPortNumb = serverPortNumb;
    }

    /**
     * Operation to Ordinary Thieves to roll a canvas (service request).
     * @param roomID room id
     * @return true if the canvas was rolled, false if not
     */
    public boolean rollACanvas(int roomID){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(MessageType.ROLLCANVAS, false, ((OrdinaryThief) Thread.currentThread()).getThiefID(), ((OrdinaryThief) Thread.currentThread()).getThiefState());
        outMessage.setRoomId(roomID);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();

        if (inMessage.getMsgType() != MessageType.ROLLCANVASDONE && inMessage.getMsgType() != MessageType.NOCANVAS) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        con.close();
        ((OrdinaryThief) Thread.currentThread()).setThiefState(inMessage.getOrdinaryThiefState());
        return inMessage.getMsgType() == MessageType.ROLLCANVASDONE;
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

    public int getRoomDistance(int roomID) {
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while(!con.open()){
            try{
                Thread.currentThread().sleep((long) (10));
            }catch(InterruptedException e){}
        }

        outMessage = new Message(MessageType.GETROOMDIS);
        outMessage.setRoomId(roomID);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();

        if(inMessage.getMsgType() != MessageType.GETROOMDISDONE){
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        con.close();
        return inMessage.getRoomDistance();
    }

    public int getRoomPaintings(int roomID) {
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while(!con.open()){
            try{
                Thread.currentThread().sleep((long) (10));
            }catch(InterruptedException e){}
        }

        outMessage = new Message(MessageType.GETROOMPAINT);
        outMessage.setRoomId(roomID);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();

        if(inMessage.getMsgType() != MessageType.GETROOMPAINTDONE){
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        con.close();
        return inMessage.getRoomPaintings();
    }

    public void setRoomPaintings(int roomID, int paintings) {
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while(!con.open()){
            try{
                Thread.currentThread().sleep((long) (10));
            }catch(InterruptedException e){}
        }

        outMessage = new Message(MessageType.SETROOMPAINT);
        outMessage.setRoomId(roomID);
        outMessage.setRoomPaintings(paintings);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();

        if(inMessage.getMsgType() != MessageType.SETROOMPAINTDONE){
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        con.close();
    }
}
