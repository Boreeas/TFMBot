/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tfmbot.packets;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import tfmbot.main.TFMBot;

/**
 * This class provides a common interface for all packets, namely a method to 
 * write its data to the output stream and a name used for identification
 * @author malte
 */
public abstract class Packet {
    

    /**
     * Writes the packet data to the output stream by passing on <code>outputStream</code> 
     * to the implementation.<br />
     * This method takes care of flushing the stream and logging exceptions
     * @param outputStream The output stream to the target
     */
    public void write(DataOutputStream outputStream) {
        
        //Write default (length, fingerprint, c, cc), then pass to the implementation, then flush. Also log any uncaught exceptions
        try {
            outputStream.write(getCCC());
            
            writePacketData(outputStream);
            outputStream.flush();
        } catch (IOException ex) {
            tfmbot.main.TFMBot.log("Connection: ", "[ERROR] ", "Could not write data to server");
            ex.printStackTrace();
        }
    }
    
    
    /**
     * Writes the packet data to the output stream
     * @param outputStream The output stream to the target
     * @throws IOException If an exception ocurred while writing the data
     */
    protected abstract void writePacketData(DataOutputStream outputStream) throws IOException;
    
    /**
     * Returns the identifying name for this packet
     * @return the identifying name for this packet
     */
    public abstract String getName();
    
    /**
     * Returns the c and cc identifying codes for this packet
     * @return the c and cc identifying codes for this packet
     */
    public abstract byte[] getCCC();
    
    /**
     * Calculates the packet length based on the packets parameters
     * @return the packet length based on the packets parameters
     */
    public abstract int calculateLength();
}
