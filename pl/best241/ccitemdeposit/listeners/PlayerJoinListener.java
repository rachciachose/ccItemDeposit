// 
// Decompiled by Procyon v0.5.30
// 

package pl.best241.ccitemdeposit.listeners;

import org.bukkit.event.EventHandler;
import java.util.UUID;
import org.bukkit.entity.Player;
import pl.best241.ccitemdeposit.data.DataManager;
import pl.best241.ccitemdeposit.data.RedisBackend;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.Listener;

public class PlayerJoinListener implements Listener
{
    @EventHandler
    public static void playerJoinListener(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();
        DataManager.setPlayer(uuid, RedisBackend.getPlayerDepositData(uuid));
    }
}
