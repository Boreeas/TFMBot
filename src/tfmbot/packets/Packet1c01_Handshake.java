package tfmbot.packets;

import java.io.DataOutputStream;
import java.io.IOException;
import tfmbot.main.TFMBot;

/**
 * This packet is used as a handshake by the client to establish a connection to the server
 * @author malte
 */
public class Packet1c01_Handshake extends Packet {
    
   
    short version;
    String key;
    
    public Packet1c01_Handshake(byte[] data) {
        
        //Ignore it
    }
    
    public Packet1c01_Handshake(short version) {
    
        this.version = version;
        key = TFMBot.key;
        
    }

    @Override
    public void writePacketData(DataOutputStream outputStream) throws IOException {
        
        
        outputStream.writeShort(version);
        
        outputStream.writeUTF(TFMBot.key);
        
        outputStream.writeShort(0x17ed);    //Launcher size
    }
    
    @Override
    public String getName() {
        
        return "Handshake";
    }

    @Override
    public byte[] getCCC() {
        return new byte[]{0x1c, 0x01};
    }

    @Override
    public int calculateLength() {
        /**
         * Length       | 4
         * Fingerprint  | 4
         * C            | 1
         * CC           | 1
         * version      | 2
         * len(key)     | 2
         * loaderSize   | 2
         * ----------------
         * Total        |16
         */
        return 16 + key.length();
    }
}
