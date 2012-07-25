package tfmbot.packets;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import tfmbot.main.TFMBot;
import tfmbot.main.TFMProtocol.Emotion;

/**
 *
 * @author malte
 */
public class Packet0801_Emotion extends Packet {

    public int mouseID;
    public Emotion emotion;
    
    public Packet0801_Emotion(byte[] bytes) {
        
        mouseID = ByteBuffer.allocate(4).put(bytes[6]).put(bytes[7]).put(bytes[8]).put(bytes[9]).getInt(0);
        emotion = Emotion.values()[bytes[bytes.length - 1]];
    }
    
    public Packet0801_Emotion(Emotion emotion) {
        
        this.emotion = emotion;
    }
    
    public Packet0801_Emotion(byte emotionVal) {
        try {
            this.emotion = Emotion.values()[emotionVal];
        } catch (Exception e) {
            this.emotion = Emotion.FACEPALM;
            TFMBot.log("Protocol: ", "[WARNING] ", "No emotion found for " + emotionVal + ", facepalming");
        }
    }
    
    @Override
    protected void writePacketData(DataOutputStream outputStream) throws IOException {
        outputStream.writeByte(emotion.ordinal());
    }

    @Override
    public String getName() {
        return "Emotion";
    }

    @Override
    public byte[] getCCC() {
        return new byte[]{0x08, 0x01};
    }

    @Override
    public int calculateLength() {
        /**
         * Length       | 4
         * Fingerprint  | 4
         * C            | 1
         * CC           | 1
         * Emotion      | 1
         * ----------------
         * Total        |11
         */
        return 11;
    }
    
}
