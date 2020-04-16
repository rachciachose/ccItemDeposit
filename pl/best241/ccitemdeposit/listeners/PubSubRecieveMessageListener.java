// 
// Decompiled by Procyon v0.5.30
// 

package pl.best241.ccitemdeposit.listeners;

import org.bukkit.event.EventHandler;
import pl.best241.rdbplugin.pubsub.PubSub;
import pl.best241.cctools.CcTools;
import org.bukkit.plugin.Plugin;
import pl.best241.ccitemdeposit.messages.MessagesData;
import pl.best241.ccitemdeposit.CcItemDeposit;
import pl.best241.rdbplugin.events.PubSubRecieveMessageEvent;
import org.bukkit.event.Listener;

public class PubSubRecieveMessageListener implements Listener
{
    @EventHandler
    public static void pubSubRecieveMessageListener(final PubSubRecieveMessageEvent event) {
        if (event.getChannel().equals("reloadAllMessagesRequest")) {
            MessagesData.loadMessages((Plugin)CcItemDeposit.getPlugin());
            PubSub.broadcast("reloadAllMessagesResponse", CcTools.getPlugin().getName());
        }
    }
}
