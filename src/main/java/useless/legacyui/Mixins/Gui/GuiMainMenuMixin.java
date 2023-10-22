package useless.legacyui.Mixins.Gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.render.Tessellator;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import useless.legacyui.Gui.GuiScreens.UtilGui;
import useless.legacyui.LegacyUI;

import java.util.Random;

@Mixin(value = GuiMainMenu.class, remap = false)
public class GuiMainMenuMixin extends GuiScreen {
    @Shadow @Final private static Random rand;
    @Unique
    private int panoCount = -1;
    @Unique
    private int currentPano = -1;
    @Inject(method = "<init>()V", at = @At("TAIL"))
    private void init(CallbackInfo ci){
        panoCount = Math.max(1, Minecraft.getMinecraft(this).texturePackList.selectedTexturePack.getFilesInDirectory("/assets/legacyui/panoramas/").length);
        currentPano = rand.nextInt(panoCount);
    }
    @Inject(method = "drawBackground(I)V", at = @At("HEAD"), cancellable = true)
    private void panorama(int i, CallbackInfo ci){
        if (LegacyUI.modSettings.getEnablePanorama().value && panoCount != -1){
            UtilGui.drawPanorama(this, currentPano);
            ci.cancel();
        }
    }

    @Redirect(method = "drawBackground(I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Tessellator;setColorRGBA_F(FFFF)V"))
    private void changeBrightness(Tessellator instance, float r, float g, float b, float a){
        float brightness = LegacyUI.modSettings.getMainMenuBrightness().value;
        instance.setColorRGBA_F(brightness, brightness, brightness, a);
    }
}
