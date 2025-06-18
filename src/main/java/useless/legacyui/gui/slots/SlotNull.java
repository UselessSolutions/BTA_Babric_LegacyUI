package useless.legacyui.gui.slots;

import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.player.inventory.slot.Slot;

public class SlotNull extends Slot {
    public SlotNull(Container inventory, int id, int x, int y) {
        super(null, id, x, y);
    }
    @Override
    public ItemStack remove(int i) {
        return null;
    }

    @Override
    public boolean hasItem() {
        return false;
    }

    @Override
    public int getMaxStackSize() {
        return 0;
    }

    @Override
    public ItemStack getItemStack() {
        return null;
    }
    @Override
    public boolean mayPlace(ItemStack itemstack) {
        return false;
    }

    @Override
    public void onTake(ItemStack itemstack) {
    }
    @Override
    public void setChanged() {
    }
    @Override
    public Container getContainer() {
        return null;
    }
    @Override
    public void set(ItemStack itemstack) {
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
