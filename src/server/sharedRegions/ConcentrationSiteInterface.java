package server.sharedRegions;

import genclass.GenericIO;
import server.entities.ConcentrationSiteClientProxy;
import utils.Message;
import utils.MessageException;
import utils.MessageType;

/**
 * Interface to the Concentration Site.
 *
 * It is responsible to validate and process the incoming message, execute the corresponding method on the
 * Concentration Site and generate the outgoing message.
 */
public class ConcentrationSiteInterface {
    /**
     * Reference to the Concentration Site.
     */
    private final ConcentrationSite concentrationSite;

    /**
     * Instantiation of an interface to the Concentration Site.
     *
     * @param concentrationSite reference to the Concentration Site
     */
    public ConcentrationSiteInterface(ConcentrationSite concentrationSite) {
        this.concentrationSite = concentrationSite;
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
        switch(inMessage.getMsgType()) {
            // TODO: ConcentrationSite Interface validation of incoming messages
        }

        /* processing */
        int response;
        switch (inMessage.getMsgType()) {
            case MessageType.STARTOPS:
                ((ConcentrationSiteClientProxy) Thread.currentThread()).setMasterId(inMessage.getMasterThiefId());
                ((ConcentrationSiteClientProxy) Thread.currentThread()).setMasterState(inMessage.getMasterThiefState());
                concentrationSite.startOperations();
                outMessage = new Message(MessageType.STARTOPSDONE, true,
                        ((ConcentrationSiteClientProxy) Thread.currentThread()).getMasterId(),
                        ((ConcentrationSiteClientProxy) Thread.currentThread()).getMasterState());
                break;
            case MessageType.AMINEEDED:
                ((ConcentrationSiteClientProxy) Thread.currentThread()).setOrdinaryId(inMessage.getOrdinaryThiefId());
                ((ConcentrationSiteClientProxy) Thread.currentThread()).setOrdinaryState(inMessage.getOrdinaryThiefState());
                boolean isNeeded = concentrationSite.amINeeded();
                response = isNeeded ? MessageType.ISNEEDED : MessageType.ISNOTNEEDED;
                outMessage = new Message(response, false,
                        ((ConcentrationSiteClientProxy) Thread.currentThread()).getOrdinaryId(),
                        ((ConcentrationSiteClientProxy) Thread.currentThread()).getOrdinaryState());
                break;
            case MessageType.PREPPARTY:
                ((ConcentrationSiteClientProxy) Thread.currentThread()).setOrdinaryId(inMessage.getOrdinaryThiefId());
                ((ConcentrationSiteClientProxy) Thread.currentThread()).setOrdinaryState(inMessage.getOrdinaryThiefState());
                int party = concentrationSite.prepareAssaultParty();
                response = party == -1 ? MessageType.NOFREEROOMS : MessageType.PREPPARTYDONE;
                outMessage = new Message(response, true,
                        ((ConcentrationSiteClientProxy) Thread.currentThread()).getMasterId(),
                        ((ConcentrationSiteClientProxy) Thread.currentThread()).getMasterState());
                outMessage.setPartyId(party);
                break;
            case MessageType.PREPEXCURSION:
                ((ConcentrationSiteClientProxy) Thread.currentThread()).setOrdinaryId(inMessage.getOrdinaryThiefId());
                ((ConcentrationSiteClientProxy) Thread.currentThread()).setOrdinaryState(inMessage.getOrdinaryThiefState());
                int[] data = concentrationSite.prepareExcursion();
                response = data == null ? MessageType.NOFREEROOMS : MessageType.PREPEXCURSIONDONE;
                outMessage = new Message(response, false,
                        ((ConcentrationSiteClientProxy) Thread.currentThread()).getOrdinaryId(),
                        ((ConcentrationSiteClientProxy) Thread.currentThread()).getOrdinaryState());
                if (data != null) {
                    outMessage.setPartyId(data[0]);
                    outMessage.setRoomId(data[1]);
                }
                break;
            case MessageType.ENDOPS:
                concentrationSite.endOperations();
                outMessage = new Message(MessageType.ENDOPSDONE);
                break;
            case MessageType.CONSOCC:
                int occupancy = concentrationSite.occupancy();
                outMessage = new Message(MessageType.CONSOCCDONE);
                outMessage.setConcentrationSiteOccupancy(occupancy);
                break;
            case MessageType.CONSSETROOMSTATE:
                concentrationSite.setRoomState(inMessage.getRoomId(), inMessage.getRoomState());
                outMessage = new Message(MessageType.CONSSETROOMSTATEDONE);
                break;
            case MessageType.CONSGETROOMSTATE:
                int roomState = concentrationSite.getRoomState(inMessage.getRoomId());
                outMessage = new Message(MessageType.CONSGETROOMSTATEDONE);
                outMessage.setRoomState(roomState);
                break;
            case MessageType.CONSFREEROOM:
                int freeRoom = concentrationSite.peekFreeRoom();
                outMessage = new Message(MessageType.CONSFREEROOMDONE);
                outMessage.setRoomId(freeRoom);
                break;
            case MessageType.CONSFREEPARTY:
                int freeParty = concentrationSite.getFreeParty();
                outMessage = new Message(MessageType.CONSFREEPARTYDONE);
                outMessage.setPartyId(freeParty);
                break;
            case MessageType.CONSSETPARTYACTIVE:
                concentrationSite.setPartyActive(inMessage.getPartyId(), inMessage.isPartyActive());
                outMessage = new Message(MessageType.CONSSETPARTYACTIVEDONE);
                break;
            case MessageType.SHUT:
                concentrationSite.shutdown();
                outMessage = new Message(MessageType.SHUTDONE);
                break;
        }

        return (outMessage);
    }
}
