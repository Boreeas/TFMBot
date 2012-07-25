package tfmbot.packets;

import tfmbot.packets.OldProtocolPacket;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import tfmbot.main.Mouse;
import tfmbot.main.TFMBot;

/**
 *
 * @author malte
 */
public class Packet0101_0809_RoomPlayers extends OldProtocolPacket {
    
    public Packet0101_0809_RoomPlayers(byte[] bytes) {
        
        byte[] data = new byte[bytes.length - 11];
        System.arraycopy(bytes, 11, data, 0, data.length);
        try {
            String[] miceData = new String(data, "UTF-8").split(new String(new byte[]{0x01}, "UTF-8"));
            
            for (String mouseInfo: miceData) {
            
                String[] mouseData = mouseInfo.split("#");
                TFMBot.mice.put(Integer.parseInt(mouseData[1]), new Mouse(mouseData[0], mouseData[1], mouseData[2], mouseData[3], mouseData[4], mouseData[5], mouseData[6], mouseData[7].split(","), mouseData[8], mouseData[9], mouseData[10]));
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Packet0101_0809_RoomPlayers.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }

    @Override
    public String getName() {
        return "RoomPlayers";
    }

    @Override
    public byte[] getOldCCC() {
        return new byte[]{0x08, 0x09};
    }

    @Override
    public short calculateOldLength() {
        return 0;//Not sent by client
    }

    @Override
    public void writeOldPacketData(DataOutputStream outputStream) throws IOException {
        //not sent by client
    }
}
