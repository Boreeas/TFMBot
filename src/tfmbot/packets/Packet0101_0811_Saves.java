package tfmbot.packets;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author malte
 */
public class Packet0101_0811_Saves extends OldProtocolPacket {
    
    
    public Packet0101_0811_Saves(byte[] bytes) {
        
        // TODO Do something with this?
    }

    @Override
    public String getName() {
        return "Saves";
    }

    @Override
    public byte[] getOldCCC() {
        return new byte[]{0x08, 0x11};
    }

    @Override
    public short calculateOldLength() {
        //Not sent by client
        return 0;
    }

    @Override
    public void writeOldPacketData(DataOutputStream outputStream) throws IOException {
        //Not sent by client
    }
}
