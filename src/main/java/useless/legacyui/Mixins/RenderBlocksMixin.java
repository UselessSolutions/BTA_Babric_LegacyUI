package useless.legacyui.Mixins;

import net.minecraft.client.render.RenderBlocks;
import net.minecraft.core.block.Block;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = RenderBlocks.class, remap = false)
public class RenderBlocksMixin {
    @Inject(method = "renderBlockOnInventory(Lnet/minecraft/core/block/Block;IFF)V", at = @At(value = "HEAD"))
    private void addAlphaSupport(Block block, int metadata, float brightness, float alpha, CallbackInfo ci){
        GL11.glEnable(GL11.GL_BLEND);
    }
}