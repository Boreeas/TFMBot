package tfmbot.packets;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author malte
 */
public class Packet0101_061a_Command extends OldProtocolPacket {

    public String command;
    
    public Packet0101_061a_Command(String command) {
        
        this.command = command;
    }
    
    @Override
    public String getName() {
        return "Command";
    }

    @Override
    public byte[] getOldCCC() {
        return new byte[]{0x06, 0x1a};
    }

    @Override
    public short calculateOldLength() {
        /**
         * Old C        | 1
         * Old CC       | 1
         * Delimeters   | 1
         * ----------------
         * Total        | 3
         */
        return (short) (3 + command.length());
    }

    @Override
    public void writeOldPacketData(DataOutputStream outputStream) throws IOException {
        outputStream.writeByte(0x01);
        
        outputStream.write(command.getBytes("UTF-8"));
    }
    
}
