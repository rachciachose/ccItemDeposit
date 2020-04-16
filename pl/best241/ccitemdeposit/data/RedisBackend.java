// 
// Decompiled by Procyon v0.5.30
// 

package pl.best241.ccitemdeposit.data;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Iterator;
import java.util.Map;
import redis.clients.jedis.Jedis;
import java.util.HashMap;
import pl.best241.rdbplugin.JedisFactory;
import java.util.UUID;

public class RedisBackend
{
    public static PlayerDepositData getPlayerDepositData(final UUID uuid) {
        final Jedis jedis = JedisFactory.getInstance().getJedis();
        String value = null;
        try {
            value = jedis.hget("ccDeposit.playerDeposit", uuid.toString());
            JedisFactory.getInstance().returnJedis(jedis);
        }
        catch (Exception e) {
            JedisFactory.getInstance().returnBrokenJedis(jedis);
        }
        if (value == null) {
            return new PlayerDepositData(uuid, new HashMap<DepositItemType, Integer>());
        }
        final PlayerDepositData data = DataParser.parseRawDataToObject(value);
        data.setNeedUpdate(false);
        return data;
    }
    
    public static PlayerDepositData setPlayerDepositData(final PlayerDepositData data) {
        data.setNeedUpdate(false);
        final Jedis jedis = JedisFactory.getInstance().getJedis();
        jedis.hset("ccDeposit.playerDeposit", data.getUuid().toString(), DataParser.parseData(data));
        JedisFactory.getInstance().returnJedis(jedis);
        return data;
    }
    
    public static void loadAllPlayerDepositData() {
        final Jedis jedis = JedisFactory.getInstance().getJedis();
        final Map<String, String> uuidValue = (Map<String, String>)jedis.hgetAll("ccDeposit.playerDeposit");
        JedisFactory.getInstance().returnJedis(jedis);
        for (final String next : uuidValue.keySet()) {
            final UUID uuid = UUID.fromString(next);
            final String value = uuidValue.get(next);
            final PlayerDepositData data = DataParser.parseRawDataToObject(value);
            data.setNeedUpdate(false);
            DataManager.setPlayer(uuid, data);
        }
    }
    
    public static void saveAllPlayerDepositData() {
        final Jedis jedis = JedisFactory.getInstance().getJedis();
        final ConcurrentHashMap<UUID, PlayerDepositData> allCachedPlayers = DataManager.getAllCachedPlayers();
        for (final UUID nextUUID : allCachedPlayers.keySet()) {
            final PlayerDepositData nextData = allCachedPlayers.get(nextUUID);
            if (!nextData.isNeedUpdate()) {
                continue;
            }
            nextData.setNeedUpdate(false);
            final String parsedUUID = nextUUID.toString();
            final String parsedData = DataParser.parseData(nextData);
            DataManager.getAllCachedPlayers().put(nextUUID, nextData);
            jedis.hset("ccDeposit.playerDeposit", parsedUUID, parsedData);
            System.out.println("Hsetted ccDeposit.playerDepo");
        }
        JedisFactory.getInstance().returnJedis(jedis);
    }
    
    public static void savePlayerData(final UUID uuid) {
        final Jedis jedis = JedisFactory.getInstance().getJedis();
        final PlayerDepositData playerData = DataManager.getPlayer(uuid);
        playerData.setNeedUpdate(false);
        final String parsedUUID = uuid.toString();
        final String parsedData = DataParser.parseData(playerData);
        DataManager.setPlayer(uuid, playerData);
        jedis.hset("ccDeposit.playerDeposit", parsedUUID, parsedData);
        JedisFactory.getInstance().returnJedis(jedis);
    }
}
