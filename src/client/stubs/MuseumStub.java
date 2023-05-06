package client.stubs;

import genclass.GenericIO;
import server.sharedRegions.Museum;
import utils.*;
import client.entities.*;
import client.main.*;

import static utils.Parameters.*;
import static utils.Parameters.MAX_PAINTINGS;
import static utils.Utils.random;

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

        outMessage = new Message(MessageType.ROLLCANVAS);
        outMessage.setRoomId(roomID);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();

        if (inMessage.getMsgType() != MessageType.ROLLCANVASDONE || inMessage.getMsgType() != MessageType.NOCANVAS) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        con.close();
        ((OrdinaryThief) Thread.currentThread()).setThiefState(inMessage.getOrdinaryThiefState());
        return inMessage.getMsgType() == MessageType.ROLLCANVASDONE;

    }

    /**
     * Operation to get a Room
     */
    public Room getRoom(int roomId) {
        ClientCom con = new ClientCom(serverHostName, serverPortNumb);
        Message inMessage, outMessage;
        Room room = null;

        while (!con.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(MessageType.MSGETROOM);
        outMessage.setRoomId(roomId);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();

        if (inMessage.getMsgType() != MessageType.MSGETROOMDONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        room = new Room(inMessage.getRoomId(), inMessage.getRoomDistance(), inMessage.getRoomPaintings(), inMessage.getRoomTotalPaintings(), inMessage.getAssaultPartyId());

        con.close();
        return room;
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

    public static class Room {

        private int id;
        private int distance;
        private int paintings;
        private final int totalPaintings;
        private int assaultPartyID;


        /**
         * Get the distance of the room
         * @return Distance
         */
        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        /**
         * Get the number of paintings in the room
         * @return Number of paintings
         */
        public int getPaintings() {
            return paintings;
        }

        /**
         * Set the number of paintings in the room
         * @param paintings Number of paintings
         */
        public void setPaintings(int paintings) {
            this.paintings = paintings;
        }

        /**
         * Get the AssaultParty ID of the room
         * @return AssaultParty ID
         */
        public int getAssaultPartyID() {
            return assaultPartyID;
        }

        /**
         * Set the AssaultParty ID of the room
         * @param assaultPartyID AssaultParty ID
         */
        public void setAssaultPartyID(int assaultPartyID) {
            this.assaultPartyID = assaultPartyID;
        }

        /**
         * Get the total number of paintings in the room
         * @return Total number of paintings
         */
        public int getTotalPaintings() {
            return totalPaintings;
        }

        /**
         * Get the ID of the room
         * @return ID
         */
        public int getID() {
            return id;
        }

        /**
         * Set the ID of the room
         * @param id
         */

        public void setID(int id) {
            this.id = id;
        }

        /**
         * Room constructor that takes an ID to initialize the room
         * It also initializes the distance and number of paintings in the room randomly
         * @param id Room ID
         */
        public Room(int id) {
            this.id = id;
            distance = random(MIN_DISTANCE, MAX_DISTANCE);
            paintings = totalPaintings = random(MIN_PAINTINGS, MAX_PAINTINGS);
            assaultPartyID = -1;
        }

        /**
         * Room constructor with all fields
         *
         * @param id Room ID
         * @param distance Distance
         * @param paintings Number of paintings
         * @param totalPaintings Total number of paintings
         * @param assaultPartyID AssaultParty ID
         */
        public Room(int id, int distance, int paintings, int totalPaintings, int assaultPartyID) {
            this.id = id;
            this.distance = distance;
            this.paintings = paintings;
            this.totalPaintings = totalPaintings;
            this.assaultPartyID = assaultPartyID;
        }

        @Override
        public String toString() {
            return "Room_"+id;
        }

    }
}
