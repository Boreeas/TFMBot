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
public class Packet0101_0513_GetCheese extends OldProtocolPacket {
    
    String roundID = "0";
    String mouseID = "0";
    
    public Packet0101_0513_GetCheese(String roundID) {
        
        this.roundID = roundID;
    }
    
    public Packet0101_0513_GetCheese(byte[] bytes) {
        
        byte[] data = new byte[bytes.length - 11];
        System.arraycopy(bytes, 11, data, 0, data.length);
        try {
            mouseID = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Packet0101_0513_GetCheese.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getName() {
        return "GetCheese";
    }

    @Override
    public byte[] getOldCCC() {
        return new byte[]{0x05, 0x13};
    }

    @Override
    public short calculateOldLength() {
        /**
         * Old C        | 1
         * Old CC       | 1
         * Delimeter    | 1
         * ----------------
         * Total        | 1
         */
        return (short) (4 + roundID.length());
    }

    @Override
    public void writeOldPacketData(DataOutputStream outputStream) throws IOException {
        
        outputStream.writeByte(0x01);
        outputStream.write(roundID.getBytes("UTF-8"));
    }
}
