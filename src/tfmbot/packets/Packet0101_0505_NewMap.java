package tfmbot.packets;

import tfmbot.packets.OldProtocolPacket;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author malte
 */
public class Packet0101_0505_NewMap extends OldProtocolPacket {

    public String mapCode;
    public String roundNumber;
    public int numPlayers;
    public String stuffAboutPortals;//fields[3]
    
    public Packet0101_0505_NewMap(byte[] bytes) {
        
        byte[] data = new byte[bytes.length - 11];
        System.arraycopy(bytes, 11, data, 0, data.length);
        try {
            String[] fields = new String(data, "UTF-8").split(new String(new byte[]{0x01}, "UTF-8"));
            mapCode = fields[0];
            numPlayers = Integer.parseInt(fields[1]);
            roundNumber = fields[2];
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Packet0101_0505_NewMap.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public String getName() {
        return "NewMap";
    }

    @Override
    public byte[] getOldCCC() {
        return new byte[]{0x05, 0x05};
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
