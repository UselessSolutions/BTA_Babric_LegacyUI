package useless.legacyui.Rendering;

import net.minecraft.client.render.*;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.core.Global;
import net.minecraft.core.block.Block;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class LegacyItemEntityRenderer extends ItemEntityRenderer {
    private final LegacyRenderBlocks renderBlocks = new LegacyRenderBlocks();
    private final Random random = new Random();
    public boolean field_27004_a = true;
    public LegacyItemEntityRenderer() {
        this.shadowSize = 0.15f;
        this.shadowOpacity = 0.75f;
    }
    public void drawItemIntoGui(FontRenderer fontrenderer, RenderEngine renderengine, int i, int j, int k, int l, int i1) {
        this.drawItemIntoGui(fontrenderer, renderengine, i, j, k, l, i1, 1.0f, 1.0f, 1.0f);
    }

    public void drawItemIntoGui(FontRenderer fontrenderer, RenderEngine renderengine, int i, int j, int k, int l, int i1, float brightness, float alpha, float scale) {
        if (i < Block.blocksList.length && ((BlockModel) BlockModelDispatcher.getInstance().getDispatch(Block.blocksList[i])).shouldItemRender3d()) {
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            int j1 = i;
            renderengine.bindTexture(renderengine.getTexture("/terrain.png"));
            Block block = Block.blocksList[j1];
            GL11.glPushMatrix();
            float offset = (9 * (scale -1)); // Offset to place block at right location in slot
            GL11.glTranslatef(l - 2  + offset, i1 + 3 + offset, -3.0f);
            GL11.glScalef(10.0f, 10.0f, 10.0f);
            GL11.glTranslatef(1.0f, 0.5f, 1.0f);
            GL11.glScalef(1.0f, 1.0f, -1.0f);
            GL11.glRotatef(210.0f, 1.0f, 0.0f, 0.0f);
            GL11.glRotatef(45.0f, 0.0f, 1.0f, 0.0f);
            int l1 = Item.itemsList[i].getColorFromDamage(j);
            float f2 = (float) (l1 >> 16 & 0xFF) / 255.0f;
            float f4 = (float) (l1 >> 8 & 0xFF) / 255.0f;
            float f5 = (float) (l1 & 0xFF) / 255.0f;
            if (this.field_27004_a) {
                GL11.glColor4f(f2 * brightness, f4 * brightness, f5 * brightness, alpha);
            } else {
                GL11.glColor4f(brightness, brightness, brightness, alpha);
            }
            GL11.glRotatef(-90.0f, 0.0f, 1.0f, 0.0f);
            this.renderBlocks.useInventoryTint = this.field_27004_a;
            GL11.glScaled(scale, scale, scale); // Scales item
            this.renderBlocks.renderBlockOnInventory(block, j, brightness); // Renders blocks
            this.renderBlocks.useInventoryTint = true;
            GL11.glPopMatrix();
            GL11.glDisable(3042);
        } else if (k >= 0) {
            int tileWidth;
            GL11.glDisable(2896);
            if (i < Block.blocksList.length) {
                renderengine.bindTexture(renderengine.getTexture("/terrain.png"));
                tileWidth = TextureFX.tileWidthTerrain;
            } else {
                renderengine.bindTexture(renderengine.getTexture("/gui/items.png"));
                tileWidth = TextureFX.tileWidthItems;
            }
            int k1 = Item.itemsList[i].getColorFromDamage(j);
            float f = (float) (k1 >> 16 & 0xFF) / 255.0f;
            float f1 = (float) (k1 >> 8 & 0xFF) / 255.0f;
            float f3 = (float) (k1 & 0xFF) / 255.0f;
            if (this.field_27004_a) {
                GL11.glColor4f(f * brightness, f1 * brightness, f3 * brightness, alpha);
            } else {
                GL11.glColor4f(brightness, brightness, brightness, alpha);
            }
            this.renderTexturedQuad(l, i1, k % Global.TEXTURE_ATLAS_WIDTH_TILES * tileWidth, k / Global.TEXTURE_ATLAS_WIDTH_TILES * tileWidth, tileWidth, tileWidth, scale);
            GL11.glEnable(2896);
        }
        GL11.glEnable(2884);
    }

    public void renderItemIntoGUI(FontRenderer fontrenderer, RenderEngine renderengine, ItemStack itemstack, int i, int j, float alpha) {
        if (itemstack == null) {
            return;
        }
        this.drawItemIntoGui(fontrenderer, renderengine, itemstack.itemID, itemstack.getMetadata(), itemstack.getIconIndex(), i, j, 1.0f, alpha, 1.0f);
    }

    public void renderItemIntoGUI(FontRenderer fontrenderer, RenderEngine renderengine, ItemStack itemstack, int i, int j, float brightness, float alpha) {
        if (itemstack == null) {
            return;
        }
        this.drawItemIntoGui(fontrenderer, renderengine, itemstack.itemID, itemstack.getMetadata(), itemstack.getIconIndex(), i, j, brightness, alpha, 1.0f);
    }
    public void renderItemIntoGUI(FontRenderer fontrenderer, RenderEngine renderengine, ItemStack itemstack, int i, int j, float brightness, float alpha, float scale) {
        if (itemstack == null) {
            return;
        }
        this.drawItemIntoGui(fontrenderer, renderengine, itemstack.itemID, itemstack.getMetadata(), itemstack.getIconIndex(), i, j, brightness, alpha, scale);
    }

    // Scaled Rendering of items
    public void renderTexturedQuad(int x, int y, int tileX, int tileY, int tileWidth, int tileHeight, float scale) {
        float f = 0.0F;
        float f1 = 1.0F / (float)(Global.TEXTURE_ATLAS_WIDTH_TILES * tileWidth);
        float f2 = 1.0F / (float)(Global.TEXTURE_ATLAS_WIDTH_TILES * tileHeight);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double)(x + 0), (double)(y + 16 * scale), 0.0, (double)((float)(tileX + 0) * f1), (double)((float)(tileY + tileHeight) * f2));
        tessellator.addVertexWithUV((double)(x + 16 * scale), (double)(y + 16 * scale), 0.0, (double)((float)(tileX + tileWidth) * f1), (double)((float)(tileY + tileHeight) * f2));
        tessellator.addVertexWithUV((double)(x + 16 * scale), (double)(y + 0), 0.0, (double)((float)(tileX + tileWidth) * f1), (double)((float)(tileY + 0) * f2));
        tessellator.addVertexWithUV((double)(x + 0), (double)(y + 0), 0.0, (double)((float)(tileX + 0) * f1), (double)((float)(tileY + 0) * f2));
        tessellator.draw();
    }
    public void renderItemOverlayIntoGUI(FontRenderer fontrenderer, RenderEngine renderengine, ItemStack itemstack, int i, int j, String override, int fontHeight) {
        if (itemstack == null) {
            return;
        }
        if (itemstack.stackSize != 1 || override != null) {
            String s = "" + itemstack.stackSize;
            if (override != null) {
                s = override;
            }
            GL11.glDisable(2896);
            GL11.glDisable(2929);
            int previousHeight = fontrenderer.fontHeight;
            fontrenderer.fontHeight = fontHeight;
            fontrenderer.drawStringWithShadow(s, i + (int)((19-2- fontrenderer.getStringWidth(s)) * fontHeight/9f), j + (int) ((6  + 3)* fontHeight/9f), 0xFFFFFF);
            fontrenderer.fontHeight = previousHeight;
            GL11.glEnable(2896);
            GL11.glEnable(2929);
        }
        if (itemstack.isItemDamaged() || itemstack.getItem().showFullDurability()) {
            int k = (int)Math.round(13.0 - (double)itemstack.getItemDamageForDisplay() * 13.0 / (double)itemstack.getMaxDamage());
            int l = (int)Math.round(255.0 - (double)itemstack.getItemDamageForDisplay() * 255.0 / (double)itemstack.getMaxDamage());
            GL11.glDisable(2896);
            GL11.glDisable(2929);
            GL11.glDisable(3553);
            Tessellator tessellator = Tessellator.instance;
            int i1 = 255 - l << 16 | l << 8;
            int j1 = (255 - l) / 4 << 16 | 0x3F00;
            this.renderQuad(tessellator, i + 2, j + 13, 13, 2, 0);
            this.renderQuad(tessellator, i + 2, j + 13, 12, 1, j1);
            this.renderQuad(tessellator, i + 2, j + 13, k, 1, i1);
            GL11.glEnable(3553);
            GL11.glEnable(2896);
            GL11.glEnable(2929);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
    private void renderQuad(Tessellator tessellator, int i, int j, int k, int l, int i1) {
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_I(i1);
        tessellator.addVertex(i + 0, j + 0, 0.0);
        tessellator.addVertex(i + 0, j + l, 0.0);
        tessellator.addVertex(i + k, j + l, 0.0);
        tessellator.addVertex(i + k, j + 0, 0.0);
        tessellator.draw();
    }
}
