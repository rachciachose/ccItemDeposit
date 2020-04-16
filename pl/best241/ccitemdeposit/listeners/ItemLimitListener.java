// 
// Decompiled by Procyon v0.5.30
// 

package pl.best241.ccitemdeposit.listeners;

import pl.best241.cctools.components.Stun;
import pl.best241.cctools.components.ThrownTNT;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import pl.best241.ccitemdeposit.data.PlayerDepositData;
import pl.best241.ccitemdeposit.messages.MessagesData;
import org.bukkit.ChatColor;
import pl.best241.ccitemdeposit.data.DataManager;
import org.bukkit.inventory.Inventory;
import pl.best241.ccitemdeposit.inventory.InventoryManager;
import pl.best241.ccitemdeposit.data.DepositItemType;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.Plugin;
import org.bukkit.entity.Player;
import pl.best241.ccitemdeposit.CcItemDeposit;
import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.Listener;

public class ItemLimitListener implements Listener
{
    @EventHandler
    public static void onPickupItem(final PlayerPickupItemEvent event) {
        final Player player = event.getPlayer();
        Bukkit.getScheduler().runTask((Plugin)CcItemDeposit.getPlugin(), (Runnable)new Runnable() {
            @Override
            public void run() {
                ItemLimitListener.autoReduceItems(player);
            }
        });
    }
    
    @EventHandler
    public static void onInventoryClick(final InventoryClickEvent event) {
        final HumanEntity human = event.getWhoClicked();
        if (human instanceof Player) {
            final Player player = (Player)human;
            if (event.getClickedInventory() == null) {
                return;
            }
            if (((event.getAction() == InventoryAction.PLACE_SOME || event.getAction() == InventoryAction.PLACE_ONE || event.getAction() == InventoryAction.PLACE_ALL) && event.getClickedInventory().getType() == InventoryType.PLAYER && (isItemToReduce(event.getCurrentItem()) || isItemToReduce(event.getCursor()))) || (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY && !event.getClickedInventory().equals(event.getView().getBottomInventory())) || (event.getAction() == InventoryAction.SWAP_WITH_CURSOR && event.getClickedInventory().equals(event.getView().getBottomInventory()))) {
                Bukkit.getScheduler().runTask((Plugin)CcItemDeposit.getPlugin(), () -> autoReduceItems(player));
            }
        }
    }
    
    @EventHandler
    public static void onInventoryDrag(final InventoryDragEvent event) {
        final HumanEntity human = event.getWhoClicked();
        if (human instanceof Player) {
            final Player player = (Player)human;
            Bukkit.getScheduler().runTask((Plugin)CcItemDeposit.getPlugin(), (Runnable)new Runnable() {
                @Override
                public void run() {
                    ItemLimitListener.autoReduceItems(player);
                }
            });
        }
    }
    
    public static void autoReduceItems(final Player player) {
        boolean needUpdate = false;
        for (final DepositItemType depositItemType : DepositItemType.values()) {
            final int reffils = calculateItems(depositItemType, player.getInventory().getContents());
            if (reffils > InventoryManager.getLimit(depositItemType)) {
                final int toReduce = reffils - InventoryManager.getLimit(depositItemType);
                reduceDepositItemsFromEq((Inventory)player.getInventory(), depositItemType, toReduce);
                final PlayerDepositData depositData = DataManager.getPlayer(player.getUniqueId());
                depositData.addDepositItemNumber(depositItemType, toReduce);
                depositData.setNeedUpdate(true);
                DataManager.setPlayer(player.getUniqueId(), depositData);
                needUpdate = true;
            }
        }
        if (needUpdate) {
            player.updateInventory();
            player.sendMessage(MessagesData.getMessage("yourItemsHasFallenIntoDeposit", ChatColor.RED + "Twoje itemy wpadly do depozytu! Wpisz /depozyt aby otworzyc!"));
        }
    }
    
