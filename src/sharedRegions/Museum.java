package sharedRegions;

import entities.OrdinaryThief;
import entities.OrdinaryThiefStates;
import genclass.GenericIO;

import static utils.Parameters.*;
import static utils.Utils.*;

public class Museum {
    private final Room rooms[] = new Room[N_ROOMS];

    public Room[] getRooms() {
        return rooms;
    }

    public void clearRooms(int assaultID) {
        for (Room room : rooms) {
            if (room.getAssaultPartyID() == assaultID)
                room.setAssaultPartyID(-1);
        }
    }

    public Museum(GeneralRepos repos) {
        for (int i = 0; i < rooms.length; i++) {
            rooms[i] = new Room(i);
        }
    }

    @Override
    public String toString() {
        return "Museum";
    }

    public synchronized void rollACanvas(int assaultID) {
        OrdinaryThief ordinaryThief = (OrdinaryThief) Thread.currentThread();
        ordinaryThief.setThiefState(OrdinaryThiefStates.AT_A_ROOM);
        logger(ordinaryThief, "Rolling a canvas");

        Room room = getRoom(assaultID);
        if (room.getPaintings() == 0) {
            logger(ordinaryThief, "Left empty handed from " + room);
            ordinaryThief.hasCanvas(false);
        }
        else {
            room.setPaintings(room.getPaintings() - 1);
            logger(ordinaryThief, "Rolled a canvas from " + room + ". " + room.getPaintings() + "/"+ room.getTotalPaintings() +" left");
            ordinaryThief.hasCanvas(true);
        }
    }

    public Room getRoom(int assaultID) {
        for (Room room : rooms) {
            if (room.getAssaultPartyID() == assaultID)
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

        public int getDistance() {
            return distance;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public int getPaintings() {
            return paintings;
        }

        public void setPaintings(int paintings) {
            this.paintings = paintings;
        }

        public int getAssaultPartyID() {
            return assaultPartyID;
        }

        public void setAssaultPartyID(int assaultPartyID) {
            this.assaultPartyID = assaultPartyID;
        }

        public int getTotalPaintings() {
            return totalPaintings;
        }

        public void setTotalPaintings(int totalPaintings) {
            this.totalPaintings = totalPaintings;
        }

        public int getID() {
            return id;
        }

        public void setID(int id) {
            this.id = id;
        }

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
