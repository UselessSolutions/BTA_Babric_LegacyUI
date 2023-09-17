package useless.legacyui.Mixins.Gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.core.sound.SoundType;
import net.minecraft.core.util.helper.Color;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import useless.legacyui.LegacySoundManager;
import useless.legacyui.ModSettings;
import useless.legacyui.Gui.GuiElements.Buttons.IButtonSounds;

@Mixin(value = GuiScreen.class, remap = false)
public class GuiScreenMixin extends Gui {
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
                if (ModSettings.Sounds.UseLegacySounds()){
                    LegacySoundManager.play.press(true);
                }
                else {
                    soundManager.playSound(soundButton.getSound(), soundType, volume, pitch);
                }
            }
        }
        else {
            if (ModSettings.Sounds.UseLegacySounds()){
                LegacySoundManager.play.press(true);
            }
            else {
                soundManager.playSound(soundPath, soundType, volume, pitch);
            }
        }
    }
    @Redirect(method = "drawWorldBackground(I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/util/helper/Color;getARGB()I"))
    private int newBackgroundColor(Color instance){
        return ModSettings.Colors.GuiBackgroundColor();
    }
}
