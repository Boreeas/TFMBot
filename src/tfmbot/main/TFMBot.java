/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tfmbot.main;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import tfmbot.events.*;
import tfmbot.events.globalevents.*;
import tfmbot.events.roomevents.*;
import tfmbot.events.mouseevents.*;
import tfmbot.main.TFMProtocol.Emotion;
import tfmbot.main.TFMProtocol.ServerType;
import tfmbot.packets.*;
import tfmbot.plugins.Plugin;
import tfmbot.plugins.PluginManager;
import tfmbot.threads.BotShutdownHook;
import tfmbot.threads.SchedulePong;
import tfmbot.threads.ThreadBufferInput;
import tfmbot.threads.ThreadGetKey;
import tfmbot.threads.ThreadKeepAlive;
import tfmbot.util.HashUtils;

/**
 *
 * @author malte
 */
public class TFMBot extends Thread {
    
    public static final Object lock = new Object();
    
    
    private static TFMBot instance;
    
    //Default connection address
    public static final String host = "37.59.43.48";
    public static final int port = 443;
    
    
    static final EnumMap<ServerType, Server> connected = new EnumMap<ServerType, Server>(ServerType.class);
    
    public static final HashMap<String, String> packetNames = new HashMap<String, String>();
    public static final HashMap<String, String> titleNames = new HashMap<String, String>();
    public static final HashMap<String, Action> actions = new HashMap<String, Action>();
    public static final HashMap<String, CommandHandler> commands = new HashMap<String, CommandHandler>();
    public static final HashMap<String, String> commandHelp = new HashMap<String, String>();
    private static HashMap<String, User> users = new HashMap<String, User>();
    public static final HashMap<Integer, Mouse> mice = new HashMap<Integer, Mouse>();
    
    public static final Set<String> availableTitles = new HashSet<String>();
    
    //This is not the real data, btw
    public static String name = "Bot";
    public static String password = "";
    public static String pwHash = "";
    public static String room = "1";
    public static String community = "en";
    
    public static short protocolVersion = 0;
    public static String key = "\"This is an empty key\"";
    
    public static final ArrayList<byte[]> inputBuffer = new ArrayList<byte[]>();
    
    public PluginManager pluginManager;
    
    public static void main(String[] args) throws UnknownHostException, IOException {
        
        Runtime.getRuntime().addShutdownHook(new BotShutdownHook());
        
        readConfig();
        
        fillLists();
        
        TFMBot bot = new TFMBot();
        instance = bot;
        
        ThreadGetKey sanky = new ThreadGetKey();
        sanky.start();
        bot.start();
    }

    
    private TFMBot() throws UnknownHostException, IOException {
        
        log("Setting up connection (Host: " + host + ", Port: " + port + ")");
        connected.put(ServerType.MAIN, new Server(host, port));
        
        log("Enabling plugins...");
        pluginManager = new PluginManager();
        pluginManager.loadAllPlugins();
    }
    
    @Override
    public void run() {
        try {
            makeConnection();
        } catch (IOException ex) {
            log("Connection: "  + "[ERROR] " + "Unable to make connection to " + host + ":" + port);
            return;
        }
        
        while (true) {
            
            synchronized (lock) {
                for (byte[] b: inputBuffer) {
                    try {
                        Packet p = TFMProtocol.parsePacket(b);
                        handlePacket(p);
                    } catch (Exception ex) {
                        Logger.getLogger(TFMBot.class.getName()).log(Level.SEVERE, null, ex);
                        return;
                    }
                }
                
                inputBuffer.clear();
            }
            try {
                sleep(50);  //Reduce load - The packet throtteling will be slowing us down anyways
            } catch (InterruptedException ex) {
                //Bleah
            }
        }
    }
    
    /**
     * Sets up the connection to the server. When control returns from this method, 
     * the bot should have joined the server and a random room
     */
    void makeConnection() throws IOException {
        
        log("Setting up input buffer");
        ThreadBufferInput tbi = new ThreadBufferInput(connected.get(ServerType.MAIN).getReader());
        tbi.start();
        
        log(new StringBuilder("Sending Packet1c01 (Handshake) to ").append(host).append(":").append(port).append(" with protocol version ").append(protocolVersion).toString());
        
        getConnectedServer(ServerType.MAIN).sendPacket(new Packet1c01_Handshake(protocolVersion));
    }
    
    /**
     * Returns the <code>Server</code> currently used as <code>type</code>
     * @param type The server type
     * @return The server currently used as <code>type</code>
     */
    public Server getConnectedServer(ServerType type) {
        
        Server result = connected.get(type);
        if (result == null) {
            throw new RuntimeException("Not connected to " + type.toString().toLowerCase() + " yet");
        }
        
        return result;
    }
    
