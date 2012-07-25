package tfmbot.packets;

import java.io.DataOutputStream;
import java.io.IOException;
import tfmbot.main.TFMBot;

/**
 * ???
 * @author malte
 */
public class Packet0802_Language extends Packet {
    
    byte lang;
    
    public Packet0802_Language() {
        
        lang = 0x00;
    }
    
    public Packet0802_Language(byte language) {
        
        lang = language;
    }

    @Override
    protected void writePacketData(DataOutputStream outputStream) throws IOException {
        
        outputStream.writeByte(lang);
    }

    @Override
    public String getName() {
        return "Language";
    }
    
    @Override
    public byte[] getCCC() {
        
        return new byte[]{0x08, 0x02};
    }

    @Override
    public int calculateLength() {
        /*
         * Length       | 4
         * Fingerprint  | 4
         * C            | 1
         * CC           | 1
         * lang         | 1
         * ----------------
         * Total        |11
         */
        return 11;
    }
}
