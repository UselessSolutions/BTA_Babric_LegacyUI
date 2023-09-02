package useless.legacyui.mixin.Gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.Lighting;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import useless.legacyui.GlobalOverrides;
import useless.legacyui.LegacyUI;

@Mixin(value = GuiIngame.class, remap = false)
public class GuiIngameMixin extends Gui {

    @Unique
    protected Minecraft mc = Minecraft.getMinecraft(this);
    @Inject(method = "renderGameOverlay(FZII)V", at = @At("HEAD"))
    private void paperdoll(float partialTicks, boolean flag, int mouseX, int mouseY, CallbackInfo ci){
        GlobalOverrides.offset += Mouse.getDWheel();
        LegacyUI.LOGGER.info(String.valueOf(GlobalOverrides.offset));
        int width = mc.resolution.scaledWidth;
        int height = mc.resolution.scaledHeight;
        int j = (width) / 2;
        int k = (height) / 2;
        GL11.glEnable(32826);
        GL11.glEnable(2903);
        GL11.glPushMatrix();
        GL11.glTranslatef(0,0, ((float) GlobalOverrides.offset) /1000f);
        Lighting.enableLight();
        EntityRenderDispatcher.instance.viewLerpYaw = 180.0f;
        EntityRenderDispatcher.instance.renderEntityWithPosYaw(this.mc.thePlayer, 0.0, 0.0, 0.0, 0.0f, 1.0f);
        GL11.glPopMatrix();
        Lighting.disable();
        GL11.glDisable(32826);
    }
}
