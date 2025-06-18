package useless.legacyui.mixins.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.client.gui.hud.HudIngame;
import net.minecraft.client.render.Font;
import net.minecraft.client.render.TextureManager;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.player.gamemode.Gamemode;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import useless.legacyui.gui.screens.UtilGui;
import useless.legacyui.LegacyUI;

@Mixin(value = HudIngame.class, remap = false)
public class HudIngameMixin {
    @Shadow
    protected Minecraft mc;
    @Redirect(method = "renderGameOverlay(FZII)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/model/ItemModel;renderItemIntoGui(Lnet/minecraft/client/render/tessellator/Tessellator;Lnet/minecraft/client/render/Font;Lnet/minecraft/client/render/TextureManager;Lnet/minecraft/core/item/ItemStack;IIF)V"))
    private void fadeOut(final ItemModel instance, final Tessellator tessellator, final Font font, final TextureManager textureManager, final ItemStack itemstack, final int x, final int y, final float alpha){
        instance.renderItemIntoGui(tessellator, font, textureManager, itemstack, x, y, UtilGui.getHotbarAlpha());
    }

    @Inject(method = "renderGameOverlay(FZII)V", at = @At(value = "TAIL"))
    private void paperDoll(final float partialTicks, final boolean flag, final int mouseX, final int mouseY, final CallbackInfo ci){
        if (LegacyUI.modSettings.legacyui$getHideHotbarInGUIs().value){
            if (this.mc.currentScreen instanceof ScreenContainerAbstract){
                return;
            }
        }
        if (LegacyUI.modSettings.legacyui$getEnablePaperDoll().value && !this.mc.gameSettings.showDebugScreen.value){
            boolean clock = false;
            boolean compass = false;
            boolean rotaryCalendar = false;
            if (this.mc.thePlayer.getGamemode() == Gamemode.creative) {
                clock = true;
                compass = true;
                rotaryCalendar = true;
            } else {
                for (int iinv = 0; iinv < this.mc.thePlayer.inventory.getContainerSize(); ++iinv) {
                    final ItemStack item = this.mc.thePlayer.inventory.getItem(iinv);
                    if (item == null) continue;
                    if (item.itemID == Items.TOOL_CLOCK.id) {
                        clock = true;
                    }
                    if (item.itemID == Items.TOOL_COMPASS.id) {
                        compass = true;
                    }
                    if (item.itemID != Items.TOOL_CALENDAR.id) continue;
                    rotaryCalendar = true;
                }
            }
            boolean drawRight;
            drawRight = (clock && this.mc.gameSettings.overlayShowTime.value);
            drawRight = drawRight || (compass && (this.mc.gameSettings.overlayShowCoords.value || this.mc.gameSettings.overlayShowDirection.value));
            drawRight = drawRight || (rotaryCalendar && (this.mc.gameSettings.overlayShowSeason.value || this.mc.gameSettings.overlayShowWeather.value));
            UtilGui.drawPaperDoll(drawRight);
        }
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor4f(1,1,1, 1);
        UtilGui.blockAlpha = 1f;
    }
}
