package tfmbot.events.roomevents;

import tfmbot.events.Event;
import tfmbot.events.EventPump.EventClass;
import tfmbot.events.EventPump.EventType;

/**
 *
 * @author malte
 */
public abstract class RoomEvent extends Event {

    @Override
    public abstract EventType getEventType();

    @Override
    public EventClass getEventClass() {
        return EventClass.ROOM_EVENT;
    }
    
}
