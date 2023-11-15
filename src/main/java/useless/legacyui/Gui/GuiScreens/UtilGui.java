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

import java.util.Random;

public class UtilGui {
    public static Minecraft mc = Minecraft.getMinecraft(Minecraft.class);
    public static final int tabScrollRepeatDelay = 1000/6;
    public static final int tabScrollInitialDelay = 150;
    public static final int verticalScrollRepeatDelay = 1000/6;
    public static final int verticalScrollInitialDelay = 150;
    public static final int repeatCraftDelay = 1000/10;
    public static final int initialCraftDelay = 300;
    public static void bindTexture(String texture){
        int inventoryTex = mc.renderEngine.getTexture(texture);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        mc.renderEngine.bindTexture(inventoryTex);
    }
    public static void drawTexturedModalRect(Gui gui, double x, double y, double u, double v, double width, double height, double scale) {
        double uScale = scale;
        double vScale = scale;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x + 0, y + height, gui.zLevel, (u + 0) * uScale, (v + height) * vScale);
        tessellator.addVertexWithUV(x + width, y + height, gui.zLevel, (u + width) * uScale, (v + height) * vScale);
        tessellator.addVertexWithUV(x + width, y + 0, gui.zLevel, (u + width) * uScale, (v + 0) * vScale);
        tessellator.addVertexWithUV(x + 0, y + 0, gui.zLevel, (u + 0) * uScale, (v + 0) * vScale);
        tessellator.draw();
    }

    public static void drawIconTexture(Gui gui, double x, double y, int[] iconCoord, double scale){
        double width = IconHelper.ICON_RESOLUTION * scale;
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
    private static float lastOffset = -1;
    private static int scrollsCompleted = 0;
    private static long fadeTime = 0;
    private static long prevTime = -1;
    private static boolean changedPano = false;
    public static int panoCount = -1;
    public static int currentPano = -1;
    private static final Random random = new Random();
    public static void drawPanorama(GuiScreen gui){
        if (prevTime == -1){
            prevTime = System.currentTimeMillis();
        }
        long deltaTime = System.currentTimeMillis() - prevTime;
        prevTime = System.currentTimeMillis();

        GL11.glDisable(2896);
        GL11.glDisable(2912);
        Tessellator tessellator = Tessellator.instance;
        float imageWidth = 820f;
        float imageHeight = 144f;
        float imageAspectRatio = imageWidth / imageHeight;
        float screenAspectRatio = (float)gui.width / (float)gui.height;
        float finalAspectRatio = (float)gui.width / imageWidth / ((float)gui.height / imageHeight);
        GL11.glBindTexture(3553, mc.renderEngine.getTexture("%blur%/assets/legacyui/panoramas/pn_"+currentPano+".png"));
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        tessellator.startDrawingQuads();
        float brightness = LegacyUI.modSettings.getMainMenuBrightness().value;


        int scrollLength = (1000 * ((LegacyUI.modSettings.getPanoramaScrollLength().value + 1) * 15));

        float offset = (float) (System.currentTimeMillis() % scrollLength) /scrollLength;

        if (lastOffset == -1){
            lastOffset = offset;
        }

        if (offset < lastOffset){
            scrollsCompleted++;
        }
        if (scrollsCompleted > 0){
            fadeTime += deltaTime;
            float fadeFactor = Math.abs((500f - fadeTime)/500f);
            tessellator.setColorRGBA_F(brightness * fadeFactor, brightness * fadeFactor, brightness * fadeFactor, 1.0f);
            if (500 <= fadeTime && fadeTime < 1000){
                if (!changedPano){
                    changedPano = true;
                    currentPano = random.nextInt(panoCount);
                }

            } else if (fadeTime >= 1000) {
                changedPano = false;
                scrollsCompleted = 0;
                fadeTime = 0;
            }
        } else {
            tessellator.setColorRGBA_F(brightness, brightness, brightness, 1.0f);
        }
        lastOffset = offset;
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
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
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
        if (mc.currentScreen != null) return 1f;
        if (!LegacyUI.modSettings.getEnableHUDFadeout().value) return 1;
        if (mc.thePlayer.inventory.currentItem != prevItem) {
            timeHotbarLastActive = System.currentTimeMillis();
        }
        prevItem = mc.thePlayer.inventory.currentItem;
        long currentTime = System.currentTimeMillis();
        if (currentTime - timeHotbarLastActive <= (LegacyUI.modSettings.getHUDFadeoutDelay().value * 10000)){
            return 1f;
        }
        return (float) Math.max(1f - ((currentTime - timeHotbarLastActive) - (LegacyUI.modSettings.getHUDFadeoutDelay().value * 10000))/1000f, LegacyUI.modSettings.getHUDFadeoutAlpha().value);
    }
}
