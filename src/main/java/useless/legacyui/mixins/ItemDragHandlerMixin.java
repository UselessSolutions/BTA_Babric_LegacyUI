package useless.legacyui.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ItemElement;
import net.minecraft.client.util.helper.ItemDragHandler;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.slot.Slot;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import useless.legacyui.gui.slots.IResizable;

@Mixin(value = ItemDragHandler.class, remap = false)
public class ItemDragHandlerMixin extends Gui {
    @Shadow
    private ItemStack renderItemStack;

    @Inject(method = "drawSlotOverlayWhileDragging(Lnet/minecraft/core/player/inventory/slot/Slot;)V",
            at = @At(value = "HEAD"))
    private void drawSlotOverlayWhileDraggingInject(Slot slot, CallbackInfo cbi) {

    }

    @Redirect(method = "drawSlotOverlayWhileDragging(Lnet/minecraft/core/player/inventory/slot/Slot;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/ItemElement;render(Lnet/minecraft/core/item/ItemStack;II)V"))
    private void drawSlotOverlayWhileDraggingInjectTail(ItemElement instance, ItemStack itemStack, int x, int y, @Local(name = "slot") Slot slot) {
        float renderScale = 1f;
        if (slot instanceof IResizable){
            final int slotSize = ((IResizable) slot).getWidth();
            renderScale = (slotSize)/18f;
        }
        double newX = slot.x * (1d/renderScale);
        double newY = slot.y * (1d/renderScale);

        GL11.glPushMatrix();
        GL11.glScaled(renderScale, renderScale, renderScale);
        GL11.glTranslated(newX, newY,0);
        instance.render(this.renderItemStack, 0, 0);
        GL11.glPopMatrix();
    }

    @Redirect(method = "drawSlotOverlayWhileDragging(Lnet/minecraft/core/player/inventory/slot/Slot;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/helper/ItemDragHandler;drawRectWidthHeight(IIIII)V"))
    private void drawBackground(ItemDragHandler guiItemDragHandler, int x, int y, int width, int height, int argb, @Local(name = "slot") Slot slot){
        int slotSize = 18;
        if (slot instanceof IResizable){
            slotSize = ((IResizable) slot).getWidth();
        }

        this.drawRectWidthHeight(x, y, slotSize -2, slotSize -2, argb);
    }

}
