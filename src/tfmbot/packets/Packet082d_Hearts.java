package tfmbot.packets;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import tfmbot.main.TFMBot;

/**
 *
 * @author malte
 */
public class Packet082d_Hearts extends Packet {

    public Packet082d_Hearts(byte[] bytes) {
        
        short hearts = ByteBuffer.allocate(2).put(bytes[bytes.length - 2]).put(bytes[bytes.length - 1]).getShort(0);
        TFMBot.log("I have " + hearts + " hearts");
        
    }
    
    @Override
    protected void writePacketData(DataOutputStream outputStream) throws IOException {
        //Not sent by client
    }

    @Override
    public String getName() {
        return "Hearts";
    }

    @Override
    public byte[] getCCC() {
        return new byte[] {0x08, 0x2d};
    }

    @Override
    public int calculateLength() {
        return 0; //Not sent by client
    }
    
}
