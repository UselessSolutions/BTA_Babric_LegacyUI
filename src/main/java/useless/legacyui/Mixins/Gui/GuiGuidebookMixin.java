package useless.legacyui.Mixins.Gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGuidebook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiGuidebook.class, remap = false)
public class GuiGuidebookMixin {
    @Inject(method = "keyTyped(CIII)V", at = @At("HEAD"))
    private void closeWhenButtonBPressed(char c, int i, int mouseX, int mouseY, CallbackInfo ci){
        if (i == 14) {
            Minecraft.getMinecraft(this).thePlayer.closeScreen();
        }
    }
}
