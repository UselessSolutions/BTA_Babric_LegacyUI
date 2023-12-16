package useless.legacyui.Gui.Slots;

import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.slot.Slot;
import useless.prismaticlibe.gui.slot.IHighlighting;
import useless.prismaticlibe.gui.slot.IResizable;

public class SlotCraftingDisplayLegacy extends Slot implements IHighlighting, IResizable {
    public ItemStack item;
    private final boolean highlighted;
    private final int highlightColor;
    private final int slotWidth;
    public SlotCraftingDisplayLegacy(int id, int x, int y, ItemStack item, boolean discovered, boolean highlight, int color) {
        this(id, x, y, item, discovered, highlight, color, 16);
    }
    public SlotCraftingDisplayLegacy(int id, int x, int y, ItemStack item, boolean discovered, boolean highlight, int color, int width) {
        super(null, id, x, y);
        this.item = item;
        this.discovered = discovered;
        highlighted = highlight;
        highlightColor = color;
        slotWidth = width;
    }
    @Override
    public ItemStack decrStackSize(int i) {
        return null;
    }

    @Override
    public boolean hasStack() {
        return this.item != null;
    }

    @Override
    public int getSlotStackLimit() {
        return this.item.getMaxStackSize();
    }

    @Override
    public ItemStack getStack() {
        return this.item;
    }

    @Override
    public void onPickupFromSlot(ItemStack itemstack) {
    }

    @Override
    public void onSlotChanged() {
    }

    @Override
    public void putStack(ItemStack itemstack) {
    }

    @Override
    public boolean canPutStackInSlot(ItemStack itemstack) {
        return false;
    }

    @Override
    public boolean enableDragAndPickup() {
        return false;
    }

    @Override
    public boolean allowItemInteraction() {
        return false;
    }

    @Override
    public boolean isHighlighted() {
        return highlighted;
    }

    @Override
    public int getHighlightColor() {
        return highlightColor;
    }

    @Override
    public boolean drawStandardHighlight() {
        return false;
    }

    @Override
    public int getWidth() {
        return slotWidth;
    }

}