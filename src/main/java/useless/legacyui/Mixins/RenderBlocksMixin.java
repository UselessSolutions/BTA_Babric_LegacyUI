package useless.legacyui.Mixins;

import net.minecraft.client.render.RenderBlocks;
import net.minecraft.core.block.Block;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import useless.legacyui.Gui.GuiScreens.UtilGui;

@Mixin(value = RenderBlocks.class, remap = false)
public class RenderBlocksMixin {
    @Redirect(method = "renderBlockOnInventory(Lnet/minecraft/core/block/Block;IF)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glColor4f(FFFF)V"))
    private void addAlphaSupport(float red, float green, float blue, float alpha){
        GL11.glColor4f(red, green, blue, UtilGui.blockAlpha);
    }
    @Inject(method = "renderBlockOnInventory(Lnet/minecraft/core/block/Block;IF)V", at = @At(value = "TAIL"))
    private void resetAlpha(Block block, int metadata, float brightness, CallbackInfo ci){
        GL11.glColor4f(1f, 1f, 1f, 1f);
    }
}
