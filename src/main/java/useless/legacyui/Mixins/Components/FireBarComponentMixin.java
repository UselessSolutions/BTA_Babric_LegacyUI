package useless.legacyui.Mixins.Components;

import net.minecraft.client.gui.hud.FireBarComponent;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import useless.legacyui.Gui.GuiScreens.UtilGui;

@Mixin(FireBarComponent.class)
public class FireBarComponentMixin {
    @Redirect(method = "render(Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/GuiIngame;IIF)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glColor4f(FFFF)V"))
    private void hotbarFadeout(float red, float green, float blue, float alpha){
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(red, green, blue, UtilGui.getHotbarAlpha());
    }
    @Redirect(method = "render(Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/GuiIngame;IIF)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glDisable(I)V"))
    private void disableDisable(int cap){}
}
