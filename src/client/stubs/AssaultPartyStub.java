package client.stubs;

import client.entities.*;
import client.main.*;
import utils.*;
import genclass.GenericIO;

/**
 * Assault Party Stub - implements a stub for the Assault Party server
 * Methods that are called by the thieves
 *
 * Used to simulate the client server connection
 *
 *
 */

public class AssaultPartyStub {

    /**
     * Name of the platform where is located the Assault Party server
     **/
    private String serverHostName;

    /**
     * Number of the port of the Assault Party server
     **/
    private int serverPortNumb;


    /**
     * Instantiation of the Assault Party Stub
     * @param serverHostName name of the platform where is located the Assault Party server
     * @param serverPortNumb number of the port of the Assault Party server
     **/

    public AssaultPartyStub(String serverHostName, int serverPortNumb) {
        this.serverHostName = serverHostName;
        this.serverPortNumb = serverPortNumb;
    }

    /**
     * Operation Add Thief
     * @param thief thief to be added
     * @param displacement displacement of the thief
     */
    public void addThief(Thief thief, int displacement) {
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(MessageType.ADDTHIEF);
        outMessage.setOrdinaryThiefId(thief.getThiefID());
        outMessage.setDisplacement(displacement);

        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();

        if (inMessage.getMsgType() != MessageType.ADDTHIEFDONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        con.close();
    }

    /**
     * Operation Reset Assault Party
     */
    public void resetAssaultParty() {
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(MessageType.APRESET);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();

        if (inMessage.getMsgType() != MessageType.APRESETDONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        con.close();
    }


    /**
     * Operation to send Assault Party
     */
    public void sendAssaultParty(){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while (!con.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(MessageType.SENDPARTY, true, ((MasterThief) Thread.currentThread()).getThiefID(), ((MasterThief) Thread.currentThread()).getThiefState());
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();

        if (inMessage.getMsgType() != MessageType.SENDPARTYDONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        con.close();
        ((MasterThief) Thread.currentThread()).setThiefState(inMessage.getMasterThiefState());
    }

    /**
     * Reverse direction of the crawling
     */
    public void reverseDirection(){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while(!con.open()){
            try{
                Thread.currentThread().sleep((long) (10));
            }catch(InterruptedException e){}
        }

        outMessage = new Message(MessageType.REVERSE, false, ((OrdinaryThief) Thread.currentThread()).getThiefID(), ((OrdinaryThief) Thread.currentThread()).getThiefState());
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();

        if(inMessage.getMsgType() != MessageType.REVERSEDONE){
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        con.close();
        ((OrdinaryThief) Thread.currentThread()).setThiefState(inMessage.getOrdinaryThiefState());

    }

    /**
     * Operation to crawl in
     * @param roomDistance distance to the room
     * @param displacement displacement of the thief
     */

    public void crawlIn(int roomDistance, int displacement){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while(!con.open()){
            try{
                Thread.currentThread().sleep((long) (10));
            }catch(InterruptedException e){}
        }

        outMessage = new Message(MessageType.CRAWLIN, false, ((OrdinaryThief) Thread.currentThread()).getThiefID(), ((OrdinaryThief) Thread.currentThread()).getThiefState());
        outMessage.setRoomDistance(roomDistance);
        outMessage.setDisplacement(displacement);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();

        if(inMessage.getMsgType() != MessageType.CRAWLINDONE){
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        con.close();
        ((OrdinaryThief) Thread.currentThread()).setThiefState(inMessage.getOrdinaryThiefState());
    }

    /**
     * Operation to crawl out
     * @param roomDistance distance to the room
     * @param displacement displacement of the thief
     */

    public void crawlOut(int roomDistance, int displacement){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;

        while(!con.open()){
            try{
                Thread.currentThread().sleep((long) (10));
            }catch(InterruptedException e){}
        }

        outMessage = new Message(MessageType.CRAWLOUT, false, ((OrdinaryThief) Thread.currentThread()).getThiefID(), ((OrdinaryThief) Thread.currentThread()).getThiefState());
        outMessage.setRoomDistance(roomDistance);
        outMessage.setDisplacement(displacement);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();

        if(inMessage.getMsgType() != MessageType.CRAWLOUTDONE){
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        con.close();
        ((OrdinaryThief) Thread.currentThread()).setThiefState(inMessage.getOrdinaryThiefState());
    }

    /**
     * Operation get assault party id
     * @return assault party id
     */

    public int getID(){
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;
        int id = -1;

        while(!con.open()){
            try{
                Thread.currentThread().sleep((long) (10));
            }catch(InterruptedException e){}
        }

        outMessage = new Message(MessageType.APGETID);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();

        if(inMessage.getMsgType() != MessageType.APGETIDDONE){
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        id = inMessage.getPartyId();
        con.close();
        return id;
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
