package useless.legacyui.Gui.Containers;

import net.minecraft.core.block.entity.TileEntityFlag;
import net.minecraft.core.player.inventory.ContainerFlag;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.core.player.inventory.slot.SlotDye;

public class LegacyContainerFlag extends ContainerFlag {
    public LegacyContainerFlag(IInventory inv, TileEntityFlag flag) {
        super(inv, flag);
        inventorySlots.clear();
        this.addSlot(new SlotDye(flag, 36, 44, 40));
        this.addSlot(new SlotDye(flag, 37, 77, 40));
        this.addSlot(new SlotDye(flag, 38, 110, 40));
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlot(new Slot(inv, x + y * 9 + 9, 8 + 170 + x * 18, 100 + y * 18));
            }
        }
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inv, i, 8 + 170 + i * 18, 158));
        }
    }
}
