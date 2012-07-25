package tfmbot.threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import tfmbot.main.TFMBot;

/**
 * This thread reads data from Sanky's web site periodically to update the key and the protocol version
 * Thanks to Sanky for offering this service
 * 
 * @author malte
 */
public class ThreadGetKey extends Thread {
    
    String urlloc = "http://kikoo.formice.com/data.txt";
    
    public ThreadGetKey() {
        
        TFMBot.log("Sanky: ", "[INFO] ", "Attempting to make new connection to " + urlloc + "...");
        BufferedReader reader = null;
        try {
            URL sanky = new URL(urlloc);
            reader = new BufferedReader(new InputStreamReader(sanky.openStream()));
                   
            TFMBot.log("Sanky: ", "[INFO] ", "Got information from Sanky");
            String[] args = reader.readLine().split(" ");
                    
            if (TFMBot.protocolVersion != Integer.parseInt(args[1])) {
                TFMBot.log("Sanky: ", "[INFO] ", new StringBuilder("Updating protocol version (Was ").append(TFMBot.protocolVersion).append(", is now ").append(args[1]).append(")").toString());
                TFMBot.protocolVersion = Short.parseShort(args[1]);
            }
            if (!TFMBot.key.equals(args[2])) {
                TFMBot.log("Sanky: ", "[INFO] ", new StringBuilder("Updating key (Was ").append(TFMBot.key).append(", is now ").append(args[2]).append(")").toString());
                TFMBot.key = args[2];
            }
            reader.close();
            sanky = null;
                    
        } catch (IOException ex) {
            Logger.getLogger(ThreadGetKey.class.getName()).log(Level.SEVERE, null, ex);
            return;
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(ThreadGetKey.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        this.setDaemon(true);
    }
    
    @Override
    public void run() {
        
        while (true) {
            {
                TFMBot.log("Sanky: ", "[INFO] ", "Attempting to make new connection to " + urlloc + "...");
                BufferedReader reader = null;
                try {
                    URL sanky = new URL(urlloc);
                    reader = new BufferedReader(new InputStreamReader(sanky.openStream()));
                    
                    TFMBot.log("Sanky: ", "[INFO] ", "Got information from Sanky");
                    String[] args = reader.readLine().split(" ");
                    
                    if (TFMBot.protocolVersion != Integer.parseInt(args[1])) {
                        TFMBot.log("Sanky: ", "[INFO] ", new StringBuilder("Updating protocol version (Was ").append(TFMBot.protocolVersion).append(", is now ").append(args[1]).toString());
                        TFMBot.protocolVersion = Short.parseShort(args[1]);
                    }
                    if (!TFMBot.key.equals(args[2])) {
                        TFMBot.log("Sanky: ", "[INFO] ", new StringBuilder("Updating key (Was ").append(TFMBot.key).append(", is now ").append(args[2]).toString());
                        TFMBot.key = args[2];
                    }
                    reader.close();
                    sanky = null;
                    
                } catch (IOException ex) {
                    Logger.getLogger(ThreadGetKey.class.getName()).log(Level.SEVERE, null, ex);
                    return;
                } finally {
                    try {
                        reader.close();
                    } catch (IOException ex) {
                        Logger.getLogger(ThreadGetKey.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                //We don't need this anymore
                reader = null;
                try {
                    Thread.sleep(900000); //900000 == 15 minutes
                } catch (InterruptedException ex) {
                    Logger.getLogger(ThreadGetKey.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
