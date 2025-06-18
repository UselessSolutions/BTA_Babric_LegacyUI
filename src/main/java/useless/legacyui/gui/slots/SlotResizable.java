package useless.legacyui.gui.slots;

import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.player.inventory.slot.Slot;

public class SlotResizable extends Slot implements IResizable {
    private final int slotWidth;
    public SlotResizable(Container inventory, int id, int x, int y, int width) {
        super(inventory, id, x, y);
        slotWidth = width;
    }

    @Override
    public int getWidth() {
        return slotWidth;
    }
}
