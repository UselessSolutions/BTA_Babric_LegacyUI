package useless.legacyui.mixin;

import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.player.inventory.Container;
import net.minecraft.core.player.inventory.ContainerPlayer;
import net.minecraft.core.player.inventory.InventoryCrafting;
import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.core.player.inventory.slot.SlotCrafting;
import org.spongepowered.asm.mixin.Mixin;
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
    @Shadow
    public List<Integer> getMoveSlots(InventoryAction inventoryAction, Slot slot, int i, EntityPlayer entityPlayer) {
        return null;
    }

    @Shadow
    public List<Integer> getTargetSlots(InventoryAction inventoryAction, Slot slot, int i, EntityPlayer entityPlayer) {
        return null;
    }

    @Shadow
    public boolean isUsableByPlayer(EntityPlayer entityPlayer) {
        return false;
    }
}
