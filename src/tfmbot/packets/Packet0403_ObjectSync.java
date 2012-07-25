package tfmbot.packets;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author malte
 */
public class Packet0403_ObjectSync extends Packet {

    public Packet0403_ObjectSync(byte[] bytes) {
        
        
    }
    
    @Override
    protected void writePacketData(DataOutputStream outputStream) throws IOException {
        //Not sent by client
    }

    @Override
    public String getName() {
        return "ObjectSync";
    }

    @Override
    public byte[] getCCC() {
        return new byte[]{0x04, 0x03};
    }

    @Override
    public int calculateLength() {
        return 0;   //not Sent by client
    }
    
}
