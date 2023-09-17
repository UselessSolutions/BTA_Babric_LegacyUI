package useless.legacyui.Mixins;

import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.Container;
import net.minecraft.core.player.inventory.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import useless.legacyui.Gui.Slots.SlotNull;
import useless.legacyui.LegacyUI;
import useless.prismaticlibe.gui.slot.SlotCraftingDisplay;

@Mixin(value = Container.class, remap = false)
public class ContainerMixin {
    @Inject(method = "clickInventorySlot(Lnet/minecraft/core/InventoryAction;[ILnet/minecraft/core/entity/player/EntityPlayer;)Lnet/minecraft/core/item/ItemStack;", at = @At("HEAD"), cancellable = true)
    private void dontInteractWithProtectedSlots(InventoryAction action, int[] args, EntityPlayer player, CallbackInfoReturnable<ItemStack> cir){
        if (action != InventoryAction.PICKUP_SIMILAR){
            if (args == null){
                cir.setReturnValue(null);
            } else if (getSlot(args[0]) instanceof SlotCraftingDisplay || getSlot(args[0]) instanceof SlotNull){
                cir.setReturnValue(null);
            }
        }

    }
    @Shadow
    public Slot getSlot(int arg) {
        return null;
    }
}
