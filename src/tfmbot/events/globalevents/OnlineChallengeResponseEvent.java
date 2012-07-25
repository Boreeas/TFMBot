package tfmbot.events.globalevents;

import tfmbot.events.EventPump.EventType;

/**
 *
 * @author malte
 */
public class OnlineChallengeResponseEvent extends GlobalEvent {

    public String user;
    
    
    public OnlineChallengeResponseEvent(String user) {
        
        this.user = user;
    }
    
    @Override
    public EventType getEventType() {
        return EventType.ONLINE_CHALLENGE_RESPONSE_EVENT;
    }
    
}