    /**
     * Takes action based off the packet received
     * @param packet The packet received
     */
    public void handlePacket(Packet packet) {
        
        if (packet == null) return;
        
        Action action = actions.get(packet.getName());
        if (action != null) action.onAction(packet);
    }
    
    //<editor-fold defaultstate="collapsed" desc="Chatting methods (Room chat/PM/Mod messages)">
    /**
     * Sends a message to the current room
     * @param message the message to send
     */
    public void sendChatMessage(String message) {
        
        getConnectedServer(ServerType.BULLE).sendPacket(new Packet0606_Message(message.replaceAll("<", "&lt;")));
        log("","[" + name + "] ", message);
    }
    
    /**
     * Sends multiple chat messages to the current room
     * @param messages The messages to send
     */
    public void sendChatMessage(String[] messages) {
        
        for (String s: messages) sendChatMessage(s);
    }
    
    /**
     * Sends multiple messages to the current room
     * @param newlineSeperatedMessage The messages as a \n newline separated String
     */
    public void sendNewlineChatMessage(String newlineSeperatedMessage) {
        
        for (String s: newlineSeperatedMessage.split("\n")) sendChatMessage(s);
    }
    
    /**
     * Sends a private message to <code>target</code>
     * @param target The player that should receive the message
     * @param message The message to send
     */
    public void sendPrivateMessage(String target, String message) {
        
        getConnectedServer(ServerType.MAIN).sendPacket(new Packet0607_PrivateMessage(target, message.replaceAll("<", "&lt;")));
        log("[" + target + "] <- ", "[" + name + "] ", message);
    }
    
    /**
     * Sends multiple messages to <code>target</code>
     * @param target The player that should receive the messages
     * @param messages The messages to send
     */
    public void sendPrivateMessage(String target, String[] messages) {
        
        for (String s: messages) sendPrivateMessage(target, s);
    }
    
    /**
     * Sends multiple messages to <code>target</code>
     * @param target The player that should receive the messages
     * @param newlineSeperatedMessage The messages as a \n newline separated String
     */
    public void sendNewlinePrivateMessage(String target, String newlineSeperatedMessage) {
        
        for (String s: newlineSeperatedMessage.split("\n")) sendPrivateMessage(target, s);
    }
    
    /**
     * Sends a mod message
     * @param global If the message will be a global message or not
     * @param message The message to send
     */
    public void sendModMessage(boolean global, String message) {
        
        getConnectedServer(ServerType.MAIN).sendPacket(new Packet060a_ModMessage(global, message));
    }
    
    /**
     * Sends a mod message to the current room
     * @param message The message to send
     */
    public void sendLocalModMessage(String message) {
        
        sendModMessage(false, message);
    }
    
    /**
     * Sends multiple moderation messages to the current room
     * @param messages The messages to send
     */
    public void sendLocalModMessages(String[] messages) {
        
        for (String s: messages) sendModMessage(false, s);
    }
    //</editor-fold>
    
    /**
     * Makes the bot act out an emotion
     * @param emotion The emotion to send
     * @throws IOException If an IOException occurred
     */
    public void sendEmotion(Emotion emotion) throws IOException {
        
        getConnectedServer(ServerType.BULLE).sendPacket(new Packet0801_Emotion(emotion));
        log("I'm " + emotion.toString().toLowerCase() + "ing");
    }
    
    /**
     * Sends a chat message to the client that will not be displayed and only return a confirmation when executed successfully.
     * This method blocks for writing, but not receiving the answer.
     * If, and when an answer will come is uncertain.
     * @param target The user to challenge
     * @throws IOException 
     */
    public void sendOnlineChallenge(String target) throws IOException {
        
        getConnectedServer(ServerType.MAIN).sendPacket(new Packet0607_PrivateMessage(target, "<"));
    }
    
    //<editor-fold defaultstate="collapsed" desc="Logging methods">
    /**
     * Logs a message with prefix "Bot: " and level "[INFO] "<\br>
     * This is a convenience method for log(String prefix, String level, String message) and the same
     * as log("Bot: ", "[INFO] ", message)
     *
     * @param message The message to send.
     * @see #log(String prefix, String level, String message)
     */
    public static void log(String message) {
        
        log("Bot: ", "[INFO] ", message);
    }
    
    /**
     * Logs a message to System.err
     * @param prefix The prefix for the message
     * @param level The level of the message
     * @param message The message to display
     */
    public static void log(String prefix, String level, String message) {
        
        System.err.println(new StringBuilder(prefix).append(level).append(message).toString());
    }
    //</editor-fold>
    
    /**
     * Returns the static global instance of the bot
     * 
     * @return the static global instance of the bot 
     */
    public static TFMBot getInstance() {
        
        if (instance == null) {
            
            throw new RuntimeException("Bot not yet instantiated");
        }
        
        return instance;
    }
    
