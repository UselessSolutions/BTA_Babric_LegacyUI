package useless.legacyui.mixins;

import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import net.minecraft.core.player.inventory.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import useless.legacyui.gui.slots.SlotCraftingDisplayLegacy;
import useless.legacyui.gui.slots.SlotNull;

@Mixin(value = MenuAbstract.class, remap = false)
public class ContainerMixin {
    @Inject(method = "clicked", at = @At("HEAD"), cancellable = true)
    private void dontInteractWithProtectedSlots(final InventoryAction action, final int[] args, final Player player, final CallbackInfoReturnable<ItemStack> cir){
        if (action != InventoryAction.PICKUP_SIMILAR){
            if (args == null){
                cir.setReturnValue(null);
            } else if (getSlot(args[0]) instanceof SlotCraftingDisplayLegacy || getSlot(args[0]) instanceof SlotNull){
                cir.setReturnValue(null);
            }
        }

    }
    @Shadow
    public Slot getSlot(final int arg) {
        return null;
    }
}
