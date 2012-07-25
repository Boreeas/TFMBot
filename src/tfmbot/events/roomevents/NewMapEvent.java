package tfmbot.events.roomevents;

import tfmbot.events.EventPump.EventType;

/**
 *
 * @author malte
 */
public class NewMapEvent extends RoomEvent {

    public String mapCode;
    public int roundNumber;
    public int numPlayers;
    
    public NewMapEvent(String mapCode, int roundNumber, int numPlayers) {
        
        this.mapCode = mapCode;
        this.roundNumber = roundNumber;
        this.numPlayers = numPlayers;
    }
    
    @Override
    public EventType getEventType() {
        return EventType.NEW_MAP_EVENT;
    }
    
}
