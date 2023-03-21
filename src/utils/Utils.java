package utils;

import genclass.GenericIO;

/**
 * Several general purpose static functions
 */

public class Utils {

    /**
     * Random number.
     * Get a random number between a given interval
     *
     * @param min lowest number from the interval
     * @param max highest number from the interval
     * @return a number between min and max
     */

    public static int random(int min, int max) {
        if (min >= max)
            throw new IllegalArgumentException("Max must be greater than Min");

        return (int)(Math.random() * (max - min + 1)) + min;
    }

    /**
     * Entity logger.
     * Conveniently formatted print to log messages from an entity
     *
     * @param entity any instance of class that has a toString()
     * @param message text presented after the enunciation of the entity
     */

    public static void logger(Object entity, String message) {
        GenericIO.writelnString("[" + entity.toString() + "]: " + message);
    }

    /**
     * Entity logger.
     * Conveniently formatted print to log messages from an entity
     *
     * @param entity any string that conveniently represents the entity
     * @param message text presented after the enunciation of the entity
     */

    public static void logger(String entity, String message) {
        GenericIO.writelnString("[" + entity + "]: " + message);
    }
}
