package useless.legacyui.Mixins.Gui;

import net.minecraft.client.gui.GuiContainer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.core.player.inventory.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import useless.legacyui.LegacySoundManager;
import useless.legacyui.ModSettings;
import useless.prismaticlibe.gui.slot.SlotResizable;

@Mixin(value = GuiContainer.class, remap = false)
public class GuiContainerMixin extends GuiScreen {
    @Shadow
    public int xSize;
    @Shadow
    public int ySize;
    @Unique
    private GuiScreen superContainer = (GuiScreen)(Object)this;
    @Inject(method = "getIsMouseOverSlot(Lnet/minecraft/core/player/inventory/slot/Slot;II)Z", at = @At("HEAD"), cancellable = true)
    private void hoverAccountsForResizable(Slot slot, int x, int y, CallbackInfoReturnable<Boolean> cir){
        int GUIx = (superContainer.width - xSize) / 2;
        int GUIy = (superContainer.height - ySize) / 2;
        x -= GUIx; // X shifted to be relative to gui
        y -= GUIy; // Y shifted to be relative to gui
        int slotSize = 16;
        if (slot instanceof SlotResizable) {
            slotSize = ((SlotResizable) slot).width;
        }
        cir.setReturnValue(x >= slot.xDisplayPosition - 1 && x < slot.xDisplayPosition + slotSize - 2 + 1 && y >= slot.yDisplayPosition - 1 && y < slot.yDisplayPosition + slotSize - 2 + 1);
    }
    @Inject(method = "onGuiClosed()V", at = @At("HEAD"))
    private void closingSound(CallbackInfo cbi){
        if (ModSettings.Sounds.UseLegacySounds()){
            LegacySoundManager.play.back(false);
        }
    }
}
