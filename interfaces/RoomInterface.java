package interfaces;

import java.rmi.*;
import java.io.Serializable;

public interface RoomInterface extends Remote, Serializable {
    public int getDistance();

    public void setDistance(int distance);

    /**
     * Get the number of paintings in the room
     * @return Number of paintings
     */
    public int getPaintings();

    /**
     * Set the number of paintings in the room
     * @param paintings Number of paintings
     */
    public void setPaintings(int paintings);

    /**
     * Get the AssaultParty ID of the room
     * @return AssaultParty ID
     */
    public int getAssaultPartyID();

    /**
     * Set the AssaultParty ID of the room
     * @param assaultPartyID AssaultParty ID
     */
    public void setAssaultPartyID(int assaultPartyID);

    /**
     * Get the total number of paintings in the room
     * @return Total number of paintings
     */
    public int getTotalPaintings();

    /**
     * Get the ID of the room
     * @return ID
     */
    public int getID();

    /**
     * Set the ID of the room
     * @param id
     */

    public void setID(int id);

}
