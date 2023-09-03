package useless.legacyui.gui.container;

import net.minecraft.core.player.inventory.*;
import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.core.player.inventory.slot.SlotArmor;

public class ContainerInventoryLegacy extends ContainerPlayer {
    public ContainerInventoryLegacy(InventoryPlayer inventoryplayer, InventoryCrafting craftMatrix, IInventory craftResult) {
        this(inventoryplayer, true, craftMatrix, craftResult);
    }
    public ContainerInventoryLegacy(InventoryPlayer inventory, boolean isSinglePlayer, InventoryCrafting craftMatrix, IInventory craftResult) {
        super(inventory, isSinglePlayer);
        inventorySlots.clear();
        this.craftMatrix = craftMatrix;
        this.craftResult = craftResult;
        int i;
        for (i = 0; i < 4; ++i) {
            this.addSlot(new SlotArmor(this, inventory, inventory.getSizeInventory() - 1 - i, 8 + 41, 8 + i * 18, i));
        }
        for (i = 0; i < 3; ++i) {
            for (int k1 = 0; k1 < 9; ++k1) {
                this.addSlot(new Slot(inventory, k1 + (i + 1) * 9, 8 + k1 * 18, 84 + 10 + i * 18));
            }
        }
        for (i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventory, i, 8 + i * 18, 142 + 10));
        }
    }
}
