package useless.legacyui.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.PlayerLocal;
import net.minecraft.core.block.entity.TileEntityFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import useless.legacyui.gui.screens.GuiLegacyCrafting;
import useless.legacyui.gui.screens.GuiLegacyFlag;
import useless.legacyui.LegacyUI;

@Mixin(value = PlayerLocal.class, remap = false)
public class PlayerLocalMixin {
    @Shadow
    protected Minecraft mc;

    @Inject(method = "displayWorkbenchScreen", at = @At("HEAD"), cancellable = true)
    private void displayLegacyCrafting(final int x, final int y, final int z, final CallbackInfo ci){
        if (LegacyUI.modSettings.legacyui$getEnableLegacyCrafting().value){
            this.mc.displayScreen(new GuiLegacyCrafting(this.mc.thePlayer, x, y, z, 9));
            ci.cancel();
        }
    }
    @Inject(method = "displayFlagEditorScreen", at = @At("HEAD"), cancellable = true)
    private void displayLegacyFlag(final TileEntityFlag tileEntityFlag, final CallbackInfo ci){
        if (LegacyUI.modSettings.legacyui$getEnableLegacyFlag().value){
            this.mc.displayScreen(new GuiLegacyFlag(this.mc.thePlayer, tileEntityFlag));
            ci.cancel();
        }
    }
}
