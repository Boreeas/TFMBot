package tfmbot.events.mouseevents;

import tfmbot.events.EventPump.EventType;
import tfmbot.main.TFMProtocol.Community;

/**
 *
 * @author malte
 */
public class PrivateMessageEvent extends MouseEvent {

    public String nick;
    public String message;
    public Community community;
    public boolean isMod;
    
    public PrivateMessageEvent(String nick, String message, Community community, boolean isMod) {
        
        this.nick = nick;
        this.message = message;
        this.community = community;
        this.isMod = isMod;
    }
    
    @Override
    public EventType getEventType() {
        return EventType.PRIVATE_MESSAGE_EVENT;
    }
    
    
}
