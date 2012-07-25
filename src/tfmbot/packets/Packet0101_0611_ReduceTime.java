package tfmbot.packets;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author malte
 */
public class Packet0101_0611_ReduceTime extends OldProtocolPacket {

    public Packet0101_0611_ReduceTime(byte[] b) {
        
        // We can ignore this
    }
    
    @Override
    public String getName() {
        return "ReduceTime";
    }

    @Override
    public byte[] getOldCCC() {
        return new byte[]{0x06, 0x11};
    }

    @Override
    public short calculateOldLength() {
        throw new RuntimeException("Not sent by client");
    }

    @Override
    public void writeOldPacketData(DataOutputStream outputStream) throws IOException {
        throw new RuntimeException("Not sent by client");
    }
    
}
