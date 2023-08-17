package useless.legacyui.Gui;

import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.slot.SlotGuidebook;

public class SlotCraftingDisplay extends SlotGuidebook {
    public boolean highlight;
    public int color;

    public SlotCraftingDisplay(int id, int x, int y, ItemStack item, boolean discovered, boolean highlight, int color) {
        super(id, x, y, item, discovered);
        this.highlight = highlight;
        this.color = color;
    }
}