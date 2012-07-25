package tfmbot.packets;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import tfmbot.packets.OldProtocolPacket;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author malte
 */
public class Packet0101_080e_Title extends OldProtocolPacket {
    
    public String unlocked;
    
    public Packet0101_080e_Title(byte[] bytes) {
        try {
            String[] titles = new String(bytes, "UTF-8").split(",");
            unlocked = titles[titles.length - 1];
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Packet0101_080e_Title.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public String getName() {
        return "Title";
    }

    @Override
    public byte[] getOldCCC() {
        return new byte[]{0x08, 0x14};
    }

    @Override
    public short calculateOldLength() {
        return 0;//Not sent by client
    }

    @Override
    public void writeOldPacketData(DataOutputStream outputStream) throws IOException {
        //Not sent by client
    }
}
