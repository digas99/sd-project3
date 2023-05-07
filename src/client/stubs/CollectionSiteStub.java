package client.stubs;

import genclass.GenericIO;
import utils.*;
import client.entities.*;
import client.main.*;

import static utils.Parameters.*;

public class CollectionSiteStub {

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

    public CollectionSiteStub (String serverHostName, int serverPortNumb)
    {
        this.serverHostName = serverHostName;
        this.serverPortNumb = serverPortNumb;
    }

    /**
     * Operation to check the number of thieves inside the collection site (service request).
     */
    public int occupancy(){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;
        int occupancy = -1;

        while (!con.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(MessageType.COLLSOCC);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();

        if (inMessage.getMsgType() != MessageType.COLLSOCCDONE){
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        } else {
            occupancy = inMessage.getCollectionSiteOccupancy();
        }

        con.close();
        return occupancy;

    }

    /**
     * Operation to Master Thief to apraise the situation (service request).
     * @return
     */
    public int appraiseSit(int concentrationSiteOccupancy, int freeParty, int freeRoom){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;
        int decision = -1;

        while (!con.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.APPRAISESIT, true, ((MasterThief) Thread.currentThread()).getThiefID(), ((MasterThief) Thread.currentThread()).getThiefState());
        outMessage.setConcentrationSiteOccupancy(concentrationSiteOccupancy);
        outMessage.setFreeParty(freeParty);
        outMessage.setFreeRoom(freeRoom);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();

        if (inMessage.getMsgType() != MessageType.CREATEPARTY && inMessage.getMsgType() != MessageType.WAITFORCANVAS && inMessage.getMsgType() != MessageType.ENDHEIST){
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        if(inMessage.getMsgType() == MessageType.CREATEPARTY){
            decision = CREATE_ASSAULT_PARTY;
        } else if(inMessage.getMsgType() == MessageType.WAITFORCANVAS){
            decision = WAIT_FOR_CANVAS;
        } else if(inMessage.getMsgType() == MessageType.ENDHEIST){
            decision = END_HEIST;
        }

        con.close();
        ((MasterThief) Thread.currentThread()).setThiefState(inMessage.getMasterThiefState());
        return decision;

    }

    /**
     * Operation to Master Thief to take a rest while waiting for an ordinary thief to hand a canvas (service request).
     */
    public void takeARest(){
ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(MessageType.TAKEREST, true, ((MasterThief) Thread.currentThread()).getThiefID(), ((MasterThief) Thread.currentThread()).getThiefState());
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();

        if (inMessage.getMsgType() != MessageType.TAKERESTDONE){
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        con.close();
        ((MasterThief) Thread.currentThread()).setThiefState(inMessage.getMasterThiefState());
    }

    /**
     * Operation to Ordinary Thief to hand a canvas to the Master Thief (service request).
     */
    public void handACanvas(int partyID, int roomID, boolean hasCanvas){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.HANDACANVAS, false, ((OrdinaryThief) Thread.currentThread()).getThiefID(), ((OrdinaryThief) Thread.currentThread()).getThiefState());
        outMessage.setPartyId(partyID);
        outMessage.setRoomId(roomID);
        outMessage.hasCanvas(hasCanvas);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();

        if (inMessage.getMsgType() != MessageType.HANDACANVASDONE){
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        con.close();
        ((OrdinaryThief) Thread.currentThread()).setThiefState(inMessage.getOrdinaryThiefState());
    }

    /**
     * Operation to Master Thief to collect a canvas from an ordinary thief (service request).
     */

    public int[] collectACanvas(){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;
        int[] canvas = new int[4];

        while (!con.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.COLLECTACANVAS, true, ((MasterThief) Thread.currentThread()).getThiefID(), ((MasterThief) Thread.currentThread()).getThiefState());
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();

        if (inMessage.getMsgType() != MessageType.COLLECTACANVASDONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        canvas[0] = inMessage.getPartyId();
        canvas[1] = inMessage.getRoomId();
        canvas[2] = inMessage.getRoomState();
        canvas[3] = inMessage.lastThief() ? 1 : 0;

        con.close();
        ((MasterThief) Thread.currentThread()).setThiefState(inMessage.getMasterThiefState());
        return canvas;

    }

    /**
     * Operation to Master Thief to present the final report (service request).
     */
    public void sumUpResults(){
ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.SUMUPRES, true, ((MasterThief) Thread.currentThread()).getThiefID(), ((MasterThief) Thread.currentThread()).getThiefState());
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();

        if (inMessage.getMsgType() != MessageType.SUMUPRESDONE){
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        con.close();
        ((MasterThief) Thread.currentThread()).setThiefState(inMessage.getMasterThiefState());

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
