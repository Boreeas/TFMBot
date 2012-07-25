package tfmbot.events.mouseevents;

import tfmbot.events.Event;
import tfmbot.events.EventPump.EventClass;
import tfmbot.events.EventPump.EventType;

/**
 *
 * @author malte
 */
public abstract class MouseEvent extends Event {

    @Override
    public abstract EventType getEventType();

    @Override
    public EventClass getEventClass() {
        return EventClass.MOUSE_EVENT;
    }
    
}