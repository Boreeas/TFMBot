package tfmbot.packets;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author malte
 */
public class Packet0606_Message extends Packet {

    public String message;
    public String name;
    int mouseID;
    
    
    public Packet0606_Message(byte[] bytes) {
        try {
            mouseID = ByteBuffer.allocate(4).put(bytes, 6, 4).getInt(0);
            
            short nameLen = ByteBuffer.allocate(2).put(bytes, 10, 2).getShort(0);
            byte[] data = new byte[nameLen];
            System.arraycopy(bytes, 12, data, 0, nameLen);
            name = new String(data, "UTF-8");
            
            short messageLen = ByteBuffer.allocate(2).put(bytes, 12+nameLen, 2).getShort(0);
            data = new byte[messageLen];
            System.arraycopy(bytes, 14+nameLen, data, 0, messageLen);
            message = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Packet0606_Message.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Packet0606_Message(String message) {
        this.message = message;
    }
    
    @Override
    protected void writePacketData(DataOutputStream outputStream) throws IOException {
        
        outputStream.writeUTF(message);
    }

    @Override
    public String getName() {
        return "Message";
    }

    @Override
    public byte[] getCCC() {
        return new byte[]{0x06, 0x06};
    }

    @Override
    public int calculateLength() {
        /**
         * Length       | 4
         * Fingerprint  | 4
         * C            | 1
         * CC           | 1
         * len(msg)     | 2
         * ----------------
         * Total        |12
         */
        return 12 + message.length();
    }
    
}
