package useless.legacyui.mixins.gui;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.sound.SoundEngine;
import net.minecraft.core.sound.SoundCategory;
import net.minecraft.core.util.helper.Color;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import useless.legacyui.LegacySoundManager;
import useless.legacyui.LegacyUI;

@Mixin(value = Screen.class, remap = false, priority = 2000)
public abstract class ScreenMixin extends Gui {

    @Shadow protected Minecraft mc;

    @Redirect(method = "mouseClicked", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/ButtonElement;playSound:Z", opcode = Opcodes.GETFIELD))
    private boolean ignorePlaysoundBool(ButtonElement instance) {
        return true;
    }

    @Redirect(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/SoundEngine;playSound(Ljava/lang/String;Lnet/minecraft/core/sound/SoundCategory;FF)V"))
    private void reimplPlaySoundBool(SoundEngine instance, String name, SoundCategory category, float volume, float pitch, @Local(name = "button") ButtonElement button) {
        if (button.playSound) {
            if (LegacyUI.modSettings.legacyui$getUseLegacySounds().value){
                LegacySoundManager.play.press(true);
            }
            else {
                this.mc.sndManager.playSound("random.click", SoundCategory.GUI_SOUNDS, 1.0F, 1.0F);
            }
        }
    }

    @Redirect(method = "renderBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/util/helper/Color;getARGB()I"))
    private int newBackgroundColor(final Color instance){
        return LegacyUI.modSettings.legacyui$getGuiBackgroundColor().value.value;
    }
}
