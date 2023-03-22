package sharedRegions;

import static utils.Utils.random;
import static utils.Parameters.*;

public class Museum {
    private final Room rooms[] = new Room[N_ROOMS];

    public Room getRoom(int roomID) {
        return rooms[roomID];
    }

    public Museum() {
        for (int i = 0; i < rooms.length; i++) {
            rooms[i] = new Room(i);
        }
    }

    public synchronized void rollACanvas(int assaultID) {

    }

    class Room {

        private int id;
        private int distance;
        private int paintings;

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

        public Room(int id) {
            this.id = id;
            distance = random(MIN_DISTANCE, MAX_DISTANCE);
            paintings = random(MIN_PAINTINGS, MAX_PAINTINGS);
        }
    }
}
