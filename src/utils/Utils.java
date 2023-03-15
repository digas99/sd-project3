package utils;

public class Utils {

    public static int random(int min, int max) {
        if (min >= max)
            throw new IllegalArgumentException("Max must be greater than Min");

        return (int)(Math.random() * (max - min + 1)) + min;
    }
}
