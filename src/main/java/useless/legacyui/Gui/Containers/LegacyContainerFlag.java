package useless.legacyui.Gui.Containers;

import net.minecraft.core.block.entity.TileEntityFlag;
import net.minecraft.core.player.inventory.ContainerFlag;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.core.player.inventory.slot.SlotDye;
import useless.legacyui.Gui.Slots.SlotCraftingDisplayLegacy;

public class LegacyContainerFlag extends ContainerFlag {
    public IInventory inventory;
    public TileEntityFlag flag;
    public LegacyContainerFlag(IInventory inv, TileEntityFlag flag) {
        super(inv, flag);
        this.flag = flag;
        inventory = inv;
        setSlots();

    }
    public void setSlots(){
        inventorySlots.clear();
        this.addSlot(new SlotDye(flag, 36, 44 + 170, 40));
        this.addSlot(new SlotDye(flag, 37, 77 + 170, 40));
        this.addSlot(new SlotDye(flag, 38, 110 + 170, 40));
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlot(new Slot(inventory, x + y * 9 + 9, 8 + 170 + x * 18, 100 + y * 18));
            }
        }
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventory, i, 8 + 170 + i * 18, 158));
        }
        this.addSlot(new SlotCraftingDisplayLegacy(101, 44, 40, flag.getStackInSlot(36), true, false, -1));
        this.addSlot(new SlotCraftingDisplayLegacy(102, 77, 40, flag.getStackInSlot(37), true, false, -1));
        this.addSlot(new SlotCraftingDisplayLegacy(103, 110, 40, flag.getStackInSlot(38), true, false, -1));
    }
}
