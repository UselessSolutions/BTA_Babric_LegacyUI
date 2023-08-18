package useless.legacyui.Gui;

import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.slot.Slot;

public class SlotResizable extends Slot implements IResizable{
    public int width;
    public SlotResizable(IInventory inventory, int id, int x, int y, int width) {
        super(inventory, id, x, y);
        this.width = width;
    }

    @Override
    public int getWidth() {
        return width;
    }
}
