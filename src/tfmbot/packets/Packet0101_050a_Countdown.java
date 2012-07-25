package tfmbot.packets;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author malte
 */
public class Packet0101_050a_Countdown extends OldProtocolPacket {

    public Packet0101_050a_Countdown(byte[] bytes) {
        
        // We can ignore this
    }
    
    @Override
    public String getName() {
        return "Countdown";
    }

    @Override
    public byte[] getOldCCC() {
        return new byte[]{0x05, 0x0a};
    }

    @Override
    public short calculateOldLength() {
        
        //Not sent by client;
        return 0;
    }

    @Override
    public void writeOldPacketData(DataOutputStream outputStream) throws IOException {
        
        //Not sent by client
    }
    
    
}
