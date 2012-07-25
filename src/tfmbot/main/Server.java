package tfmbot.main;

import java.util.ArrayList;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import tfmbot.main.TFMProtocol.ServerType;
import tfmbot.packets.Packet;

/**
 * Represents a connection to the server. Handles sending, receiving and 
 * throttling of packets
 * @author malte
 */
public class Server {
    
    private final static int MAX_PACKETS = 2;
    private int tokens;
    private ArrayList<Packet> queue = new ArrayList<Packet>();
    
    private Socket socket;
    
    private DataInputStream reader;
    private DataOutputStream writer;
    
    private short CMDTEC;
    private int[] MDT;
    
    /**
     * Creates a new connection to a server at <code>ip</code>:<code>port</code>
     * @param ip The ip of the server
     * @param port The port the server listens on
     * @throws IOException If an IOException occurred while connecting to the server
     */
    public Server(String ip, int port) throws IOException {
        
        this(new Socket(ip, port));
        
    }
    
    /**
     * Creates a new connection to <code>socket</code>
     * @param socket The socket to connect to
     * @throws IOException If an IOException occurred while connecting to the socket
     */
    public Server(final Socket socket) throws IOException {
        
        this.socket = socket;
        
        initStreams();
        
        //Set up token timer for token bukket implementation of the output stream
        Timer tokenTimer = new Timer(true);
        tokenTimer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                if (!queue.isEmpty()) {
                    try {
                        //Instead of adding tokens, empty the queue first
                        writePacket(queue.remove(0));
                    } catch (IOException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    if (tokens < MAX_PACKETS) {
                        //Add tokens if no packets are present and we have not reached the burst cap yet
                        tokens++;
                    }
                }
            }
        }, 0, 1000);
    }
    
    /**
     * Initializes the input and output streams
     * @throws IOException 
     */
    private void initStreams() throws IOException {
        
        reader = new DataInputStream(socket.getInputStream());
        writer = new DataOutputStream(socket.getOutputStream());
    }
    
    /**
     * Sends a packet to the server
     * @param packet the packet to send
     */
    public void sendPacket(Packet packet) {
        
        if (tokens > 0) {
            //Write packet only if tokens are present
            try {
                writePacket(packet);
                tokens--;
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        } else {
            //Queue packets otherwise
            queue.add(packet);
        }
        
    }
    
    /**
     * Writes the packet to the output stream. This takes care of length,
     * incomplete packets, the fingerprint, etc.
     */
    private void writePacket(Packet packet) throws IOException {
        
        //Protect against unfinished packets that might mess up the server
        int packetLen = packet.calculateLength();
        int size = writer.size();
        //The output stream size caps at Integer.MAX_VALUE
        int expectedOut = (Integer.MAX_VALUE - size < packetLen) ? Integer.MAX_VALUE : size + packetLen;
    
        try {
            //Try to write the header
            writer.writeInt(packetLen);
            writer.writeInt(fingerprint());
        } catch (SocketException socketException) {
            TFMBot.log("Server: ", "[ERROR] ", "Disconnected (reconnecting). (Time: " + new Date() + ")");
            
            //We lost the connection
            //Reconnect completely, clear all queued packets and tell the bot to start over
            synchronized (TFMBot.lock) {
                
                TFMBot.getInstance().getConnectedServer(ServerType.MAIN).close();
                TFMBot.getInstance().getConnectedServer(ServerType.BULLE).close();
                
                TFMBot.connected.put(ServerType.MAIN, new Server(TFMBot.host, TFMBot.port));
                TFMBot.inputBuffer.clear();
                
                TFMBot.getInstance().makeConnection();
            }
            return;
        }
        
        try {
            //If we are here, we should still be connected
            packet.write(writer);
        } finally {
            
            if (writer.size() < expectedOut) {
                
                while (writer.size() < expectedOut) {

                    writer.write(0);
                }
            }
        }
        
    }
    
    /**
     * Calculates the fingerprint needed for every packet
     * @return The new fingerprint
     */
    private int fingerprint() {
        
        if (MDT == null) return 0;    //We didn't receive fingerprint data yet
        
        int loc3 = (CMDTEC % 9000) + 1000;
            
        byte a = (byte)MDT[loc3 / 1000];
        byte b = (byte)MDT[(loc3 / 100) % 10];
        byte c = (byte)MDT[(loc3 / 10) % 10];
        byte d = (byte)MDT[loc3 % 10];
        
        
        CMDTEC++;
            
        return ByteBuffer.allocate(4).put(a).put(b).put(c).put(d).getInt(0);
    }
    
    /**
     * Returns the reader for the connection
     * @return the reader for the connection
     */
    DataInputStream getReader() {
        
        return reader;
    }
    
    /**
     * Updates the fingerprint data to match the last received fingerprint packet
     * @param MDT The MDT data
     * @param CMDTEC The CMDTEC data
     */
    public void setFingerprintData(int[] MDT, short CMDTEC) {
        
        this.MDT = MDT;
        this.CMDTEC = CMDTEC;
    }
    
    /**
     * Close the connection to the server
     */
    public void close() {
        try {
            reader.close();
            writer.close();
            socket.close();
        } catch (IOException ex) {
            TFMBot.log("Connection: ", "[ERROR] ", "Unable to close connections, program will not terminate gracefully");
        }
    }
    
}
