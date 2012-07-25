package tfmbot.events.roomevents;

import tfmbot.events.EventPump.EventType;
import tfmbot.main.Mouse;

/**
 *
 * @author malte
 */
public class MouseJoinedRoomEvent extends RoomEvent {
    
    public Mouse mouse;
    
    public MouseJoinedRoomEvent(Mouse mouse) {
        
        this.mouse = mouse;
    }

    @Override
    public EventType getEventType() {
        return EventType.MOUSE_JOINED_ROOM_EVENT;
    }
}
