package tfmbot.packets;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import tfmbot.main.TFMProtocol.Community;

/**
 *
 * @author malte
 */
public class Packet0607_PrivateMessage extends Packet {

    public boolean isVerification;
    public String name;
    public String message;
    public Community community;
    public boolean isMod;
    
    public Packet0607_PrivateMessage(byte[] bytes) {
        
        try {
            isVerification = bytes[6] == 0; 
            
            short nameLen = ByteBuffer.allocate(2).put(bytes, 7, 2).getShort(0);
            byte[] rawName = new byte[nameLen];
            System.arraycopy(bytes, 9, rawName, 0, nameLen);
            name = new String(rawName, "UTF-8");
            
            try {
                community = Community.values()[bytes[9+nameLen]];
            } catch (ArrayIndexOutOfBoundsException ex) {
                
                System.out.println("Unknown community detected: " + bytes[9+nameLen]);
            }
            
            short msgLen = ByteBuffer.allocate(2).put(bytes, 10+nameLen, 2).getShort(0);
            byte[] rawMsg = new byte[msgLen];
            System.arraycopy(bytes, 12 + nameLen, rawMsg, 0, msgLen);
            message = new String(rawMsg, "UTF-8");
            
            isMod = (bytes.length > 13 + nameLen + msgLen) && (bytes[bytes.length - 1] > 0);
            
        } catch (UnsupportedEncodingException ex) {
            
        }
    }
    
    public Packet0607_PrivateMessage(String target, String message) {
        
        this.message = message;
        this.name = target;
    }
    
    @Override
    protected void writePacketData(DataOutputStream outputStream) throws IOException {
        
        outputStream.writeUTF(name);
        outputStream.writeUTF(message);
    }

    @Override
    public String getName() {
        return "PrivateMessage";
    }

    @Override
    public byte[] getCCC() {
        return new byte[]{0x06, 0x07};
    }

    @Override
    public int calculateLength() {
        /**
         * Length       | 4
         * Fingerprint  | 4
         * C            | 1
         * CC           | 1
         * len(target)  | 2
         * len(msg)     | 2
         * ----------------
         * Total        |14
         */
        return 14 + message.length() + name.length();
    }
    
}
