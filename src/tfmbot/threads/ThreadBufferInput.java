package tfmbot.threads;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import tfmbot.main.TFMBot;

/**
 *
 * @author malte
 */
public class ThreadBufferInput extends Thread {
    
    public DataInputStream in;
    
    public ThreadBufferInput(DataInputStream in) {
        
        this.in = in;
        setDaemon(true);
    }
    
    @Override
    public void run() {
        
        while (true) {
            try {
                if (in.available() > 0) {
                    
                    //Read the first 4 bytes indicating the packet length, then allocate a byte buffer for the remaining bytes
                    int length = in.readInt();
                    byte[] buf = new byte[length];
                    
                    //Put packet length in buffer
                    buf[0] = (byte) (length >> 24);
                    buf[1] = (byte) ((length >> 16));
                    buf[2] = (byte) ((length >> 8));
                    buf[3] = (byte) (length);
                    
                    //Read data in buffer
                    int check = in.read(buf, 4, length-4);
                    if (check != length-4) {
                        TFMBot.log("Input: ", "[WARNING] ", "Could not read enough bytes (" + (length-4) + " expected, " + check + " read)");
                    }
                    
                    //Make packet public
                    synchronized (TFMBot.lock) {
                        TFMBot.inputBuffer.add(buf);
                    }
                }
            } catch (IOException ex) {
                TFMBot.log("Input: ", "[ERROR] ", "Unable to read input, halting...");
                return;
            }
            try {
                sleep(50);  //Reduce load
            } catch (InterruptedException ex) {
                Logger.getLogger(ThreadBufferInput.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