    /**
     * Registers a command to be executed
     * @param command The string representation of the command
     * @param commandHandler The handler to be executed when the command is called
     * @throws CommandAlreadyRegisteredException If another plugin already uses this command
     */
    public static void registerCommand(String command, CommandHandler commandHandler) throws CommandAlreadyRegisteredException {
        
        if (commands.containsKey(command.toLowerCase())) {
            throw new CommandAlreadyRegisteredException(command);
        }
        
        commands.put(command, commandHandler);
    }
    
    /**
     * Returns the user for <code>name</code>, or creates a new one if none exists
     * @param name The name of the user
     * @return The user with name <code>name</code>
     */
    public static User getUser(String name) {
        
        User user = users.get(name.toLowerCase());
        if (user == null) {
            user = new User(name);
            users.put(name, user);
        }
        
        return user;
    }
    
   
    //<editor-fold defaultstate="collapsed" desc="Put packet codes / packet names">
    private static void setupPacketList() {
        
        //Old protocol packets
        packetNames.put("0101_0408", "_ShamanTurned");
        packetNames.put("0101_0409", "_Crouch");
        packetNames.put("0101_040a", "_KeepAlive");
        packetNames.put("0101_040c", "_Unk1");
        packetNames.put("0101_040d", "_Unk2");
        packetNames.put("0101_040f", "_DeleteConjure");
        packetNames.put("0101_0505", "_NewMap");
        packetNames.put("0101_0506", "_MapTime");
        packetNames.put("0101_0507", "_SpawnAnchor");
        packetNames.put("0101_0508", "_BeginSummon");
        packetNames.put("0101_0509", "_EndSummon");
        packetNames.put("0101_050a", "_Countdown");
        packetNames.put("0101_0513", "_GetCheese");
        packetNames.put("0101_0515", "_EnterRoom");
        packetNames.put("0101_0517", "_Snowball");
        packetNames.put("0101_0611", "_ReduceTime");
        packetNames.put("0101_061a", "_Command");
        packetNames.put("0101_0805", "_MouseDeath");
        packetNames.put("0101_0806", "_MouseEnterHole");
        packetNames.put("0101_0807", "_MouseLeftRoom");
        packetNames.put("0101_0808", "_MouseJoinedRoom");
        packetNames.put("0101_0809", "_RoomPlayers");
        packetNames.put("0101_080f", "_Titles");
        packetNames.put("0101_080e", "_Title");
        packetNames.put("0101_0811", "_Saves");
        packetNames.put("0101_0814", "_Shaman");
        packetNames.put("0101_0815", "_Sync");
        packetNames.put("0101_0817", "_Unk4");
        packetNames.put("0101_1314", "_Easter");
        packetNames.put("0101_1a03", "_LoginError");
        packetNames.put("0101_1a04", "_Login");
        packetNames.put("0101_1a08", "_LoggedIn");
        packetNames.put("0101_1a16", "_Ad");
        packetNames.put("0101_1a1b", "_Fingerprint");
        
        //New protocol packets
        packetNames.put("0403", "_ObjectSync");
        packetNames.put("0404", "_Movement");
        packetNames.put("0514", "_ObjectSpawn");
        packetNames.put("0606", "_Message");
        packetNames.put("0607", "_PrivateMessage");
        packetNames.put("060a", "_ModMessage");
        packetNames.put("0801", "_Emotion");
        packetNames.put("0802", "_Language");
        packetNames.put("082d", "_Hearts");
        packetNames.put("1a1a", "_Pong");
        packetNames.put("1c01", "_Handshake");
        packetNames.put("1c0d", "_Email");
        packetNames.put("1c11", "_ClientInfo");
        packetNames.put("1c32", "_Verify");
        packetNames.put("2c01", "_BulleServer");
        packetNames.put("2c02", "_BulleFingerprint");
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Load titles from dropbox file">
    private static void setupTitleList() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new URL("http://dl.dropbox.com/u/31575034/Other/TFM/ParseTitles/titles").openStream()));
            String input;
            while ((input = reader.readLine()) != null) {
                
                if (!input.equalsIgnoreCase("")) {
                    
                    titleNames.put(input.split(" ", 2)[0], input.split(" ", 2)[1]);
                }
                
            }
            
            reader.close();
        } catch (IOException ex) {
            Logger.getLogger(TFMBot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //</editor-fold>
    
    private static void setupActionList() {
        
        //<editor-fold defaultstate="collapsed" desc="Put "EnterRoom" packet action">
        actions.put("EnterRoom", new Action() {
            
            @Override
            public void onAction(Packet packet) {
                Packet0101_0515_EnterRoom roomPacket = (Packet0101_0515_EnterRoom) packet;
                
                TFMBot.log("Entering room " + roomPacket.roomName);
                EventPump.fireEvent(new EnterRoomEvent(roomPacket.roomName));
                
            }
        });
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Put "NewMap" packet action">
        actions.put("NewMap", new Action() {
            
            @Override
            public void onAction(Packet packet) {
                
                final Packet0101_0505_NewMap newMapPacket = (Packet0101_0505_NewMap) packet;
                EventPump.fireEvent(new NewMapEvent(newMapPacket.mapCode, Integer.parseInt(newMapPacket.roundNumber), newMapPacket.numPlayers));
                
                TFMBot.getInstance().getConnectedServer(ServerType.MAIN).sendPacket(new Packet0101_040a_AntiAFK());
                
            }
        });
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Put "Fingerprint" packet action">
        actions.put("Fingerprint", new Action() {
            
            @Override
            public void onAction(Packet packet) {
                ThreadKeepAlive keepAlive = new ThreadKeepAlive();
                keepAlive.start();
                    
                log("Calculating fingerprint");
                TFMBot.getInstance().getConnectedServer(ServerType.MAIN).setFingerprintData(((Packet0101_1a1b_Fingerprint)packet).MDT, (short)((Packet0101_1a1b_Fingerprint)packet).CMDTEC);
                    
                log("Logging in...");
                TFMBot.getInstance().getConnectedServer(ServerType.MAIN).sendPacket(new Packet0802_Language());
                TFMBot.getInstance().getConnectedServer(ServerType.MAIN).sendPacket(new Packet1c11_ClientInfo());
                TFMBot.getInstance().getConnectedServer(ServerType.MAIN).sendPacket(new Packet0101_1a04_Login());
            }
        });
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Put "BulleFingerprint" packet action">
        actions.put("BulleFingerprint", new Action() {
            
            @Override
            public void onAction(Packet packet) {
                
                log("Connection: ", "[INFO] ", "Successfull login to bulle server");
                
                TFMBot.getInstance().getConnectedServer(ServerType.BULLE).setFingerprintData(((Packet2c02_BulleFingerprint)packet).MDT, ((Packet2c02_BulleFingerprint)packet).CMDTEC);
            }
        });
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Put "Pong" packet action">
        actions.put("Pong", new Action() {
            
            @Override
            public void onAction(Packet packet) {
                
                new SchedulePong((Packet1a1a_Pong)packet).start();
            }
        });
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Put "LoginError" packet action">
        actions.put("LoginError", new Action() {

            @Override
            public void onAction(Packet packet) {
                log("Connection: ", "[ERROR] ", "Someone or something is blocking the login (Already logged in?)");
            }
        });
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Put "BulleServer" packet action">
        actions.put("BulleServer", new Action() {
            
            @Override
            public void onAction(Packet packet) {
                Packet2c01_BulleServer bulle = (Packet2c01_BulleServer)packet;
                
                try {
                    connected.put(ServerType.BULLE, new Server(bulle.ip, port));
                    log("Connection: ", "[INFO] ", "Connection to " + bulle.ip + " established");
                    
                    ThreadBufferInput tbi = new ThreadBufferInput(TFMBot.getInstance().getConnectedServer(ServerType.BULLE).getReader());
                    tbi.start();
                    
                    TFMBot.getInstance().getConnectedServer(ServerType.BULLE).sendPacket(bulle);
                } catch (UnknownHostException ex) {
                    log("Connection: ", "[ERROR] ", "Couldn't connect to " + bulle.ip + ": Unknown host");
                } catch (IOException ex) {
                    log("Connection: ", "[ERROR] ", "IOException while establishing connection to bulle server, dumping stack trace...");
                    ex.printStackTrace();
                }
            }
        });
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Put "Title" packet action">
        actions.put("Title", new Action() {
            
            @Override
            public void onAction(Packet packet) {
                
                log("Unlocked title " + titleNames.get(((Packet0101_080e_Title)packet).unlocked) + " (" + ((Packet0101_080e_Title)packet).unlocked + ")");
                EventPump.fireEvent(new TitleUnlockedEvent(((Packet0101_080e_Title)packet).unlocked));
            }
        });
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Put "Message" packet action">
        actions.put("Message", new Action() {
            
            @Override
            public void onAction(Packet packet) {
                
                Packet0606_Message msgPacket = (Packet0606_Message) packet;
                log("", "[" + msgPacket.name + "] ", msgPacket.message);
                EventPump.fireEvent(new ChatEvent(msgPacket.name, msgPacket.message));

            }
        });
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Put "MouseDeath" packet action">
        actions.put("MouseDeath", new Action() {
            
            @Override
            public void onAction(Packet packet) {
                
                Packet0101_0805_MouseDeath deathPacket = (Packet0101_0805_MouseDeath) packet;
                EventPump.fireEvent(new MouseDeathEvent(deathPacket.mouseID, deathPacket.miceLeft, deathPacket.points));
            }
        });
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Put "MouseLeftRoom" packet action">
        actions.put("MouseLeftRoom", new Action() {
            
            @Override
            public void onAction(Packet packet) {
                
                Packet0101_0807_MouseLeftRoom leftPacket = (Packet0101_0807_MouseLeftRoom) packet;
                EventPump.fireEvent(new MouseLeftRoomEvent(Integer.parseInt(leftPacket.mouseID), leftPacket.mouseName));
            }
        });
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Put "MouseJoinedRoom" event">
        actions.put("MouseJoinedRoom", new Action() {
            
            @Override
            public void onAction(Packet packet) {
                
                Packet0101_0808_MouseJoinedRoom joinPacket = (Packet0101_0808_MouseJoinedRoom) packet;
                EventPump.fireEvent(new MouseJoinedRoomEvent(joinPacket.mouse));
            }
        });
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Put "Emotion" packet action">
        actions.put("Emotion", new Action() {
            
            @Override
            public void onAction(Packet packet) {
                
                Packet0801_Emotion emoPacket = (Packet0801_Emotion) packet;
                EventPump.fireEvent(new EmotionEvent(emoPacket.mouseID, emoPacket.emotion));
            }
        });
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Put "PrivateMessage" packet action">
        actions.put("PrivateMessage", new Action() {
            
            @Override
            public void onAction(Packet packet) {
                
                Packet0607_PrivateMessage msgPacket = (Packet0607_PrivateMessage) packet;
                
                //Create a new user for every nick that pms us
                if (!users.containsKey(msgPacket.name.toLowerCase()) || users.get(msgPacket.name.toLowerCase()) == null) users.put(msgPacket.name.toLowerCase(), new User(msgPacket.name));
                
                
                
                if (!msgPacket.isVerification) {
                    String command = msgPacket.message.split(" ", 2)[0];
                    String[] args;
                    try {
                        args = msgPacket.message.split(" ", 2)[1].split(" ");
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        args = new String[0];
                    }
                    CommandHandler handler = commands.get(command);

                    //We have a command for that -> Execute it
                    if (handler != null) {
                        handler.onCommand(msgPacket.name, command, args);
                    }
                    
                    log("[" + msgPacket.name + "] -> ", "[" + name + "] ", msgPacket.message);
                    EventPump.fireEvent(new PrivateMessageEvent(msgPacket.name, msgPacket.message, msgPacket.community, msgPacket.isMod));
                } else {
                    if (msgPacket.message.trim().equalsIgnoreCase("<")) {
                        EventPump.fireEvent(new OnlineChallengeResponseEvent(msgPacket.name));
                    }
                }
            }
        });
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Put "MouseEnterHole" packet event">
        actions.put("MouseEnterHole", new Action() {
            
            @Override
            public void onAction(Packet packet) {
                
                Packet0101_0806_MouseEnterHole enterPacket = (Packet0101_0806_MouseEnterHole) packet;
                EventPump.fireEvent(new MouseEnterHoleEvent(enterPacket.mouseID, enterPacket.place, enterPacket.playersLeft, enterPacket.points, enterPacket.seconds));
            }
        });
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Put "ModMessage" packet event">
        actions.put("ModMessage", new Action() {
            
            @Override
            public void onAction(Packet packet) {
                
                Packet060a_ModMessage modpacket = (Packet060a_ModMessage) packet;
                
                log("[" + modpacket.name + "] ", "", modpacket.message);
                
                if (modpacket.global) {
                    EventPump.fireEvent(new GlobalMessageEvent(modpacket.name, modpacket.message));
                } else {
                    EventPump.fireEvent(new ModMessageEvent(modpacket.name, modpacket.message));
                }
            }
        });
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc=""ObjectSpawn" event">
        actions.put("ObjectSpawn", new Action() {
            
            @Override
            public void onAction(Packet packet) {
                
                Packet0514_ObjectSpawn objPacket = (Packet0514_ObjectSpawn) packet;
                EventPump.fireEvent(new ObjectSpawnEvent(objPacket.object, objPacket.trueX, objPacket.trueY, objPacket.rotation, objPacket.velocityX, objPacket.velocityY, objPacket.ghosted));
            }
        });
        //</editor-fold>
        
        actions.put("Unknown", new Action() {

            @Override
            public void onAction(Packet packet) {
                
                log("Unknown packet:" +  packet.getClass().getSimpleName());
                
            }
        });
    }
    
    private static void setupUserList() throws IOException {
        
        File userFile = new File(System.getProperty("user.dir") + "/files/users.data");
        if (!userFile.exists()) {
            
            userFile.getParentFile().mkdirs();
            userFile.createNewFile();
            log("No user list found, creating new user list...");
            users = new HashMap<String, User>();
            User me = new User("Boreeas");
            me.isOper = true;
            me.permissions.add("*");
            users.put("boreeas", me);
            return;
        }
        
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(userFile));
            users = (HashMap<String, User>) ois.readObject();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TFMBot.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EOFException ex) {
            log("No user list found, creating new user list...");
            users = new HashMap<String, User>();
            User me = new User("Boreeas");
            me.isOper = true;
            me.permissions.add("*");
            users.put("boreeas", me);
            return;
        }
    }
    
    private static void setupCommandList() {
        
        //<editor-fold defaultstate="collapsed" desc=""stop" command">
        commands.put("stop", new CommandHandler() {
            
            @Override
            public void onCommand(String sender, String command, String[] args) {
                
                if (users.get(sender.toLowerCase()).hasPermission("base.stop")) {
                    
                    log("Stop command invoked by " + sender + " at " + new Date().toString());
                    log("Closing sockets...");
                    for (Server server: connected.values()) {
                        server.close();
                    }
                    
                    log("Calling shutdown hooks...");
                    exit(0);
                } else {
                    TFMBot.getInstance().sendPrivateMessage(sender, "Not enough permissions");
                    
                }
            }
        });
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc=""say" command">
        commands.put("say", new CommandHandler() {
            
            @Override
            public void onCommand(String sender, String command, String[] args) {
                if (users.get(sender.toLowerCase()).hasPermission("base.say")) {
                    
                    StringBuilder buffer = new StringBuilder();
                    for (String s: args) {
                        buffer.append(s).append(" ");
                    }
                    
                    TFMBot.getInstance().sendChatMessage(buffer.toString().trim());
                } else {
                    
                    TFMBot.getInstance().sendPrivateMessage(sender, "Not enough permissions");
                }
            }
        });
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc=""add" command">
        commands.put("add", new CommandHandler() {
            
            @Override
            public void onCommand(String sender, String command, String[] args) {
                
                if (users.get(sender.toLowerCase()).hasPermission("base.add")) {
                    
                    if (args.length < 2) {
                        TFMBot.getInstance().sendPrivateMessage(sender, "Missing parameters: add <user> <perms>");
                    }
                    
                    if (users.get(sender.toLowerCase()).hasPermission(args[1].toLowerCase())) {
                        User user = users.get(args[0].toLowerCase());
                        if (user == null) {
                            user = new User(args[0]);
                            users.put(args[0].toLowerCase(), user);
                        }
                        user.permissions.add(args[1].toLowerCase());
                        TFMBot.getInstance().sendPrivateMessage(sender, "Added " + args[1] + " to " + args[0]);
                    } else {
                        TFMBot.getInstance().sendPrivateMessage(sender, "You cannot add permissions you do not have yourself");

                    }
                } else {
                    TFMBot.getInstance().sendPrivateMessage(sender, "Not enough permissions");
                }
            }
        });
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc=""remove" command">
        commands.put("remove", new CommandHandler() {
            
            @Override
            public void onCommand(String sender, String command, String[] args) {
                
                if (users.get(sender.toLowerCase()).hasPermission("base.remove")) {
                    
                    if (args.length < 2) {
                            TFMBot.getInstance().sendPrivateMessage(sender, "Missing parameters: remove <user> <perms>");

                    }
                    
                    if (users.get(sender.toLowerCase()).hasPermission(args[1].toLowerCase())) {
                        User user = users.get(args[0].toLowerCase());
                        if (user == null) {
                            return;
                        }
                        user.permissions.remove(args[1].toLowerCase());
                        TFMBot.getInstance().sendPrivateMessage(sender, "Removed " + args[1] + " from " + args[0]);
                    } else {
                        TFMBot.getInstance().sendPrivateMessage(sender, "You cannot remove permissions you do not have yourself");

                    }
                } else {
                        TFMBot.getInstance().sendPrivateMessage(sender, "Not enough permissions");
                }
            }
        });
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc=""license" command">
        commands.put("license", new CommandHandler() {
            
            @Override
            public void onCommand(String sender, String command, String[] args) {
                    TFMBot.getInstance().sendPrivateMessage(sender, "TFMBot by Boreeas is licensed under the Creative Commons Attribution-ShareAlike 3.0 Unported License.");
                    TFMBot.getInstance().sendPrivateMessage(sender, "See https://creativecommons.org/licenses/by-sa/3.0/ for more information.");

            }
        });
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc=""help" command">
        commands.put("help", new CommandHandler() {
            
            @Override
            public void onCommand(String sender, String command, String[] args) {
            
                if (args.length == 0) {
                    
                    TFMBot.getInstance().sendPrivateMessage(sender, "TFMBot framework by Boreeas. Listing plugins...");
                    StringBuilder pluginList = new StringBuilder("core");
                    for (Plugin plugin: TFMBot.getInstance().pluginManager.getLoadedPlugins()) {
                            
                        pluginList.append(", ").append(plugin.getName());
                    }
                    TFMBot.getInstance().sendPrivateMessage(sender, pluginList.toString());
                    TFMBot.getInstance().sendPrivateMessage(sender, "Use \"help <plugin>\" for more information");
                } else if (args[0].equalsIgnoreCase("core")) {
                       
                    //<editor-fold defaultstate="collapsed" desc="Core help">
                        if (args.length == 1) {
                            
                            //Core plugin help
                            TFMBot.getInstance().sendPrivateMessage(sender, ">>> CORE overview:");
                            TFMBot.getInstance().sendPrivateMessage(sender, "Core offers basic functionality for using the bot. The following commands are availlable:");
                            TFMBot.getInstance().sendPrivateMessage(sender, "say, stop, add, remove, join, help, license");
                            TFMBot.getInstance().sendPrivateMessage(sender, "Use \"help core [command]\" for more information on a command");
                        } else {
                            
                            //Core command help
                            String help = commandHelp.get(args[1].toLowerCase());
                            if (help == null) {
                                
                                TFMBot.getInstance().sendPrivateMessage(sender, ">>> CORE No command by name \"" + args[1] + "\".");
                            } else {
                                
                                TFMBot.getInstance().sendNewlinePrivateMessage(sender, help);
                            }
                        }
                        //</editor-fold>
                } else {
                        
                    Plugin plugin = null;
                    for (Plugin loaded: TFMBot.getInstance().pluginManager.getLoadedPlugins()) {
                        if (loaded.getName().equalsIgnoreCase(args[0])) {
                            plugin = loaded;
                            break;
                        }
                    }
                        
                    if (plugin == null) {
                        //No plugin by that name
                        TFMBot.getInstance().sendPrivateMessage(sender, "No plugin named \"" + args[0] + "\"");
                    } else {
                           
                        if (args.length == 1) {
                                
                            TFMBot.getInstance().sendPrivateMessage(">>> ", plugin.getName() + " overview:");
                            TFMBot.getInstance().sendNewlinePrivateMessage(sender, plugin.getDescription());
                            TFMBot.getInstance().sendPrivateMessage(sender, "Use \"help " + args[0] + " [command]\" for more information on a command");
                        } else {
                                
                            String help = plugin.getHelp(args[1].toLowerCase());
                            if (help == null) {
                                TFMBot.getInstance().sendPrivateMessage(sender, "No command named \"" + args[1].toLowerCase() + "\" in " + plugin.getName());
                            } else {
                                    
                                TFMBot.getInstance().sendPrivateMessage(sender, ">>> " + plugin.getName() + " \"" + args[1].toLowerCase() + "\" command:");
                                TFMBot.getInstance().sendNewlinePrivateMessage(sender, help);

                            }
                        }
                    }
                }
            }
        });
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc=""join" command">
        commands.put("join", new CommandHandler() {
            
            @Override
            public void onCommand(String sender, String command, String[] args) {
                
                if (users.get(sender.toLowerCase()).hasPermission("base.join")) {
                        
                    if (args.length >= 1) {
                        TFMBot.getInstance().getConnectedServer(ServerType.MAIN).sendPacket(new Packet0101_061a_Command("room " + args[0]));
                        TFMBot.getInstance().sendPrivateMessage(sender, "Joined room " + args[0]);
                    } else {
                        TFMBot.getInstance().sendPrivateMessage(sender, "Not enough arguments: join <room>");
                    }
                } else {
                    TFMBot.getInstance().sendPrivateMessage(sender, "Not enough permissions");
                }
            }
        });
        //</editor-fold>
    }
    
    private static void setupCommandHelpList() {
        
        commandHelp.put("help", ">>> CORE \"help\" command:\n"
                                + "SYNOPSIS: This command offers a basic overview over the bot's plugins and commands.\n"
                                + "SYNTAX: help [plugin [command]]");
        
        commandHelp.put("license", ">>> CORE \"license\" command:\n"
                                + "SYNOPSIS: This command displays the license of the bot.\n"
                                + "SYNTAX: license");
        
        commandHelp.put("stop", ">>> CORE \"stop\" command:\n"
                                + "SYNOPSIS: This command halts the bot.\n"
                                + "SYNTAX: stop\n"
                                + "PERMISSIONS: base.stop");
        
        commandHelp.put("say", ">>> CORE \"say\" command:\n"
                                + "SYNOPSIS: This command broadcasts a message to the room the bot is currently in.\n"
                                + "SYNTAX: say <message>\n"
                                + "PERMISSIONS: base.say");
        
        commandHelp.put("add", ">>> CORE \"add\" command:\n"
                                + "SYNOPSIS: This command adds permissions to a bot user. Users can not give permissions they don't own themselves.\n"
                                + "SYNTAX: add <user> <permission>\n"
                                + "PERMISSIONS: base.add");
        
        commandHelp.put("remove", ">>> CORE \"remove\" command:\n"
                                + "SYNOPSIS: This command removes permissions from a bot user. Users can not take permissions they don't own themselves.\n"
                                + "SYNTAX: remove <user> <permissions>\n"
                                + "PERMISSIONS: base.remove");
        
        commandHelp.put("join", ">>> CORE \"join\" command:\n"
                                + "SYNOPSIS: This command forces the bot to join another room.\n"
                                + "SYNTAX: join <room>\n"
                                + "PERMISSIONS: base.join");
    }
    
    
    public static void fillLists() {
        
        log("Filling packet list...");
        setupPacketList();
        
        log("Filling title list...");
        setupTitleList();
        
        log("Filling action list...");
        setupActionList();
        
        log("Filling command list...");
        setupCommandList();
        
        log("Filling command help list...");
        setupCommandHelpList();
        
        log("Filling user list...");
        try {
            setupUserList();
        } catch (IOException ex) {
            Logger.getLogger(TFMBot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void readConfig() {
        try {
            Properties config = new Properties();
            config.load(new FileInputStream(System.getProperty("user.dir") + "/config.properties"));
            
            readConfigurationFile(config);
        } catch (FileNotFoundException ex) {
            try {
                new File(System.getProperty("user.dir") + "/config.properties").createNewFile();
                Properties config = new Properties();
                config.load(new FileInputStream(System.getProperty("user.dir") + "/config.properties"));
                
                putDefaultBotConfiguration(config);
            } catch (IOException ex1) {
                Logger.getLogger(TFMBot.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (IOException ioe) {
            log("Bot: ", "[FATAL] ", "Could not read data, quitting to prevent accidental override of settings");
            exit(1);
        }
    }
    
    private static void putDefaultBotConfiguration(Properties emptyConfigFile) throws IOException {
        
        log("No (or incomplete) configuration file found, creating new one...");
        emptyConfigFile.put("nick", "ChangeMe");
        emptyConfigFile.put("pass", "ChangeMe");
        emptyConfigFile.put("community", "en");
        emptyConfigFile.put("room", "1");
        emptyConfigFile.store(new FileOutputStream(System.getProperty("user.dir") + "/config.properties"), "Bot configuration file.");
   
        log("Please edit the file and restart the bot.");
        exit(0);
    }
    
    private static void readConfigurationFile(Properties configFile) throws IOException {
        
        String name = configFile.getProperty("nick");
        String pass = configFile.getProperty("pass");
        String comm = configFile.getProperty("community");
        String room = configFile.getProperty("room");
        
        if (name == null || pass == null || comm == null || room == null) putDefaultBotConfiguration(configFile);
        
        TFMBot.name = name;
        TFMBot.password = pass;
        TFMBot.pwHash = HashUtils.SHA256(pass);
        TFMBot.community = comm;
        /*TFMBot.community = (comm.equalsIgnoreCase("en")) ? TFMProtocol.Community.ENGLISH :
                           (comm.equalsIgnoreCase("fr")) ? TFMProtocol.Community.FRENCH :
                           (comm.equalsIgnoreCase("br")) ? TFMProtocol.Community.BRAZILIAN :
                           (comm.equalsIgnoreCase("cn")) ? TFMProtocol.Community.CHINESE :
                           (comm.equalsIgnoreCase("ru")) ? TFMProtocol.Community.RUSSIAN :
                           (comm.equalsIgnoreCase("es")) ? TFMProtocol.Community.SPANISH :
                           (comm.equalsIgnoreCase("tr")) ? TFMProtocol.Community.TURKISH :
                           (comm.equalsIgnoreCase("vk")) ? TFMProtocol.Community.VK : TFMProtocol.Community.ENGLISH;*/
        TFMBot.room = room;
    }
    
    private static void exit(int status) {
        
        String s = "Stopping ";
        s += (status == 0) ? "normally..." : "abnormally...";
        log(s);
        try {
            saveFiles();
        } catch (IOException ex) {
            log("Shutdown: ", "[ERROR] ", " Error saving files, possible loss of data");
            ex.printStackTrace();
        }
        System.exit(status);
    }
    
    private static void saveFiles() throws IOException {
        
        File dir = new File(System.getProperty("user.dir") + "/files");
        
        File users = new File(dir, "/users.data");
        
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(users));
        oos.writeObject(TFMBot.users);
        oos.flush();
        oos.close();
        
        File mice = new File(dir, "/mice.data");
        
        oos = new ObjectOutputStream(new FileOutputStream(mice));
        oos.writeObject(TFMBot.mice);
        oos.flush();
        oos.close();
    }
}
