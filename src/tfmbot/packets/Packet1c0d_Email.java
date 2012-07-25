package tfmbot.packets;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * This is sent to the client on login and has something to do with email
 * @author malte
 */
public class Packet1c0d_Email extends Packet {
    
    public Packet1c0d_Email(byte[] data) {
        
        // TODO figure this out
    }

    @Override
    public String getName() {
        return "Email";
    }

    @Override
    protected void writePacketData(DataOutputStream outputStream) throws IOException {
        
        //Not sent by client
    }

    @Override
    public byte[] getCCC() {
        return new byte[]{0x1c, 0x0d};
    }

    @Override
    public int calculateLength() {
        return 0;
    }
}
