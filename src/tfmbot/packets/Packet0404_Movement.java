package tfmbot.packets;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author malte
 */
public class Packet0404_Movement extends Packet {

    public Packet0404_Movement(byte[] bytes) {
        
        // TODO implement
    }
    
    @Override
    protected void writePacketData(DataOutputStream outputStream) throws IOException {
        // TODO implement
    }

    @Override
    public String getName() {
        return "Movement";
    }

    @Override
    public byte[] getCCC() {
        return new byte[]{0x04, 0x04};
    }

    @Override
    public int calculateLength() {
        // TODO implement
        return 0;
    }
    
}
