package sharedRegions;

import static utils.Utils.random;
import static utils.Parameters.*;

public class Museum {
    private final Room rooms[] = new Room[N_ROOMS];

    public Room[] getRooms() {
        return rooms;
    }

    public Museum() {
        for (int i = 0; i < rooms.length; i++) {
            rooms[i] = new Room();
        }
    }

    public synchronized void rollACanvas(int assaultID) {

    }

    private class Room {

        private int distance;
        private int paintings;

        public Room() {
            this.distance = random(MIN_DISTANCE, MAX_DISTANCE);
            this.paintings = random(MIN_PAINTINGS, MAX_PAINTINGS);
        }
    }
}
