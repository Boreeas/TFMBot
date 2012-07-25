package tfmbot.packets;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author malte
 */
public class Packet0101_0507_SpawnAnchor extends OldProtocolPacket {

    public Packet0101_0507_SpawnAnchor(byte[] bytes) {
        
        //Ignore tihs
    }
    
    @Override
    public String getName() {
        return "SpawnAnchor";
    }

    @Override
    public byte[] getOldCCC() {
        return new byte[]{0x05, 0x07};
    }

    @Override
    public short calculateOldLength() {
        return 0;   //Not sent by client
    }

    @Override
    public void writeOldPacketData(DataOutputStream outputStream) throws IOException {
        //not sent by client
    }
    
}
