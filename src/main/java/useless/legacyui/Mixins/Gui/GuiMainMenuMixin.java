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
    @Inject(method = "<init>()V", at = @At("TAIL"))
    private void init(CallbackInfo ci){
        UtilGui.panoCount = Math.max(1, Minecraft.getMinecraft(this).texturePackList.selectedTexturePack.getFilesInDirectory("/assets/legacyui/panoramas/").length);
        UtilGui.currentPano = rand.nextInt(UtilGui.panoCount);
    }
    @Inject(method = "drawBackground()V", at = @At("HEAD"), cancellable = true)
    private void panorama(CallbackInfo ci){
        if (LegacyUI.modSettings.getEnablePanorama().value && UtilGui.panoCount != -1 && !mc.gameSettings.alphaMenu.value){
            UtilGui.drawPanorama(this);
            ci.cancel();
        }
    }
}
