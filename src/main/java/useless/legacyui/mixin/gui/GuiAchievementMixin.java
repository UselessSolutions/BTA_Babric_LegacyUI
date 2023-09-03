package useless.legacyui.mixin.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiAchievement;
import net.minecraft.core.achievement.Achievement;
import net.minecraft.core.sound.SoundType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiAchievement.class, remap = false)
public class GuiAchievementMixin extends Gui {

    @Inject(method = "queueTakenAchievement(Lnet/minecraft/core/achievement/Achievement;)V", at = @At("HEAD"))
    private void achievementSound(Achievement achievement, CallbackInfo cbi){
        Minecraft minecraft = Minecraft.getMinecraft(this);
        minecraft.sndManager.playSound("legacyui.ui.achievement", SoundType.GUI_SOUNDS, 1, 1);
    }

}
