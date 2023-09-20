package useless.legacyui.Mixins.Gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGuidebook;
import net.minecraft.client.render.FontRenderer;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import useless.legacyui.Settings.ModSettings;

@Mixin(value = GuiGuidebook.class, remap = false, priority = 999)
public class GuiGuidebookMixin {
    @Inject(method = "keyTyped(CIII)V", at = @At("HEAD"), cancellable = true)
    private void closeWhenButtonBPressed(char c, int i, int mouseX, int mouseY, CallbackInfo ci){
        if ((i == 14 || i == 1) && !Keyboard.isKeyDown(Keyboard.KEY_BACK)) {
            Minecraft.getMinecraft(this).thePlayer.closeScreen();
            ci.cancel();
        }
    }
    @Redirect(method = "drawGuiContainerForegroundLayer()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGuidebook;drawStringNoShadow(Lnet/minecraft/client/render/FontRenderer;Ljava/lang/String;III)V"))
    public void drawGuiContainerForegroundLayer(GuiGuidebook instance, FontRenderer fontRenderer, String s, int x, int y, int color) {
        instance.drawStringNoShadow(fontRenderer, s, x, y, ModSettings.Colors.GuiLabelColor());
    }
}
