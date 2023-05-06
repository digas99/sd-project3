package utils;

import client.entities.Thief;

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
     * Room distance
     */
    private int roomDistance = -1;
    /**
     * Ordinary Thief displacement.
     */
    private int displacement = -1;
    /**
     * ConcentrationSite occupancy
     */
    private int concentrationSiteOccupancy = -1;
    /**
     * Free Party
     */
    private int freeParty = -1;
    /**
     * Free Room
     */
    private int freeRoom = -1;
    /**
     * Ordinary Thief has canvas
     */
    private boolean hasCanvas = false;
    /**
     * Assault Party id
     */
    private int assaultPartyId = -1;
    /**
     * Collection Site occupancy
     */
    private int collectionSiteOccupancy = -1;
    /**
     * Ordinary thief is last thief
     */
    private boolean lastThief = false;
    /**
     * Ordinary thief room state
     */
    private int roomState = -1;
    /**
     * Party State
     */
    private boolean partyActive = false;
    /**
     * Party Room Paintings
     */
    private int roomPaintings = -1;
    /**
     * Party Room Total Paintings
     */
    private int roomTotalPaintings = -1;

    /**
     * Message instantiation
     *
     * @param type message type
     */
    public Message (int type) {
        this.msgType = type;
    }

    /**
     * Message instantiation
     *
     * @param type message type
     * @param master master thief flag (true if master thief, false if ordinary thief)
     * @param id thief id
     * @param state thief state
     */
    public Message(int type, boolean master, int id, int state) {
        this.msgType = type;
        if (master) {
            this.masterThiefId = id;
            this.masterThiefState = state;
        } else {
            this.ordinaryThiefId = id;
            this.ordinaryThiefState = state;
        }
    }

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
     * Set ordinary thief id
     *
     * @param ordinaryThiefId ordinary thief id
     */
    public void setOrdinaryThiefId(int ordinaryThiefId) {
        this.ordinaryThiefId = ordinaryThiefId;
    }

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
     * Set ordinary thief party id
     *
     * @param partyId party id
     */
    public void setPartyId(int partyId) {
        this.partyId = partyId;
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
     * Set ordinary thief room id
     *
     * @param roomId room id
     */
    public void setRoomId(int roomId) {
        this.roomId = roomId;
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
     * Set ordinary thief displacement
     *
     * @param displacement displacement
     */
    public void setDisplacement(int displacement) {
        this.displacement = displacement;
    }

    /**
     * Get room distance
     *
     * @return room distance
     */
    public int getRoomDistance() {
        return roomDistance;
    }

    /**
     * Set room distance
     *
     * @param roomDistance room distance
     */
    public void setRoomDistance(int roomDistance) {
        this.roomDistance = roomDistance;
    }

    public int getConcentrationSiteOccupancy() {
        return concentrationSiteOccupancy;
    }

    public void setConcentrationSiteOccupancy(int concentrationSiteOccupancy) {
        this.concentrationSiteOccupancy = concentrationSiteOccupancy;
    }

    public int getFreeParty() {
        return freeParty;
    }

    public void setFreeParty(int freeParty) {
        this.freeParty = freeParty;
    }

    public int getFreeRoom() {
        return freeRoom;
    }

    public void setFreeRoom(int freeRoom) {
        this.freeRoom = freeRoom;
    }

    public boolean hasCanvas() {
        return hasCanvas;
    }

    public void hasCanvas(boolean hasCanvas) {
        this.hasCanvas = hasCanvas;
    }

    public int getAssaultPartyId() {
        return assaultPartyId;
    }

    public void setAssaultPartyId(int assaultPartyId) {
        this.assaultPartyId = assaultPartyId;
    }

    public int getCollectionSiteOccupancy() {
        return collectionSiteOccupancy;
    }

    public void setCollectionSiteOccupancy(int collectionSiteOccupancy) {
        this.collectionSiteOccupancy = collectionSiteOccupancy;
    }

    public int getRoomState() {
        return roomState;
    }

    public void setRoomState(int roomState) {
        this.roomState = roomState;
    }

    public boolean lastThief() {
        return lastThief;
    }

    public void lastThief(boolean lastThief) {
        this.lastThief = lastThief;
    }

    public boolean isPartyActive() {
        return partyActive;
    }

    public void setPartyActive(boolean partyActive) {
        this.partyActive = partyActive;
    }

    public int getRoomPaintings() {
        return roomPaintings;
    }

    public void setRoomPaintings(int roomPaintings) {
        this.roomPaintings = roomPaintings;
    }

    public int getRoomTotalPaintings() {
        return roomTotalPaintings;
    }

    public void setRoomTotalPaintings(int roomTotalPaintings) {
        this.roomTotalPaintings = roomTotalPaintings;
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