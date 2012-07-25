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
public class Packet0101_0805_MouseDeath extends OldProtocolPacket {
    
    public int mouseID;
    public int points;
    public int miceLeft;
    
    public Packet0101_0805_MouseDeath(byte[] bytes) {
        try {
            String[] data = new String(bytes, "UTF-8").split(new String(new byte[]{0x01}, "UTF-8"));
            points = Integer.parseInt(data[data.length - 1]);
            miceLeft = Integer.parseInt(data[data.length - 2]);
            mouseID = Integer.parseInt(data[data.length - 3]);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Packet0101_0805_MouseDeath.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getName() {
        return "MouseDeath";
    }

    @Override
    public byte[] getOldCCC() {
        return new byte[]{0x08, 0x05};
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
