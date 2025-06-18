package useless.legacyui.mixins;

import com.llamalad7.mixinextras.sugar.Local;
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
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import useless.legacyui.gui.slots.IHighlightable;
import useless.legacyui.gui.slots.IResizable;

@Mixin(value = ItemElement.class, remap = false, priority = 2000)
public class ItemElementMixin extends Gui {

    @Redirect(method = "render(Lnet/minecraft/core/item/ItemStack;IIZLnet/minecraft/core/player/inventory/slot/Slot;)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/render/item/model/ItemModel;renderItemIntoGui(Lnet/minecraft/client/render/tessellator/Tessellator;Lnet/minecraft/client/render/Font;Lnet/minecraft/client/render/TextureManager;Lnet/minecraft/core/item/ItemStack;IIFF)V"))
    private void drawItemRedirect(final ItemModel model, final Tessellator tessellator, final Font font, final TextureManager textureManager, final ItemStack itemstack, final int x, final int y, final float brightness, final float alpha, @Local(name = "slot") final Slot slot){
        final float renderScale;
        if (slot instanceof IResizable){
            renderScale = (((IResizable) slot).getWidth())/18f;
        } else {
            renderScale = 1f;
        }

        final int newX = (int)(x * (1/ renderScale));
        final int newY = (int)(y * (1/ renderScale));
        GL11.glPushMatrix();
        GL11.glScaled(renderScale, renderScale, renderScale);
        GL11.glTranslated( newX, newY, 0);
        model.renderItemIntoGui(tessellator, font, textureManager, itemstack, 0, 0, brightness, alpha);
    }

    @Redirect(method = "render(Lnet/minecraft/core/item/ItemStack;IIZLnet/minecraft/core/player/inventory/slot/Slot;)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/render/item/model/ItemModel;renderItemOverlayIntoGUI(Lnet/minecraft/client/render/tessellator/Tessellator;Lnet/minecraft/client/render/Font;Lnet/minecraft/client/render/TextureManager;Lnet/minecraft/core/item/ItemStack;IILjava/lang/String;F)V"))
    private void drawTextRedirect(final ItemModel model, final Tessellator tessellator, final Font font, final TextureManager textureManager, final ItemStack itemstack, final int x, final int y, final String s, final float alpha,
                                  @Local(name = "discovered") final boolean discovered, @Local(name = "slot") final Slot slot){
        model.renderItemOverlayIntoGUI(tessellator, font, textureManager, itemstack, 0, 0, discovered ? null : "?", alpha);
        GL11.glPopMatrix();
    }

    @Redirect(method = "render(Lnet/minecraft/core/item/ItemStack;IIZLnet/minecraft/core/player/inventory/slot/Slot;)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/ItemElement;drawRect(IIIII)V"))
    private void drawRectRedirect(final ItemElement guiRenderItem, final int minX, final int minY, final int maxX, final int maxY, final int argb,
                                  @Local(name = "slot") final Slot slot, @Local(name = "isSelected") final boolean isSelected){
        final int slotSize;
        if (slot instanceof IResizable){
            slotSize = ((IResizable) slot).getWidth();
        } else {
            slotSize = 18;
        }

        this.drawRect(minX, minY,minX + slotSize - 2,minY + slotSize - 2, 0x80ffffff);
    }

    @Inject(method = "render(Lnet/minecraft/core/item/ItemStack;IIZLnet/minecraft/core/player/inventory/slot/Slot;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Lighting;disable()V", shift = At.Shift.BEFORE))
    private void drawHighlight(final ItemStack itemStack, final int x, final int y, final boolean isSelected, final Slot slot, final CallbackInfo ci){
        if(slot instanceof IHighlightable && ((IHighlightable)slot).isHighlighted()){
            final int slotSize;
            if (slot instanceof IResizable){
                slotSize = ((IResizable) slot).getWidth();
            } else {
                slotSize = 18;
            }

            GL11.glEnable(32826);
            GL11.glDisable(2896);
            GL11.glDisable(2929);
            this.drawRect(x, y, x + slotSize - 2, y + slotSize - 2, 0x80000000 | ((IHighlightable)slot).getHighlightColor());
            GL11.glEnable(2896);
            GL11.glEnable(2929);
            GL11.glDisable(32826);
        }
    }
}
