package useless.legacyui.gui.slots;

import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.slot.Slot;

public class SlotCraftingDisplayLegacy extends Slot implements IResizable, IHighlightable {
    public ItemStack item;
    private final boolean highlighted;
    private final int highlightColor;
    private final int slotWidth;
    public SlotCraftingDisplayLegacy(final int id, final int x, final int y, final ItemStack item, final boolean discovered, final boolean highlight, final int color) {
        this(id, x, y, item, discovered, highlight, color, 18);
    }
    public SlotCraftingDisplayLegacy(final int id, final int x, final int y, final ItemStack item, final boolean discovered, final boolean highlight, final int color, final int width) {
        super(null, id, x, y);
        this.item = item;
        this.discovered = discovered;
        this.highlighted = highlight;
        this.highlightColor = color;
        this.slotWidth = width;
    }
    @Override
    public ItemStack remove(final int i) {
        return null;
    }

    @Override
    public boolean hasItem() {
        return this.item != null;
    }

    @Override
    public int getMaxStackSize() {
        return this.item.getMaxStackSize();
    }

    @Override
    public ItemStack getItemStack() {
        return this.item;
    }

    @Override
    public void onTake(final ItemStack itemstack) {
    }

    @Override
    public void setChanged() {
    }

    @Override
    public void set(final ItemStack itemstack) {
    }

    @Override
    public boolean mayPlace(final ItemStack itemstack) {
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
        return this.highlighted;
    }

    @Override
    public int getHighlightColor() {
        return this.highlightColor;
    }

    @Override
    public boolean drawStandardHighlight() {
        return false;
    }

    @Override
    public int getWidth() {
        return this.slotWidth;
    }
}