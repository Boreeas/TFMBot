package tfmbot.packets;

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
public class Packet0101_0808_MouseJoinedRoom extends OldProtocolPacket {
    
    /*public enum Action {JOIN, RESPAWN;}
    
    public String name;
    public String mouseID;
    public Action action;*/
    
    public Mouse mouse;
    
    
    public Packet0101_0808_MouseJoinedRoom(byte[] bytes) {
        
        byte[] data = new byte[bytes.length - 11];
        System.arraycopy(bytes, 11, data, 0, data.length);
        
        try {
            String[] fields = new String(data, "UTF-8").split("#");
            
            mouse = new Mouse(fields[0], fields[1], fields[2], fields[3], fields[4], fields[5], fields[6], fields[7].split(","), fields[8], fields[9], fields[10]);
            TFMBot.mice.put(Integer.parseInt(fields[1]), mouse);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Packet0101_0808_MouseJoinedRoom.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getName() {
        return "MouseJoinedRoom";
    }

    @Override
    public byte[] getOldCCC() {
        return new byte[]{0x08, 0x08};
    }

    @Override
    public short calculateOldLength() {
        return 0;   //Not sent by client
    }

    @Override
    public void writeOldPacketData(DataOutputStream outputStream) throws IOException {
        //Not sent by client
    }
}
