package tfmbot.packets;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author malte
 */
public class Packet0101_0817_Unk4 extends OldProtocolPacket {

    public Packet0101_0817_Unk4(byte[] bytes) {
        
        for (String s: getFields(bytes)) {
            
            System.out.println("s = " + s);
        }
    }
    
    @Override
    public String getName() {
        return "Unknown";
    }

    @Override
    public byte[] getOldCCC() {
        return new byte[]{0x08, 0x17};
    }

    @Override
    public short calculateOldLength() {
        return 0;   //Unknown
    }

    @Override
    public void writeOldPacketData(DataOutputStream outputStream) throws IOException {
        //Unknown
    }
    
}
