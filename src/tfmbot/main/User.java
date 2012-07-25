package tfmbot.main;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author malte
 */
public class User implements Serializable {
    
    public static final long serialVersionUID = 280120122312L;
    
    public String name;
    public boolean isOper;
    public ArrayList<String> permissions = new ArrayList<String>();
    
    public User(String name) {
        
        this.name = name;
    }
    
    public boolean hasPermission(String node) {
        
        //All permissions given or user has current node
        if (isOper || permissions.contains("*") || permissions.contains(node)) return true;
        
        String[] nodes = node.split("\\.");
        String current = "";
        for (String s: nodes) {
            current += s + ".";
            if (permissions.contains(current + "*")) return true;
        }
        
        return false;
    }
}
