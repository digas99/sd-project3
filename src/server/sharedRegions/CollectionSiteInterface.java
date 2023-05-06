package server.sharedRegions;

import server.entities.CollectionSiteClientProxy;
import utils.Message;
import utils.MessageException;
import utils.MessageType;

/**
 * Interface to the Collection Site.
 *
 * It is responsible to validate and process the incoming message, execute the corresponding method on the
 * Collection Site and generate the outgoing message.
 */

public class CollectionSiteInterface {
    /**
     * Reference to the Collection Site.
     */
    private final CollectionSite collectionSite;

    /**
     * Instantiation of an interface to the Collection Site.
     * @param collectionSite reference to the Collection Site
     */
    public CollectionSiteInterface(CollectionSite collectionSite) {
        this.collectionSite = collectionSite;
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
            // TODO: CollectionSite Interface validation of incoming messages
        }

        /* processing */
        switch (inMessage.getMsgType()) {
            case MessageType.APPRAISESIT:
                ((CollectionSiteClientProxy) Thread.currentThread()).setMasterId(inMessage.getMasterThiefId());
                ((CollectionSiteClientProxy) Thread.currentThread()).setMasterState(inMessage.getMasterThiefState());
                int decision = collectionSite.appraiseSit(inMessage.getConcentrationSiteOccupancy(), inMessage.getFreeParty(), inMessage.getFreeRoom());

                int response = MessageType.CREATEPARTY;
                if (decision == 1)
                    response = MessageType.WAITFORCANVAS;
                else if (decision == 2)
                    response = MessageType.ENDHEIST;

                outMessage = new Message(response, true,
                        ((CollectionSiteClientProxy) Thread.currentThread()).getMasterId(),
                        ((CollectionSiteClientProxy) Thread.currentThread()).getMasterState());
                break;
            case MessageType.TAKEREST:
                ((CollectionSiteClientProxy) Thread.currentThread()).setMasterId(inMessage.getOrdinaryThiefId());
                ((CollectionSiteClientProxy) Thread.currentThread()).setMasterState(inMessage.getOrdinaryThiefState());
                collectionSite.takeARest();
                outMessage = new Message(MessageType.TAKERESTDONE, true,
                        ((CollectionSiteClientProxy) Thread.currentThread()).getMasterId(),
                        ((CollectionSiteClientProxy) Thread.currentThread()).getMasterState());
                break;
            case MessageType.HANDACANVAS:
                ((CollectionSiteClientProxy) Thread.currentThread()).setOrdinaryId(inMessage.getOrdinaryThiefId());
                ((CollectionSiteClientProxy) Thread.currentThread()).setOrdinaryState(inMessage.getOrdinaryThiefState());
                collectionSite.handACanvas(inMessage.getPartyId(), inMessage.getRoomId(), inMessage.hasCanvas());
                outMessage = new Message(MessageType.HANDACANVASDONE, false,
                        ((CollectionSiteClientProxy) Thread.currentThread()).getOrdinaryId(),
                        ((CollectionSiteClientProxy) Thread.currentThread()).getOrdinaryState());
                break;
            case MessageType.COLLECTACANVAS:
                ((CollectionSiteClientProxy) Thread.currentThread()).setMasterId(inMessage.getMasterThiefId());
                ((CollectionSiteClientProxy) Thread.currentThread()).setMasterState(inMessage.getMasterThiefState());
                int[] data = collectionSite.collectACanvas();
                outMessage = new Message(MessageType.COLLECTACANVASDONE, true,
                        ((CollectionSiteClientProxy) Thread.currentThread()).getMasterId(),
                        ((CollectionSiteClientProxy) Thread.currentThread()).getMasterState());
                outMessage.setPartyId(data[0]);
                outMessage.setRoomId(data[1]);
                outMessage.setRoomState(data[2]);
                outMessage.lastThief(data[3] == 1);
                break;
            case MessageType.SUMUPRES:
                ((CollectionSiteClientProxy) Thread.currentThread()).setMasterId(inMessage.getMasterThiefId());
                ((CollectionSiteClientProxy) Thread.currentThread()).setMasterState(inMessage.getMasterThiefState());
                collectionSite.sumUpResults();
                outMessage = new Message(MessageType.SUMUPRESDONE, true,
                        ((CollectionSiteClientProxy) Thread.currentThread()).getMasterId(),
                        ((CollectionSiteClientProxy) Thread.currentThread()).getMasterState());
                break;
            case MessageType.COLLSOCC:
                int occupancy = collectionSite.occupancy();
                outMessage = new Message(MessageType.COLLSOCCDONE);
                outMessage.setCollectionSiteOccupancy(occupancy);
                break;
            case MessageType.SHUT:
                collectionSite.shutdown();
                outMessage = new Message(MessageType.SHUTDONE);
                break;

        }

        return (outMessage);
    }
}
