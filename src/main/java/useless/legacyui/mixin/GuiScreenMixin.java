package useless.legacyui.mixin;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.core.sound.SoundType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import useless.legacyui.ConfigTranslations;
import useless.legacyui.LegacyUI;

@Mixin(value = GuiScreen.class, remap = false)
public class GuiScreenMixin extends Gui {
    @Redirect(method = "mouseClicked(III)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/SoundManager;playSound(Ljava/lang/String;Lnet/minecraft/core/sound/SoundType;FF)V"))
    private void legacySound(SoundManager soundManager, String soundPath, SoundType soundType, float volume, float pitch){
        if (LegacyUI.config.getBoolean(ConfigTranslations.USE_LEGACY_SOUNDS.getKey())){
            soundManager.playSound("legacyui.ui.press", soundType, volume, pitch);
        }
        else {
            soundManager.playSound(soundPath, soundType, volume, pitch);
        }

    }
}
