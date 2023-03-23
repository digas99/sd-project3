package sharedRegions;

import com.sun.org.apache.xpath.internal.operations.Or;
import entities.OrdinaryThief;
import genclass.GenericIO;

import static utils.Parameters.*;
import static utils.Utils.*;

public class Museum {
    private final Room rooms[] = new Room[N_ROOMS];

    public Room getRoom(int roomID) {
        GenericIO.writelnString("Museum: getRoom("+roomID+") "+rooms.length);
        return rooms[roomID];
    }

    public Room getRoomFromAssault(int assaultPartyID) {
        for (Room r : rooms) {
            if (r.getAssaultPartyID() == assaultPartyID) {
                return r;
            }
        }
        return null;
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
        OrdinaryThief thief = (OrdinaryThief) Thread.currentThread();
        // get room from assault party
        Room room = getRoomFromAssault(assaultID);
        // roll a canvas
        room.setPaintings(room.getPaintings() - 1);
        GenericIO.writelnString();
        logger(this, thief, "Rolled a canvas from "+ room + ". There are " + room.getPaintings() + " left.");
        GenericIO.writelnString();
    }

    class Room {

        private int id;
        private int distance;
        private int paintings;
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

        public Room(int id) {
            this.id = id;
            distance = random(MIN_DISTANCE, MAX_DISTANCE);
            paintings = random(MIN_PAINTINGS, MAX_PAINTINGS);
            assaultPartyID = -1;
        }

        @Override
        public String toString() {
            return "Room_"+id;
        }
    }
}
