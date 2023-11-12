package useless.legacyui.Gui.GuiScreens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.core.item.ItemStack;
import org.lwjgl.opengl.GL11;
import useless.legacyui.Helper.IconHelper;
import useless.legacyui.LegacyUI;
import useless.legacyui.Mixins.Gui.GuiIngameAccessor;

public class UtilGui {
    public static Minecraft mc = Minecraft.getMinecraft(Minecraft.class);
    public static void bindTexture(String texture){
        int inventoryTex = mc.renderEngine.getTexture(texture);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        mc.renderEngine.bindTexture(inventoryTex);
    }
    public static void drawTexturedModalRect(Gui gui, int x, int y, int u, int v, int width, int height, float scale) {
        float uScale = scale;
        float vScale = scale;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x + 0, y + height, gui.zLevel, (float)(u + 0) * uScale, (float)(v + height) * vScale);
        tessellator.addVertexWithUV(x + width, y + height, gui.zLevel, (float)(u + width) * uScale, (float)(v + height) * vScale);
        tessellator.addVertexWithUV(x + width, y + 0, gui.zLevel, (float)(u + width) * uScale, (float)(v + 0) * vScale);
        tessellator.addVertexWithUV(x + 0, y + 0, gui.zLevel, (float)(u + 0) * uScale, (float)(v + 0) * vScale);
        tessellator.draw();
    }

    public static void drawIconTexture(Gui gui, int x, int y, int[] iconCoord, float scale){
        int width = (int) (IconHelper.ICON_RESOLUTION * scale);
        drawTexturedModalRect(gui,
                x,
                y,
                (iconCoord[0] * width),
                (iconCoord[1] * width),
                width,
                width,
                (1f/(IconHelper.ICON_RESOLUTION * IconHelper.ICON_ATLAS_WIDTH_TILES)) * (1/scale));
    }
    public static float blockAlpha = 1f;
    public static void renderInventorySlot(GuiIngame gui, int itemIndex, int x, int y, float delta, float alpha) {
        blockAlpha = alpha;
        ItemEntityRenderer itemRenderer = ((GuiIngameAccessor)gui).getItemRenderer();
        ItemStack itemstack = mc.thePlayer.inventory.mainInventory[itemIndex];
        if (itemstack == null) {
            return;
        }
        float animProgress = (float)itemstack.animationsToGo - delta;
        if (animProgress > 0.0f) {
            GL11.glPushMatrix();
            float f2 = 1.0f + animProgress / 5.0f;
            GL11.glTranslatef(x + 8, y + 12, 0.0f);
            GL11.glScalef(1.0f / f2, (f2 + 1.0f) / 2.0f, 1.0f);
            GL11.glTranslatef(-(x + 8), -(y + 12), 0.0f);
        }
        itemRenderer.renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, itemstack, x, y, alpha);
        if (animProgress > 0.0f) {
            GL11.glPopMatrix();
        }
        itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, mc.renderEngine, itemstack, x, y, alpha);
        blockAlpha = 1f;
    }
    public static void drawPanorama(GuiScreen gui, int panoNum){
        GL11.glDisable(2896);
        GL11.glDisable(2912);
        Tessellator tessellator = Tessellator.instance;
        float imageWidth = 820f;
        float imageHeight = 144f;
        float imageAspectRatio = imageWidth / imageHeight;
        float screenAspectRatio = (float)gui.width / (float)gui.height;
        float finalAspectRatio = (float)gui.width / imageWidth / ((float)gui.height / imageHeight);
        GL11.glBindTexture(3553, mc.renderEngine.getTexture("%blur%/assets/legacyui/panoramas/pn_"+panoNum+".png"));
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        tessellator.startDrawingQuads();
        float brightness = LegacyUI.modSettings.getMainMenuBrightness().value;
        tessellator.setColorRGBA_F(brightness, brightness, brightness, 1.0f);
        int num = (1000 * ((LegacyUI.modSettings.getPanoramaScrollLength().value + 1) * 15));
        float offset = (float) (System.currentTimeMillis() % num) /num;
        if (screenAspectRatio < imageAspectRatio) {
            tessellator.addVertexWithUV(0.0, gui.height, 0.0, (0.5f + offset) - finalAspectRatio / 2.0f, 1.0);
            tessellator.addVertexWithUV(gui.width, gui.height, 0.0, (0.5f + offset) + finalAspectRatio / 2.0f, 1.0);
            tessellator.addVertexWithUV(gui.width, 0.0, 0.0, (0.5f + offset) + finalAspectRatio / 2.0f, 0.0);
            tessellator.addVertexWithUV(0.0, 0.0, 0.0, (0.5f + offset) - finalAspectRatio / 2.0f, 0.0);
        } else {
            tessellator.addVertexWithUV(0.0, gui.height, 0.0, 0.0, 0.5f + 0.5f / finalAspectRatio);
            tessellator.addVertexWithUV(gui.width, gui.height, 0.0, 1.0, 0.5f + 0.5f / finalAspectRatio);
            tessellator.addVertexWithUV(gui.width, 0.0, 0.0, 1.0, 0.5f - 0.5f / finalAspectRatio);
            tessellator.addVertexWithUV(0.0, 0.0, 0.0, 0.0, 0.5f - 0.5f / finalAspectRatio);
        }
        tessellator.draw();
    }
    private static float prevYRot = -1000;
    private static float desiredYRot = 0;
    public static void drawPaperDoll(boolean drawRight){
        if (prevYRot == -1000){
            prevYRot = mc.thePlayer.yRot;
        }
        GL11.glColor4d(1,1,1,1);
        desiredYRot += mc.thePlayer.yRot - prevYRot;
        float lookRange = 30;
        desiredYRot = Math.max(desiredYRot, -lookRange);
        desiredYRot = Math.min(desiredYRot, lookRange);
        GL11.glEnable(32826);
        GL11.glEnable(2903);
        GL11.glEnable(2929);
        GL11.glPushMatrix();
        int width = mc.resolution.scaledWidth;
        float xOff = drawRight ? width - 30: 30;
        float yOff = 75;
        GL11.glTranslatef(xOff, yOff, 0.0f);
        float f1 = 30.0f;
        GL11.glScalef(-f1, f1, f1);
        GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
        float oYawOff = mc.thePlayer.renderYawOffset;
        float oYRot = mc.thePlayer.yRot;
        float oXRot = mc.thePlayer.xRot;

        Lighting.enableLight();
        mc.thePlayer.renderYawOffset = drawRight ? 15: -15;
        mc.thePlayer.yRot = desiredYRot;

        mc.thePlayer.entityBrightness = 1.0f;
        GL11.glTranslatef(0.0f, mc.thePlayer.heightOffset, 0.0f);
        EntityRenderDispatcher.instance.viewLerpYaw = 180.0f;
        EntityRenderDispatcher.instance.renderEntityWithPosYaw(mc.thePlayer, 0.0, 0.0, 0.0, 0.0f, 1.0f);
        mc.thePlayer.entityBrightness = 0.0f;

        mc.thePlayer.renderYawOffset = oYawOff;
        mc.thePlayer.yRot = oYRot;
        mc.thePlayer.xRot = oXRot;
        GL11.glPopMatrix();
        Lighting.disable();
        GL11.glDisable(32826);
        prevYRot = mc.thePlayer.yRot;
    }
    private static int prevItem = -1;
    private static long timeHotbarLastActive = 0;
    public static float getHotbarAlpha(){
        if (!LegacyUI.modSettings.getEnableHUDFadeout().value) return 1;
        if (mc.thePlayer.inventory.currentItem != prevItem) {
            timeHotbarLastActive = System.currentTimeMillis();
        }
        prevItem = mc.thePlayer.inventory.currentItem;
        long currentTime = System.currentTimeMillis();
        if (currentTime - timeHotbarLastActive <= (LegacyUI.modSettings.getHUDFadeoutDelay().value * 10000)){
            return 1f;
        }
        return (float) Math.max(1f - ((currentTime - timeHotbarLastActive) - (LegacyUI.modSettings.getHUDFadeoutDelay().value * 10000))/1000f, 0.2);
    }
}
