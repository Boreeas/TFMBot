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
public class Packet2c01_BulleServer extends Packet {
    
    public int key;
    public String ip;
    
    public Packet2c01_BulleServer(byte[] bytes) {
        
        key = ByteBuffer.allocate(4).put(bytes, 6, 4).getInt(0);
        
        byte[] data = new byte[bytes.length - 12];
        System.arraycopy(bytes, 12, data, 0, data.length);
        try {
            ip = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Packet2c01_BulleServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        TFMBot.log("Connection: ", "[INFO] ", "Connecting to " + ip + " with key " + key);
    }

    @Override
    protected void writePacketData(DataOutputStream outputStream) throws IOException {
        
        outputStream.writeInt(key);
    }

    @Override
    public String getName() {
        return "BulleServer";
    }

    @Override
    public byte[] getCCC() {
        return new byte[] {0x2c, 0x01};
    }

    @Override
    public int calculateLength() {
        /*
         * Length       | 4
         * Fingerprint  | 4
         * C            | 1
         * CC           | 1
         * Key          | 4
         * ----------------
         * Total        |14
         */
        return 14;
    }
}
