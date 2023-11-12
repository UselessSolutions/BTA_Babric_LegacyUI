package useless.legacyui.Mixins;

import net.minecraft.client.render.RenderBlocks;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import useless.legacyui.Gui.GuiScreens.UtilGui;

@Mixin(value = RenderBlocks.class, remap = false)
public class RenderBlocksMixin {
    @Redirect(method = "renderBlockOnInventory(Lnet/minecraft/core/block/Block;IF)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glColor4f(FFFF)V"))
    private void addAlphaSupport(float red, float green, float blue, float alpha){
        GL11.glColor4f(red, green, blue, UtilGui.blockAlpha);
    }
}
