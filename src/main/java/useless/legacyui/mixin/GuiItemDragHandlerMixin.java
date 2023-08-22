package useless.legacyui.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiContainer;
import net.minecraft.client.gui.GuiRenderItem;
import net.minecraft.client.util.helper.GuiItemDragHandler;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.slot.Slot;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import useless.legacyui.Gui.Slot.IResizable;

@Mixin(value = GuiItemDragHandler.class, remap = false)
public class GuiItemDragHandlerMixin extends Gui {
    // TODO better mixin
    @Shadow
    private ItemStack draggingItemStack;
    @Shadow
    private ItemStack renderItemStack;
    @Shadow
    private int renderOffsetX;
    @Shadow
    private int renderOffsetY;
    @Shadow
    private int renderItemStackCount;
    @Final
    @Shadow
    public GuiRenderItem guiRenderItem;

    /**
     * @author Useless
     * @reason This Overwrite should only be temporary and a better fix for dragging items should be found
     */
    @Overwrite
    private void drawSlotOverlayWhileDragging(Slot slot) {
        float renderScale = 1f;
        int slotSize = 18;
        if (slot instanceof IResizable){
            slotSize = ((IResizable) slot).getWidth();
            renderScale = (slotSize)/18f;
        }
        if (slot == null) {
            throw new NullPointerException("Slot is null");
        }
        if (this.draggingItemStack == null) {
            throw new NullPointerException("Stack is null!");
        }
        ItemStack stack = slot.getStack();
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        this.drawRectWidthHeight(this.renderOffsetX + slot.xDisplayPosition, this.renderOffsetY + slot.yDisplayPosition, slotSize -2, slotSize -2, -2130706433);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        this.renderItemStack.stackSize = this.renderItemStackCount;
        if (stack != null) {
            this.renderItemStack.stackSize = Math.min(this.renderItemStack.getMaxStackSize(), this.renderItemStack.stackSize + stack.stackSize);
        }
        GL11.glScaled(renderScale, renderScale, renderScale);
        int newX = (int)(slot.xDisplayPosition*(1d/renderScale));
        int newY = (int)(slot.yDisplayPosition* (1d/renderScale));
        this.guiRenderItem.render(this.renderItemStack, newX, newY);
        GL11.glScaled(1/renderScale, 1/renderScale, 1/renderScale);
    }
}
