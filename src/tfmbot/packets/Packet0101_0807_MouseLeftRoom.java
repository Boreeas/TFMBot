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
public class Packet0101_0807_MouseLeftRoom extends OldProtocolPacket {
    
    public String mouseID;
    public String mouseName;
    
    public Packet0101_0807_MouseLeftRoom(byte[] bytes) {
        
        byte[] data = new byte[bytes.length - 11];
        System.arraycopy(bytes, 11, data, 0, data.length);
        try {
            String[] fields = new String(data, "UTF-8").split(new String(new byte[]{0x01}, "UTF-8"));
            mouseID = fields[0];
            mouseName = fields[1];
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Packet0101_0807_MouseLeftRoom.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getName() {
        return "MouseLeftRoom";
    }

    @Override
    public byte[] getOldCCC() {
        return new byte[]{0x08, 0x08};
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
