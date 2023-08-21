package useless.legacyui.Gui.Slot;

import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.slot.SlotGuidebook;

public class SlotCraftingDisplay extends SlotGuidebook implements IResizable{
    public boolean highlight;
    public int color;
    private int width;

    public SlotCraftingDisplay(int id, int x, int y, ItemStack item, boolean discovered, boolean highlight, int color) {
        super(id, x, y, item, discovered);
        this.highlight = highlight;
        this.color = color;
        this.width = 18;
    }
    public SlotCraftingDisplay(int id, int x, int y, ItemStack item, boolean discovered, boolean highlight, int color, int width) {
        super(id, x, y, item, discovered);
        this.highlight = highlight;
        this.color = color;
        this.width = width;
    }

    @Override
    public int getWidth() {
        return width;
    }
}