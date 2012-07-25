/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tfmbot.plugins;

/**
 *
 * @author malte
 */
public abstract class Plugin {
    
    public Plugin() {}
    public abstract void onEnable();
    public abstract void onDisable();
    
    public abstract String getName();
    public abstract String getDescription();
    public abstract String getHelp(String command);
}
