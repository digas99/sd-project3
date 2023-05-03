package utils;

import java.io.Serializable;

/**
 *   Internal structure of the exchanged messages.
 *
 *   Implementation of a client-server model of type 2 (server replication).
 *   Communication is based on a communication channel under the TCP protocol.
 */
public class Message implements Serializable {
    /**
     * Serialization key.
     */
    private static final long serialVersionUID = 2023L;
    /**
     * Message type.
     */
    private int msgType = -1;
    /**
     * Master Thief identification.
     */
    private int masterThiefId = -1;
    /**
     * Master Thief state.
     */
    private int masterThiefState = -1;
    /**
     * Ordinary Thief identification.
     */
    private int ordinaryThiefId = -1;
    /**
     * Ordinary Thief state.
     */
    private int ordinaryThiefState = -1;
    /**
     * End of operations (master thief).
     */
    private boolean endOp = false;
    /**
     * Name of the logging file.
     */
    private String fName = null;
    /**
     * Ordinary Thief party id.
     */
    private int partyId = -1;
    /**
     * Ordinary Thief room id.
     */
    private int roomId = -1;
    /**
     * Ordinary Thief displacement.
     */
    private int displacement = -1;

    /**
     * Message instantiation (form 1).
     *
     * @param type message type
     */
    public Message (int type) {
        this.msgType = type;
    }

    // TODO: add more constructor forms

    /**
     * Get message type.
     *
     * @return message type
     */
    public int getMsgType() {
        return (msgType);
    }
    /**
     * Get master thief id.
     *
     * @return master thief id
     */
    public int getMasterThiefId() {
        return (masterThiefId);
    }
    /**
     * Get master thief state.
     *
     * @return master thief state
     */
    public int getMasterThiefState() {
        return (masterThiefState);
    }
    /**
     * Get ordinary thief id.
     *
     * @return ordinary thief id
     */
    public int getOrdinaryThiefId() {
        return (ordinaryThiefId);
    }
    /**
     * Get ordinary thief state.
     *
     * @return ordinary thief state
     */
    public int getOrdinaryThiefState() {
        return (ordinaryThiefState);
    }
    /**
     * Get end of operations (master thief).
     *
     * @return end of operations flag
     */
    public boolean getEndOp() {
        return (endOp);
    }
    /**
     * Get name of the logging file.
     *
     * @return name of the logging file
     */
    public String getLogFName() {
        return (fName);
    }
    /**
     * Get ordinary thief party id.
     *
     * @return ordinary thief party id
     */
    public int getPartyId() {
        return (partyId);
    }
    /**
     * Get ordinary thief room id.
     *
     * @return ordinary thief room id
     */
    public int getRoomId() {
        return (roomId);
    }
    /**
     * Get ordinary thief displacement.
     *
     * @return ordinary thief displacement
     */
    public int getDisplacement() {
        return (displacement);
    }

    /**
     * Printing the values of the internal fields.
     *
     * It is used for debugging purposes.
     *
     * @return string containing, in separate lines, the pair field name - field value
     */
    @Override
    public String toString() {
        return ("Message Type = " + msgType + "\n" +
                "Master Thief Id = " + masterThiefId + "\n" +
                "Master Thief State = " + masterThiefState + "\n" +
                "Ordinary Thief Id = " + ordinaryThiefId + "\n" +
                "Ordinary Thief State = " + ordinaryThiefState + "\n" +
                "End of Operations = " + endOp + "\n" +
                "Log File Name = " + fName + "\n" +
                "Ordinary Thief Party Id = " + partyId + "\n" +
                "Ordinary Thief Room Id = " + roomId + "\n" +
                "Ordinary Thief Displacement = " + displacement + "\n");
    }
}