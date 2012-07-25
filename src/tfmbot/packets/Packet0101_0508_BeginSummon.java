package tfmbot.packets;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author malte
 */
public class Packet0101_0508_BeginSummon extends OldProtocolPacket {
    
    public Packet0101_0508_BeginSummon(byte[] bytes) {
        
        super();
    }

    @Override
    public String getName() {
        return "BeginSummon";
    }

    @Override
    public byte[] getOldCCC() {
        return new byte[]{0x05, 0x08};
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
