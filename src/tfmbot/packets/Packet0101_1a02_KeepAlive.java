package tfmbot.packets;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author malte
 */
public class Packet0101_1a02_KeepAlive extends OldProtocolPacket {

    public Packet0101_1a02_KeepAlive() {
        
        
    }
    
    @Override
    public String getName() {
        return "KeepAlive";
    }

    @Override
    public byte[] getOldCCC() {
        return new byte[]{0x1a, 0x02};
    }

    @Override
    public short calculateOldLength() {
        /**
         * Old C    | 1
         * Old CC   | 1
         * ------------
         * Total    | 2
         */
        return 2;
    }

    @Override
    public void writeOldPacketData(DataOutputStream outputStream) throws IOException {
        
        //Empty
    }
    
}
