// 
// Decompiled by Procyon v0.5.30
// 

package pl.best241.ccitemdeposit.listeners;

import org.bukkit.event.EventHandler;
import pl.best241.ccitemdeposit.data.RedisBackend;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.Listener;

public class PlayerQuitListener implements Listener
{
    @EventHandler
    public static void playerQuitListener(final PlayerQuitEvent event) {
        RedisBackend.savePlayerData(event.getPlayer().getUniqueId());
    }
}
