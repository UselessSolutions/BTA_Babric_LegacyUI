package useless.legacyui.Gui.Slots;

import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import useless.prismaticlibe.gui.slot.IResizable;

public class SlotResizable extends Slot implements IResizable {
    private final int slotWidth;
    public SlotResizable(IInventory inventory, int id, int x, int y, int width) {
        super(inventory, id, x, y);
        slotWidth = width;
    }

    @Override
    public int getWidth() {
        return slotWidth;
    }
}
