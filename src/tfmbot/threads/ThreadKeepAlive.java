package tfmbot.threads;

import tfmbot.main.TFMBot;
import tfmbot.main.TFMProtocol.ServerType;
import tfmbot.packets.Packet0101_1a02_KeepAlive;

/**
 *
 * @author malte
 */
public class ThreadKeepAlive extends Thread {
    
    public ThreadKeepAlive() {
        
        this.setDaemon(true);
    }
    
    @Override
    public void run() {
        
        while (true) {
                
            try {
                sleep(15000);   //15 seconds delay
            } catch (InterruptedException ex) {
                TFMBot.log("Keep-Alive: ", "[WARNING] ", "Could not sleep for 15 seconds, possible disconnect");
            }
                
            try {
                TFMBot.getInstance().getConnectedServer(ServerType.MAIN).sendPacket(new Packet0101_1a02_KeepAlive());
                TFMBot.getInstance().getConnectedServer(ServerType.BULLE).sendPacket(new Packet0101_1a02_KeepAlive());
            } catch (RuntimeException e) {
                // Not connected to Server
            }
            TFMBot.log("Keep-Alive: ", "[INFO] ", "Sending keep-alive data");
        }
    }
}
