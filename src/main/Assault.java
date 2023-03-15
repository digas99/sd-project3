package main;

import genclass.GenericIO;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import static utils.Parameters.*;

@Command(name = "Assault", mixinStandardHelpOptions = true, description = "Project 1 for Sistemas Distribuídos")
public class Assault {

    @Option(names = {"--masters", "-m"}, description = "Number of thieves Masters")
    private int n_thieves_master = MIN_THIEVES_MASTER;
    @Option(names = {"--ordinary", "-o"}, description = "Number of thieves Ordinary")
    private int n_thieves_ordinary = MIN_THIEVES_ORDINARY;

    public static void main(String[] args) {
        Assault assault = new Assault();
        CommandLine cl = new CommandLine(assault);

        // handle wrong arguments
        try {
            cl.parseArgs(args);
        } catch (CommandLine.ParameterException e) {
            cl.usage(System.err);
            e.printStackTrace();
            System.exit(1);
        }

        // print help if arg is --help
        if (cl.isUsageHelpRequested()) {
            cl.usage(System.out);
            System.exit(0);
        }

        GenericIO.writelnInt(assault.n_thieves_master);
        GenericIO.writelnInt(assault.n_thieves_ordinary);
    }
}
