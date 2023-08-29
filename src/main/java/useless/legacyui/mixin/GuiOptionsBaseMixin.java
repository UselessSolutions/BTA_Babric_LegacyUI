package useless.legacyui.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.options.GuiOptionsPageBase;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.core.sound.SoundType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import useless.legacyui.ConfigTranslations;
import useless.legacyui.Gui.IButtonSounds;
import useless.legacyui.LegacyUI;

@Mixin(value = GuiOptionsPageBase.class, remap = false)
public class GuiOptionsBaseMixin extends GuiScreen {
    public GuiButton button;

    @Redirect(method = "mouseClicked(III)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiButton;mousePressed(Lnet/minecraft/client/Minecraft;II)Z"))
    private boolean buttonStealer(GuiButton button, Minecraft minecraft, int i, int j){
        this.button = button;
        return button.mousePressed(minecraft, i, j);
    }
    @Redirect(method = "mouseClicked(III)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/SoundManager;playSound(Ljava/lang/String;Lnet/minecraft/core/sound/SoundType;FF)V"))
    private void legacySound(SoundManager soundManager, String soundPath, SoundType soundType, float volume, float pitch){
        if (button != null && button instanceof IButtonSounds){
            IButtonSounds soundButton = (IButtonSounds)button;
            if (!soundButton.isMuted()){
                if (LegacyUI.config.getBoolean(ConfigTranslations.USE_LEGACY_SOUNDS.getKey())){
                    soundManager.playSound("legacyui.ui.press", soundType, volume, pitch);
                }
                else {
                    soundManager.playSound(soundButton.getSound(), soundType, volume, pitch);
                }
            }
        }
        else {
            if (LegacyUI.config.getBoolean(ConfigTranslations.USE_LEGACY_SOUNDS.getKey())){
                soundManager.playSound("legacyui.ui.press", soundType, volume, pitch);
            }
            else {
                soundManager.playSound(soundPath, soundType, volume, pitch);
            }
        }


    }
}
