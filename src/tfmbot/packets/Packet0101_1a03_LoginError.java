package tfmbot.packets;

import tfmbot.packets.OldProtocolPacket;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author malte
 */
public class Packet0101_1a03_LoginError extends OldProtocolPacket {

    public Packet0101_1a03_LoginError(byte[] data) {
        
        //Forget it
    }
    
    @Override
    public String getName() {
        return "LoginError";
    }

    @Override
    public byte[] getOldCCC() {
        return new byte[]{0x1a, 0x03};
    }

    @Override
    public short calculateOldLength() {
        return 0;
    }

    @Override
    public void writeOldPacketData(DataOutputStream outputStream) throws IOException {
        
        //We don't need this
    }
    
}
