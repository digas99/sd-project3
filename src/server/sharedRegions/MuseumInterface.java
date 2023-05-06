package server.sharedRegions;

import server.entities.MuseumClientProxy;
import utils.Message;
import utils.MessageException;
import utils.MessageType;

/**
 * Interface to the Museum.
 *
 * It is responsible to validate and process the incoming message, execute the corresponding method on the
 * Museum and generate the outgoing message.
 */
public class MuseumInterface {
    /**
     * Reference to the Museum.
     */
    private final Museum museum;

    /**
     * Instantiation of an interface to the Museum.
     *
     * @param museum reference to the Museum
     */
    public MuseumInterface(Museum museum) {
        this.museum = museum;
    }

    /**
     * Processing of incoming messages.
     *
     * Validation, execution of the corresponding method and generation of the outgoing message.
     *
     * @param inMessage service request
     * @return service reply
     * @throws MessageException if the incoming message is not valid
     */
    public Message processAndReply(Message inMessage) throws MessageException {
        Message outMessage = null;

        /* validation of the incoming message */
        switch (inMessage.getMsgType()) {
            // TODO: Museum Interface validation of incoming messages
        }

        /* processing */
        int response;
        switch (inMessage.getMsgType()) {
            case MessageType.ROLLCANVAS:
                ((MuseumClientProxy) Thread.currentThread()).setOrdinaryId(inMessage.getOrdinaryThiefId());
                ((MuseumClientProxy) Thread.currentThread()).setOrdinaryState(inMessage.getOrdinaryThiefState());
                boolean hasCanvas = museum.rollACanvas(inMessage.getRoomId());
                response = hasCanvas ? MessageType.ROLLCANVASDONE : MessageType.NOCANVAS;
                outMessage = new Message(response, false,
                        ((MuseumClientProxy) Thread.currentThread()).getOrdinaryId(),
                        ((MuseumClientProxy) Thread.currentThread()).getOrdinaryState());
                break;
            case MessageType.MSGETROOM:
                Museum.Room room = museum.getRoom(inMessage.getRoomId());
                outMessage = new Message(MessageType.MSGETROOMDONE);
                outMessage.setRoomId(room.getID());
                outMessage.setRoomDistance(room.getDistance());
                outMessage.setRoomPaintings(room.getPaintings());
                outMessage.setRoomTotalPaintings(room.getTotalPaintings());
                outMessage.setAssaultPartyId(room.getAssaultPartyID());
                break;
            case MessageType.SHUT:
                museum.shutdown();
                outMessage = new Message(MessageType.SHUTDONE);
                break;
        }

        return (outMessage);
    }
}
