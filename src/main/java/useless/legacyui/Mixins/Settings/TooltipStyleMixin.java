package useless.legacyui.Mixins.Settings;

import net.minecraft.client.option.enums.TooltipStyle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import useless.legacyui.LegacyUI;

@Mixin(value = TooltipStyle.class, remap = false)
public class TooltipStyleMixin {
    @Inject(method = "getFilePath()Ljava/lang/String;", at = @At("HEAD"), cancellable = true)
    private void legacyTooltipImage(CallbackInfoReturnable<String> cir){
        if (LegacyUI.modSettings != null && LegacyUI.modSettings.getForceLegacyTooltip().value){
            cir.setReturnValue("assets/legacyui/gui/tooltip/legacy.png");
        }
    }
    @Inject(method = "getTranslationKey()Ljava/lang/String;", at = @At("HEAD"), cancellable = true)
    private void legacyTooltipKey(CallbackInfoReturnable<String> cir){
        if (LegacyUI.modSettings != null && LegacyUI.modSettings.getForceLegacyTooltip().value) {
            cir.setReturnValue("options.legacyui.tooltipStyle.legacy");
        }
    }
    @Inject(method = "toString()Ljava/lang/String;", at = @At("HEAD"), cancellable = true)
    private void legacyTooltipName(CallbackInfoReturnable<String> cir){
        if (LegacyUI.modSettings != null && LegacyUI.modSettings.getForceLegacyTooltip().value){
            cir.setReturnValue("legacy");
        }
    }
}
