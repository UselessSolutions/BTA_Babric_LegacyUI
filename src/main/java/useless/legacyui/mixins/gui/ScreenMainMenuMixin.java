package useless.legacyui.mixins.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.ScreenMainMenu;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import useless.legacyui.gui.screens.UtilGui;
import useless.legacyui.LegacyUI;

import java.util.Random;

@Mixin(value = ScreenMainMenu.class, remap = false)
public class ScreenMainMenuMixin extends Screen {
    @Shadow @Final private static Random rand;
    @Inject(method = "<init>()V", at = @At("TAIL"))
    private void init(CallbackInfo ci){
        UtilGui.panoCount = Math.max(1, 11/*Minecraft.getMinecraft().texturePackList.selectedTexturePack.getFilesInDirectory("/assets/legacyui/panoramas/").length*/); // TODO make not hardcoded
        UtilGui.currentPano = rand.nextInt(UtilGui.panoCount);
    }
    @Inject(method = "renderTexturedBackground", at = @At("HEAD"), cancellable = true)
    private void panorama(CallbackInfo ci){
        if (LegacyUI.modSettings.legacyui$getEnablePanorama().value && UtilGui.panoCount != -1 && !mc.gameSettings.alphaMenu.value){
            UtilGui.drawPanorama(this);
            ci.cancel();
        }
    }
}
