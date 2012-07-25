package tfmbot.packets;

import tfmbot.packets.OldProtocolPacket;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import tfmbot.main.TFMBot;

/**
 *
 * @author malte
 */
public class Packet0101_0515_EnterRoom extends OldProtocolPacket {
    
    public String roomName;
    
    public Packet0101_0515_EnterRoom(byte[] bytes) {
        
        byte[] data = new byte[bytes.length - 11];
        System.arraycopy(bytes, 11, data, 0, data.length);
        try {
            roomName = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Packet0101_0515_EnterRoom.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public String getName() {
        return "EnterRoom";
    }

    @Override
    public byte[] getOldCCC() {
        return new byte[]{0x05, 0x15};
    }

    @Override
    public short calculateOldLength() {
        return 0; //Not sent by client
    }

    @Override
    public void writeOldPacketData(DataOutputStream outputStream) throws IOException {
        //Not sent by client
    }
    
}
