package tfmbot.packets;

import tfmbot.packets.OldProtocolPacket;
import java.io.DataOutputStream;
import java.io.IOException;
import tfmbot.main.TFMBot;

/**
 * Pong packet. Reply after ten seconds
 * @author malte
 */
public class Packet1a1a_Pong extends Packet {
    
    public byte key;
    
    public Packet1a1a_Pong(byte[] data) {
        
        key = data[data.length - 1];
    }

    
    @Override
    public String getName() {
        
        return "Pong";
    }

    @Override
    public byte[] getCCC() {
        return new byte[]{0x1a, 0x1a};
    }

    @Override
    public int calculateLength() {
        
        /*
         * Length       | 4
         * Fingerprint  | 4
         * C            | 1
         * CC           | 1
         * Byte         | 1
         * ----------------
         * Total        |11
         */
        return 11;
    }

    @Override
    public void writePacketData(DataOutputStream outputStream) throws IOException {
        
        outputStream.writeByte(key);
    }
}
