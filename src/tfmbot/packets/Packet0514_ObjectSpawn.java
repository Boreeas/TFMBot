package tfmbot.packets;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import tfmbot.main.TFMProtocol.ObjectType;

/**
 *
 * @author malte
 */
public class Packet0514_ObjectSpawn extends Packet {

    public ObjectType object;
    public short trueX;
    public short trueY;
    public short rotation;
    public byte velocityX;
    public byte velocityY;
    public boolean ghosted;
    
    
    public Packet0514_ObjectSpawn(ObjectType type, short x, short y, short rotation, byte velX, byte velY, boolean ghosted) {
        
        this.object = type;
        this.trueX = x;
        this.trueY = y;
        this.rotation = rotation;
        this.velocityX = velX;
        this.velocityY = velY;
        this.ghosted = ghosted;
    }
    
    public Packet0514_ObjectSpawn(byte[] bytes) {
        
        object = ObjectType.values()[bytes[7]];
        trueX = (short) (ByteBuffer.allocate(2).put(bytes, 8, 2).getShort(0) / 33);
        trueY = (short) (ByteBuffer.allocate(2).put(bytes, 10, 2).getShort(0) / 33);
        rotation = ByteBuffer.allocate(2).put(bytes, 12, 2).getShort(0);
        velocityX = bytes[14];
        velocityY = bytes[15];
        ghosted = bytes[16] == 1;
    }
    
    @Override
    protected void writePacketData(DataOutputStream outputStream) throws IOException {
        //Not sent by client
    }

    @Override
    public String getName() {
        return "ObjectSpawn";
    }

    @Override
    public byte[] getCCC() {
        return new byte[]{0x05, 0x14};
    }

    @Override
    public int calculateLength() {
        /**
         * Length       | 4
         * Fingerprint  | 4
         * C            | 1
         * CC           | 1
         * object       | 1
         * trueX        | 2
         * trueY        | 2
         * rotation     | 2
         * velX         | 1
         * velY         | 1
         * ghosted      | 1
         * ----------------
         * Total        |20
         */
        return 20;
    }
    
    public void print() {
        
        int len = calculateLength();
        byte[] data = new byte[len];
        data[0] = (byte) ((len >> 24) & 0xff);
        data[1] = (byte) ((len >> 16) & 0xff);
        data[2] = (byte) ((len >> 8) & 0xff);
        data[3] = (byte) (len & 0xff);
        
        data[4] = 0;
        data[5] = 0;
        data[6] = 0;
        data[7] = 0;
        
        data[8] = getCCC()[0];
        data[9] = getCCC()[1];
        
        data[10] = (byte) object.ordinal();
        data[11] = (byte) ((trueX >> 8) & 0xff);
        data[12] = (byte) (trueX & 0xff);
        data[13] = (byte) ((trueY >> 8) & 0xff);
        data[14] = (byte) (trueY & 0xff);
        
        data[15] = (byte) ((rotation >> 8) & 0xff);
        data[16] = (byte) (rotation & 0xff);
        
        data[17] = velocityX;
        data[18] = velocityY;
        data[19] = (byte) ((ghosted) ? 1 : 0);
        
        System.out.println(Arrays.toString(data));
        
    }
    
}
