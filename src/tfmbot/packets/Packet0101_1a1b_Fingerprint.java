package tfmbot.packets;

import tfmbot.packets.OldProtocolPacket;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * The packet used to generate the fingerprint
 * @author malte
 */
public class Packet0101_1a1b_Fingerprint extends OldProtocolPacket {
    
    public int[] MDT;
    public int CMDTEC;
    
    public Packet0101_1a1b_Fingerprint(byte[] data) throws UnsupportedEncodingException {
        
        int index = 11;  //Length (4) + c/cc (2) + oldLength (2) + old c/cc (2)
        boolean foundField2 = false;
        boolean foundField3 = false;
        ArrayList<Integer> list = new ArrayList<Integer>();
        String mdtString = "";
        
        for (;index < data.length; index++) {
            
            if (!foundField2) {
                
                if (data[index] == 0x01) foundField2 = true;
            } else if (!foundField3) {
                
                if (data[index] == 0x01) {
                    foundField3 = true;
                } else {
                    
                    int i = Integer.parseInt(new String(new byte[] {data[index]}, "UTF-8"));
                    if (i == 0) i = 10;
                    list.add(i);
                }
            } else {
                
                if (data[index] == 0x01 || data[index] == 0x00) break;
                
                mdtString += (new String(new byte[] {data[index]}, "UTF-8"));
            }
        }
        
        MDT = new int[list.size()];
        
        for (int i = 0; i < list.size(); i++) {
            
            MDT[i] = list.get(i);
        }
        
        CMDTEC = Integer.parseInt(mdtString);
        
    }

    @Override
    protected void writePacketData(DataOutputStream outputStream) throws IOException {
        
        //Not sent by client
    }

    @Override
    public String getName() {
        return "Fingerprint";
    }

    @Override
    public byte[] getOldCCC() {
        return new byte[]{0x1a, 0x1b};
    }

    @Override
    public short calculateOldLength() {
        return 0;
    }

    @Override
    public void writeOldPacketData(DataOutputStream outputStream) throws IOException {
        //Not sent by client
    }
}
