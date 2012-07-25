/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tfmbot.events;

import java.util.logging.Level;
import java.util.logging.Logger;
import tfmbot.main.TFMBot;

/**
 * <p>The Listener interface provides a common interface for all Listeners 
 * and Listener Subclasses.</p>
 * <p>The Contract of the <code>Listener</code> is as followed:
 * <list>
 * - The implementing classes must be named as followed: "<EventClass>EventListener"
 * - Subclasses must be named as followed: 
 * - - For listeners that are used to manage a package: "Listener" 
 *      + the first three letters of the event class + the package name
 * - - For listeners that manage a certain class: "Listener" 
 *      + the first three letters of the event class + the class name
 * </list></p>
 * @author malte
 */
public abstract class Listener {
    
    public void onEvent(Event e) {
        
        try {
            getClass().getDeclaredMethod("on" + e.getClass().getSimpleName(), e.getClass()).invoke(this, e);
        } catch (NoSuchMethodException ex) {
            TFMBot.log("Event: ", "[ERROR] ", "No hook found for event " + e.getClass().getName());
        } catch (Exception ex) {
            Logger.getLogger(Listener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
