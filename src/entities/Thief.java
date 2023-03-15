package entities;

import static utils.Parameters.*;
import static utils.Utils.random;

public abstract class Thief {

    private int displacement;

    public Thief() {
        this.displacement = random(MIN_DISPLACEMENT, MAX_DISPLACEMENT);
    }
}
