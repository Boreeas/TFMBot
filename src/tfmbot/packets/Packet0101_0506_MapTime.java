package tfmbot.packets;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author malte
 */
public class Packet0101_0506_MapTime extends OldProtocolPacket {

    
    public Packet0101_0506_MapTime(byte[] bytes) {
        
        //We can ignore this... again
    }
    
    @Override
    public String getName() {
        return "MapTime";
    }

    @Override
    public byte[] getOldCCC() {
        return new byte[]{0x05, 0x06};
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
