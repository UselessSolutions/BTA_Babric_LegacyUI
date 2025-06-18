package useless.legacyui.gui.slots;

import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.player.inventory.slot.Slot;

public class SlotNull extends Slot {
    public SlotNull(Container inventory, int id, int x, int y) {
        super(null, id, x, y);
    }
    public ItemStack decrStackSize(int i) {
        return null;
    }

    @Override
    public boolean hasStack() {
        return false;
    }

    @Override
    public int getSlotStackLimit() {
        return 0;
    }

    @Override
    public ItemStack getStack() {
        return null;
    }
    @Override
    public boolean canPutStackInSlot(ItemStack itemstack) {
        return false;
    }

    @Override
    public void onPickupFromSlot(ItemStack itemstack) {
    }
    @Override
    public void onSlotChanged() {
    }
    @Override
    public Container getInventory() {
        return null;
    }
    @Override
    public void putStack(ItemStack itemstack) {
    }
    @Override
    public boolean enableDragAndPickup() {
        return false;
    }
    @Override
    public boolean allowItemInteraction() {
        return false;
    }
}
