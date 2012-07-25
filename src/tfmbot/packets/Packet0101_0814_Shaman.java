package tfmbot.packets;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author malte
 */
public class Packet0101_0814_Shaman extends OldProtocolPacket {

    int mouseID;
    
    public Packet0101_0814_Shaman(byte[] bytes) {
        
        String[] fields = getFields(bytes);
        
        if (fields.length < 1) {
            mouseID = 0;
        } else {
            mouseID = Integer.parseInt(fields[0]);
        }
    }
    
    @Override
    public String getName() {
        return "Shaman";
    }

    @Override
    public byte[] getOldCCC() {
        return new byte[]{0x08, 0x14};
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
