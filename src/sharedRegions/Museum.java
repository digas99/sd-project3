package sharedRegions;

import static utils.Utils.random;
import static utils.Parameters.*;

public class Museum {
    private final Room rooms[] = new Room[5];

    public Room[] getRooms() {
        return rooms;
    }

    public Museum() {
        for (int i = 0; i < this.rooms.length; i++) {
            this.rooms[i] = new Room();
        }
    }

    public void rollACanvas() {

    }

    public void crawlIn() {

    }

    public void crawlOut() {

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
