package tfmbot.events.roomevents;

import tfmbot.events.EventPump.EventType;

/**
 *
 * @author malte
 */
public class MouseDeathEvent extends RoomEvent {
    
    public int mouseID;
    public int miceLeft;
    public int points;
    
    public MouseDeathEvent(int mouseID, int miceLeft, int points) {
        
        this.mouseID = mouseID;
        this.miceLeft = miceLeft;
        this.points = points;
    }

    @Override
    public EventType getEventType() {
        return EventType.MOUSE_DEATH_EVENT;
    }
    
}
