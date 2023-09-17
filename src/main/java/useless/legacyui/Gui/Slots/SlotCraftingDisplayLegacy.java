package useless.legacyui.Gui.Slots;

import net.minecraft.core.item.ItemStack;
import useless.prismaticlibe.gui.slot.SlotCraftingDisplay;

public class SlotCraftingDisplayLegacy extends SlotCraftingDisplay {
    public SlotCraftingDisplayLegacy(int id, int x, int y, ItemStack item, boolean discovered, boolean highlight, int color) {
        super(id, x, y, item, discovered, highlight, color);
    }
    public SlotCraftingDisplayLegacy(int id, int x, int y, ItemStack item, boolean discovered, boolean highlight, int color, int width) {
        super(id, x, y, item, discovered, highlight, color, width);
    }

    public boolean canPutStackInSlot(ItemStack itemstack) {
        return false;
    }
    public void onPickupFromSlot(ItemStack itemstack) {
        return;
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