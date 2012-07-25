package tfmbot.events.roomevents;

import tfmbot.events.EventPump.EventType;

/**
 *
 * @author malte
 */
public class EnterRoomEvent extends RoomEvent {
    
    public String room;
    
    public EnterRoomEvent(String roomname) {
        
        room = roomname;
    }

    @Override
    public EventType getEventType() {
        return EventType.ENTER_ROOM_EVENT;
    }
}
