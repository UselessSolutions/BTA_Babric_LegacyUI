package useless.legacyui.Mixins.Gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiContainer;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.option.ImmersiveModeOption;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.Lighting;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import useless.legacyui.Settings.ModSettings;

@Mixin(value = GuiIngame.class, remap = false)
public class GuiIngameMixin {
    @Shadow
    protected Minecraft mc;
    @Redirect(method = "renderGameOverlay(FZII)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/ImmersiveModeOption;drawHotbar()Z"))
    private boolean dontRenderInGui(ImmersiveModeOption instance){
        if (ModSettings.legacyOptions.getHideHotbarInGUIs().value){
            if (mc.currentScreen instanceof GuiContainer){
                return false;
            }
        }
        return instance.drawHotbar();
    }
    @Inject(method = "renderGameOverlay(FZII)V", at = @At("HEAD"))
    private void paperDoll(float partialTicks, boolean flag, int mouseX, int mouseY, CallbackInfo ci){
        renderDoll(mouseX, mouseY);
    }
    @Unique
    private void renderDoll(int mousex, int mouseY){
        int width = this.mc.resolution.scaledWidth;
        int height = this.mc.resolution.scaledHeight;
        int GUIx = width/2;
        int GUIy = height/2;
        GL11.glEnable(32826);
        GL11.glEnable(2903);
        GL11.glEnable(2929);
        GL11.glPushMatrix();
        GL11.glTranslatef(GUIx + 51 + 44, GUIy + 75 - 5, 50.0f);
        float f1 = 30.0f;
        GL11.glScalef(-f1, f1, f1);
        GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
        float f2 = this.mc.thePlayer.renderYawOffset;
        float f3 = this.mc.thePlayer.yRot;
        float f4 = this.mc.thePlayer.xRot;
        float f5 = (float)(GUIx + 51 + 44) - mousex;
        float f6 = (float)(GUIy + 75 - 50 - 5) - mouseY;
        GL11.glRotatef(135.0f, 0.0f, 1.0f, 0.0f);
        Lighting.enableLight();
        GL11.glRotatef(-135.0f, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(-((float)Math.atan(f6 / 40.0f)) * 20.0f, 1.0f, 0.0f, 0.0f);
        this.mc.thePlayer.renderYawOffset = (float)Math.atan(f5 / 40.0f) * 20.0f;
        this.mc.thePlayer.yRot = (float)Math.atan(f5 / 40.0f) * 40.0f;
        this.mc.thePlayer.xRot = -((float)Math.atan(f6 / 40.0f)) * 20.0f;
        this.mc.thePlayer.entityBrightness = 1.0f;
        GL11.glTranslatef(0.0f, this.mc.thePlayer.heightOffset, 0.0f);
        EntityRenderDispatcher.instance.viewLerpYaw = 180.0f;
        EntityRenderDispatcher.instance.renderEntityWithPosYaw(this.mc.thePlayer, 0.0, 0.0, 0.0, 0.0f, 1.0f);
        this.mc.thePlayer.entityBrightness = 0.0f;
        this.mc.thePlayer.renderYawOffset = f2;
        this.mc.thePlayer.yRot = f3;
        this.mc.thePlayer.xRot = f4;
        GL11.glPopMatrix();
        Lighting.disable();
        GL11.glDisable(32826);
    }
}
