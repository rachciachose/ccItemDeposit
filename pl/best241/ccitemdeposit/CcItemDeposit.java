// 
// Decompiled by Procyon v0.5.30
// 

package pl.best241.ccitemdeposit;

import org.bukkit.ChatColor;
import pl.best241.ccitemdeposit.inventory.InventoryManager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import pl.best241.ccitemdeposit.listeners.PlayerQuitListener;
import pl.best241.ccitemdeposit.listeners.PlayerKickListener;
import pl.best241.ccitemdeposit.listeners.PlayerJoinListener;
import pl.best241.ccitemdeposit.listeners.ItemLimitListener;
import pl.best241.ccitemdeposit.listeners.PubSubRecieveMessageListener;
import org.bukkit.event.Listener;
import pl.best241.ccitemdeposit.listeners.InventoryListener;
import pl.best241.ccitemdeposit.messages.MessagesData;
import org.bukkit.plugin.Plugin;
import pl.best241.ccitemdeposit.config.ConfigManager;
import pl.best241.ccitemdeposit.data.DataManager;
import pl.best241.ccitemdeposit.data.RedisBackend;
import org.bukkit.scheduler.BukkitTask;
import java.util.UUID;
import java.util.HashMap;
import org.bukkit.plugin.java.JavaPlugin;

public class CcItemDeposit extends JavaPlugin
{
    private static CcItemDeposit ccItemDeposit;
    private static final HashMap<UUID, BukkitTask> informedToOpen;
    
    public void onEnable() {
        CcItemDeposit.ccItemDeposit = this;
        RedisBackend.loadAllPlayerDepositData();
        DataManager.saveDataTicker();
        ConfigManager.run((Plugin)this);
        MessagesData.loadMessages((Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new InventoryListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new PubSubRecieveMessageListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new ItemLimitListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new PlayerJoinListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new PlayerKickListener(), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new PlayerQuitListener(), (Plugin)this);
        ItemLimitListener.runScanner();
    }
    
    public void onDisable() {
        RedisBackend.saveAllPlayerDepositData();
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String lable, final String[] args) {
        if (cmd.getName().equalsIgnoreCase("depozyt")) {
            if (sender instanceof Player) {
                final Player player = (Player)sender;
                if (args.length == 0) {
                    if (!CcItemDeposit.informedToOpen.containsKey(player.getUniqueId())) {
                        sender.sendMessage(MessagesData.getMessage("costMessage", "Aby otworzyc wpisz ponownie /depozyt. Pamietaj ze kosztuje to 5 sztabek!"));
                        final BukkitTask deleteTask = Bukkit.getScheduler().runTaskLater((Plugin)getPlugin(), () -> CcItemDeposit.informedToOpen.remove(player.getUniqueId()), 600L);
                        CcItemDeposit.informedToOpen.put(player.getUniqueId(), deleteTask);
                    }
                    else if (player.getInventory().contains(Material.GOLD_INGOT, 3)) {
                        player.getInventory().removeItem(new ItemStack[] { new ItemStack(Material.GOLD_INGOT, 3) });
                        final BukkitTask deleteTaskLocal = CcItemDeposit.informedToOpen.remove(player.getUniqueId());
                        deleteTaskLocal.cancel();
                        player.openInventory(InventoryManager.buildDepositInventory(player));
                    }
                    else {
                        sender.sendMessage(MessagesData.getMessage("youHaveNotEnoughItems", "Nie masz wystarczajacej liczby itemow!"));
                        final BukkitTask deleteTaskLocal = CcItemDeposit.informedToOpen.remove(player.getUniqueId());
                        deleteTaskLocal.cancel();
                    }
                }
            }
            else {
                sender.sendMessage(MessagesData.getMessage("commandOnlyForPlayers", ChatColor.DARK_GRAY + " »" + ChatColor.RED + "Komenda tylko dla graczy!"));
            }
        }
        return false;
    }
    
    public static CcItemDeposit getPlugin() {
        return CcItemDeposit.ccItemDeposit;
    }
    
    static {
        informedToOpen = new HashMap<UUID, BukkitTask>();
    }
}
