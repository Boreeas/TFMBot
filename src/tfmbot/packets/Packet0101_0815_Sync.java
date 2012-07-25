package tfmbot.packets;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author malte
 */
public class Packet0101_0815_Sync extends OldProtocolPacket {

    int mouseID;
    
    public Packet0101_0815_Sync(byte[] bytes) {
        
        String[] fields = getFields(bytes);
        mouseID = Integer.parseInt(fields[0]);
    }
    
    @Override
    public String getName() {
        return "Sync";
    }

    @Override
    public byte[] getOldCCC() {
        return new byte[]{0x08, 0x15};
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
