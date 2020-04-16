// 
// Decompiled by Procyon v0.5.30
// 

package pl.best241.ccitemdeposit.config;

import org.bukkit.configuration.file.FileConfiguration;
import java.io.File;
import java.util.HashMap;
import org.bukkit.plugin.Plugin;

public class ConfigManager
{
    private static Plugin plugin;
    private static final HashMap<String, Object> configData;
    
    public static void run(final Plugin plugin) {
        ConfigManager.plugin = plugin;
        final File fileConfig = new File(plugin.getDataFolder(), "config.yml");
        if (!fileConfig.exists()) {
            System.out.println("Nie znaleziono config.yml! Generowanie!");
            plugin.getConfig().options().copyDefaults(true);
            plugin.saveConfig();
        }
        System.out.println("Ladowanie configa!");
        plugin.getConfig();
    }
    
    public static FileConfiguration getConfig() {
        return ConfigManager.plugin.getConfig();
    }
    
    public static String getString(final String path, final String defaultValue) {
        final String value = getConfig().getString(path);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }
    
    public static int getInt(final String path) {
        final Integer value = getConfig().getInt(path);
        return value;
    }
    
    static {
        configData = new HashMap<String, Object>();
    }
}
