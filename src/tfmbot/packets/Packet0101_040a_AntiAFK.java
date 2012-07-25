package tfmbot.packets;

import tfmbot.packets.OldProtocolPacket;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Keep alive packet sent from client to server
 * @author malte
 */
public class Packet0101_040a_AntiAFK extends OldProtocolPacket {
    
    
    public Packet0101_040a_AntiAFK() {
        
    }
    
    @Override
    public String getName() {
        
        return "AntiAFK";
    }

    @Override
    public byte[] getOldCCC() {
        
        return new byte[]{0x04, 0x0a};
    }

    @Override
    public short calculateOldLength() {
        
        /*
         * Old C    | 1
         * Old CC   | 1
         * ------------
         * Total    | 2
         */
        return 2;
    }

    @Override
    public void writeOldPacketData(DataOutputStream outputStream) throws IOException {
        
        //Empty packet
    }
    
    
}
