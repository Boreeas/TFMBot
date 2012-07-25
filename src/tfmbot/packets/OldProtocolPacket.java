package tfmbot.packets;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import tfmbot.packets.Packet;

/**
 *
 * @author malte
 */
public abstract class OldProtocolPacket extends Packet {

    @Override
    protected void writePacketData(DataOutputStream outputStream) throws IOException {
        
        outputStream.writeShort(calculateOldLength());
        outputStream.write(getOldCCC());
        
        writeOldPacketData(outputStream);
    }

    @Override
    public abstract String getName();

    @Override
    public int calculateLength() {
        
        /**
         * Length       | 4
         * Fingerprint  | 4
         * C            | 1
         * CC           | 1
         * Old Length   | 2
         * ----------------
         * Total        | 2
         */
        return 12 + calculateOldLength();
    }
    
    public String[] getFields(byte[] rawPacketData) {
        
        if (rawPacketData.length < 11) {
            return new String[0];
        }
        
        byte[] data = new byte[rawPacketData.length - 11];
        System.arraycopy(rawPacketData, 11, data, 0, data.length);
        try {
            String out = new String(data, "UTF-8");
            return out.split(new String(new byte[]{0x01}, "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(OldProtocolPacket.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    @Override
    public byte[] getCCC() {
        
        return new byte[]{0x01, 0x01};
    }
    
    /**
     * Returns the old identifying c/cc bytes
     * @return the old identifying c/cc bytes
     */
    public abstract byte[] getOldCCC();
    
    /**
     * Calculates the length of the old packet section
     * @return the length of the old packet section
     */
    public abstract short calculateOldLength();

    public abstract void writeOldPacketData(DataOutputStream outputStream) throws IOException;
}
