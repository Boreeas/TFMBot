package tfmbot.events.roomevents;

import tfmbot.events.EventPump.EventType;

/**
 *
 * @author malte
 */
public class MouseLeftRoomEvent extends RoomEvent {
    
    public final int mouseID;
    public final String mouseName;
    
    public MouseLeftRoomEvent(int mouseID, String mouseName) {
        
        this.mouseID = mouseID;
        this.mouseName = mouseName;
    }

    @Override
    public EventType getEventType() {
        return EventType.MOUSE_LEFT_ROOM_EVENT;
    }
}
