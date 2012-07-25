package tfmbot.events.globalevents;

import tfmbot.events.EventPump.EventType;

/**
 *
 * @author malte
 */
public class GlobalMessageEvent extends GlobalEvent {

    public final String name;
    public final String message;
    
    public GlobalMessageEvent(String name, String message) {
        
        this.name = name;
        this.message = message;
    }
    
    @Override
    public EventType getEventType() {
        return EventType.GLOBAL_MESSAGE_EVENT;
    }
    
}
