package tfmbot.events.mouseevents;

import tfmbot.events.EventPump.EventType;

/**
 *
 * @author malte
 */
public class TitleUnlockedEvent extends MouseEvent {

    public String titleID;
    
    public TitleUnlockedEvent(String titleID) {
        
        this.titleID = titleID;
    }
    
    @Override
    public EventType getEventType() {
        return EventType.TITLE_UNLOCKED_EVENT;
    }
    
}
