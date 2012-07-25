package tfmbot.events.roomevents;

import tfmbot.events.EventPump.EventType;

/**
 *
 * @author malte
 */
public class MouseEnterHoleEvent extends RoomEvent {

    public int mouseID;
    public int place;
    public int playersLeft;
    public int points;
    public float seconds;
    
    public MouseEnterHoleEvent(int mouseID, int place, int playersLeft, int points, float seconds) {
        this.mouseID = mouseID;
        this.place = place;
        this.playersLeft = playersLeft;
        this.points = points;
        this.seconds = seconds;
    }

    
    
    @Override
    public EventType getEventType() {
        return EventType.MOUSE_ENTER_HOLE_EVENT;
    }
    
}
