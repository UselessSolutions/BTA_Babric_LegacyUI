package useless.legacyui.mixins.gui;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.sound.SoundEngine;
import net.minecraft.core.sound.SoundCategory;
import net.minecraft.core.util.helper.Color;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import useless.legacyui.LegacySoundManager;
import useless.legacyui.LegacyUI;

import java.util.List;

@Mixin(value = Screen.class, remap = false, priority = 2000)
public abstract class ScreenMixin extends Gui {

    @Shadow protected Minecraft mc;

    @Shadow protected abstract void buttonClicked(ButtonElement button);

    @Shadow public List<ButtonElement> buttons;

    /**
     * @author Useless
     * @reason Need to fix a bug with muted buttons not being clickable
     */
    @Overwrite
    public void mouseClicked(final int mx, final int my, final int buttonNum) {
        if (buttonNum == 0) {
            for(final ButtonElement button : this.buttons) {
                if (button.mouseClicked(this.mc, mx, my)) {
                    if (button.playSound) {
                        if (LegacyUI.modSettings.legacyui$getUseLegacySounds().value){
                            LegacySoundManager.play.press(true);
                        }
                        else {
                            this.mc.sndManager.playSound("random.click", SoundCategory.GUI_SOUNDS, 1.0F, 1.0F);
                        }
                    }
                    if (button.listener != null) {
                        button.listener.listen(button);
                    } else {
                        this.buttonClicked(button);
                    }

                    return;
                }
            }
        }
    }

    @Redirect(method = "renderBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/util/helper/Color;getARGB()I"))
    private int newBackgroundColor(final Color instance){
        return LegacyUI.modSettings.legacyui$getGuiBackgroundColor().value.value;
    }
}
