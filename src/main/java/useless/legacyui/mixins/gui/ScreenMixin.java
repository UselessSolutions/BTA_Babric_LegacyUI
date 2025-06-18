package useless.legacyui.mixins.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.sound.SoundEngine;
import net.minecraft.core.sound.SoundCategory;
import net.minecraft.core.util.helper.Color;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import useless.legacyui.LegacySoundManager;
import useless.legacyui.LegacyUI;

@Mixin(value = Screen.class, remap = false, priority = 2000)
public class ScreenMixin extends Gui {
    public ButtonElement button;

    @Redirect(method = "mouseClicked(III)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/ButtonElement;mouseClicked(Lnet/minecraft/client/Minecraft;II)Z"))
    private boolean buttonStealer(final ButtonElement button, final Minecraft minecraft, final int i, final int j){
        this.button = button;
        return button.mouseClicked(minecraft, i, j);
    }
    @Redirect(method = "mouseClicked(III)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/SoundEngine;playSound(Ljava/lang/String;Lnet/minecraft/core/sound/SoundCategory;FF)V"))
    private void legacySound(final SoundEngine soundManager, final String soundPath, final SoundCategory soundType, final float volume, final float pitch){
        if (LegacyUI.modSettings.legacyui$getUseLegacySounds().value){
            LegacySoundManager.play.press(true);
        }
        else {
            soundManager.playSound("random.click", soundType, volume, pitch);
        }
    }
    @Redirect(method = "renderBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/util/helper/Color;getARGB()I"))
    private int newBackgroundColor(final Color instance){
        return LegacyUI.modSettings.legacyui$getGuiBackgroundColor().value.value;
    }
}
