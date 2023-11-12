package useless.legacyui.Mixins;

import net.minecraft.client.render.FontRenderer;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ItemEntityRenderer.class, remap = false)
public class ItemEntityRendererMixin {
    @Inject(method = "drawItemIntoGui(Lnet/minecraft/client/render/FontRenderer;Lnet/minecraft/client/render/RenderEngine;IIIIIFF)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glDisable(I)V", ordinal = 1))
    private void enableAlphaOnItems(FontRenderer fontrenderer, RenderEngine renderengine, int i, int j, int k, int l, int i1, float brightness, float alpha, CallbackInfo ci){
        GL11.glEnable(GL11.GL_BLEND);
    }
    @Inject(method = "drawItemIntoGui(Lnet/minecraft/client/render/FontRenderer;Lnet/minecraft/client/render/RenderEngine;IIIIIFF)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glEnable(I)V", ordinal = 1))
    private void disableAlphaAfterItems(FontRenderer fontrenderer, RenderEngine renderengine, int i, int j, int k, int l, int i1, float brightness, float alpha, CallbackInfo ci){
        GL11.glDisable(GL11.GL_BLEND);
    }
}
