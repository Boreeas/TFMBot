package tfmbot.events.globalevents;

import tfmbot.events.Event;
import tfmbot.events.EventPump.EventClass;
import tfmbot.events.EventPump.EventType;

/**
 *
 * @author malte
 */
public abstract class GlobalEvent extends Event {


    @Override
    public EventClass getEventClass() {
        return EventClass.GLOBAL_EVENT;
    }
    
    
}
