package tfmbot.plugins;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;
import tfmbot.main.TFMBot;

/**
 *
 * @author malte
 */
public class PluginManager {
    
    public Pattern match = Pattern.compile("\\.jar$");
    private final ArrayList<Plugin> loadedPlugins = new ArrayList<Plugin>();
    
    public void loadAllPlugins() {
        
        loadAllPlugins(System.getProperty("user.dir") + "/plugins");
    }
    
    public void loadAllPlugins(String pathToPluginFolder) {
        
        PluginLoader loader = new PluginLoader();
        
        File dir = new File(pathToPluginFolder);
        
        if (dir.isFile()) throw new RuntimeException("Cannot load all plugins from single file, must be dir");
        
        if (!dir.exists()) dir.mkdirs();
        
        String[] pluginJarNames = dir.list(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                
                return match.matcher(name).find();
            }
        });
        
        
        for (String fileName: pluginJarNames) {
            try {
                Plugin loadedPlugin = loader.loadPlugin(new File(dir, fileName));
                loadedPlugin.onEnable();
                loadedPlugins.add(loadedPlugin);
            } catch (IOException ex) {
                TFMBot.log("Plugins: ", "[ERROR] ", "Unable to load plugin " + fileName + ": IOException");
                ex.printStackTrace();
            }
        }
    }
    
    public void disableAllPlugins() {
        
        for (Plugin p: loadedPlugins) p.onDisable();
    }
    
    public ArrayList<Plugin> getLoadedPlugins() {
        
        return loadedPlugins;
    }
}
