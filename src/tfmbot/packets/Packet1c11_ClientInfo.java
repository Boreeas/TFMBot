package tfmbot.packets;

import java.io.DataOutputStream;
import java.io.IOException;
import tfmbot.main.TFMBot;

/**
 * Client information
 * @author malte
 */
public class Packet1c11_ClientInfo extends Packet {

    public static final String lang = TFMBot.community.toLowerCase();
    public static final String OS = "Linux 2.6.35-29-generic-pae";
    public static final String flash = "LNX 10,2,159,1";
    
    @Override
    protected void writePacketData(DataOutputStream outputStream) throws IOException {
        
        outputStream.writeShort((short)lang.length());
        outputStream.write(lang.getBytes("UTF-8"));
        
        outputStream.writeShort((short)OS.length());
        outputStream.write(OS.getBytes("UTF-8"));
        
        outputStream.writeShort((short)flash.length());
        outputStream.write(flash.getBytes("UTF-8"));
    }

    @Override
    public String getName() {
        return "ClientInfo";
    }

    @Override
    public byte[] getCCC() {
        return new byte[]{0x1c, 0x11};
    }

    @Override
    public int calculateLength() {
        /**
         * Length       | 4
         * Fingerprint  | 4
         * C            | 1
         * CC           | 1
         * len(lang)    | 2
         * len(os)      | 2
         * len(flash)   | 2
         * -----------------
         * result       |16
         */
        return 16 + lang.length() + OS.length() + flash.length();
    }
    
}
