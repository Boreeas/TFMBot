/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tfmbot.packets;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author Boreeas
 */
public class Packet0101_1314_Easter extends OldProtocolPacket {

    private String roundID;
    
    public Packet0101_1314_Easter(int roundID) {
        
        this.roundID = Integer.toString(roundID);
    }
    
    public Packet0101_1314_Easter(String roundID) {
        
        this.roundID = roundID;
    }
    
    public Packet0101_1314_Easter(byte[] rawdata) {
        
        
    }
    
    @Override
    public String getName() {
        return "Easter";
    }

    @Override
    public byte[] getOldCCC() {
        return new byte[]{0x13, 0x14};
    }

    @Override
    public short calculateOldLength() {
        /*
         * Old C        | 1
         * Old CC       | 1
         * Delimeter    | 1
         * ----------------
         * Total        | 3
         */
        return (short) (3 + roundID.length());
    }

    @Override
    public void writeOldPacketData(DataOutputStream outputStream) throws IOException {
        
        outputStream.writeByte(0x01);
        outputStream.write(roundID.getBytes("UTF-8"));
    }
    
}
