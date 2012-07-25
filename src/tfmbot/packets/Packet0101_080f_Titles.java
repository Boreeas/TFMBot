package tfmbot.packets;

import tfmbot.packets.OldProtocolPacket;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import tfmbot.main.TFMBot;

/**
 * Sent to the client on login, contains the title ids
 * @author malte
 */
public class Packet0101_080f_Titles extends OldProtocolPacket {

    public Packet0101_080f_Titles(byte[] data) {
        
        byte[] titleIDs = new byte[data.length - 11];   //Packet header length without fingerprint + first x01
        System.arraycopy(data, 11, titleIDs, 0, titleIDs.length);
        try {
            String[] ids = new String(titleIDs, "UTF-8").split(new String(new byte[]{0x01}, "UTF-8"));
            TFMBot.availableTitles.addAll(Arrays.asList(ids));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Packet0101_080f_Titles.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public String getName() {
        return "Titles";
    }

    @Override
    public byte[] getOldCCC() {
        return new byte[]{0x08, 0x0f};
    }

    @Override
    public short calculateOldLength() {
        return 0; //Not sent by client
    }

    @Override
    public void writeOldPacketData(DataOutputStream outputStream) throws IOException {
        //Not sent by client
    }
    
}
