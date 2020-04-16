// 
// Decompiled by Procyon v0.5.30
// 

package pl.best241.ccitemdeposit.listeners;

import org.bukkit.event.EventHandler;
import pl.best241.ccitemdeposit.data.PlayerDepositData;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import pl.best241.ccitemdeposit.data.DepositItemType;
import pl.best241.ccitemdeposit.data.DataManager;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.inventory.InventoryAction;
import pl.best241.ccitemdeposit.inventory.InventoryManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import java.util.UUID;
import java.util.ArrayList;
import org.bukkit.event.Listener;

public class InventoryListener implements Listener
{
    private static ArrayList<UUID> uuidsCanceled;
    
    @EventHandler
    public static void onInventoryClick(final InventoryClickEvent event) {
        final HumanEntity human = event.getWhoClicked();
        if (human instanceof Player) {
            final Player player = (Player)human;
            if (event.getClickedInventory() != null) {
                if (event.getInventory().getTitle().equals(InventoryManager.depositName) && event.getAction() == InventoryAction.COLLECT_TO_CURSOR) {
                    event.setCancelled(true);
                    event.getView().setCursor((ItemStack)null);
                    event.setResult(Event.Result.DENY);
                }
                if (event.getClickedInventory().getName().equals(InventoryManager.depositName)) {
                    event.setCancelled(true);
                    event.getView().setCursor((ItemStack)null);
                    event.setResult(Event.Result.DENY);
                    final int slot = event.getSlot();
                    final PlayerDepositData depositData = DataManager.getPlayer(player.getUniqueId());
                    DepositItemType itemType = null;
                    switch (slot) {
                        case 2: {
                            itemType = DepositItemType.REFILL;
                            break;
                        }
                        case 3: {
                            itemType = DepositItemType.KOX;
                            break;
                        }
                        case 4: {
                            itemType = DepositItemType.ENDER_PEARL;
                            break;
                        }
                        case 5: {
                            itemType = DepositItemType.STUN;
                            break;
                        }
                        case 6: {
                            itemType = DepositItemType.THROWN_TNT;
                            break;
                        }
                    }
                    if (itemType == null) {
                        return;
                    }
                    final Integer depositedItemNumber = depositData.getDepositedItemNumber(itemType);
                    if (depositedItemNumber <= 0) {
                        return;
                    }
                    int toWithdraw = 0;
                    if (null != event.getAction()) {
                        switch (event.getAction()) {
                            case PICKUP_ALL: {
                                toWithdraw += InventoryManager.getLimit(itemType) - ItemLimitListener.calculateItems(itemType, player.getInventory().getContents());
                                if (toWithdraw > depositedItemNumber) {
                                    toWithdraw = depositedItemNumber;
                                    break;
                                }
                                break;
                            }
                            case PICKUP_HALF: {
                                ++toWithdraw;
                                if (InventoryManager.getLimit(itemType) < toWithdraw + ItemLimitListener.calculateItems(itemType, player.getInventory().getContents())) {
                                    toWithdraw = 0;
                                    break;
                                }
                                break;
                            }
                            default: {
                                return;
                            }
                        }
                    }
                    if (toWithdraw > 0 && !InventoryListener.uuidsCanceled.contains(player.getUniqueId())) {
                        InventoryListener.uuidsCanceled.add(player.getUniqueId());
                        final int notWithdrawed = InventoryManager.addToInvAsMuchAsPossible((Inventory)player.getInventory(), itemType, toWithdraw);
                        final int withdrawed = toWithdraw - notWithdrawed;
                        depositData.setDepositItemNumber(itemType, depositedItemNumber - withdrawed);
                        depositData.setNeedUpdate(true);
                        DataManager.setPlayer(player.getUniqueId(), depositData);
                        InventoryManager.rebuildDepositInventory(event.getInventory(), depositData, player);
                        InventoryListener.uuidsCanceled.remove(player.getUniqueId());
                    }
                }
            }
        }
    }
    
    public static void main(final String... args) {
        final int inEq = 0;
        final int deposited = 6;
        final int limit = 5;
        int toWithdraw = 0;
        toWithdraw = limit - inEq;
        System.out.println(toWithdraw);
    }
    
    static {
        InventoryListener.uuidsCanceled = new ArrayList<UUID>();
    }
}
