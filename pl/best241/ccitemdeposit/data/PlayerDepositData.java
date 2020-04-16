// 
// Decompiled by Procyon v0.5.30
// 

package pl.best241.ccitemdeposit.data;

import java.util.HashMap;
import java.util.UUID;

public class PlayerDepositData
{
    private UUID uuid;
    private HashMap<DepositItemType, Integer> depositItems;
    private boolean needUpdate;
    
    public boolean isNeedUpdate() {
        return this.needUpdate;
    }
    
    public void setNeedUpdate(final boolean needUpdate) {
        this.needUpdate = needUpdate;
    }
    
    public UUID getUuid() {
        return this.uuid;
    }
    
    public void setUuid(final UUID uuid) {
        this.uuid = uuid;
    }
    
    public HashMap<DepositItemType, Integer> getDepositItems() {
        return this.depositItems;
    }
    
    public void setDepositItems(final HashMap<DepositItemType, Integer> depositItems) {
        this.depositItems = depositItems;
    }
    
    public PlayerDepositData(final UUID uuid, final HashMap<DepositItemType, Integer> depositItems) {
        this.needUpdate = true;
        this.uuid = uuid;
        this.depositItems = depositItems;
    }
    
    public Integer getDepositedItemNumber(final DepositItemType itemType) {
        final Integer get = this.getDepositItems().get(itemType);
        if (get == null) {
            return 0;
        }
        return get;
    }
    
    public void setDepositItemNumber(final DepositItemType itemType, final Integer number) {
        this.depositItems.put(itemType, number);
    }
    
    public void addDepositItemNumber(final DepositItemType itemType, final Integer number) {
        final Integer depositedItemNumber = this.getDepositedItemNumber(itemType) + number;
        this.depositItems.put(itemType, depositedItemNumber);
    }
}
