package useless.legacyui.Mixins.Modded.NoNameDyes;

import goocraft4evr.nonamedyes.client.gui.GuiBleacher;
import net.minecraft.client.render.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import useless.legacyui.Settings.ModSettings;
@Mixin(value = GuiBleacher.class, remap = false)
public class GuiBleacherMixin {
    @Redirect(method = "drawGuiContainerForegroundLayer()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/FontRenderer;drawString(Ljava/lang/String;III)V"))
    private void changeColor(FontRenderer instance, String text, int x, int y, int color){
        instance.drawString(text, x, y, ModSettings.legacyOptions.getGuiLabelColor().value.value);
    }
}
