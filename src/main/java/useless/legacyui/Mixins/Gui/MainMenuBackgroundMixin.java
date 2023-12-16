package useless.legacyui.Mixins.Gui;

import net.minecraft.client.gui.MainMenuBackground;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import useless.legacyui.LegacyUI;

@Mixin(value = MainMenuBackground.class, remap = false)
public class MainMenuBackgroundMixin {
    @Redirect(method = "drawImage(Lnet/minecraft/client/gui/MainMenuBackground$Background;F)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glColor4d(DDDD)V"))
    private void darkenImage(double red, double green, double blue, double alpha){
        float brightness = LegacyUI.modSettings.getMainMenuBrightness().value;
        GL11.glColor4d(brightness * red, brightness * green, brightness * brightness, alpha);
    }
}
