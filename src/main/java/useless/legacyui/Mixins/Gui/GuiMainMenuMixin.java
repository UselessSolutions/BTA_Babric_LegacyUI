package useless.legacyui.Mixins.Gui;

import net.minecraft.client.gui.GuiMainMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiMainMenu.class, remap = false)
public class GuiMainMenuMixin {
    @Inject(method = "drawBackground(I)V", @At("HEAD"), cancellable = true)
    private void panorama(int i, CallbackInfo ci){

    }
}
