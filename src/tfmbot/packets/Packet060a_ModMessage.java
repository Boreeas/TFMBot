package tfmbot.packets;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 *
 * @author malte
 */
public class Packet060a_ModMessage extends Packet{

    public String message;
    public boolean global;
    public String name;
    
    public Packet060a_ModMessage(byte[] bytes) {
        
        global = bytes[6] == 1;
        
        try {
            
            short namelen = ByteBuffer.allocate(2).put(bytes, 7, 2).getShort(0);
            byte[] namebuffer = new byte[namelen];
            System.arraycopy(bytes, 9, namebuffer, 0, namelen);
            name = new String(namebuffer, "UTF-8");
            
            short messagelen = ByteBuffer.allocate(2).put(bytes, 9+namelen, 2).getShort(0);
            byte[] messagebuffer = new byte[messagelen];
            System.arraycopy(bytes, 9+namelen, messagebuffer, 0, messagelen);
            message = new String(messagebuffer, "UTF-8");
        } catch (UnsupportedEncodingException uee) {
            
            
        }
    }
    
    public Packet060a_ModMessage(boolean global, String message) {
        
        this.global = global;
        this.message = message;
    }
    
    @Override
    protected void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.writeBoolean(global);
        outputStream.writeUTF(message);
    }

    @Override
    public String getName() {
        return "ModMessage";
    }

    @Override
    public byte[] getCCC() {
        return new byte[]{0x06, 0x0a};
    }

    @Override
    public int calculateLength() {
        /**
         * Length       | 4
         * Fingerprint  | 4
         * C            | 1
         * CC           | 1
         * global       | 1
         * len(msg)     | 2
         * ----------------
         * Total        |13
         */
        return 13 + message.length();
    }
    
}
