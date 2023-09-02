package useless.legacyui.mixin.Gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiContainer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.core.sound.SoundType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import useless.legacyui.ConfigTranslations;
import useless.legacyui.GlobalOverrides;
import useless.legacyui.LegacyUI;

@Mixin(value = GuiContainer.class, remap = false)
public class GuiContainerMixin extends GuiScreen {
    @Inject(method = "onGuiClosed()V", at = @At("HEAD"))
    private void closingSound(CallbackInfo cbi){
        if (LegacyUI.config.getBoolean(ConfigTranslations.USE_LEGACY_SOUNDS.getKey()) && !GlobalOverrides.getBackOverride()){
            Minecraft.getMinecraft(this).sndManager.playSound("legacyui.ui.back", SoundType.GUI_SOUNDS, 1,1);
        }
    }
}
