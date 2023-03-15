package main;

import genclass.GenericIO;
import static utils.Parameters.*;

public class Assault {

    private static int n_thieves_master = MIN_THIEVES_MASTER;
    private static int n_thieves_ordinary = MIN_THIEVES_ORDINARY;

    public static void main(String[] args) {
        if (args.length >= 0 && args.length <= 4) {

        }
        else {
            GenericIO.writeString("Invalid number of arguments.");
            System.exit(1);
        }

        GenericIO.writeInt(n_thieves_master);
        GenericIO.writeInt(n_thieves_ordinary);
    }
}
