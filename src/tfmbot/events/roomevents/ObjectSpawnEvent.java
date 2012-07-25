package tfmbot.events.roomevents;

import tfmbot.events.EventPump.EventType;
import tfmbot.main.TFMProtocol.ObjectType;

/**
 *
 * @author malte
 */
public class ObjectSpawnEvent extends RoomEvent {
    
    public ObjectType type;
    public short x;
    public short y;
    public short rotation;
    public byte xVelocity;
    public byte yVelocity;
    public boolean ghosted;
    
    public ObjectSpawnEvent(ObjectType type, short x, short y, short rotation, byte xVelocity, byte yVelocity, boolean ghosted) {
        
        this.type = type;
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;
        this.ghosted = ghosted;
    }

    @Override
    public EventType getEventType() {
        return EventType.OBJECT_SPAWN_EVENT;
    }
}
