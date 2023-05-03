package utils;

public class MessageType {
    /**
     * Initialization of the logging file name and the number of iterations (service request).
     */
    public static final int SETNFIC = 1;
    /**
     * Logging file was initialized (reply).
     */
    public static final int NFICDONE = 2;
    /**
     * Request party to be sent (service request).
     */
    public static final int SENDPARTY = 3;
    /**
     * Party was sent (reply).
     */
    public static final int SENDPARTYDONE = 4;
    /**
     * Request direction reversal (service request).
     */
    public static final int REVERSE = 5;
    /**
     * Direction was reversed (reply).
     */
    public static final int REVERSEDONE = 6;
    /**
     * Request to crawl in (service request)
     */
    public static final int CRAWLIN = 7;
    /**
     * Crawl in was done (reply)
     */
    public static final int CRAWLINDONE = 8;
    /**
     * Request to crawl out (service request)
     */
    public static final int CRAWLOUT = 9;
    /**
     * Crawl out was done (reply)
     */
    public static final int CRAWLOUTDONE = 10;
    /**
     * Request to appraise situation (service request)
     */
    public static final int APPRAISESIT = 11;
    /**
     * MasterThief ends heist (reply)
     */
    public static final int ENDHEIST = 12;
    /**
     * MasterThief waits for canvas (reply)
     */
    public static final int WAITFORCANVAS = 13;
    /**
     * MasterThief creates assault partu (reply)
     */
    public static final int CREATEPARTY = 14;
    /**
     * Request to take a rest (service request)
     */
    public static final int TAKEREST = 15;
    /**
     * Taking a rest was done (reply)
     */
    public static final int TAKERESTDONE = 16;
    /**
     * Request to hand a canvas (service request)
     */
    public static final int HANDACANVAS = 17;
    /**
     * Handing a canvas was done (reply)
     */
    public static final int HANDACANVASDONE = 18;
    /**
     * Request to collect a canvas (service request)
     */
    public static final int COLLECTACANVAS = 19;
    /**
     * Collecting a canvas was done (reply)
     */
    public static final int COLLECTACANVASDONE = 20;
    /**
     * Request to sum up results (service request)
     */
    public static final int SUMUPRES = 21;
    /**
     * Summing up results was done (reply)
     */
    public static final int SUMUPRESDONE = 22;
    /**
     * Request to start operations (service request)
     */
    public static final int STARTOPS = 23;
    /**
     * Starting operations was done (reply)
     */
    public static final int STARTOPSDONE = 24;
    /**
     * Request if needed (service request)
     */
    public static final int AMINEEDED = 25;
    /**
     * Thief is needed (reply)
     */
    public static final int ISNEEDED = 26;
    /**
     * Thief is not needed (reply)
     */
    public static final int ISNOTNEEDED = 27;
    /**
     * Request to prepare assault party (service request)
     */
    public static final int PREPPARTY = 28;
    /**
     * NO FREE ROOMS (reply)
     */
    public static final int NOFREEROOMS = 29;
    /**
     * Assault party was prepared (reply)
     */
    public static final int PREPPARTYDONE = 30;
    /**
     * Request to prepare excursion (service request)
     */
    public static final int PREPEXCURSION = 31;
    /**
     * Excursion was prepared (reply)
     */
    public static final int PREPEXCURSIONDONE = 32;
    /**
     * Request to end operations (service request)
     */
    public static final int ENDOPS = 33;
    /**
     * Ending operations was done (reply)
     */
    public static final int ENDOPSDONE = 34;
    /**
     * Request to roll a canvas
     */
    public static final int ROLLCANVAS = 35;
    /**
     * No more canvases to roll
     */
    public static final int NOCANVAS = 36;
    /**
     * Canvas was rolled
     */
    public static final int ROLLCANVASDONE = 37;
    /**
     * Server shutdown (service request)
     */
    public static final int SHUT = 38;
    /**
     * Server shutdown was done (reply)
     */
    public static final int SHUTDONE = 39;
    /**
     * Set master thief state (service request)
     */
    public static final int SETMTSTATE = 40;
    /**
     * Set ordinary thief state (service request)
     */
    public static final int SETOTSTATE = 41;
    /**
     * Set master and ordinary thief states (service request)
     */
    public static final int SETMTOTSTATE = 42;
    /**
     * Setting acknowledged (reply)
     */
    public static final int SACK = 43;
}
