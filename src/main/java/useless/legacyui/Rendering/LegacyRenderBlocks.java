package useless.legacyui.Rendering;

import net.minecraft.client.render.RenderBlocks;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.TextureFX;
import net.minecraft.client.render.block.color.BlockColor;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.block.model.BlockModelRenderBlocks;
import net.minecraft.core.Global;
import net.minecraft.core.block.Block;
import net.minecraft.core.util.helper.Side;
import org.lwjgl.opengl.GL11;
import useless.legacyui.LegacyUI;

public class LegacyRenderBlocks extends RenderBlocks {
    private boolean flipTexture = false;
    private int overrideBlockTexture = -1;
    private int uvRotateEast = 0;
    private int uvRotateWest = 0;
    private int uvRotateSouth = 0;
    private int uvRotateNorth = 0;
    private int uvRotateTop = 0;
    private int uvRotateBottom = 0;
    private float colorRedTopLeft;
    private float colorRedBottomLeft;
    private float colorRedBottomRight;
    private float colorRedTopRight;
    private float colorGreenTopLeft;
    private float colorGreenBottomLeft;
    private float colorGreenBottomRight;
    private float colorGreenTopRight;
    private float colorBlueTopLeft;
    private float colorBlueBottomLeft;
    private float colorBlueBottomRight;
    private float colorBlueTopRight;
    private boolean enableAO;
    public void renderBlockOnInventory(Block block, int metadata, float brightness, float scale) {
        float f4;
        float f9;
        float f8;
        float f42;
        int renderType;
        boolean isGrass;
        Tessellator tessellator = Tessellator.instance;
        boolean bl = isGrass = block.id == Block.grass.id;
        if (this.useInventoryTint) {
            int j = ((BlockColor) BlockColorDispatcher.getInstance().getDispatch(block)).getFallbackColor(metadata);
            if (isGrass) {
                j = 0xFFFFFF;
            }
            float f1 = (float)(j >> 16 & 0xFF) / 255.0f;
            float f3 = (float)(j >> 8 & 0xFF) / 255.0f;
            float f5 = (float)(j & 0xFF) / 255.0f;
            GL11.glColor4f(f1 * brightness, f3 * brightness, f5 * brightness, 1.0f);
        }
        if ((renderType = ((BlockModelRenderBlocks) BlockModelDispatcher.getInstance().getDispatch(block)).renderType) == 0 || renderType == 16 || renderType == 27 || renderType == 30) {
            float yOffset = 0.5f;
            if (renderType == 16) {
                metadata = 1;
            }
            if (renderType == 30) {
                yOffset = 0.25f;
            }
            block.setBlockBoundsForItemRender();
            GL11.glTranslatef(-0.5f, 0.0f - yOffset, -0.5f);
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, -1.0f, 0.0f);
            this.renderBottomFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.BOTTOM, metadata), scale);
            tessellator.draw();
            if (isGrass && this.useInventoryTint) {
                int l = ((BlockColor)BlockColorDispatcher.getInstance().getDispatch(block)).getFallbackColor(metadata);
                f42 = (float)(l >> 16 & 0xFF) / 255.0f;
                f8 = (float)(l >> 8 & 0xFF) / 255.0f;
                f9 = (float)(l & 0xFF) / 255.0f;
                GL11.glColor4f(f42 * brightness, f8 * brightness, f9 * brightness, 1.0f);
            }
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, 1.0f, 0.0f);
            this.renderTopFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.TOP, metadata), scale);
            tessellator.draw();
            if (isGrass && this.useInventoryTint) {
                GL11.glColor4f(brightness, brightness, brightness, 1.0f);
            }
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, 0.0f, -1.0f);
            this.renderNorthFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.NORTH, metadata), scale);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, 0.0f, 1.0f);
            this.renderSouthFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.SOUTH, metadata), scale);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(-1.0f, 0.0f, 0.0f);
            this.renderWestFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.WEST, metadata), scale);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(1.0f, 0.0f, 0.0f);
            this.renderEastFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.EAST, metadata), scale);
            tessellator.draw();
            GL11.glTranslatef(0.5f, 0.5f, 0.5f);
        } else if (renderType == 21) {
            int mossCoord = Block.texCoordToIndex(0, 2);
            GL11.glColor4f(brightness, brightness, brightness, 1.0f);
            block.setBlockBoundsForItemRender();
            GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, -1.0f, 0.0f);
            this.renderBottomFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.BOTTOM, metadata), scale);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, 1.0f, 0.0f);
            this.renderTopFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.TOP, metadata), scale);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, 0.0f, -1.0f);
            this.renderNorthFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.NORTH, metadata), scale);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, 0.0f, 1.0f);
            this.renderSouthFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.SOUTH, metadata), scale);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(-1.0f, 0.0f, 0.0f);
            this.renderWestFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.WEST, metadata), scale);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(1.0f, 0.0f, 0.0f);
            this.renderEastFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.EAST, metadata), scale);
            tessellator.draw();
            int l = ((BlockColor)BlockColorDispatcher.getInstance().getDispatch(block)).getFallbackColor(metadata);
            f42 = (float)(l >> 16 & 0xFF) / 255.0f;
            f8 = (float)(l >> 8 & 0xFF) / 255.0f;
            f9 = (float)(l & 0xFF) / 255.0f;
            GL11.glColor4f(f42 * brightness, f8 * brightness, f9 * brightness, 1.0f);
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, -1.0f, 0.0f);
            this.renderBottomFace(block, 0.0, 0.0, 0.0, mossCoord, scale);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, 1.0f, 0.0f);
            this.renderTopFace(block, 0.0, 0.0, 0.0, mossCoord, scale);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, 0.0f, -1.0f);
            this.renderNorthFace(block, 0.0, 0.0, 0.0, mossCoord, scale);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, 0.0f, 1.0f);
            this.renderSouthFace(block, 0.0, 0.0, 0.0, mossCoord, scale);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(-1.0f, 0.0f, 0.0f);
            this.renderWestFace(block, 0.0, 0.0, 0.0, mossCoord, scale);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(1.0f, 0.0f, 0.0f);
            this.renderEastFace(block, 0.0, 0.0, 0.0, mossCoord, scale);
            tessellator.draw();
            GL11.glTranslatef(0.5f, 0.5f, 0.5f);
        } else if (renderType == 22) {
            GL11.glColor4f(brightness, brightness, brightness, 1.0f);
            block.setBlockBoundsForItemRender();
            GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, -1.0f, 0.0f);
            this.renderBottomFace(block, 0.0, 0.0, 0.0, Block.chestPlanksOak.getBlockTextureFromSideAndMetadata(Side.BOTTOM, 0), scale);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, 1.0f, 0.0f);
            this.renderTopFace(block, 0.0, 0.0, 0.0, Block.chestPlanksOak.getBlockTextureFromSideAndMetadata(Side.TOP, 0), scale);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, 0.0f, -1.0f);
            this.renderNorthFace(block, 0.0, 0.0, 0.0, Block.chestPlanksOak.getBlockTextureFromSideAndMetadata(Side.NORTH, 0), scale);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, 0.0f, 1.0f);
            this.renderSouthFace(block, 0.0, 0.0, 0.0, Block.chestPlanksOak.getBlockTextureFromSideAndMetadata(Side.SOUTH, 0), scale);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(-1.0f, 0.0f, 0.0f);
            this.renderWestFace(block, 0.0, 0.0, 0.0, Block.chestPlanksOak.getBlockTextureFromSideAndMetadata(Side.WEST, 0), scale);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(1.0f, 0.0f, 0.0f);
            this.renderEastFace(block, 0.0, 0.0, 0.0, Block.chestPlanksOak.getBlockTextureFromSideAndMetadata(Side.EAST, 0), scale);
            tessellator.draw();
            int l = ((BlockColor)BlockColorDispatcher.getInstance().getDispatch(block)).getFallbackColor(metadata);
            f4 = (float)(l >> 16 & 0xFF) / 255.0f;
            float f82 = (float)(l >> 8 & 0xFF) / 255.0f;
            float f92 = (float)(l & 0xFF) / 255.0f;
            GL11.glColor4f(f4 * brightness, f82 * brightness, f92 * brightness, 1.0f);
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, -1.0f, 0.0f);
            this.renderBottomFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.BOTTOM, 0), scale);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, 1.0f, 0.0f);
            this.renderTopFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.TOP, 0), scale);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, 0.0f, -1.0f);
            this.renderNorthFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.NORTH, 0), scale);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, 0.0f, 1.0f);
            this.renderSouthFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.SOUTH, 0), scale);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(-1.0f, 0.0f, 0.0f);
            this.renderWestFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.WEST, 0), scale);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(1.0f, 0.0f, 0.0f);
            this.renderEastFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.EAST, 0), scale);
            tessellator.draw();
            GL11.glTranslatef(0.5f, 0.5f, 0.5f);
        } else if (renderType == 23) {
            int cherryCoord = Block.texCoordToIndex(3, 17);
            int l = ((BlockColor)BlockColorDispatcher.getInstance().getDispatch(block)).getFallbackColor(metadata);
            f42 = (float)(l >> 16 & 0xFF) / 255.0f;
            f8 = (float)(l >> 8 & 0xFF) / 255.0f;
            f9 = (float)(l & 0xFF) / 255.0f;
            GL11.glColor4f(f42 * brightness, f8 * brightness, f9 * brightness, 1.0f);
            block.setBlockBoundsForItemRender();
            GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, -1.0f, 0.0f);
            this.renderBottomFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.BOTTOM, metadata), scale);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, 1.0f, 0.0f);
            this.renderTopFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.TOP, metadata), scale);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, 0.0f, -1.0f);
            this.renderNorthFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.NORTH, metadata), scale);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, 0.0f, 1.0f);
            this.renderSouthFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.SOUTH, metadata), scale);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(-1.0f, 0.0f, 0.0f);
            this.renderWestFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.WEST, metadata), scale);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(1.0f, 0.0f, 0.0f);
            this.renderEastFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.EAST, metadata), scale);
            tessellator.draw();
            GL11.glColor4f(brightness, brightness, brightness, 1.0f);
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, -1.0f, 0.0f);
            this.renderBottomFace(block, 0.0, 0.0, 0.0, cherryCoord, scale);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, 1.0f, 0.0f);
            this.renderTopFace(block, 0.0, 0.0, 0.0, cherryCoord, scale);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, 0.0f, -1.0f);
            this.renderNorthFace(block, 0.0, 0.0, 0.0, cherryCoord, scale);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, 0.0f, 1.0f);
            this.renderSouthFace(block, 0.0, 0.0, 0.0, cherryCoord, scale);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(-1.0f, 0.0f, 0.0f);
            this.renderWestFace(block, 0.0, 0.0, 0.0, cherryCoord, scale);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(1.0f, 0.0f, 0.0f);
            this.renderEastFace(block, 0.0, 0.0, 0.0, cherryCoord, scale);
            tessellator.draw();
            GL11.glTranslatef(0.5f, 0.5f, 0.5f);
        } else if (renderType == 1) {
            boolean isTallGrass;
            boolean bl2 = isTallGrass = block.id == Block.tallgrass.id;
            if (isTallGrass && this.useInventoryTint) {
                int l = ((BlockColor)BlockColorDispatcher.getInstance().getDispatch(block)).getFallbackColor(metadata);
                f42 = (float)(l >> 16 & 0xFF) / 255.0f;
                f8 = (float)(l >> 8 & 0xFF) / 255.0f;
                f9 = (float)(l & 0xFF) / 255.0f;
                GL11.glColor4f(f42 * brightness, f8 * brightness, f9 * brightness, 1.0f);
            }
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, -1.0f, 0.0f);
            this.renderCrossedSquares(block, metadata, -0.5, -0.5, -0.5);
            tessellator.draw();
        } else if (renderType == 13) {
            block.setBlockBoundsForItemRender();
            GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
            float f2 = 0.0625f;
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, -1.0f, 0.0f);
            this.renderBottomFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.BOTTOM, metadata), scale);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, 1.0f, 0.0f);
            this.renderTopFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.TOP, metadata), scale);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, 0.0f, -1.0f);
            tessellator.offsetTranslation(0.0f, 0.0f, f2);
            this.renderNorthFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.NORTH, metadata), scale);
            tessellator.offsetTranslation(0.0f, 0.0f, -f2);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, 0.0f, 1.0f);
            tessellator.offsetTranslation(0.0f, 0.0f, -f2);
            this.renderSouthFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.SOUTH, metadata), scale);
            tessellator.offsetTranslation(0.0f, 0.0f, f2);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(-1.0f, 0.0f, 0.0f);
            tessellator.offsetTranslation(f2, 0.0f, 0.0f);
            this.renderWestFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.WEST, metadata), scale);
            tessellator.offsetTranslation(-f2, 0.0f, 0.0f);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(1.0f, 0.0f, 0.0f);
            tessellator.offsetTranslation(-f2, 0.0f, 0.0f);
            this.renderEastFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.EAST, metadata), scale);
            tessellator.offsetTranslation(f2, 0.0f, 0.0f);
            tessellator.draw();
            GL11.glTranslatef(0.5f, 0.5f, 0.5f);
        } else if (renderType == 6) {
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, -1.0f, 0.0f);
            this.func_1245_b(block, metadata, -0.5, -0.5, -0.5);
            tessellator.draw();
        } else if (renderType == 2) {
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, -1.0f, 0.0f);
            this.renderTorchAtAngle(block, -0.5, -0.5, -0.5, 0.0, 0.0);
            tessellator.draw();
        } else if (renderType == 10) {
            for (int l = 0; l < 2; ++l) {
                if (l == 0) {
                    block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.5f);
                }
                if (l == 1) {
                    block.setBlockBounds(0.0f, 0.0f, 0.5f, 1.0f, 0.5f, 1.0f);
                }
                GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0f, -1.0f, 0.0f);
                this.renderBottomFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.BOTTOM, metadata), scale);
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0f, 1.0f, 0.0f);
                this.renderTopFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.TOP, metadata), scale);
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0f, 0.0f, -1.0f);
                this.renderNorthFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.NORTH, metadata), scale);
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0f, 0.0f, 1.0f);
                this.renderSouthFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.SOUTH, metadata), scale);
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(-1.0f, 0.0f, 0.0f);
                this.renderWestFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.WEST, metadata), scale);
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(1.0f, 0.0f, 0.0f);
                this.renderEastFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.EAST, metadata), scale);
                tessellator.draw();
                GL11.glTranslatef(0.5f, 0.5f, 0.5f);
            }
        } else if (renderType == 11) {
            for (int i1 = 0; i1 < 4; ++i1) {
                f4 = 0.125f;
                if (i1 == 0) {
                    block.setBlockBounds(0.5f - f4, 0.0f, 0.0f, 0.5f + f4, 1.0f, f4 * 2.0f);
                }
                if (i1 == 1) {
                    block.setBlockBounds(0.5f - f4, 0.0f, 1.0f - f4 * 2.0f, 0.5f + f4, 1.0f, 1.0f);
                }
                f4 = 0.0625f;
                if (i1 == 2) {
                    block.setBlockBounds(0.5f - f4, 1.0f - f4 * 3.0f, -f4 * 2.0f, 0.5f + f4, 1.0f - f4, 1.0f + f4 * 2.0f);
                }
                if (i1 == 3) {
                    block.setBlockBounds(0.5f - f4, 0.5f - f4 * 3.0f, -f4 * 2.0f, 0.5f + f4, 0.5f - f4, 1.0f + f4 * 2.0f);
                }
                GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0f, -1.0f, 0.0f);
                this.renderBottomFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.BOTTOM, metadata), scale);
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0f, 1.0f, 0.0f);
                this.renderTopFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.TOP, metadata), scale);
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0f, 0.0f, -1.0f);
                this.renderNorthFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.NORTH, metadata), scale);
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0f, 0.0f, 1.0f);
                this.renderSouthFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.SOUTH, metadata), scale);
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(-1.0f, 0.0f, 0.0f);
                this.renderWestFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.WEST, metadata), scale);
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(1.0f, 0.0f, 0.0f);
                this.renderEastFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.EAST, metadata), scale);
                tessellator.draw();
                GL11.glTranslatef(0.5f, 0.5f, 0.5f);
            }
            block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        }
        if (renderType == 20) {
            block.setBlockBoundsForItemRender();
            GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, -1.0f, 0.0f);
            this.renderBottomFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.BOTTOM, metadata), scale);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, 1.0f, 0.0f);
            this.renderTopFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.TOP, metadata), scale);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, 0.0f, -1.0f);
            this.renderNorthFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.NORTH, metadata), scale);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0f, 0.0f, 1.0f);
            this.renderSouthFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.SOUTH, metadata), scale);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(-1.0f, 0.0f, 0.0f);
            this.renderWestFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.WEST, metadata), scale);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(1.0f, 0.0f, 0.0f);
            this.renderEastFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.EAST, metadata), scale);
            tessellator.draw();
            GL11.glTranslatef(0.5f, 0.5f, 0.5f);
        } else if (renderType == 18) {
            for (int k1 = 0; k1 < 3; ++k1) {
                float f6 = 0.0625f;
                if (k1 == 0) {
                    block.setBlockBounds(0.5f - f6, 0.3f, 0.0f, 0.5f + f6, 1.0f, f6 * 2.0f);
                }
                if (k1 == 1) {
                    block.setBlockBounds(0.5f - f6, 0.3f, 1.0f - f6 * 2.0f, 0.5f + f6, 1.0f, 1.0f);
                }
                f6 = 0.0625f;
                if (k1 == 2) {
                    block.setBlockBounds(0.5f - f6, 0.5f, 0.0f, 0.5f + f6, 1.0f - f6, 1.0f);
                }
                GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0f, -1.0f, 0.0f);
                this.renderBottomFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.BOTTOM, metadata), scale);
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0f, 1.0f, 0.0f);
                this.renderTopFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.TOP, metadata), scale);
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0f, 0.0f, -1.0f);
                this.renderNorthFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.NORTH, metadata), scale);
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0f, 0.0f, 1.0f);
                this.renderSouthFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.SOUTH, metadata), scale);
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(-1.0f, 0.0f, 0.0f);
                this.renderWestFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.WEST, metadata), scale);
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(1.0f, 0.0f, 0.0f);
                this.renderEastFace(block, 0.0, 0.0, 0.0, block.getBlockTextureFromSideAndMetadata(Side.EAST, metadata), scale);
                tessellator.draw();
                GL11.glTranslatef(0.5f, 0.5f, 0.5f);
            }
            block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        } else if (renderType == 31) {
            for (int i1 = 0; i1 < 2; ++i1) {
                float f43 = 0.0625f;
                if (i1 == 0) {
                    block.setBlockBounds(0.5f - f43, 0.0f, 0.0f, 0.5f + f43, 1.0f, f43 * 2.0f);
                }
                if (i1 == 1) {
                    block.setBlockBounds(0.5f - f43, 0.0f, 1.0f - f43 * 2.0f, 0.5f + f43, 1.0f, 1.0f);
                }
                GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0f, -1.0f, 0.0f);
                this.renderBottomFace(block, 0.0, 0.0, 0.0, Block.texCoordToIndex(16, 6), scale);
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0f, 1.0f, 0.0f);
                this.renderTopFace(block, 0.0, 0.0, 0.0, Block.texCoordToIndex(16, 6), scale);
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0f, 0.0f, -1.0f);
                this.renderNorthFace(block, 0.0, 0.0, 0.0, Block.texCoordToIndex(16, 6), scale);
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0f, 0.0f, 1.0f);
                this.renderSouthFace(block, 0.0, 0.0, 0.0, Block.texCoordToIndex(16, 6), scale);
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(-1.0f, 0.0f, 0.0f);
                this.renderWestFace(block, 0.0, 0.0, 0.0, Block.texCoordToIndex(16, 6), scale);
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(1.0f, 0.0f, 0.0f);
                this.renderEastFace(block, 0.0, 0.0, 0.0, Block.texCoordToIndex(16, 6), scale);
                tessellator.draw();
                GL11.glTranslatef(0.5f, 0.5f, 0.5f);
            }
            GL11.glTranslatef(-0.5f, -0.5f, -0.5f);
            GL11.glDisable(2884);
            block.setBlockBounds(0.0f, 0.0f, 0.0f, 0.5f, 1.0f, 1.0f);
            tessellator.startDrawingQuads();
            tessellator.setNormal(1.0f, 0.0f, 0.0f);
            this.renderEastFace(block, 0.0, 0.0, 0.0, Block.texCoordToIndex(8, 19), scale);
            tessellator.draw();
            GL11.glTranslatef(0.5f, 0.5f, 0.5f);
            block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        }
    }
    public void renderBottomFace(Block block, double d, double d1, double d2, int i, float scale) {
        Tessellator tessellator = Tessellator.instance;
        if (this.overrideBlockTexture >= 0) {
            i = this.overrideBlockTexture;
        }
        int j = i % Global.TEXTURE_ATLAS_WIDTH_TILES * TextureFX.tileWidthTerrain;
        int k = i / Global.TEXTURE_ATLAS_WIDTH_TILES * TextureFX.tileWidthTerrain;
        double d3 = ((double)j + block.minX * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
        double d4 = ((double)j + block.maxX * (double)TextureFX.tileWidthTerrain - 0.01) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
        double d5 = ((double)k + block.minZ * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
        double d6 = ((double)k + block.maxZ * (double)TextureFX.tileWidthTerrain - 0.01) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
        if (block.minX < 0.0 || block.maxX > 1.0) {
            d3 = ((float)j + 0.0f) / (float)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d4 = ((float)j + ((float)TextureFX.tileWidthTerrain - 0.01f)) / (float)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
        }
        if (block.minZ < 0.0 || block.maxZ > 1.0) {
            d5 = ((float)k + 0.0f) / (float)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d6 = ((float)k + ((float)TextureFX.tileWidthTerrain - 0.01f)) / (float)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
        }
        double d7 = d4;
        double d8 = d3;
        double d9 = d5;
        double d10 = d6;
        if (this.uvRotateBottom == 2) {
            d3 = ((double)j + block.minZ * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d5 = ((double)(k + TextureFX.tileWidthTerrain) - block.maxX * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d4 = ((double)j + block.maxZ * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d6 = ((double)(k + TextureFX.tileWidthTerrain) - block.minX * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d7 = d4;
            d8 = d3;
            d9 = d5;
            d10 = d6;
            d7 = d3;
            d8 = d4;
            d5 = d6;
            d6 = d9;
        } else if (this.uvRotateBottom == 1) {
            d3 = ((double)(j + TextureFX.tileWidthTerrain) - block.maxZ * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d5 = ((double)k + block.minX * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d4 = ((double)(j + TextureFX.tileWidthTerrain) - block.minZ * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d6 = ((double)k + block.maxX * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d7 = d4;
            d8 = d3;
            d9 = d5;
            d10 = d6;
            d3 = d7;
            d4 = d8;
            d9 = d6;
            d10 = d5;
        } else if (this.uvRotateBottom == 3) {
            d3 = ((double)(j + TextureFX.tileWidthTerrain) - block.minX * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d4 = ((double)(j + TextureFX.tileWidthTerrain) - block.maxX * (double)TextureFX.tileWidthTerrain - 0.01) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d5 = ((double)(k + TextureFX.tileWidthTerrain) - block.minZ * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d6 = ((double)(k + TextureFX.tileWidthTerrain) - block.maxZ * (double)TextureFX.tileWidthTerrain - 0.01) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d7 = d4;
            d8 = d3;
            d9 = d5;
            d10 = d6;
        }
        double d11 = d + block.minX;
        double d12 = d + block.maxX;
        double d13 = d1 + block.minY;
        double d14 = d2 + block.minZ;
        double d15 = d2 + block.maxZ;
        if (this.enableAO) {
            tessellator.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
            tessellator.addVertexWithUV(d11 * scale, d13 * scale, d15 * scale, d8, d10);
            tessellator.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
            tessellator.addVertexWithUV(d11 * scale, d13 * scale, d14 * scale, d3, d5);
            tessellator.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
            tessellator.addVertexWithUV(d12 * scale, d13 * scale, d14 * scale, d7, d9);
            tessellator.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
            tessellator.addVertexWithUV(d12 * scale, d13 * scale, d15 * scale, d4, d6);
        } else {
            tessellator.addVertexWithUV(d11 * scale, d13 * scale, d15 * scale, d8, d10);
            tessellator.addVertexWithUV(d11 * scale, d13 * scale, d14 * scale, d3, d5);
            tessellator.addVertexWithUV(d12 * scale, d13 * scale, d14 * scale, d7, d9);
            tessellator.addVertexWithUV(d12 * scale, d13 * scale, d15 * scale, d4, d6);
        }
        //LegacyUI.LOGGER.info("Running: " + scale);
    }

    public void renderTopFace(Block block, double d, double d1, double d2, int i, float scale) {
        Tessellator tessellator = Tessellator.instance;
        if (this.overrideBlockTexture >= 0) {
            i = this.overrideBlockTexture;
        }
        int j = i % Global.TEXTURE_ATLAS_WIDTH_TILES * TextureFX.tileWidthTerrain;
        int k = i / Global.TEXTURE_ATLAS_WIDTH_TILES * TextureFX.tileWidthTerrain;
        double d3 = ((double)j + block.minX * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
        double d4 = ((double)j + block.maxX * (double)TextureFX.tileWidthTerrain - 0.01) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
        double d5 = ((double)k + block.minZ * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
        double d6 = ((double)k + block.maxZ * (double)TextureFX.tileWidthTerrain - 0.01) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
        if (block.minX < 0.0 || block.maxX > 1.0) {
            d3 = ((float)j + 0.0f) / (float)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d4 = ((float)j + ((float)TextureFX.tileWidthTerrain - 0.01f)) / (float)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
        }
        if (block.minZ < 0.0 || block.maxZ > 1.0) {
            d5 = ((float)k + 0.0f) / (float)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d6 = ((float)k + ((float)TextureFX.tileWidthTerrain - 0.01f)) / (float)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
        }
        double d7 = d4;
        double d8 = d3;
        double d9 = d5;
        double d10 = d6;
        if (this.uvRotateTop == 1) {
            d3 = ((double)j + block.minZ * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d5 = ((double)(k + TextureFX.tileWidthTerrain) - block.maxX * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d4 = ((double)j + block.maxZ * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d6 = ((double)(k + TextureFX.tileWidthTerrain) - block.minX * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d7 = d4;
            d8 = d3;
            d9 = d5;
            d10 = d6;
            d7 = d3;
            d8 = d4;
            d5 = d6;
            d6 = d9;
        } else if (this.uvRotateTop == 2) {
            d3 = ((double)(j + TextureFX.tileWidthTerrain) - block.maxZ * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d5 = ((double)k + block.minX * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d4 = ((double)(j + TextureFX.tileWidthTerrain) - block.minZ * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d6 = ((double)k + block.maxX * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d7 = d4;
            d8 = d3;
            d9 = d5;
            d10 = d6;
            d3 = d7;
            d4 = d8;
            d9 = d6;
            d10 = d5;
        } else if (this.uvRotateTop == 3) {
            d3 = ((double)(j + TextureFX.tileWidthTerrain) - block.minX * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d4 = ((double)(j + TextureFX.tileWidthTerrain) - block.maxX * (double)TextureFX.tileWidthTerrain - 0.01) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d5 = ((double)(k + TextureFX.tileWidthTerrain) - block.minZ * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d6 = ((double)(k + TextureFX.tileWidthTerrain) - block.maxZ * (double)TextureFX.tileWidthTerrain - 0.01) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d7 = d4;
            d8 = d3;
            d9 = d5;
            d10 = d6;
        }
        double d11 = d + block.minX;
        double d12 = d + block.maxX;
        double d13 = d1 + block.maxY;
        double d14 = d2 + block.minZ;
        double d15 = d2 + block.maxZ;
        if (this.enableAO) {
            tessellator.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
            tessellator.addVertexWithUV(d12 * scale, d13 * scale, d15 * scale, d4, d6);
            tessellator.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
            tessellator.addVertexWithUV(d12 * scale, d13 * scale, d14 * scale, d7, d9);
            tessellator.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
            tessellator.addVertexWithUV(d11 * scale, d13 * scale, d14 * scale, d3, d5);
            tessellator.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
            tessellator.addVertexWithUV(d11 * scale, d13 * scale, d15 * scale, d8, d10);
        } else {
            tessellator.addVertexWithUV(d12 * scale, d13 * scale, d15 * scale, d4, d6);
            tessellator.addVertexWithUV(d12 * scale, d13 * scale, d14 * scale, d7, d9);
            tessellator.addVertexWithUV(d11 * scale, d13 * scale, d14 * scale, d3, d5);
            tessellator.addVertexWithUV(d11 * scale, d13 * scale, d15 * scale, d8, d10);
        }
    }
    public void renderNorthFace(Block block, double d, double d1, double d2, int i, float scale) {
        Tessellator tessellator = Tessellator.instance;
        if (this.overrideBlockTexture >= 0) {
            i = this.overrideBlockTexture;
        }
        int j = i % Global.TEXTURE_ATLAS_WIDTH_TILES * TextureFX.tileWidthTerrain;
        int k = i / Global.TEXTURE_ATLAS_WIDTH_TILES * TextureFX.tileWidthTerrain;
        double d3 = ((double)j + block.minX * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
        double d4 = ((double)j + block.maxX * (double)TextureFX.tileWidthTerrain - 0.01) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
        double d5 = ((double)(k + TextureFX.tileWidthTerrain) - block.maxY * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
        double d6 = ((double)(k + TextureFX.tileWidthTerrain) - block.minY * (double)TextureFX.tileWidthTerrain - 0.01) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
        if (this.flipTexture) {
            double d7 = d3;
            d3 = d4;
            d4 = d7;
        }
        if (block.minX < 0.0 || block.maxX > 1.0) {
            d3 = ((float)j + 0.0f) / (float)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d4 = ((float)j + ((float)TextureFX.tileWidthTerrain - 0.01f)) / (float)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
        }
        if (block.minY < 0.0 || block.maxY > 1.0) {
            d5 = ((float)k + 0.0f) / (float)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d6 = ((float)k + ((float)TextureFX.tileWidthTerrain - 0.01f)) / (float)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
        }
        double d8 = d4;
        double d9 = d3;
        double d10 = d5;
        double d11 = d6;
        if (this.uvRotateEast == 2) {
            d3 = ((double)j + block.minY * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d5 = ((double)(k + TextureFX.tileWidthTerrain) - block.minX * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d4 = ((double)j + block.maxY * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d6 = ((double)(k + TextureFX.tileWidthTerrain) - block.maxX * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d8 = d4;
            d9 = d3;
            d10 = d5;
            d11 = d6;
            d8 = d3;
            d9 = d4;
            d5 = d6;
            d6 = d10;
        } else if (this.uvRotateEast == 1) {
            d3 = ((double)(j + TextureFX.tileWidthTerrain) - block.maxY * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d5 = ((double)k + block.maxX * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d4 = ((double)(j + TextureFX.tileWidthTerrain) - block.minY * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d6 = ((double)k + block.minX * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d8 = d4;
            d9 = d3;
            d10 = d5;
            d11 = d6;
            d3 = d8;
            d4 = d9;
            d10 = d6;
            d11 = d5;
        } else if (this.uvRotateEast == 3) {
            d3 = ((double)(j + TextureFX.tileWidthTerrain) - block.minX * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d4 = ((double)(j + TextureFX.tileWidthTerrain) - block.maxX * (double)TextureFX.tileWidthTerrain - 0.01) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d5 = ((double)k + block.maxY * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d6 = ((double)k + block.minY * (double)TextureFX.tileWidthTerrain - 0.01) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d8 = d4;
            d9 = d3;
            d10 = d5;
            d11 = d6;
        }
        double d12 = d + block.minX;
        double d13 = d + block.maxX;
        double d14 = d1 + block.minY;
        double d15 = d1 + block.maxY;
        double d16 = d2 + block.minZ;
        if (this.enableAO) {
            tessellator.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
            tessellator.addVertexWithUV(d12 * scale, d15 * scale, d16 * scale, d8, d10);
            tessellator.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
            tessellator.addVertexWithUV(d13 * scale, d15 * scale, d16 * scale, d3, d5);
            tessellator.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
            tessellator.addVertexWithUV(d13 * scale, d14 * scale, d16 * scale, d9, d11);
            tessellator.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
            tessellator.addVertexWithUV(d12 * scale, d14 * scale, d16 * scale, d4, d6);
        } else {
            tessellator.addVertexWithUV(d12 * scale, d15 * scale, d16 * scale, d8, d10);
            tessellator.addVertexWithUV(d13 * scale, d15 * scale, d16 * scale, d3, d5);
            tessellator.addVertexWithUV(d13 * scale, d14 * scale, d16 * scale, d9, d11);
            tessellator.addVertexWithUV(d12 * scale, d14 * scale, d16 * scale, d4, d6);
        }
    }
    public void renderSouthFace(Block block, double d, double d1, double d2, int i, float scale) {
        Tessellator tessellator = Tessellator.instance;
        if (this.overrideBlockTexture >= 0) {
            i = this.overrideBlockTexture;
        }
        int j = i % Global.TEXTURE_ATLAS_WIDTH_TILES * TextureFX.tileWidthTerrain;
        int k = i / Global.TEXTURE_ATLAS_WIDTH_TILES * TextureFX.tileWidthTerrain;
        double d3 = ((double)j + block.minX * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
        double d4 = ((double)j + block.maxX * (double)TextureFX.tileWidthTerrain - 0.01) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
        double d5 = ((double)(k + TextureFX.tileWidthTerrain) - block.maxY * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
        double d6 = ((double)(k + TextureFX.tileWidthTerrain) - block.minY * (double)TextureFX.tileWidthTerrain - 0.01) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
        if (this.flipTexture) {
            double d7 = d3;
            d3 = d4;
            d4 = d7;
        }
        if (block.minX < 0.0 || block.maxX > 1.0) {
            d3 = ((float)j + 0.0f) / (float)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d4 = ((float)j + ((float)TextureFX.tileWidthTerrain - 0.01f)) / (float)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
        }
        if (block.minY < 0.0 || block.maxY > 1.0) {
            d5 = ((float)k + 0.0f) / (float)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d6 = ((float)k + ((float)TextureFX.tileWidthTerrain - 0.01f)) / (float)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
        }
        double d8 = d4;
        double d9 = d3;
        double d10 = d5;
        double d11 = d6;
        if (this.uvRotateWest == 1) {
            d3 = ((double)j + block.minY * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d6 = ((double)(k + TextureFX.tileWidthTerrain) - block.minX * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d4 = ((double)j + block.maxY * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d5 = ((double)(k + TextureFX.tileWidthTerrain) - block.maxX * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d8 = d4;
            d9 = d3;
            d10 = d5;
            d11 = d6;
            d8 = d3;
            d9 = d4;
            d5 = d6;
            d6 = d10;
        } else if (this.uvRotateWest == 2) {
            d3 = ((double)(j + TextureFX.tileWidthTerrain) - block.maxY * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d5 = ((double)k + block.minX * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d4 = ((double)(j + TextureFX.tileWidthTerrain) - block.minY * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d6 = ((double)k + block.maxX * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d8 = d4;
            d9 = d3;
            d10 = d5;
            d11 = d6;
            d3 = d8;
            d4 = d9;
            d10 = d6;
            d11 = d5;
        } else if (this.uvRotateWest == 3) {
            d3 = ((double)(j + TextureFX.tileWidthTerrain) - block.minX * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d4 = ((double)(j + TextureFX.tileWidthTerrain) - block.maxX * (double)TextureFX.tileWidthTerrain - 0.01) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d5 = ((double)k + block.maxY * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d6 = ((double)k + block.minY * (double)TextureFX.tileWidthTerrain - 0.01) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d8 = d4;
            d9 = d3;
            d10 = d5;
            d11 = d6;
        }
        double d12 = d + block.minX;
        double d13 = d + block.maxX;
        double d14 = d1 + block.minY;
        double d15 = d1 + block.maxY;
        double d16 = d2 + block.maxZ;
        if (this.enableAO) {
            tessellator.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
            tessellator.addVertexWithUV(d12 * scale, d15 * scale, d16 * scale, d3, d5);
            tessellator.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
            tessellator.addVertexWithUV(d12 * scale, d14 * scale, d16 * scale, d9, d11);
            tessellator.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
            tessellator.addVertexWithUV(d13 * scale, d14 * scale, d16 * scale, d4, d6);
            tessellator.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
            tessellator.addVertexWithUV(d13 * scale, d15 * scale, d16 * scale, d8, d10);
        } else {
            tessellator.addVertexWithUV(d12 * scale, d15 * scale, d16 * scale, d3, d5);
            tessellator.addVertexWithUV(d12 * scale, d14 * scale, d16 * scale, d9, d11);
            tessellator.addVertexWithUV(d13 * scale, d14 * scale, d16 * scale, d4, d6);
            tessellator.addVertexWithUV(d13 * scale, d15 * scale, d16 * scale, d8, d10);
        }
    }
    public void renderWestFace(Block block, double d, double d1, double d2, int i, float scale) {
        Tessellator tessellator = Tessellator.instance;
        if (this.overrideBlockTexture >= 0) {
            i = this.overrideBlockTexture;
        }
        int j = i % Global.TEXTURE_ATLAS_WIDTH_TILES * TextureFX.tileWidthTerrain;
        int k = i / Global.TEXTURE_ATLAS_WIDTH_TILES * TextureFX.tileWidthTerrain;
        double d3 = ((double)j + block.minZ * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
        double d4 = ((double)j + block.maxZ * (double)TextureFX.tileWidthTerrain - 0.01) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
        double d5 = ((double)(k + TextureFX.tileWidthTerrain) - block.maxY * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
        double d6 = ((double)(k + TextureFX.tileWidthTerrain) - block.minY * (double)TextureFX.tileWidthTerrain - 0.01) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
        if (this.flipTexture) {
            double d7 = d3;
            d3 = d4;
            d4 = d7;
        }
        if (block.minZ < 0.0 || block.maxZ > 1.0) {
            d3 = ((float)j + 0.0f) / (float)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d4 = ((float)j + ((float)TextureFX.tileWidthTerrain - 0.01f)) / (float)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
        }
        if (block.minY < 0.0 || block.maxY > 1.0) {
            d5 = ((float)k + 0.0f) / (float)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d6 = ((float)k + ((float)TextureFX.tileWidthTerrain - 0.01f)) / (float)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
        }
        double d8 = d4;
        double d9 = d3;
        double d10 = d5;
        double d11 = d6;
        if (this.uvRotateNorth == 1) {
            d3 = ((double)j + block.minY * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d5 = ((double)(k + TextureFX.tileWidthTerrain) - block.maxZ * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d4 = ((double)j + block.maxY * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d6 = ((double)(k + TextureFX.tileWidthTerrain) - block.minZ * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d8 = d4;
            d9 = d3;
            d10 = d5;
            d11 = d6;
            d8 = d3;
            d9 = d4;
            d5 = d6;
            d6 = d10;
        } else if (this.uvRotateNorth == 2) {
            d3 = ((double)(j + TextureFX.tileWidthTerrain) - block.maxY * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d5 = ((double)k + block.minZ * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d4 = ((double)(j + TextureFX.tileWidthTerrain) - block.minY * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d6 = ((double)k + block.maxZ * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d8 = d4;
            d9 = d3;
            d10 = d5;
            d11 = d6;
            d3 = d8;
            d4 = d9;
            d10 = d6;
            d11 = d5;
        } else if (this.uvRotateNorth == 3) {
            d3 = ((double)(j + TextureFX.tileWidthTerrain) - block.minZ * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d4 = ((double)(j + TextureFX.tileWidthTerrain) - block.maxZ * (double)TextureFX.tileWidthTerrain - 0.01) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d5 = ((double)k + block.maxY * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d6 = ((double)k + block.minY * (double)TextureFX.tileWidthTerrain - 0.01) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d8 = d4;
            d9 = d3;
            d10 = d5;
            d11 = d6;
        }
        double d12 = d + block.minX;
        double d13 = d1 + block.minY;
        double d14 = d1 + block.maxY;
        double d15 = d2 + block.minZ;
        double d16 = d2 + block.maxZ;
        d12 *= scale;
        d13 *= scale;
        d14 *= scale;
        d15 *= scale;
        d16 *= scale;
        if (this.enableAO) {
            tessellator.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
            tessellator.addVertexWithUV(d12, d14, d16, d8, d10);
            tessellator.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
            tessellator.addVertexWithUV(d12, d14, d15, d3, d5);
            tessellator.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
            tessellator.addVertexWithUV(d12, d13, d15, d9, d11);
            tessellator.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
            tessellator.addVertexWithUV(d12, d13, d16, d4, d6);
        } else {
            tessellator.addVertexWithUV(d12, d14, d16, d8, d10);
            tessellator.addVertexWithUV(d12, d14, d15, d3, d5);
            tessellator.addVertexWithUV(d12, d13, d15, d9, d11);
            tessellator.addVertexWithUV(d12, d13, d16, d4, d6);
        }
    }

    public void renderEastFace(Block block, double d, double d1, double d2, int i, float scale) {
        Tessellator tessellator = Tessellator.instance;
        if (this.overrideBlockTexture >= 0) {
            i = this.overrideBlockTexture;
        }
        int j = i % Global.TEXTURE_ATLAS_WIDTH_TILES * TextureFX.tileWidthTerrain;
        int k = i / Global.TEXTURE_ATLAS_WIDTH_TILES * TextureFX.tileWidthTerrain;
        double d3 = ((double)j + block.minZ * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
        double d4 = ((double)j + block.maxZ * (double)TextureFX.tileWidthTerrain - 0.01) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
        double d5 = ((double)(k + TextureFX.tileWidthTerrain) - block.maxY * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
        double d6 = ((double)(k + TextureFX.tileWidthTerrain) - block.minY * (double)TextureFX.tileWidthTerrain - 0.01) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
        if (this.flipTexture) {
            double d7 = d3;
            d3 = d4;
            d4 = d7;
        }
        if (block.minZ < 0.0 || block.maxZ > 1.0) {
            d3 = ((float)j + 0.0f) / (float)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d4 = ((float)j + ((float)TextureFX.tileWidthTerrain - 0.01f)) / (float)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
        }
        if (block.minY < 0.0 || block.maxY > 1.0) {
            d5 = ((float)k + 0.0f) / (float)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d6 = ((float)k + ((float)TextureFX.tileWidthTerrain - 0.01f)) / (float)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
        }
        double d8 = d4;
        double d9 = d3;
        double d10 = d5;
        double d11 = d6;
        if (this.uvRotateSouth == 2) {
            d3 = ((double)j + block.minY * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d5 = ((double)(k + TextureFX.tileWidthTerrain) - block.minZ * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d4 = ((double)j + block.maxY * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d6 = ((double)(k + TextureFX.tileWidthTerrain) - block.maxZ * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d8 = d4;
            d9 = d3;
            d10 = d5;
            d11 = d6;
            d8 = d3;
            d9 = d4;
            d5 = d6;
            d6 = d10;
        } else if (this.uvRotateSouth == 1) {
            d3 = ((double)(j + TextureFX.tileWidthTerrain) - block.maxY * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d5 = ((double)k + block.maxZ * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d4 = ((double)(j + TextureFX.tileWidthTerrain) - block.minY * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d6 = ((double)k + block.minZ * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d8 = d4;
            d9 = d3;
            d10 = d5;
            d11 = d6;
            d3 = d8;
            d4 = d9;
            d10 = d6;
            d11 = d5;
        } else if (this.uvRotateSouth == 3) {
            d3 = ((double)(j + TextureFX.tileWidthTerrain) - block.minZ * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d4 = ((double)(j + TextureFX.tileWidthTerrain) - block.maxZ * (double)TextureFX.tileWidthTerrain - 0.01) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d5 = ((double)k + block.maxY * (double)TextureFX.tileWidthTerrain) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d6 = ((double)k + block.minY * (double)TextureFX.tileWidthTerrain - 0.01) / (double)(TextureFX.tileWidthTerrain * Global.TEXTURE_ATLAS_WIDTH_TILES);
            d8 = d4;
            d9 = d3;
            d10 = d5;
            d11 = d6;
        }
        double d12 = d + block.maxX;
        double d13 = d1 + block.minY;
        double d14 = d1 + block.maxY;
        double d15 = d2 + block.minZ;
        double d16 = d2 + block.maxZ;
        d12 *= scale;
        d13 *= scale;
        d14 *= scale;
        d15 *= scale;
        d16 *= scale;
        if (this.enableAO) {
            tessellator.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
            tessellator.addVertexWithUV(d12, d13, d16, d9, d11);
            tessellator.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
            tessellator.addVertexWithUV(d12, d13, d15, d4, d6);
            tessellator.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
            tessellator.addVertexWithUV(d12, d14, d15, d8, d10);
            tessellator.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
            tessellator.addVertexWithUV(d12, d14, d16, d3, d5);
        } else {
            tessellator.addVertexWithUV(d12, d13, d16, d9, d11);
            tessellator.addVertexWithUV(d12, d13, d15, d4, d6);
            tessellator.addVertexWithUV(d12, d14, d15, d8, d10);
            tessellator.addVertexWithUV(d12, d14, d16, d3, d5);
        }
    }

}
