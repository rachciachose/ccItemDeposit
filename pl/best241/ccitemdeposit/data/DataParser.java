// 
// Decompiled by Procyon v0.5.30
// 

package pl.best241.ccitemdeposit.data;

import org.bukkit.craftbukkit.libs.com.google.gson.GsonBuilder;
import org.bukkit.craftbukkit.libs.com.google.gson.Gson;

public class DataParser
{
    public static Gson gson;
    
    public static String parseData(final PlayerDepositData data) {
        return DataParser.gson.toJson((Object)data);
    }
    
    public static PlayerDepositData parseRawDataToObject(final String rawData) {
        if (rawData == null) {
            return null;
        }
        return (PlayerDepositData)DataParser.gson.fromJson(rawData, (Class)PlayerDepositData.class);
    }
    
    static {
        DataParser.gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
    }
}
