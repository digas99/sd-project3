package client.stubs;

import genclass.GenericIO;
import utils.*;
import client.entities.*;
import client.main.*;

public class ConcentrationSiteStub {

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

    public ConcentrationSiteStub (String serverHostName, int serverPortNumb)
    {
        this.serverHostName = serverHostName;
        this.serverPortNumb = serverPortNumb;
    }

    /**
     * Operation to check the number of thieves in the concentration site (service request).
     */
    public int occupancy(){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(MessageType.CONSOCC);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();

        if (inMessage.getMsgType()!= MessageType.CONSOCCDONE){
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        con.close();
        return inMessage.getConcentrationSiteOccupancy();

    }

    /**
     * Operation to set the room state (service request).
     * @param state room state
     */
    public void setRoomState(int state,int roomID) {
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(MessageType.CONSSETROOMSTATE);
        outMessage.setRoomState(state);
        outMessage.setRoomId(roomID);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();

        if (inMessage.getMsgType() != MessageType.CONSSETROOMSTATEDONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        con.close();


    }

    /**
     * Operation to peek the next free room (service request).
     * @return room id of the next free room
     */

    public int peekFreeRoom(){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;
        int roomID = -1;

        while (!con.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(MessageType.CONSFREEROOM);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();

        if (inMessage.getMsgType()!= MessageType.CONSFREEROOMDONE){
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        roomID = inMessage.getRoomId();
        con.close();
        return roomID;
    }

    /**
     * Operation to set the room state (service request).
     * @param roomID room id
     */
    public int getRoomState(int roomID) {
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;
        int state = -1;

        while (!con.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(MessageType.CONSGETROOMSTATE);
        outMessage.setRoomId(roomID);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();

        if (inMessage.getMsgType() != MessageType.CONSGETROOMSTATEDONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        state = inMessage.getRoomState();
        con.close();
        return state;

    }

    /**
     * Operation to get the next free party (service request).
     *
     * @return party id
     */
    public int getFreeParty() {
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;
        int partyID = -1;

        while (!con.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(MessageType.CONSFREEPARTY);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();

        if (inMessage.getMsgType() != MessageType.CONSFREEPARTYDONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        partyID = inMessage.getPartyId();
        con.close();
        return partyID;
    }


    /**
     * Operation to set the active state of the party (service request).
     * @param state party state
     * @param partyID party id
     */
    public void setPartyActive(int partyID, boolean state){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.CONSSETPARTYACTIVE);
        outMessage.setPartyId(partyID);
        outMessage.setPartyActive(state);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();

        if (inMessage.getMsgType()!= MessageType.CONSSETPARTYACTIVEDONE){
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close();
    }

    /**
     * Operation to call master thief to start the assault (service request).
     */
    public void startOperations() {
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(MessageType.STARTOPS);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();

        if (inMessage.getMsgType() != MessageType.STARTOPSDONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close();
        ((MasterThief) Thread.currentThread()).setThiefState(inMessage.getMasterThiefState());
    }

    /**
     * Operation to check if the ordinary thief that called it is still needed (service request).
     * @return true if the thief is still needed, false otherwise
     */
    public boolean amINeeded(){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;
        boolean needed = false;

        while (!con.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.AMINEEDED);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();

        if (inMessage.getMsgType()!= MessageType.ISNEEDED || inMessage.getMsgType()!= MessageType.ISNOTNEEDED){
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        con.close();
        ((OrdinaryThief) Thread.currentThread()).setThiefState(inMessage.getOrdinaryThiefState());
        return inMessage.getMsgType() == MessageType.ISNEEDED;
    }

    /**
     * Operation is called by the master thief to prepare the assault party (service request).
     * @return the party id or -1 if there are no more rooms available
     */

    public int prepareAssaultParty(){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;
        int partyID = -1;

        while (!con.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.PREPPARTY);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();

        if (inMessage.getMsgType()!= MessageType.NOFREEROOMS || inMessage.getMsgType()!= MessageType.PREPPARTYDONE){
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        if(inMessage.getMsgType() == MessageType.PREPPARTYDONE){
            partyID = inMessage.getPartyId();
        }
        con.close();
        ((MasterThief) Thread.currentThread()).setThiefState(inMessage.getMasterThiefState());
        return partyID;
    }

    /**
     * Operation to be called by the Ordinary Thief enrolled in a party
     * @return an array with the party ID and the room ID
     */
    public int[] prepareExcursion(){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;
        int[] party = new int[2];

        while (!con.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.PREPEXCURSION);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();

        if (inMessage.getMsgType()!= MessageType.NOFREEROOMS || inMessage.getMsgType()!= MessageType.PREPEXCURSIONDONE){
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        if(inMessage.getMsgType() == MessageType.PREPEXCURSIONDONE){
            party[0] = inMessage.getPartyId();
            party[1] = inMessage.getRoomId();
        }
        con.close();
        ((OrdinaryThief) Thread.currentThread()).setThiefState(inMessage.getOrdinaryThiefState());
        return new int[]{inMessage.getPartyId(), inMessage.getRoomId()};
    }

    /**
     * Operation to be called by the master thief to end the heist and wake up all thieves (service request).
     */
    public void endOperations(){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.ENDOPS);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();

        if (inMessage.getMsgType()!= MessageType.ENDOPSDONE){
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close();
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

        if (inMessage.getMsgType()!= MessageType.SHUTDONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        con.close();
    }

}
