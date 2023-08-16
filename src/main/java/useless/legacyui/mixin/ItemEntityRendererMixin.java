package useless.legacyui.mixin;

import net.minecraft.client.render.*;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.core.Global;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.item.Item;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import useless.legacyui.LegacyUI;

@Mixin(value = ItemEntityRenderer.class, remap = false)
public class ItemEntityRendererMixin extends EntityRenderer<EntityItem> {
    @Shadow
    private RenderBlocks renderBlocks;
    @Shadow
    private boolean field_27004_a;

    @Override
    public void doRender(EntityItem entity, double d, double e, double f, float g, float h) {

    }
    @Shadow
    public void drawItemIntoGui(FontRenderer fontrenderer, RenderEngine renderengine, int i, int j, int k, int l, int i1, float brightness, float alpha) {
        float f1;
        float f3;
        if (i < Block.blocksList.length && ((BlockModel) BlockModelDispatcher.getInstance().getDispatch(Block.blocksList[i])).shouldItemRender3d()) {
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            renderengine.bindTexture(renderengine.getTexture("/terrain.png"));
            Block block = Block.blocksList[i];
            GL11.glPushMatrix();
            GL11.glTranslatef((float)(l - 2), (float)(i1 + 3), -3.0F);
            GL11.glScalef(10.0F, 10.0F, 10.0F);
            GL11.glTranslatef(1.0F, 0.5F, 1.0F);
            GL11.glScalef(1.0F, 1.0F, -1.0F);
            GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            int l1 = Item.itemsList[i].getColorFromDamage(j);
            f1 = (float)(l1 >> 16 & 255) / 255.0F;
            f3 = (float)(l1 >> 8 & 255) / 255.0F;
            float f5 = (float)(l1 & 255) / 255.0F;
            if (this.field_27004_a) {
                GL11.glColor4f(f1 * brightness, f3 * brightness, f5 * brightness, alpha);
            } else {
                GL11.glColor4f(brightness, brightness, brightness, alpha);
            }

            GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
            this.renderBlocks.useInventoryTint = this.field_27004_a;
            this.renderBlocks.renderBlockOnInventory(block, j, brightness);
            this.renderBlocks.useInventoryTint = true;
            GL11.glPopMatrix();
            GL11.glDisable(3042);
        } else if (k >= 0) {
            GL11.glDisable(2896);
            int tileWidth;
            if (i < Block.blocksList.length) {
                renderengine.bindTexture(renderengine.getTexture("/terrain.png"));
                tileWidth = TextureFX.tileWidthTerrain;
            } else {
                renderengine.bindTexture(renderengine.getTexture("/gui/items.png"));
                tileWidth = TextureFX.tileWidthItems;
            }

            int k1 = Item.itemsList[i].getColorFromDamage(j);
            float f = (float)(k1 >> 16 & 255) / 255.0F;
            f1 = (float)(k1 >> 8 & 255) / 255.0F;
            f3 = (float)(k1 & 255) / 255.0F;
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

    @Overwrite
    public void renderTexturedQuad(int x, int y, int tileX, int tileY, int tileWidth, int tileHeight) {
        float f = 0.0F;
        float f1 = 1.0F / (float)(Global.TEXTURE_ATLAS_WIDTH_TILES * tileWidth);
        float f2 = 1.0F / (float)(Global.TEXTURE_ATLAS_WIDTH_TILES * tileHeight);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double)(x + 0), (double)(y + 16*LegacyUI.currentRenderScale), 0.0, (double)((float)(tileX + 0) * f1), (double)((float)(tileY + tileHeight) * f2));
        tessellator.addVertexWithUV((double)(x + 16*LegacyUI.currentRenderScale), (double)(y + 16*LegacyUI.currentRenderScale), 0.0, (double)((float)(tileX + tileWidth) * f1), (double)((float)(tileY + tileHeight) * f2));
        tessellator.addVertexWithUV((double)(x + 16*LegacyUI.currentRenderScale), (double)(y + 0), 0.0, (double)((float)(tileX + tileWidth) * f1), (double)((float)(tileY + 0) * f2));
        tessellator.addVertexWithUV((double)(x + 0), (double)(y + 0), 0.0, (double)((float)(tileX + 0) * f1), (double)((float)(tileY + 0) * f2));
        tessellator.draw();
    }
}
