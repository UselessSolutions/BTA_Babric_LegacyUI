package useless.legacyui.mixins.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.client.gui.hud.component.HudComponentHotbar;
import net.minecraft.client.render.Font;
import net.minecraft.client.render.TextureManager;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import useless.legacyui.LegacyUI;
import useless.legacyui.gui.screens.UtilGui;

@Mixin(value = HudComponentHotbar.class, remap = false)
public class HudComponentHotbarMixin {
    @Redirect(method = "render(Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/hud/HudIngame;IIF)V",
        at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glColor4f(FFFF)V"))
    private void hotbarFadeout(final float red, final float green, final float blue, final float alpha){
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(red, green, blue, UtilGui.getHotbarAlpha());
    }
    @Redirect(method = "renderInventorySlot(Lnet/minecraft/client/Minecraft;IIIF)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/model/ItemModel;renderItemIntoGui(Lnet/minecraft/client/render/tessellator/Tessellator;Lnet/minecraft/client/render/Font;Lnet/minecraft/client/render/TextureManager;Lnet/minecraft/core/item/ItemStack;IIF)V"))
    private void fadeIntoGUI(final ItemModel instance, final Tessellator tessellator, final Font font, final TextureManager textureManager, final ItemStack itemstack, final int x, final int y, final float alpha){
        instance.renderItemIntoGui(tessellator, font, textureManager, itemstack, x, y, UtilGui.getHotbarAlpha());
    }
    @Redirect(method = "renderInventorySlot(Lnet/minecraft/client/Minecraft;IIIF)V",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/model/ItemModel;renderItemOverlayIntoGUI(Lnet/minecraft/client/render/tessellator/Tessellator;Lnet/minecraft/client/render/Font;Lnet/minecraft/client/render/TextureManager;Lnet/minecraft/core/item/ItemStack;IIF)V"))
    private void fadeIntoGUIOverlay(final ItemModel instance, final Tessellator tessellator, final Font font, final TextureManager textureManager, final ItemStack itemstack, final int x, final int y, final float alpha){
        instance.renderItemOverlayIntoGUI(tessellator, font, textureManager, itemstack, x, y, UtilGui.getHotbarAlpha());
    }

    @Inject(method = "isVisible", at = @At("HEAD"), cancellable = true)
    private void dontRenderInGuiHotbar(final Minecraft mc, final CallbackInfoReturnable<Boolean> cir){
        if (LegacyUI.modSettings.legacyui$getHideHotbarInGUIs().value){
            if (mc.currentScreen instanceof ScreenContainerAbstract){
                cir.setReturnValue(false);
            }
        }
    }
}
