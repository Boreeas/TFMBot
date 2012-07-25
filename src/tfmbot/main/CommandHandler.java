/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tfmbot.main;

/**
 * The interface used by plugins for registering commands
 * @author malte
 */
public interface CommandHandler {
    
    /**
     * Called when the command is called
     * @param sender The nick of the person that issued the command
     * @param command The command itself
     * @param args The arguments that have been passed
     */
    public void onCommand(String sender, String command, String[] args);
}