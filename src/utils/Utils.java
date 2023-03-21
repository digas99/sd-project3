package utils;

import genclass.GenericIO;

public class Utils {

    public static int random(int min, int max) {
        if (min >= max)
            throw new IllegalArgumentException("Max must be greater than Min");

        return (int)(Math.random() * (max - min + 1)) + min;
    }

    public static void logger(String entity, String message) {
        GenericIO.writelnString("[" + entity + "]: " + message);
    }
}
