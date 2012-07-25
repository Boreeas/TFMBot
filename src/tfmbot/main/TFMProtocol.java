package tfmbot.main;

import java.lang.reflect.InvocationTargetException;
import tfmbot.packets.Packet;

/**
 * A class containing various enums and codes of the protocol.
 * @author malte
 */
public class TFMProtocol {
    
    /**
     * Parses the data received and returns the corresponding <code>Packet</code> object
     * 
     * @param bytes The data received
     * @return A new packet object based on <code>bytes</code>, or null if no matching packet was detected
     * @throws InstantiationException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws IllegalAccessException 
     */
    static Packet parsePacket(byte[] bytes) throws InstantiationException, IllegalArgumentException, InvocationTargetException, IllegalAccessException {
                
        
        String c = Integer.toHexString(bytes[4]);
        String cc = Integer.toHexString(bytes[5]);
        if (c.length() == 1) c = "0" + c;
        if (cc.length() == 1) cc = "0" + cc;
        
        if (c.equalsIgnoreCase("01") && cc.equalsIgnoreCase("01")) {
            
            c = c+cc;
            String cOld = Integer.toHexString(bytes[8]);
            String ccOld = Integer.toHexString(bytes[9]);
            if (cOld.length() == 1) cOld = "0" + cOld;
            if (ccOld.length() == 1) ccOld = "0" + ccOld;
            
            cc = new StringBuilder("_").append(cOld).append(ccOld).toString();
        }
        
        String byteName = new StringBuilder(c).append(cc).toString();
        String classname = "tfmbot.packets.Packet" + byteName + TFMBot.packetNames.get(byteName);
        
        
        //Return a new packet object with the specified data, or nul if it is an unknow packet
        try {
            return (Packet) Class.forName(classname).getDeclaredConstructor(bytes.getClass()).newInstance(bytes);
        } catch (ClassNotFoundException ex) {
            TFMBot.log("Connection: ", "[WARNING] ", new StringBuilder("Unknow packet detected: ").append(classname).toString());
            return null;
        } catch (NoSuchMethodException ex) {
            TFMBot.log("Connection: " + "[ERROR] " + "Can't parse packet " + classname + ": Missing constructor");
            return null;
        }
        
    }
    
    public enum Emotion {
        
        DANCE,
        
        LAUGH,
        
        CRY,
        
        KISS,
        
        MAD,
        
        CLAP,
        
        SLEEP,
        
        FACEPALM,
        
        SIT,
        
        CONFETTI;
    }
    
    public enum Community {
        
        ENGLISH,
        
        FRENCH,
        
        RUSSIAN,
        
        BRAZILIAN,
        
        SPANISH,
        
        CHINESE,
        
        TURKISH,
        
        VK;
    }
    
    public enum Rank {
        
        GUEST,
        
        PLAYER,
        
        PLAYERBOT,
        
        ARB,
        
        MODBOT,
        
        MOD,
        
        SUPERMOD,
        
        UNUSED_7,
        
        UNUSED_8,

        UNUSED_9,
        
        ADMIN;
    }
    
    public enum TribeRank {
        
        STOOGE,
        
        COOKER,
        
        SOLDIER,
        
        TREASURER,
        
        RECRUITER,
        
        HUNTERESS,
        
        INITIATED,
        
        SHAMAN_APPRENTICE,
        
        TRIBE_SHAMAN,
        
        SPRITUAL_CHIEF;
    }
    
    public enum ServerType {
        
        MAIN,
        
        BULLE;
    }
    
    public enum ObjectType {
        
        ARROW,
        
        SMALL_BOX,
        LARGE_BOX,
        
        SMALL_PLANK,
        LARGE_PLANK,
        
        @Deprecated
        HEAVY_BALL,
        BALL,
        
        TRAMPOLINE,
        
        @Deprecated
        SMALL_ROUGH_PLANK,
        @Deprecated
        LARGE_ROUGH_PLANK,
        
        ANVIL,
        
        ANCHOR_B,
        ANCHOR_B_CW,
        ANCHOR_B_CCW,
        
        ANCHOR_V,
        ANCHOR_V_CW,
        ANCHOR_V_CCW,
        
        CANNON_UP,
        CANNON_DOWN,
        CANNON_RIGHT,
        CANNON_LEFT,
        
        @Deprecated
        STICKY_BALL,
        
        ANCHOR_C,
        
        BOMB,
        
        SPIRIT,
        
        FAKE_CHEESE,
        
        BLUE_PORTAL,
        ORANGE_PORTAL,
        
        BALLOON,
        BALLOON_STATIC_RED,
        BALLOON_STATIC_GREEN,
        BALLOON_STATIC_YELLOW,
        
        RUNE,
        
        SNOW,
        
        VALENTINE_ARROW,
        
        TOTEM,
        
        TRANSFORMATION_SMALL_BOX,
        TRANSFORMATION_LARGE_BOX,
        TRANSFORMATION_ANVIL,
        TRANSFORMATION_SMALL_PLANK,
        TRANSFORMATION_LARGE_PLANK,
        TRANSFORMATION_MOUSE,
        
        BALLOON_ANCHOR,
        
        WINDMILL_ANCHOR;
    }
}
