package tfmbot.main;

import tfmbot.packets.Packet;

/**
 * This interface is used by the packet handler.
 * @author malte
 */
public interface Action {
    
    /**
     * Called when a packet is received and this Action is set to the packet name
     * @param packet The packet that has been received
     */
    public void onAction(Packet packet);
}
