package tfmbot.main;

/**
 * An exception that occurs when another plugin already registered this Command
 * @author malte
 */
public class CommandAlreadyRegisteredException extends Exception {
    
    
    public CommandAlreadyRegisteredException() {
        
        
    }
    
    public CommandAlreadyRegisteredException(String details) {
        
        super(details);
    }
}
