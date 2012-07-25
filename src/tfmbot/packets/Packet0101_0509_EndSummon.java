package tfmbot.packets;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author malte
 */
public class Packet0101_0509_EndSummon extends OldProtocolPacket {
    
    public Packet0101_0509_EndSummon(byte[] bytes) {
        
        
    }

    @Override
    public String getName() {
        return "EndSummon";
    }

    @Override
    public byte[] getOldCCC() {
        return new byte[]{0x05, 0x09};
    }

    @Override
    public short calculateOldLength() {
        return 0;   //Not sent by client
    }

    @Override
    public void writeOldPacketData(DataOutputStream outputStream) throws IOException {
        //Not sent by client
    }
}
