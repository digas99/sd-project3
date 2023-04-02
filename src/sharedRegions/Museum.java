package sharedRegions;

import entities.OrdinaryThief;
import entities.OrdinaryThiefStates;

import static utils.Parameters.*;
import static utils.Utils.*;

public class Museum {

    /**
     * Array of rooms in the museumS
     */
    private final Room[] rooms = new Room[N_ROOMS];

    /**
     * General Repository of Information
     */
    GeneralRepos repos;

    /**
     * Get the rooms of the museum
     * @return Rooms
     */
    public Room[] getRooms() {
        return rooms;
    }


    /**
     * Museum constructor
     * @param repos GeneralRepos
     */
    public Museum(GeneralRepos repos) {
        for (int i = 0; i < rooms.length; i++) {
            rooms[i] = new Room(i);
        }
        this.repos = repos;
    }

    @Override
    public String toString() {
        return "Museum";
    }

    /**
     * Roll a canvas from a room and add it to the thief's canvas
     *
     */
    public synchronized void rollACanvas() {
        OrdinaryThief ordinaryThief = (OrdinaryThief) Thread.currentThread();
        ordinaryThief.setThiefState(OrdinaryThiefStates.AT_A_ROOM);
        //logger(ordinaryThief, "Rolling a canvas");

        Room room = getRoom(ordinaryThief.getRoomID());
        if (room.getPaintings() == 0) {
            //logger(ordinaryThief, "Left empty handed from " + room);
            ordinaryThief.hasCanvas(false);
        }
        else {
            room.setPaintings(room.getPaintings() - 1);
            //logger(ordinaryThief, "Rolled a canvas from " + room + ". " + room.getPaintings() + "/"+ room.getTotalPaintings() +" left");
            ordinaryThief.hasCanvas(true);
        }
    }

    /**
     * Get a room
     * @param roomID Room ID
     * @return Room
     */
    public Room getRoom(int roomID) {
        for (Room room : rooms) {
            if (room.getID() == roomID)
                return room;
        }
        return null;
    }

    public class Room {

        private int id;
        private int distance;
        private int paintings;
        private int totalPaintings;
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

        public int getTotalPaintings() {
            return totalPaintings;
        }

        public void setTotalPaintings(int totalPaintings) {
            this.totalPaintings = totalPaintings;
        }

        /**
         * Get the ID of the room
         * @return ID
         */
        public int getID() {
            return id;
        }

        public void setID(int id) {
            this.id = id;
        }

        /**
         * Room constructor
         * @param id Room ID
         */
        public Room(int id) {
            this.id = id;
            distance = random(MIN_DISTANCE, MAX_DISTANCE);
            paintings = totalPaintings = random(MIN_PAINTINGS, MAX_PAINTINGS);
            assaultPartyID = -1;



        }

        @Override
        public String toString() {
            return "Room_"+id;
        }
    }
}
