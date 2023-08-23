package useless.legacyui.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiRenderItem;
import net.minecraft.client.render.FontRenderer;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.slot.Slot;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import useless.legacyui.Gui.Slot.IResizable;
import useless.legacyui.Gui.Slot.SlotCraftingDisplay;

@Mixin(value = GuiRenderItem.class, remap = false, priority = 0)
public class GuiRenderItemMixin extends Gui {
    @Unique
    private float renderScale;
    @Unique
    private boolean isDiscovered;
    @Unique
    private int slotSize;
    @Unique
    private boolean drawBackground;

    @Inject(method = "render(Lnet/minecraft/core/item/ItemStack;IIZLnet/minecraft/core/player/inventory/slot/Slot;)V", at = @At(value = "HEAD"))
    public void renderInjectHead(ItemStack itemStack, int x, int y, boolean isSelected, Slot slot, CallbackInfo cbi){
        drawBackground = isSelected;
        isDiscovered = true;
        if (slot != null) {
            isDiscovered = slot.discovered;
        }
        slotSize = 18;
        renderScale = 1f;
        if (slot instanceof IResizable){
            slotSize = ((IResizable) slot).getWidth();
            renderScale = (slotSize)/18f;
        }

        // Render Crafting fadeout
        if(slot instanceof SlotCraftingDisplay && ((SlotCraftingDisplay)slot).highlight){
            GL11.glDisable(2896);
            GL11.glDisable(2929);
            this.drawRect(x, y, x + slotSize-2, y + slotSize-2, 0x80000000 + ((SlotCraftingDisplay)slot).color);
            GL11.glEnable(2896);
            GL11.glEnable(2929);
            drawBackground = false;
        }
    }

    @Redirect(method = "render(Lnet/minecraft/core/item/ItemStack;IIZLnet/minecraft/core/player/inventory/slot/Slot;)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/render/entity/ItemEntityRenderer;renderItemIntoGUI(Lnet/minecraft/client/render/FontRenderer;Lnet/minecraft/client/render/RenderEngine;Lnet/minecraft/core/item/ItemStack;IIFF)V"))
    private void drawItemRedirect(ItemEntityRenderer itemRenderer, FontRenderer fontrenderer, RenderEngine renderengine, ItemStack itemstack, int i, int j, float brightness, float alpha){
        int newX = (int)(i * (1/renderScale));
        int newY = (int)(j * (1/renderScale));
        GL11.glScaled(renderScale, renderScale, renderScale);
        itemRenderer.renderItemIntoGUI(fontrenderer, renderengine, itemstack, newX, newY, brightness, alpha);
    }

    @Redirect(method = "render(Lnet/minecraft/core/item/ItemStack;IIZLnet/minecraft/core/player/inventory/slot/Slot;)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/render/entity/ItemEntityRenderer;renderItemOverlayIntoGUI(Lnet/minecraft/client/render/FontRenderer;Lnet/minecraft/client/render/RenderEngine;Lnet/minecraft/core/item/ItemStack;IILjava/lang/String;)V"))
    private void drawTextRedirect(ItemEntityRenderer itemRenderer, FontRenderer fontrenderer, RenderEngine renderengine, ItemStack itemstack, int i, int j, String override){
        int newX = (int)(i * (1/renderScale));
        int newY = (int)(j * (1/renderScale));
        itemRenderer.renderItemOverlayIntoGUI(fontrenderer, renderengine, itemstack, newX, newY, isDiscovered ? null : "?");
        GL11.glScaled(1/renderScale, 1/renderScale, 1/renderScale);
    }

    @Redirect(method = "render(Lnet/minecraft/core/item/ItemStack;IIZLnet/minecraft/core/player/inventory/slot/Slot;)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiRenderItem;drawRect(IIIII)V"))
    private void drawRectRedirect(GuiRenderItem guiRenderItem,int minX, int minY, int maxX, int maxY, int argb){
        if (drawBackground){
            this.drawRect(minX, minY,minX + slotSize-2,minY + slotSize-2, -2130706433);
        }
    }
}
