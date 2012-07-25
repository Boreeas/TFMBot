package tfmbot.events.roomevents;

import tfmbot.events.EventPump.EventType;

/**
 *
 * @author malte
 */
public class ModMessageEvent extends RoomEvent {

    public final String name;
    public final String message;
    
    public ModMessageEvent(String name, String message) {
        
        this.name = name;
        this.message = message;
    }
    
    @Override
    public EventType getEventType() {
        return EventType.MOD_MESSAGE_EVENT;
    }
    
}
