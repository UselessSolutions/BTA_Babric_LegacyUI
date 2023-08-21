package useless.legacyui.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiRenderItem;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.TextureFX;
import net.minecraft.core.Global;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.slot.Slot;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import useless.legacyui.Gui.Slot.IResizable;
import useless.legacyui.Gui.Slot.SlotCraftingDisplay;
import useless.legacyui.Rendering.LegacyItemEntityRenderer;

@Mixin(value = GuiRenderItem.class, remap = false)
public class GuiRenderItemMixin extends Gui {
    @Unique
    private static final LegacyItemEntityRenderer itemRenderer = new LegacyItemEntityRenderer();
    @Shadow
    Minecraft mc;
    /**
     * @author Useless
     * @reason Resize items and selection based on slot size
     */
    @Overwrite
    public void render(ItemStack itemStack, int x, int y, boolean isSelected, Slot slot) {
        boolean hasDrawnSlotBackground = false;
        boolean discovered = true;
        int slotSize = 18;
        float renderScale = 1f;
        if (slot instanceof IResizable){
            slotSize = ((IResizable) slot).getWidth();
            renderScale = (slotSize)/18f;
        }
        GL11.glPushMatrix();
        GL11.glRotatef(120.0F, 1.0F, 0.0F, 0.0F);
        Lighting.turnOff();
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(32826);

        // Render Crafting fadeout
        if(slot instanceof SlotCraftingDisplay && ((SlotCraftingDisplay)slot).highlight){
            GL11.glDisable(2896);
            GL11.glDisable(2929);
            this.drawRect(x, y, x + slotSize-2, y + slotSize-2, 0x80000000 + ((SlotCraftingDisplay)slot).color);
            GL11.glEnable(2896);
            GL11.glEnable(2929);
            isSelected = false;
        }

        if (slot != null) {
            discovered = slot.discovered;
            int iconIndex = slot.getBackgroundIconIndex();
            if (iconIndex >= 0 && itemStack == null) {
                GL11.glDisable(2896);
                this.mc.renderEngine.bindTexture(this.mc.renderEngine.getTexture("/gui/items.png"));
                this.drawTexturedModalRect(x, y, iconIndex % Global.TEXTURE_ATLAS_WIDTH_TILES * TextureFX.tileWidthItems, iconIndex / Global.TEXTURE_ATLAS_WIDTH_TILES * TextureFX.tileWidthItems, 16, 16, TextureFX.tileWidthItems, 1.0F / (float)(Global.TEXTURE_ATLAS_WIDTH_TILES * TextureFX.tileWidthItems));
                GL11.glEnable(2896);
                hasDrawnSlotBackground = true;
            }
        }

        if (!hasDrawnSlotBackground) {
            GL11.glEnable(2929);

            int fontHeight = mc.fontRenderer.fontHeight;
            if (slot instanceof IResizable){
                fontHeight = (int)(9*(((slotSize)/18f))); // Smaller font
            }

            itemRenderer.renderItemIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, itemStack, x, y, discovered ? 1.0F : 0.0F, 1.0F, renderScale);
            itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, itemStack, x, y, discovered ? null : "?", fontHeight);

            GL11.glDisable(2929);
        }

        if (isSelected) {
            GL11.glDisable(2896);
            GL11.glDisable(2929);
            this.drawRect(x, y, x + slotSize-2, y + slotSize-2, -2130706433);
            GL11.glEnable(2896);
            GL11.glEnable(2929);
        }

        GL11.glDisable(32826);
        Lighting.turnOn();
        GL11.glDisable(2896);
        GL11.glDisable(2929);
    }
}
