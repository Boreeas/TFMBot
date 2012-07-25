package tfmbot.packets;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author malte
 */
public class Packet0101_0806_MouseEnterHole extends OldProtocolPacket {

    public int mouseID;
    public int playersLeft;
    public int points;
    public int place;
    public float seconds;
    
    public Packet0101_0806_MouseEnterHole(byte[] bytes) {
        
        String[] fields = getFields(bytes);
        mouseID = Integer.parseInt(fields[0]);
        playersLeft = Integer.parseInt(fields[1]);
        points = Integer.parseInt(fields[2]);
        place = Integer.parseInt(fields[3]);
        seconds = (float) (Integer.parseInt(fields[4]) / 10.0);
    }
    
    @Override
    public String getName() {
        return "MouseEnterHole";
    }

    @Override
    public byte[] getOldCCC() {
        return new byte[]{0x08, 0x06};
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
