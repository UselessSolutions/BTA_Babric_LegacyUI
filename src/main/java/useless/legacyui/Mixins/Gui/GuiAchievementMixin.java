package useless.legacyui.Mixins.Gui;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiAchievement;
import net.minecraft.core.achievement.Achievement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import useless.legacyui.LegacySoundManager;

@Mixin(value = GuiAchievement.class, remap = false)
public class GuiAchievementMixin extends Gui {
    @Inject(method = "queueTakenAchievement(Lnet/minecraft/core/achievement/Achievement;)V", at = @At("HEAD"))
    private void achievementSound(Achievement achievement, CallbackInfo cbi){
        LegacySoundManager.play.achievement(false);
    }
}
