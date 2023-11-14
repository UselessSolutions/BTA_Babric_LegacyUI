package useless.legacyui.Mixins;

import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import useless.legacyui.Helper.KeyboardHelper;

@Mixin(value = WorldRenderer.class, remap = false)
public class WorldRendererMixin {
    @Inject(method = "updateCameraAndRender(F)V", at = @At("HEAD"))
    private void updateEveryFrame(float renderPartialTicks, CallbackInfo ci){
        KeyboardHelper.update();
    }
}
