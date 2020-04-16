// 
// Decompiled by Procyon v0.5.30
// 

package pl.best241.ccitemdeposit.inventory;

import org.bukkit.ChatColor;
import java.util.Iterator;
import java.util.HashMap;
import pl.best241.cctools.components.Stun;
import pl.best241.cctools.components.ThrownTNT;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import pl.best241.ccitemdeposit.messages.MessagesData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import pl.best241.ccitemdeposit.config.ConfigManager;
import pl.best241.ccitemdeposit.data.DepositItemType;
import pl.best241.ccitemdeposit.data.PlayerDepositData;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.Bukkit;
import pl.best241.ccitemdeposit.data.DataManager;
import org.bukkit.inventory.Inventory;
import org.bukkit.entity.Player;

public class InventoryManager
{
    public static final String depositName;
    
    public static Inventory buildDepositInventory(final Player player) {
        final PlayerDepositData playerDepositData = DataManager.getPlayer(player.getUniqueId());
        final Inventory inv = Bukkit.createInventory((InventoryHolder)player, 9, InventoryManager.depositName);
        rebuildDepositInventory(inv, playerDepositData, player);
        return inv;
    }
    
    public static void rebuildDepositInventory(final Inventory inv, final PlayerDepositData playerDepositData, final Player player) {
        inv.setItem(2, getRefillItemStack(playerDepositData, player));
        inv.setItem(3, getKoxItemStack(playerDepositData, player));
        inv.setItem(4, getPearlItemStack(playerDepositData, player));
        inv.setItem(5, getThrownTNTItemStack(playerDepositData, player));
        inv.setItem(6, getStunItemStack(playerDepositData, player));
    }
    
    public static int getLimit(final DepositItemType itemType) {
        return ConfigManager.getInt(itemType + ".limit");
    }
    
    public static ItemStack getRefillItemStack(final PlayerDepositData data, final Player player) {
        final ItemStack itemStack = new ItemStack(Material.GOLDEN_APPLE);
        final DepositItemType itemType = DepositItemType.REFILL;
        final String itemTypeString = DepositItemType.REFILL.toString();
        final ItemMeta meta = itemStack.getItemMeta();
        final Integer deposited = data.getDepositedItemNumber(itemType);
        final int limit = ConfigManager.getInt(itemType + ".limit");
        final int contain = calculateItems(player, itemType, itemStack, null);
        int withdraw = limit - contain;
        if (withdraw > deposited) {
            withdraw = deposited;
        }
        meta.setDisplayName(MessagesData.getMessage("depositItems." + itemTypeString + ".displayName", itemTypeString));
        final ArrayList<String> lore = new ArrayList<String>();
        lore.addAll(Arrays.asList(MessagesData.getMessage("depositItems." + itemTypeString + ".description", "Zdeponowanych " + itemTypeString + " \n %numberDeposited").replace("%numberDeposited", "" + deposited).replace("%limit", "" + limit).replace("%contain", contain + "").replace("%toWithraw", withdraw + "").split("\n")));
        meta.setLore((List)lore);
        itemStack.setItemMeta(meta);
        return itemStack;
    }
    
    public static ItemStack getKoxItemStack(final PlayerDepositData data, final Player player) {
        final ItemStack itemStack = new ItemStack(Material.GOLDEN_APPLE, 1, (short)1);
        final DepositItemType itemType = DepositItemType.KOX;
        final String itemTypeString = DepositItemType.KOX.toString();
        final ItemMeta meta = itemStack.getItemMeta();
        final Integer deposited = data.getDepositedItemNumber(itemType);
        final int limit = ConfigManager.getInt(itemType + ".limit");
        final int contain = calculateItems(player, itemType, itemStack, null);
        int withdraw = limit - contain;
        if (withdraw > deposited) {
            withdraw = deposited;
        }
        meta.setDisplayName(MessagesData.getMessage("depositItems." + itemTypeString + ".displayName", itemTypeString));
        final ArrayList<String> lore = new ArrayList<String>();
        lore.addAll(Arrays.asList(MessagesData.getMessage("depositItems." + itemTypeString + ".description", "Zdeponowanych " + itemTypeString + " \n %numberDeposited").replace("%numberDeposited", "" + data.getDepositedItemNumber(itemType)).replace("%limit", "" + limit).replace("%contain", contain + "").replace("%toWithraw", withdraw + "").split("\n")));
        meta.setLore((List)lore);
        itemStack.setItemMeta(meta);
        return itemStack;
    }
    
    public static ItemStack getPearlItemStack(final PlayerDepositData data, final Player player) {
        final ItemStack itemStack = new ItemStack(Material.ENDER_PEARL);
        final DepositItemType itemType = DepositItemType.ENDER_PEARL;
        final String itemTypeString = DepositItemType.ENDER_PEARL.toString();
        final ItemMeta meta = itemStack.getItemMeta();
        final Integer deposited = data.getDepositedItemNumber(itemType);
        final int limit = ConfigManager.getInt(itemType + ".limit");
        final int contain = calculateItems(player, itemType, itemStack, null);
        int withdraw = limit - contain;
        if (withdraw > deposited) {
            withdraw = deposited;
        }
        meta.setDisplayName(MessagesData.getMessage("depositItems." + itemTypeString + ".displayName", itemTypeString));
        final ArrayList<String> lore = new ArrayList<String>();
        lore.addAll(Arrays.asList(MessagesData.getMessage("depositItems." + itemTypeString + ".description", "Zdeponowanych " + itemTypeString + " \n %numberDeposited").replace("%numberDeposited", "" + data.getDepositedItemNumber(itemType)).replace("%limit", "" + limit).replace("%contain", contain + "").replace("%toWithraw", withdraw + "").split("\n")));
        meta.setLore((List)lore);
        itemStack.setItemMeta(meta);
        return itemStack;
    }
    
