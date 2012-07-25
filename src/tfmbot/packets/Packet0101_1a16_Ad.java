package tfmbot.packets;

import tfmbot.packets.OldProtocolPacket;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Ad packet
 * @author malte
 */
public class Packet0101_1a16_Ad extends OldProtocolPacket {
    
    
    public Packet0101_1a16_Ad(byte[] data) {
        
        //We can ignore that
    }

    @Override
    public String getName() {
        
        return "Ad";
    }

    @Override
    public byte[] getOldCCC() {
        return new byte[]{0x1a, 0x16};
    }

    @Override
    public short calculateOldLength() {
        return 0;
    }

    @Override
    public void writeOldPacketData(DataOutputStream outputStream) throws IOException {
        
        //Not sent by client
    }
    
}
