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
public class Packet0101_040d_DeleteConjure extends OldProtocolPacket {
    
    public int x;
    public int y;
    
    public Packet0101_040d_DeleteConjure(byte[] bytes) {
        
        byte[] data = new byte[bytes.length - 11];
        System.arraycopy(bytes, 11, data, 0, data.length);
        try {
            String[] fields = new String(data, "UTF-8").split(new String(new byte[]{0x01}, "UTF-8"));
            x = Integer.parseInt(fields[0]);
            y = Integer.parseInt(fields[1]);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Packet0101_040d_DeleteConjure.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getName() {
        return "DeleteConjure";
    }

    @Override
    public byte[] getOldCCC() {
        return new byte[]{0x04, 0x0f};
    }

    @Override
    public short calculateOldLength() {
        return 0;   //not sent by client
    }

    @Override
    public void writeOldPacketData(DataOutputStream outputStream) throws IOException {
        //not sent by client
    }
}
