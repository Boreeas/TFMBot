package tfmbot.packets;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;
import tfmbot.main.TFMBot;

/**
 * Packet send to log in to the server
 * @author malte
 */
public class Packet0101_1a04_Login extends OldProtocolPacket {

    private static final int MAX_NAME_LENGTH = 12;
    
    private String name = genRandomName();
    private String room = genRandomRoom();
    
    
    final String urlKey = "http://www.transformice.com/Transformice.swf?n="
                                + System.currentTimeMillis()
                                + "137";
    
    @Override
    public String getName() {
        return "Login";
    }

    @Override
    public byte[] getOldCCC() {
        return new byte[]{0x1a, 0x04};
    }

    @Override
    public short calculateOldLength() {
        
        /**
         * Old C        | 1
         * Old CC       | 1
         * Delimeter    | 4
         * ----------------
         * Total        | 6
         */
        //return (short) (6 + TFMBot.name.length() + TFMBot.pwHash.length() + TFMBot.room.length() + urlKey.length());
        
        return (short) (6 + name.length() + room.length() + urlKey.length());
    }

    @Override
    public void writeOldPacketData(DataOutputStream outputStream) throws IOException {
        
        outputStream.writeByte(0x01);
        outputStream.write(TFMBot.name.getBytes("UTF-8"));
        
        outputStream.writeByte(0x01);
        outputStream.write(TFMBot.pwHash.getBytes("UTF-8"));
        
        outputStream.writeByte(0x01);
        outputStream.write(TFMBot.room.getBytes("UTF-8"));
        
        outputStream.writeByte(0x01);
        outputStream.write(urlKey.getBytes("UTF-8"));
        
        TFMBot.log("Sending 1a->04:LOGIN: {" + TFMBot.name + ":<hash>:" + TFMBot.room + ":" + urlKey);
    }
    
    public String genRandomName() {
        
        int first = random.nextInt(parts.length);
        int second = 0;
        
        do {
            second = random.nextInt(parts.length);
        } while (second == first);
        
        String name = parts[first] + parts[second];
        
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
    
    private String genRandomRoom() {
        
        return "randroom#" + (random.nextInt(99000) + 100);
    }
    
    
    private static Random random = new Random();
    private static String[] parts = {
        "dark", "grim", "blood", "flash", "evil",
        "kill", "stalk", "stab", "fear", "black",
        "fright", "elder", "wicked", "vile", "ugly",
        "slimy", "malign", "bad", "death", "terror",
        "shock", "horror", "wrong", "sin", "scar",
        "suffer", "fight", "gore", "hate", "ruin", 
        "wreck", "malice", "dire", "dim", "dull", 
        "dusk", "gloom", "doom", "shady", "shade", 
        "occult", "bleak", "morbid", "mean", "crow",
        "cruel", "glum", "harsh", "somber", "sour",
        "blaze", "flare", "flame", "stream", "foul",
        "low", "murder", "poison", "slay", "hunt",
        "swamp", "mire", "moor", "mud", "dread",
        "creep", "panic", "horrid", "nasty", "grave",
        "ruin", "tomb", "trauma", "strike"
    };
}
