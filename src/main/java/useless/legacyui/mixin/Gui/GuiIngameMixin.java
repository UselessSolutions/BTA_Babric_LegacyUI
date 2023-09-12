package useless.legacyui.mixin.Gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiContainer;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.option.ImmersiveModeOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import useless.legacyui.ConfigTranslations;
import useless.legacyui.LegacyUI;

@Mixin(value = GuiIngame.class, remap = false)
public class GuiIngameMixin {
    @Shadow
    protected Minecraft mc;

    @Redirect(method = "renderGameOverlay(FZII)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/ImmersiveModeOption;drawHotbar()Z"))
    private boolean dontRenderInGui(ImmersiveModeOption instance){
        if (LegacyUI.config.getBoolean(ConfigTranslations.HIDE_HOTBAR_IN_GUIS.getKey())){
            if (mc.currentScreen instanceof GuiContainer){
                return false;
            }
        }
        return instance.drawHotbar();
    }
}
