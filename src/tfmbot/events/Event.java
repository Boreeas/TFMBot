/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tfmbot.events;

import tfmbot.events.EventPump.EventClass;
import tfmbot.events.EventPump.EventType;

/**
 *
 * @author malte
 */
public abstract class Event {
    
    public abstract EventType getEventType();
    
    public abstract EventClass getEventClass();
    
    public String toString() {
        
        return getClass().getName() + "@" + Integer.toHexString(hashCode()) + "::" + getEventType().name() + ":" + getEventClass().name();
    }
}
