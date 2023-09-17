package useless.legacyui.Gui.GuiScreens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.render.Tessellator;
import org.lwjgl.opengl.GL11;
import useless.legacyui.Helper.IconHelper;

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
}
