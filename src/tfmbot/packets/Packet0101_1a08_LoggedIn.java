package tfmbot.packets;

import tfmbot.packets.OldProtocolPacket;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import tfmbot.main.TFMBot;

/**
 *
 * @author malte
 */
public class Packet0101_1a08_LoggedIn extends OldProtocolPacket {

    public Packet0101_1a08_LoggedIn(byte[] b) {
        
        byte[] data = new byte[b.length - 11];   //Packet header length without fingerprint + first x01
        System.arraycopy(b, 11, data, 0, data.length);
        try {
            String[] stuff = new String(data, "UTF-8").split(new String(new byte[]{0x01}, "UTF-8"));
            TFMBot.log("User mode is " + stuff[2]);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Packet0101_1a08_LoggedIn.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public String getName() {
        return "LoggedIn";
    }

    @Override
    public byte[] getOldCCC() {
        return new byte[] {0x1a, 0x08};
    }

    @Override
    public short calculateOldLength() {
        return 0; //Not send by client
    }

    @Override
    public void writeOldPacketData(DataOutputStream outputStream) throws IOException {
        //Not sent by client
    }
    
}
