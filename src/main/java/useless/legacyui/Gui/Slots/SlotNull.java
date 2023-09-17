package useless.legacyui.Gui.Slots;

import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.slot.Slot;

public class SlotNull extends Slot {
    public SlotNull(IInventory inventory, int id, int x, int y) {
        super(null, id, x, y);
    }
    public ItemStack decrStackSize(int i) {
        return null;
    }

    public int getBackgroundIconIndex() {
        return -1;
    }

    public boolean hasStack() {
        return false;
    }

    public int getSlotStackLimit() {
        return 0;
    }

    public ItemStack getStack() {
        return null;
    }

    public boolean isHere(IInventory iinventory, int i) {
        return iinventory == this.inventory && i == this.slotIndex;
    }

    public boolean canPutStackInSlot(ItemStack itemstack) {
        return false;
    }

    public void onPickupFromSlot(ItemStack itemstack) {
        return;
    }

    public void onSlotChanged() {
        return;
    }

    public IInventory getInventory() {
        return null;
    }

    public void putStack(ItemStack itemstack) {
        return;
    }

    public boolean enableDragAndPickup() {
        return false;
    }

    public boolean allowItemInteraction() {
        return false;
    }
}
