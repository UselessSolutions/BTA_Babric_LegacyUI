package useless.legacyui.Rendering;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.block.model.BlockModelRenderBlocks;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.core.Global;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.tag.ItemTags;
import net.minecraft.core.util.helper.MathHelper;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class LegacyItemEntityRenderer extends ItemEntityRenderer {
    private final RenderBlocks renderBlocks = new RenderBlocks();
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
            GL11.glTranslatef(l - 2, i1 + 3, -3.0f);
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
            this.renderBlocks.renderBlockOnInventory(block, j, brightness);
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
            this.renderTexturedQuad(l, i1, k % Global.TEXTURE_ATLAS_WIDTH_TILES * tileWidth, k / Global.TEXTURE_ATLAS_WIDTH_TILES * tileWidth, tileWidth, tileWidth);
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
}
