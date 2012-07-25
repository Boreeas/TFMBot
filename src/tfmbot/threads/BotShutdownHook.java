package tfmbot.threads;

import tfmbot.main.TFMBot;

/**
 *
 * @author malte
 */
public class BotShutdownHook extends Thread {
    
    
    @Override
    public void run() {
        
        
        TFMBot.getInstance().pluginManager.disableAllPlugins();
    }
}
