// 
// Decompiled by Procyon v0.5.30
// 

package pl.best241.ccitemdeposit.listeners;

import org.bukkit.event.EventHandler;
import pl.best241.ccitemdeposit.data.RedisBackend;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.Listener;

public class PlayerKickListener implements Listener
{
    @EventHandler
    public static void playerKickListener(final PlayerKickEvent event) {
        RedisBackend.savePlayerData(event.getPlayer().getUniqueId());
    }
}
