package tfmbot.events.roomevents;

import tfmbot.events.EventPump.EventType;

/**
 *
 * @author malte
 */
public class ChatEvent extends RoomEvent {

    public String nick;
    public String message;
    
    public ChatEvent(String nick, String message) {
        
        this.nick = nick;
        this.message = message;
    }
    
    @Override
    public EventType getEventType() {
        return EventType.CHAT_EVENT;
    }
    
}
