package server.sharedRegions;

import server.entities.AssaultPartyClientProxy;
import utils.Message;
import utils.MessageException;
import utils.MessageType;

/**
 * Interface to the Assault Party.
 *
 * It is responsible to validate and process the incoming message, execute the corresponding method on the
 * Assault Party and generate the outgoing message.
 */

public class AssaultPartyInterface {
    /**
     * Reference to the Assault Party.
     */
    private final AssaultParty party;

/**
     * Instantiation of an interface to the Assault Party.
     * @param party reference to the Assault Party
     */
    public AssaultPartyInterface(AssaultParty party) {
        this.party = party;
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

        }

        /* processing */
        switch(inMessage.getMsgType()) {
            case MessageType.SENDPARTY:
                ((AssaultPartyClientProxy) Thread.currentThread()).setMasterId(inMessage.getMasterThiefId());
                ((AssaultPartyClientProxy) Thread.currentThread()).setMasterState(inMessage.getMasterThiefState());
                party.sendAssaultParty();
                outMessage = new Message(MessageType.SENDPARTYDONE,
                        ((AssaultPartyClientProxy) Thread.currentThread()).getMasterId(),
                        ((AssaultPartyClientProxy) Thread.currentThread()).getMasterState());
                break;
            case MessageType.REVERSE:
                ((AssaultPartyClientProxy) Thread.currentThread()).setOrdinaryId(inMessage.getOrdinaryThiefId());
                ((AssaultPartyClientProxy) Thread.currentThread()).setOrdinaryState(inMessage.getOrdinaryThiefState());
                party.reverseDirection();
                outMessage = new Message(MessageType.REVERSEDONE,
                        ((AssaultPartyClientProxy) Thread.currentThread()).getOrdinaryId(),
                        ((AssaultPartyClientProxy) Thread.currentThread()).getOrdinaryState());
                break;
            case MessageType.CRAWLIN:
                ((AssaultPartyClientProxy) Thread.currentThread()).setOrdinaryId(inMessage.getOrdinaryThiefId());
                ((AssaultPartyClientProxy) Thread.currentThread()).setOrdinaryState(inMessage.getOrdinaryThiefState());
                party.crawlIn(inMessage.getRoomDistance(), inMessage.getDisplacement());
                outMessage = new Message(MessageType.CRAWLINDONE,
                        ((AssaultPartyClientProxy) Thread.currentThread()).getOrdinaryId(),
                        ((AssaultPartyClientProxy) Thread.currentThread()).getOrdinaryState());
                break;
            case MessageType.CRAWLOUT:
                ((AssaultPartyClientProxy) Thread.currentThread()).setOrdinaryId(inMessage.getOrdinaryThiefId());
                ((AssaultPartyClientProxy) Thread.currentThread()).setOrdinaryState(inMessage.getOrdinaryThiefState());
                party.crawlOut(inMessage.getRoomDistance(), inMessage.getDisplacement());
                outMessage = new Message(MessageType.CRAWLOUTDONE,
                        ((AssaultPartyClientProxy) Thread.currentThread()).getOrdinaryId(),
                        ((AssaultPartyClientProxy) Thread.currentThread()).getOrdinaryState());
                break;
            case MessageType.GETID:
                int id = party.getID();
                outMessage = new Message(MessageType.GETIDDONE, id);
                break;
            case MessageType.ADDTHIEF:
                party.addThief(inMessage.getOrdinaryThiefId(), inMessage.getDisplacement());
                outMessage = new Message(MessageType.ADDTHIEFDONE);
                break;
            case MessageType.RESET:
                party.resetAssaultParty();
                outMessage = new Message(MessageType.RESETDONE);
                break;
        }

        return (outMessage);
    }
}
