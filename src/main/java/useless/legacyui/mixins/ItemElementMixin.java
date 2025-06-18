package useless.legacyui.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ItemElement;
import net.minecraft.client.render.Font;
import net.minecraft.client.render.TextureManager;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.slot.Slot;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import useless.legacyui.gui.slots.IHighlightable;
import useless.legacyui.gui.slots.IResizable;

@Mixin(value = ItemElement.class, remap = false, priority = 0)
public class ItemElementMixin extends Gui {
    @Shadow private Minecraft mc;
    @Unique
    private float renderScale;
    @Unique
    private boolean isDiscovered;
    @Unique
    private int slotSize;
    @Unique
    private boolean drawBackground;
    @Unique
    private float itemAlpha;

    @Inject(method = "render(Lnet/minecraft/core/item/ItemStack;IIZLnet/minecraft/core/player/inventory/slot/Slot;)V", at = @At(value = "HEAD"))
    public void renderInjectHead(final ItemStack itemStack, final int x, final int y, final boolean isSelected, final Slot slot, final CallbackInfo cbi){
        this.drawBackground = isSelected;

        if (slot != null) {
            this.isDiscovered = slot.getIsDiscovered(this.mc.thePlayer);
        } else {
            this.isDiscovered = true;
        }


        if (slot instanceof IResizable){
            this.slotSize = ((IResizable) slot).getWidth();
            this.renderScale = (this.slotSize)/18f;
        } else {
            this.slotSize = 18;
            this.renderScale = 1f;
        }

//        if (slot instanceof IAlpha){
//            itemAlpha = ((IAlpha) slot).getStackAlpha();
//        } else {
        this.itemAlpha = 1f;
//        }

        if(slot instanceof IHighlightable){
            this.drawBackground = ((IHighlightable) slot).drawStandardHighlight();
        }
    }

    @Redirect(method = "render(Lnet/minecraft/core/item/ItemStack;IIZLnet/minecraft/core/player/inventory/slot/Slot;)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/render/item/model/ItemModel;renderItemIntoGui(Lnet/minecraft/client/render/tessellator/Tessellator;Lnet/minecraft/client/render/Font;Lnet/minecraft/client/render/TextureManager;Lnet/minecraft/core/item/ItemStack;IIFF)V"))
    private void drawItemRedirect(final ItemModel model, final Tessellator tessellator, final Font font, final TextureManager textureManager, final ItemStack itemstack, final int x, final int y, final float brightness, final float alpha){
        final int newX = (int)(x * (1/ this.renderScale));
        final int newY = (int)(y * (1/ this.renderScale));
        GL11.glScaled(this.renderScale, this.renderScale, this.renderScale);
        model.renderItemIntoGui(tessellator, font, textureManager, itemstack, newX, newY, brightness, alpha);
    }

    @Redirect(method = "render(Lnet/minecraft/core/item/ItemStack;IIZLnet/minecraft/core/player/inventory/slot/Slot;)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/render/item/model/ItemModel;renderItemOverlayIntoGUI(Lnet/minecraft/client/render/tessellator/Tessellator;Lnet/minecraft/client/render/Font;Lnet/minecraft/client/render/TextureManager;Lnet/minecraft/core/item/ItemStack;IILjava/lang/String;F)V"))
    private void drawTextRedirect(final ItemModel model, final Tessellator tessellator, final Font font, final TextureManager textureManager, final ItemStack itemstack, final int x, final int y, final String s, final float alpha){
        final int newX = (int)(x * (1/ this.renderScale));
        final int newY = (int)(y * (1/ this.renderScale));
        model.renderItemOverlayIntoGUI(tessellator, font, textureManager, itemstack, newX, newY, this.isDiscovered ? null : "?", this.itemAlpha);
        GL11.glScaled(1/ this.renderScale, 1/ this.renderScale, 1/ this.renderScale);
    }

    @Redirect(method = "render(Lnet/minecraft/core/item/ItemStack;IIZLnet/minecraft/core/player/inventory/slot/Slot;)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/ItemElement;drawRect(IIIII)V"))
    private void drawRectRedirect(final ItemElement guiRenderItem, final int minX, final int minY, final int maxX, final int maxY, final int argb){
        if (this.drawBackground){
            this.drawRect(minX, minY,minX + this.slotSize -2,minY + this.slotSize -2, 0x80ffffff);
        }

    }
    @Inject(method = "render(Lnet/minecraft/core/item/ItemStack;IIZLnet/minecraft/core/player/inventory/slot/Slot;)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glDisable(I)V", ordinal = 4, shift = At.Shift.BEFORE))
    private void drawHighlight(final ItemStack itemStack, final int x, final int y, final boolean isSelected, final Slot slot, final CallbackInfo ci){
        if(slot instanceof IHighlightable && ((IHighlightable)slot).isHighlighted()){
            GL11.glDisable(2896);
            GL11.glDisable(2929);
            this.drawRect(x, y, x + this.slotSize -2, y + this.slotSize -2, 0x80000000 | ((IHighlightable)slot).getHighlightColor());
            GL11.glEnable(2896);
            GL11.glEnable(2929);
        }
    }
}
