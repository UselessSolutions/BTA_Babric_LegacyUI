package useless.legacyui.Mixins.Modded.PotatoLogistics;

import deboni.potatologistics.gui.GuiFilter;
import net.minecraft.client.render.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import useless.legacyui.LegacyUI;

@Mixin(value = GuiFilter.class, remap = false)
public class GuiFilterMixin {
    @Redirect(method = "drawGuiContainerForegroundLayer()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/FontRenderer;drawString(Ljava/lang/String;III)V"))
    private void changeColor(FontRenderer instance, String text, int x, int y, int color){
        instance.drawString(text, x, y, LegacyUI.modSettings.getGuiLabelColor().value.value);
    }
}
