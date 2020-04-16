// 
// Decompiled by Procyon v0.5.30
// 

package pl.best241.ccitemdeposit.messages;

import org.bukkit.ChatColor;
import java.util.Set;
import org.bukkit.plugin.Plugin;
import java.util.ArrayList;
import java.util.HashMap;

public class MessagesData
{
    private static MessagesConfig config;
    private static final HashMap<String, String> pathMessage;
    private static final HashMap<String, ArrayList<String>> pathMessageList;
    
    public static void loadMessages(final Plugin plugin) {
        (MessagesData.config = new MessagesConfig(plugin, "messages.yml")).saveDefaultConfig();
        MessagesData.config.reloadCustomConfig();
        final Set<String> keys = (Set<String>)MessagesData.config.getCustomConfig().getKeys(true);
        System.out.println(keys);
        MessagesData.pathMessage.clear();
        MessagesData.pathMessageList.clear();
    }
    
    public static String getMessage(final String path) {
        if (MessagesData.pathMessage.containsKey(path)) {
            return MessagesData.pathMessage.get(path);
        }
        final String message = MessagesData.config.getString(path);
        if (message == null) {
            return null;
        }
        MessagesData.pathMessage.put(path, message);
        return message;
    }
    
    public static String getMessage(final String path, final String defaultValue) {
        final String message = getMessage(path);
        if (message == null) {
            return ChatColor.translateAlternateColorCodes('&', defaultValue);
        }
        return message;
    }
    
    public static ArrayList<String> getMessageList(final String path) {
        if (MessagesData.pathMessageList.containsKey(path)) {
            return MessagesData.pathMessageList.get(path);
        }
        final ArrayList<String> list = (ArrayList<String>)MessagesData.config.getCustomConfig().getList(path);
        final ArrayList<String> coloredList = new ArrayList<String>();
        list.stream().forEach(message -> coloredList.add(ChatColor.translateAlternateColorCodes('&', message)));
        MessagesData.pathMessageList.put(path, coloredList);
        return coloredList;
    }
    
    static {
        pathMessage = new HashMap<String, String>();
        pathMessageList = new HashMap<String, ArrayList<String>>();
    }
}
