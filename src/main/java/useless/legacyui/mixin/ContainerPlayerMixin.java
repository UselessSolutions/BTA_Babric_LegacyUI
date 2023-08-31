package useless.legacyui.mixin;

import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.player.inventory.Container;
import net.minecraft.core.player.inventory.ContainerPlayer;
import net.minecraft.core.player.inventory.InventoryCrafting;
import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.core.player.inventory.slot.SlotCrafting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(value = ContainerPlayer.class, remap = false)
public class ContainerPlayerMixin extends Container {

    @Redirect(method = "<init>(Lnet/minecraft/core/player/inventory/InventoryPlayer;Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/player/inventory/ContainerPlayer;addSlot(Lnet/minecraft/core/player/inventory/slot/Slot;)V"))
    private void craftingSlotRemover(ContainerPlayer containerPlayer, Slot slot){
        if (slot instanceof SlotCrafting){
            return; // Remove crafting output
        } else if (slot.getInventory() instanceof InventoryCrafting) {
            return; // Remove crafting grid
        } else {
            slot.id = containerPlayer.inventorySlots.size();
            containerPlayer.inventorySlots.add(slot);
            containerPlayer.inventoryItemStacks.add(null);
        }

    }
    /**
     * @author Useless
     * @reason Legacy ui survival inventory has fewer slots
     */
    @Overwrite
    public List<Integer> getMoveSlots(InventoryAction action, Slot slot, int target, EntityPlayer player) {
        if (slot.id >= 5-5 && slot.id <= 8-5) { // armor slots
            return this.getSlots(5-5, 4, false);
        }
        if (action == InventoryAction.MOVE_SIMILAR) {
            if (slot.id >= 9-5 && slot.id <= 44-5) { // Entire inventory
                return this.getSlots(9-5, 36, false);
            }
        } else {
            if (slot.id >= 9-5 && slot.id <= 35-5) { // Main inventory
                return this.getSlots(9-5, 27, false);
            }
            if (slot.id >= 36-5 && slot.id <= 44-5) { // Hotbar
                return this.getSlots(36-5, 9, false);
            }
        }
        return null;
    }

    /**
     * @author Useless
     * @reason Legacy ui survival inventory has fewer slots
     */
    @Overwrite
    public List<Integer> getTargetSlots(InventoryAction action, Slot slot, int target, EntityPlayer player) {
        if (slot.id >= 9-5 && slot.id <= 44-5) {
            if (target == 2) { // Armor Slots
                return this.getSlots(5-5, 4, false);
            }
            if (slot.id < 36-5) { // Hotbar
                return this.getSlots(36-5, 9, false);
            }
            return this.getSlots(9-5, 27, false); // Main Inventory
        }
        return this.getSlots(9-5, 36, false); // Entire Inventory
    }

    @Shadow
    public boolean isUsableByPlayer(EntityPlayer entityPlayer) {
        return false;
    }
}
