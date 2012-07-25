/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tfmbot.events;

import java.util.ArrayList;

/**
 *
 * @author malte
 */
public class EventPump {
    
    /**
     * Nested array lists for fast access. First order are event class, then event type, then listener priority
     */
    private static ArrayList<ArrayList<ArrayList<ArrayList<Listener>>>> registeredListeners = new ArrayList<ArrayList<ArrayList<ArrayList<Listener>>>>();
    
    
    /**
     * Registers a listener so it will be informed later when an event is fired
     * @param l The listener to register
     * @param eventType The event type used to identify the listener later
     * @param eventClass The event class used to identify the listener later
     * @param priority The priority of the listener (higher priority will be called later to allow further modification of the event)
     */
    public static void registerListener(Listener l, EventType eventType, EventClass eventClass, ListenerPriority priority) {
        
        try {
            registeredListeners.get(eventClass.ordinal()).get(eventType.ordinal()).get(priority.ordinal()).add(l);
        } catch (IndexOutOfBoundsException oobe) {
            
            for (int i = 0; i < EventClass.values().length; i++) {
                
                registeredListeners.add(new ArrayList<ArrayList<ArrayList<Listener>>>());
                for (int j = 0; j < EventType.values().length; j++) {
                    
                    registeredListeners.get(i).add(new ArrayList<ArrayList<Listener>>());
                    for (int k = 0; k < ListenerPriority.values().length; k++) {
                        
                        registeredListeners.get(i).get(j).add(new ArrayList<Listener>());
                    }
                }
            }
            
            registerListener(l, eventType, eventClass, priority);
        }
    }
    
    /**
     * Fires the event, calls the onEvent method of all listeners registered for that event
     * @param e 
     */
    public static synchronized Event fireEvent(Event e) {
        
        try {
            for (ArrayList<Listener> list : registeredListeners.get(e.getEventClass().ordinal()).get(e.getEventType().ordinal())) {
                
                for (Listener listener : list) {
                    
                    listener.onEvent(e);
                }
            }
        } catch (IndexOutOfBoundsException ex) {
            //No listeners defined here
        }
        
        return e;    
    }
    
    /**
     * The event type is used to further distinguish the events from each other
     */
    public enum EventType {
        
        /**
         * Room events
         */
        
        /**
         * Called when a new room has been entered
         */
        ENTER_ROOM_EVENT,
        
        /**
         * Called on player chat
         */
        CHAT_EVENT,
        
        /**
         * Called when the map changes
         */
        NEW_MAP_EVENT,
        
        /**
         * Called when a mouse dies
         */
        MOUSE_DEATH_EVENT,
        
        /**
         * Called when a mouse leaves a room
         */
        MOUSE_LEFT_ROOM_EVENT,
        
        /**
         * Called when a mouse joins a room
         */
        MOUSE_JOINED_ROOM_EVENT,
        
        /**
         * Called when a mouse uses an emotion
         */
        EMOTION_EVENT,
        
        /**
         * Called when a mouse enters a hole
         */
        MOUSE_ENTER_HOLE_EVENT,
        
        /**
         * Called on a single-room moderation message: [Modération] foo
         */
        MOD_MESSAGE_EVENT,
        
        /**
         * Called when an object is spawned
         */
        OBJECT_SPAWN_EVENT,
        
        
        /**
         * Mouse events
         */
        
        /**
         * Called when a new title is unlocked
         */
        TITLE_UNLOCKED_EVENT,
        
        /**
         * Called when a private message is received
         */
        PRIVATE_MESSAGE_EVENT,
        
        
        /**
         * Global Events
         */
        
        /**
         * Called when an online challenge response is received from a player
         */
        ONLINE_CHALLENGE_RESPONSE_EVENT,
        
        /**
         * Called when a server-wide message is broadcasted
         */
        GLOBAL_MESSAGE_EVENT,
        
        
        /**
         * Custom Event
         */
        CUSTOM_EVENT;
    }
    
    /**
     * The event class is used to separate the events in several groups to make it easier to identify them
     */
    public enum EventClass {
        
        /**
         * Events that have to do with rooms and actions within
         */
        ROOM_EVENT,
        
        /**
         * Events that directly have to do with the bot
         */
        MOUSE_EVENT,
        
        /**
         * Events for global happenings on the server, such as online challenges
         */
        GLOBAL_EVENT,
        
        /**
         * Custom event
         */
        CUSTOM_EVENT;
    }
    
    /**
     * The order in which the listeners are called. Low priority listeners are called first to allow high priority listeners to further modify the event
     */
    public enum ListenerPriority {
        
        /**
         * Lowest priority is called first
         */
        LOWEST,
        
        /**
         * Then low priority
         */
        LOW,
        
        /**
         * Then normal priority
         */
        NORMAL,
        
        /**
         * Then high priorty
         */
        HIGH,
        
        /**
         * Then highest, which should only be used by listeners that have to have the last say under any circumstance
         */
        HIGHEST,
        
        /**
         * Monitor should only be used to check the results of an event, and not modify it in any way
         */
        MONITOR
    }
}
