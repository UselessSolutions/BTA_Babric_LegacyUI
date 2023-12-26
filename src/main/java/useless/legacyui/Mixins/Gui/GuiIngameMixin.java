package useless.legacyui.Mixins.Gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiContainer;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.option.ImmersiveModeOption;
import net.minecraft.client.render.FontRenderer;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.gamemode.Gamemode;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import useless.legacyui.Gui.GuiScreens.UtilGui;
import useless.legacyui.LegacyUI;

@Mixin(value = GuiIngame.class, remap = false)
public class GuiIngameMixin {
    @Shadow
    protected Minecraft mc;
    @Redirect(method = "renderGameOverlay(FZII)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/ItemEntityRenderer;renderItemIntoGUI(Lnet/minecraft/client/render/FontRenderer;Lnet/minecraft/client/render/RenderEngine;Lnet/minecraft/core/item/ItemStack;IIF)V"))
    private void fadeOut(ItemEntityRenderer instance, FontRenderer fontrenderer, RenderEngine renderengine, ItemStack itemstack, int i, int j, float alpha){
        instance.renderItemIntoGUI(fontrenderer, renderengine, itemstack, i, j, UtilGui.getHotbarAlpha());
    }
    @Redirect(method = "renderGameOverlay(FZII)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/ImmersiveModeOption;drawHotbar()Z"))
    private boolean dontRenderInGuiHotbar(ImmersiveModeOption instance){
        if (LegacyUI.modSettings.getHideHotbarInGUIs().value){
            if (mc.currentScreen instanceof GuiContainer){
                return false;
            }
        }
        return instance.drawHotbar();
    }
    @Inject(method = "renderGameOverlay(FZII)V", at = @At(value = "TAIL"))
    private void paperDoll(float partialTicks, boolean flag, int mouseX, int mouseY, CallbackInfo ci){
        if (LegacyUI.modSettings.getHideHotbarInGUIs().value){
            if (mc.currentScreen instanceof GuiContainer){
                return;
            }
        }
        if (LegacyUI.modSettings.getEnablePaperDoll().value && !mc.gameSettings.showDebugScreen.value){
            boolean clock = false;
            boolean compass = false;
            boolean rotaryCalendar = false;
            if (this.mc.thePlayer.getGamemode() == Gamemode.creative) {
                clock = true;
                compass = true;
                rotaryCalendar = true;
            } else {
                for (int iinv = 0; iinv < this.mc.thePlayer.inventory.getSizeInventory(); ++iinv) {
                    ItemStack item = this.mc.thePlayer.inventory.getStackInSlot(iinv);
                    if (item == null) continue;
                    if (item.itemID == Item.toolClock.id) {
                        clock = true;
                    }
                    if (item.itemID == Item.toolCompass.id) {
                        compass = true;
                    }
                    if (item.itemID != Item.toolCalendar.id) continue;
                    rotaryCalendar = true;
                }
            }
            boolean drawRight;
            drawRight = (clock && mc.gameSettings.overlayShowTime.value);
            drawRight = drawRight || (compass && (mc.gameSettings.overlayShowCoords.value || mc.gameSettings.overlayShowDirection.value));
            drawRight = drawRight || (rotaryCalendar && (mc.gameSettings.overlayShowSeason.value || mc.gameSettings.overlayShowWeather.value));
            UtilGui.drawPaperDoll(drawRight);
        }
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor4f(1,1,1, 1);
        UtilGui.blockAlpha = 1f;
    }
}
