package tfmbot.packets;

import java.io.DataOutputStream;
import java.io.IOException;
import tfmbot.main.TFMBot;

/**
 *
 * @author malte
 */
public class Packet0101_0408_ShamanTurned extends OldProtocolPacket {

    public Packet0101_0408_ShamanTurned(byte[] bytes) {
        
    }
    
    @Override
    public String getName() {
        return "ShamanTurned";
    }

    @Override
    public byte[] getOldCCC() {
        return new byte[]{0x04, 0x08};
    }

    @Override
    public short calculateOldLength() {
        return 0;   //Unknown
    }

    @Override
    public void writeOldPacketData(DataOutputStream outputStream) throws IOException {
        // Not yet sent
    }
    
}
