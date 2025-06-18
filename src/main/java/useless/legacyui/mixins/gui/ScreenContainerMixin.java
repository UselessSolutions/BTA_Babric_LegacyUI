package useless.legacyui.mixins.gui;

import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.core.player.inventory.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import useless.legacyui.gui.slots.SlotResizable;
import useless.legacyui.LegacySoundManager;
import useless.legacyui.LegacyUI;

@Mixin(value = ScreenContainerAbstract.class, remap = false)
public class ScreenContainerMixin extends Screen {
    @Shadow
    public int xSize;
    @Shadow
    public int ySize;
    @Inject(method = "getIsMouseOverSlot(Lnet/minecraft/core/player/inventory/slot/Slot;II)Z", at = @At("HEAD"), cancellable = true)
    private void hoverAccountsForResizable(final Slot slot, int x, int y, final CallbackInfoReturnable<Boolean> cir){
        final int GUIx = (this.width - this.xSize) / 2;
        final int GUIy = (this.height - this.ySize) / 2;
        x -= GUIx; // X shifted to be relative to gui
        y -= GUIy; // Y shifted to be relative to gui
        int slotSize = 16;
        if (slot instanceof SlotResizable) {
            slotSize = ((SlotResizable) slot).getWidth();
        }
        cir.setReturnValue(x >= slot.x - 1 && x < slot.x + slotSize - 2 + 1 && y >= slot.y - 1 && y < slot.y + slotSize - 2 + 1);
    }
    @Inject(method = "removed", at = @At("HEAD"))
    private void closingSound(final CallbackInfo cbi){
        if (LegacyUI.modSettings.legacyui$getUseLegacySounds().value){
            LegacySoundManager.play.back(false);
        }
    }
}
