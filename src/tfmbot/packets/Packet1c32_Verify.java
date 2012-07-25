package tfmbot.packets;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author malte
 */
public class Packet1c32_Verify extends Packet {

    
    public Packet1c32_Verify(byte[] bytes) {
        
        // Ignored again
    }
    
    @Override
    protected void writePacketData(DataOutputStream outputStream) throws IOException {
        //Not sent by client
    }

    @Override
    public String getName() {
        return "Verify";
    }

    @Override
    public byte[] getCCC() {
        return new byte[]{0x1c, 0x32};
    }

    @Override
    public int calculateLength() {
        return 0;   //Not sent by client
    }
    
}
