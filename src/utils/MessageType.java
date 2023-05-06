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
    /**
     * Request to get assault party id (service request)
     */
    public static final int APGETID = 44;
    /**
     * Assault party id was sent (reply)
     */
    public static final int APGETIDDONE = 45;
    /**
     * Add thief to assault party (service request)
     */
    public static final int ADDTHIEF = 46;
    /**
     * Thief was added to assault party (reply)
     */
    public static final int ADDTHIEFDONE = 47;
    /**
     * Request to reset assault party (service request)
     */
    public static final int APRESET = 48;
    /**
     * Assault party was reset (reply)
     */
    public static final int APRESETDONE = 49;
    /**
     * Request CollectionSite occupancy (service request)
     */
    public static final int COLLSOCC = 50;
    /**
     * CollectionSite occupancy was sent (reply)
     */
    public static final int COLLSOCCDONE = 51;
    /**
     * Request ConcentrationSite occupancy (service request)
     */
    public static final int CONSOCC = 52;
    /**
     * ConcentrationSite occupancy was sent (reply)
     */
    public static final int CONSOCCDONE = 53;
    /**
     * ConcentrationSite set room state (service request)
     */
    public static final int CONSSETROOMSTATE = 54;
    /**
     * ConcentrationSite set room state was done (reply)
     */
    public static final int CONSSETROOMSTATEDONE = 55;
    /**
     * ConcentrationSite get room state (service request)
     */
    public static final int CONSGETROOMSTATE = 56;
    /**
     * ConcentrationSite get room state was done (reply)
     */
    public static final int CONSGETROOMSTATEDONE = 57;
    /**
     * ConcentrationSite peek free room (service request)
     */
    public static final int CONSFREEROOM = 58;
    /**
     * ConcentrationSite peek free room was done (reply)
     */
    public static final int CONSFREEROOMDONE = 59;
    /**
     * ConcentrationSite get free party (service request)
     */
    public static final int CONSFREEPARTY = 60;
    /**
     * ConcentrationSite get free party was done (reply)
     */
    public static final int CONSFREEPARTYDONE = 61;
    /**
     * ConcentrationSite set party active (service request)
     */
    public static final int CONSSETPARTYACTIVE = 62;
    /**
     * ConcentrationSite set party active was done (reply)
     */
    public static final int CONSSETPARTYACTIVEDONE = 63;
    /**
     * Museum get room (service request)
     */
    public static final int MSGETROOM = 64;
    /**
     * Museum get room was done (reply)
     */
    public static final int MSGETROOMDONE = 65;
}
