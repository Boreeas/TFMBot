package tfmbot.packets;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import tfmbot.main.TFMBot;

/**
 *
 * @author malte
 */
public class Packet2c02_BulleFingerprint extends Packet {

    public int[] MDT;
    public short CMDTEC;
    
    public Packet2c02_BulleFingerprint(byte[] bytes) {
        
        
        short len = ByteBuffer.allocate(2).put(bytes[6]).put(bytes[7]).getShort(0);
        byte[] mdtBytes = new byte[len];
        System.arraycopy(bytes, 8, mdtBytes, 0, len);
        try {
            String mdtString = new String(mdtBytes, "UTF-8");
            MDT = new int[mdtString.length()];
            
            for (int i = 0; i < MDT.length; i++) {
                
                MDT[i] = (Integer.parseInt(mdtString.substring(i, i+1)) == 0) ? 10 : Integer.parseInt(mdtString.substring(i, i+1));
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Packet2c02_BulleFingerprint.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        CMDTEC = ByteBuffer.allocate(2).put(bytes, 8+len, 2).getShort(0);
    }
    
    @Override
    protected void writePacketData(DataOutputStream outputStream) throws IOException {
        //Not sent by client
    }

    @Override
    public String getName() {
        return "BulleFingerprint";
    }

    @Override
    public byte[] getCCC() {
        return new byte[]{0x2c, 0x02};
    }

    @Override
    public int calculateLength() {
        return 0; //Not sent by client
    }
    
}