    public static ItemStack getThrownTNTItemStack(final PlayerDepositData data, final Player player) {
        final ItemStack itemStack = ThrownTNT.getThrownTNTItem();
        final DepositItemType itemType = DepositItemType.THROWN_TNT;
        final String itemTypeString = DepositItemType.THROWN_TNT.toString();
        final ItemMeta meta = itemStack.getItemMeta();
        final Integer deposited = data.getDepositedItemNumber(itemType);
        final int limit = ConfigManager.getInt(itemType + ".limit");
        final int contain = calculateItems(player, itemType, itemStack, Enchantment.ARROW_DAMAGE);
        int withdraw = limit - contain;
        if (withdraw > deposited) {
            withdraw = deposited;
        }
        meta.setDisplayName(MessagesData.getMessage("depositItems." + itemTypeString + ".displayName", itemTypeString));
        ArrayList<String> lore = (ArrayList<String>)meta.getLore();
        if (lore == null) {
            lore = new ArrayList<String>();
        }
        lore.addAll(Arrays.asList(MessagesData.getMessage("depositItems." + itemTypeString + ".description", "Zdeponowanych " + itemTypeString + " \n %numberDeposited").replace("%numberDeposited", "" + data.getDepositedItemNumber(itemType)).replace("%limit", "" + limit).replace("%contain", contain + "").replace("%toWithraw", withdraw + "").split("\n")));
        meta.setLore((List)lore);
        itemStack.setItemMeta(meta);
        return itemStack;
    }
    
    public static ItemStack getStunItemStack(final PlayerDepositData data, final Player player) {
        final ItemStack itemStack = Stun.getStunItemStack();
        final DepositItemType itemType = DepositItemType.STUN;
        final String itemTypeString = DepositItemType.STUN.toString();
        final ItemMeta meta = itemStack.getItemMeta();
        final Integer deposited = data.getDepositedItemNumber(itemType);
        final int limit = ConfigManager.getInt(itemType + ".limit");
        final int contain = calculateItems(player, itemType, itemStack, Enchantment.ARROW_DAMAGE);
        int withdraw = limit - contain;
        if (withdraw > deposited) {
            withdraw = deposited;
        }
        meta.setDisplayName(MessagesData.getMessage("depositItems." + itemTypeString + ".displayName", itemTypeString));
        ArrayList<String> lore = (ArrayList<String>)meta.getLore();
        if (lore == null) {
            lore = new ArrayList<String>();
        }
        lore.addAll(Arrays.asList(MessagesData.getMessage("depositItems." + itemTypeString + ".description", "Zdeponowanych " + itemTypeString + " \n %numberDeposited").replace("%numberDeposited", "" + data.getDepositedItemNumber(itemType)).replace("%limit", "" + limit).replace("%contain", contain + "").replace("%toWithraw", withdraw + "").split("\n")));
        meta.setLore((List)lore);
        itemStack.setItemMeta(meta);
        return itemStack;
    }
    
    public static int calculateItems(final Player player, final DepositItemType depositItemType, final ItemStack itemToCompare, final Enchantment enchantToCheck) {
        int contains = 0;
        for (final ItemStack item : player.getInventory().getContents()) {
            if (item != null) {
                if (item.getType() == itemToCompare.getType() && item.getDurability() == itemToCompare.getDurability()) {
                    if (enchantToCheck != null) {
                        if (item.getEnchantmentLevel(enchantToCheck) == itemToCompare.getEnchantmentLevel(enchantToCheck)) {
                            contains += item.getAmount();
                        }
                    }
                    else {
                        contains += item.getAmount();
                    }
                }
            }
        }
        return contains;
    }
    
    public static int addToInvAsMuchAsPossible(final Inventory inv, final DepositItemType itemType, final int count) {
        final int stacks = count / 64;
        final int rest = count % 64;
        ItemStack item = null;
        if (itemType == DepositItemType.REFILL) {
            item = new ItemStack(Material.GOLDEN_APPLE);
        }
        else if (itemType == DepositItemType.KOX) {
            item = new ItemStack(Material.GOLDEN_APPLE, 1, (short)1);
        }
        else if (itemType == DepositItemType.ENDER_PEARL) {
            item = new ItemStack(Material.ENDER_PEARL);
        }
        else if (itemType == DepositItemType.STUN) {
            item = Stun.getStunItemStack();
        }
        else if (itemType == DepositItemType.THROWN_TNT) {
            item = ThrownTNT.getThrownTNTItem();
        }
        if (null == item) {
            return 0;
        }
        for (int i = 0; i < stacks; ++i) {
            item.setAmount(64);
            final HashMap<Integer, ItemStack> addItem = (HashMap<Integer, ItemStack>)inv.addItem(new ItemStack[] { item });
            if (!addItem.isEmpty()) {
                int notStored = 64 * stacks - i * 64 + rest;
                final Collection<ItemStack> values = addItem.values();
                for (final ItemStack notSotedItem : values) {
                    notStored += notSotedItem.getAmount();
                }
                return notStored;
            }
        }
        item.setAmount(rest);
        final HashMap<Integer, ItemStack> addItem2 = (HashMap<Integer, ItemStack>)inv.addItem(new ItemStack[] { item });
        if (addItem2.isEmpty()) {
            return 0;
        }
        int notStored2 = 0;
        for (final ItemStack itemNotStored : addItem2.values()) {
            notStored2 += itemNotStored.getAmount();
        }
        return notStored2;
    }
    
    static {
        depositName = MessagesData.getMessage("depositName", ChatColor.BLUE + "Depozyt");
    }
}
