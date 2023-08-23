package useless.legacyui.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiContainer;
import net.minecraft.client.gui.GuiRenderItem;
import net.minecraft.client.util.helper.GuiItemDragHandler;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.slot.Slot;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import useless.legacyui.Gui.Slot.IResizable;

@Mixin(value = GuiItemDragHandler.class, remap = false)
public class GuiItemDragHandlerMixin extends Gui {
    @Shadow
    private ItemStack renderItemStack;
    @Unique
    public GuiRenderItem guiRenderItem;
    @Unique
    private float renderScale;
    @Unique
    private int slotSize;
    @Inject(method = "<init>", at = @At("TAIL"))
    public void constructor(Minecraft minecraft, GuiContainer container, GuiRenderItem renderItem, CallbackInfo cbi){
        this.guiRenderItem = renderItem;
    }

    @Inject(method = "drawSlotOverlayWhileDragging(Lnet/minecraft/core/player/inventory/slot/Slot;)V",
            at = @At(value = "HEAD"))
    private void drawSlotOverlayWhileDraggingInject(Slot slot, CallbackInfo cbi) {
        renderScale = 1f;
        slotSize = 18;
        if (slot instanceof IResizable){
            slotSize = ((IResizable) slot).getWidth();
            renderScale = (slotSize)/18f;
        }
    }

    @Inject(method = "drawSlotOverlayWhileDragging(Lnet/minecraft/core/player/inventory/slot/Slot;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiRenderItem;render(Lnet/minecraft/core/item/ItemStack;II)V"), cancellable = true)
    private void drawSlotOverlayWhileDraggingInjectTail(Slot slot, CallbackInfo cbi) {
        GL11.glScaled(renderScale, renderScale, renderScale);
        int newX = (int)(slot.xDisplayPosition*(1d/renderScale));
        int newY = (int)(slot.yDisplayPosition* (1d/renderScale));
        this.guiRenderItem.render(this.renderItemStack, newX, newY);
        GL11.glScaled(1/renderScale, 1/renderScale, 1/renderScale);
        cbi.cancel();
    }

    @Redirect(method = "drawSlotOverlayWhileDragging(Lnet/minecraft/core/player/inventory/slot/Slot;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/helper/GuiItemDragHandler;drawRectWidthHeight(IIIII)V"))
    private void drawBackground(GuiItemDragHandler guiItemDragHandler, int x, int y, int width, int height, int argb){
        this.drawRectWidthHeight(x, y, slotSize -2, slotSize -2, argb);
    }

}
