package useless.legacyui.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiRenderItem;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.TextureFX;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.core.Global;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.slot.Slot;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import useless.legacyui.Gui.SlotResizable;
import useless.legacyui.LegacyUI;

@Mixin(value = GuiRenderItem.class, remap = false)
public class GuiRenderItemMixin extends Gui {
    @Shadow
    static ItemEntityRenderer itemRenderer = new ItemEntityRenderer();
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
        int slotSize = 16;
        float renderScale = 1f;
        if (slot instanceof SlotResizable){
            slotSize = ((SlotResizable) slot).width;
            renderScale = slotSize/16f;
        }
        GL11.glPushMatrix();
        GL11.glRotatef(120.0F, 1.0F, 0.0F, 0.0F);
        Lighting.turnOff();
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(32826);
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

            LegacyUI.currentRenderScale = renderScale;
            itemRenderer.renderItemIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, itemStack, x, y, discovered ? 1.0F : 0.0F, 1.0F);
            itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, this.mc.renderEngine, itemStack, x, y, discovered ? null : "?");
            LegacyUI.currentRenderScale = 1f;
            GL11.glDisable(2929);
        }



        if (isSelected) {
            GL11.glDisable(2896);
            GL11.glDisable(2929);
            this.drawRect(x, y, x + slotSize, y + slotSize, -2130706433);
            GL11.glEnable(2896);
            GL11.glEnable(2929);
        }

        GL11.glDisable(32826);
        Lighting.turnOn();
        GL11.glDisable(2896);
        GL11.glDisable(2929);
    }
}
