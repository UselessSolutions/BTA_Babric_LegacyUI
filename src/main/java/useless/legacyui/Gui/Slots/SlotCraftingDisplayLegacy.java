package useless.legacyui.Gui.Slots;

import net.minecraft.core.item.ItemStack;
import useless.prismaticlibe.gui.slot.IHighlighting;
import useless.prismaticlibe.gui.slot.IResizable;

public class SlotCraftingDisplayLegacy extends SlotOldGuidebook implements IHighlighting, IResizable {
    private final boolean highlighted;
    private final int highlightColor;
    private final int slotWidth;
    public SlotCraftingDisplayLegacy(int id, int x, int y, ItemStack item, boolean discovered, boolean highlight, int color) {
        this(id, x, y, item, discovered, highlight, color, 16);
    }
    public SlotCraftingDisplayLegacy(int id, int x, int y, ItemStack item, boolean discovered, boolean highlight, int color, int width) {
        super(id, x, y, item, discovered);
        highlighted = highlight;
        highlightColor = color;
        slotWidth = width;
    }

    public boolean canPutStackInSlot(ItemStack itemstack) {
        return false;
    }

    public void putStack(ItemStack itemstack) {
    }
    public boolean enableDragAndPickup() {
        return false;
    }

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