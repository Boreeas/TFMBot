package tfmbot.events.roomevents;

import tfmbot.events.EventPump.EventType;
import tfmbot.main.TFMProtocol.Emotion;

/**
 *
 * @author malte
 */
public class EmotionEvent extends RoomEvent {
    
    public int mouseID;
    public Emotion emotion;
    
    public EmotionEvent(int mouseID, Emotion emotion) {
        
        this.mouseID = mouseID;
        this.emotion = emotion;
    }

    @Override
    public EventType getEventType() {
        return EventType.EMOTION_EVENT;
    }
}