    public static int calculateItems(final DepositItemType type, final ItemStack[] contens) {
        int itemNumber = 0;
        for (final ItemStack itemInEq : contens) {
            if (itemInEq != null) {
                if (type == DepositItemType.REFILL && itemInEq.getType() == Material.GOLDEN_APPLE && itemInEq.getDurability() == 0) {
                    itemNumber += itemInEq.getAmount();
                }
                if (type == DepositItemType.ENDER_PEARL && itemInEq.getType() == Material.ENDER_PEARL) {
                    itemNumber += itemInEq.getAmount();
                }
                if (type == DepositItemType.KOX && itemInEq.getType() == Material.GOLDEN_APPLE && itemInEq.getDurability() == 1) {
                    itemNumber += itemInEq.getAmount();
                }
                if (type == DepositItemType.STUN && itemInEq.getType() == Material.FIREBALL && itemInEq.getEnchantmentLevel(Enchantment.ARROW_DAMAGE) == 10) {
                    itemNumber += itemInEq.getAmount();
                }
                if (type == DepositItemType.THROWN_TNT && itemInEq.getType() == Material.TNT && itemInEq.isSimilar(ThrownTNT.getThrownTNTItem())) {
                    itemNumber += itemInEq.getAmount();
                }
            }
        }
        return itemNumber;
    }
    
    @EventHandler
    public static void onClickInventory(final InventoryClickEvent event) {
    }
    
    public static boolean isItemToReduce(final ItemStack item) {
        return item != null && (item.getType() == Material.GOLDEN_APPLE || item.getType() == Material.ENDER_PEARL);
    }
    
    public static void reduceDepositItemsFromEq(final Inventory inv, final DepositItemType itemType, final int count) {
        final int stacks = count / 64;
        final int rest = count % 64;
        ItemStack itemToRemove;
        if (itemType == DepositItemType.ENDER_PEARL) {
            itemToRemove = new ItemStack(Material.ENDER_PEARL);
        }
        else if (itemType == DepositItemType.KOX) {
            itemToRemove = new ItemStack(Material.GOLDEN_APPLE, 1, (short)1);
        }
        else if (itemType == DepositItemType.REFILL) {
            itemToRemove = new ItemStack(Material.GOLDEN_APPLE, 1, (short)0);
        }
        else if (itemType == DepositItemType.STUN) {
            itemToRemove = Stun.getStunItemStack();
        }
        else {
            if (itemType != DepositItemType.THROWN_TNT) {
                return;
            }
            itemToRemove = ThrownTNT.getThrownTNTItem();
        }
        removeInventoryItems(inv, itemToRemove.getType(), itemToRemove.getDurability(), count);
    }
    
    public static void runScanner() {
        final Player[] array;
        int length;
        int i;
        Player online;
        Bukkit.getScheduler().runTaskTimer((Plugin)CcItemDeposit.getPlugin(), () -> {
            Bukkit.getOnlinePlayers();
            for (length = array.length; i < length; ++i) {
                online = array[i];
                autoReduceItems(online);
            }
        }, 200L, 200L);
    }
    
    public static void removeInventoryItems(final Inventory inv, final Material type, final short durability, int amount) {
        final ItemStack[] items = inv.getContents();
        for (int i = 0; i < items.length; ++i) {
            final ItemStack is = items[i];
            if (is != null && is.getType() == type) {
                if (durability == -1) {
                    final int newamount = is.getAmount() - amount;
                    if (newamount > 0) {
                        is.setAmount(newamount);
                        break;
                    }
                    items[i] = new ItemStack(Material.AIR);
                    amount = -newamount;
                    if (amount == 0) {
                        break;
                    }
                }
                else if (is.getDurability() == durability) {
                    final int newamount = is.getAmount() - amount;
                    if (newamount > 0) {
                        is.setAmount(newamount);
                        break;
                    }
                    items[i] = new ItemStack(Material.AIR);
                    amount = -newamount;
                    if (amount == 0) {
                        break;
                    }
                }
            }
        }
        inv.setContents(items);
    }
}
