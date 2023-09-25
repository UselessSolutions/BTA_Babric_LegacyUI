package useless.legacyui.Mixins.Gui;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.render.Tessellator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import useless.legacyui.Gui.GuiScreens.UtilGui;
import useless.legacyui.Settings.ModSettings;

@Mixin(value = GuiMainMenu.class, remap = false)
public class GuiMainMenuMixin extends GuiScreen {
    @Inject(method = "drawBackground(I)V", at = @At("HEAD"), cancellable = true)
    private void panorama(int i, CallbackInfo ci){
        if (ModSettings.legacyOptions.getEnablePanorama().value){
            UtilGui.drawPanorama(this);
            ci.cancel();
        }
    }

    @Redirect(method = "drawBackground(I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Tessellator;setColorRGBA_F(FFFF)V"))
    private void changeBrightness(Tessellator instance, float r, float g, float b, float a){
        float brightness = ModSettings.legacyOptions.getMainMenuBrightness().value;
        instance.setColorRGBA_F(brightness, brightness, brightness, a);
    }
}
