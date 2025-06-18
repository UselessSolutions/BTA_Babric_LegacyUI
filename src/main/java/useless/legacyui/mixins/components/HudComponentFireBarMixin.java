package useless.legacyui.mixins.components;

import net.minecraft.client.gui.hud.component.HudComponentFireBar;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import useless.legacyui.gui.screens.UtilGui;

@Mixin(value = HudComponentFireBar.class, remap = false)
public class HudComponentFireBarMixin {
    @Redirect(method = "render(Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/hud/HudIngame;IIF)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glColor4f(FFFF)V"))
    private void hotbarFadeout(final float red, final float green, final float blue, final float alpha){
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(red, green, blue, UtilGui.getHotbarAlpha());
    }
    @Redirect(method = "render(Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/hud/HudIngame;IIF)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glDisable(I)V"))
    private void disableDisable(final int cap){}
}
