package useless.legacyui.Gui.Slots;

import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.slot.Slot;

public class SlotOldGuidebook extends Slot {
    public ItemStack item;

    public SlotOldGuidebook(int id, int x, int y, ItemStack item, boolean discovered) {
        super(null, id, x, y);
        this.item = item;
        this.discovered = discovered;
    }

    public ItemStack decrStackSize(int i) {
        return null;
    }

    public boolean hasStack() {
        return this.item != null;
    }

    public int getSlotStackLimit() {
        return this.item.getMaxStackSize();
    }

    public ItemStack getStack() {
        return this.item;
    }

    public void onPickupFromSlot(ItemStack itemstack) {
    }

    public void onSlotChanged() {
    }

    public void putStack(ItemStack itemstack) {
    }
}