package tfmbot.plugins;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import tfmbot.main.TFMBot;

/**
 *
 * @author malte
 */
public class PluginLoader {
    
    
    public Plugin loadPlugin(File pluginFile) throws IOException {
        
        Plugin plugin = null;
        
        JarFile pluginJar = new JarFile(pluginFile);
        JarEntry config = pluginJar.getJarEntry("config.properties");
        
        if (config == null) {
            throw new RuntimeException("Missing config.properties");
        }
        
        InputStream in = pluginJar.getInputStream(config);
        Properties prop = new Properties(); 
        prop.load(in);
        
        String mainClass = prop.getProperty("main");
        
        if (mainClass == null) {
            throw new RuntimeException("Missing main class declaration");
        }
        
        try {
            URLClassLoader classLoader = new URLClassLoader(new URL[]{pluginFile.toURI().toURL()});
            Class<Plugin> plugClass = (Class<Plugin>) classLoader.loadClass(mainClass);
            plugin = plugClass.newInstance();
            in.close();
            return plugin;
        } catch (ClassNotFoundException ex) {
            TFMBot.log("Plugins: ", "[ERROR] ", "Can't load plugin " + pluginJar.getName() + ": Missing main class");
        } catch (IllegalAccessException ex) {
            Logger.getLogger(PluginLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(PluginLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
}
