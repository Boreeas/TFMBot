package tfmbot.threads;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import tfmbot.main.TFMBot;
import tfmbot.main.TFMProtocol.ServerType;
import tfmbot.packets.Packet1a1a_Pong;

/**
 *
 * @author malte
 */
public class SchedulePong extends Thread {
    
    Packet1a1a_Pong packet;
    
    public SchedulePong(Packet1a1a_Pong packet) {
        
        this.packet = packet;
        setDaemon(true);
    }
    
    @Override
    public void run() {
        
        boolean bulle = packet.key > 0;
        
        String suffix = (bulle) ? "bulle" : "main";
        String out = " pong packet from " + suffix;
        TFMBot.log("Pong: ", "[INFO] ", "Got" + out);
        
        try {
            sleep(11000);
        } catch (InterruptedException ex) {
            TFMBot.log("Pong: ", "[WARNING] ", "Could not sleep long enough, possible disconnect");
        }
        
        TFMBot.log("Pong: ", "[INFO] ", "Sending" + out);
        if (bulle) {
            TFMBot.getInstance().getConnectedServer(ServerType.BULLE).sendPacket(packet);
        } else {
            TFMBot.getInstance().getConnectedServer(ServerType.BULLE).sendPacket(packet);
        }
    }
}
