// 
// Decompiled by Procyon v0.5.30
// 

package pl.best241.ccitemdeposit.data;

import org.bukkit.plugin.Plugin;
import pl.best241.ccitemdeposit.CcItemDeposit;
import org.bukkit.Bukkit;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DataManager
{
    private static final ConcurrentHashMap<UUID, PlayerDepositData> cachedPlayers;
    
    public static void setPlayer(final UUID uuid, final PlayerDepositData data) {
        data.setNeedUpdate(false);
        DataManager.cachedPlayers.put(uuid, data);
    }
    
    public static PlayerDepositData getPlayer(final UUID uuid) {
        PlayerDepositData get = DataManager.cachedPlayers.get(uuid);
        if (get == null) {
            get = new PlayerDepositData(uuid, new HashMap<DepositItemType, Integer>());
            get.setNeedUpdate(true);
        }
        get.setNeedUpdate(false);
        return get;
    }
    
    public static ConcurrentHashMap<UUID, PlayerDepositData> getAllCachedPlayers() {
        return DataManager.cachedPlayers;
    }
    
    public static void saveDataTicker() {
        Bukkit.getScheduler().runTaskTimer((Plugin)CcItemDeposit.getPlugin(), (Runnable)new Runnable() {
            @Override
            public void run() {
                RedisBackend.saveAllPlayerDepositData();
            }
        }, 6000L, 6000L);
    }
    
    static {
        cachedPlayers = new ConcurrentHashMap<UUID, PlayerDepositData>();
    }
}
