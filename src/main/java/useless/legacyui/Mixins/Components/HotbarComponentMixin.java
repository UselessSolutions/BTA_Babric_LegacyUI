package useless.legacyui.Mixins.Components;

import net.minecraft.client.gui.hud.HotbarComponent;
import net.minecraft.client.render.FontRenderer;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.core.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import useless.legacyui.Gui.GuiScreens.UtilGui;

@Mixin(value = HotbarComponent.class, remap = false)
public class HotbarComponentMixin {
    @Redirect(method = "render(Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/gui/GuiIngame;IIF)V", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glColor4f(FFFF)V"))
    private void hotbarFadeout(float red, float green, float blue, float alpha){
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(red, green, blue, UtilGui.getHotbarAlpha());
    }
    @Redirect(method = "renderInventorySlot(Lnet/minecraft/client/Minecraft;IIIF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/ItemEntityRenderer;renderItemIntoGUI(Lnet/minecraft/client/render/FontRenderer;Lnet/minecraft/client/render/RenderEngine;Lnet/minecraft/core/item/ItemStack;IIF)V"))
    private void fadeIntoGUI(ItemEntityRenderer instance, FontRenderer fontrenderer, RenderEngine renderengine, ItemStack itemstack, int i, int j, float alpha){
        instance.renderItemIntoGUI(fontrenderer, renderengine, itemstack, i, j, UtilGui.getHotbarAlpha());
    }
    @Redirect(method = "renderInventorySlot(Lnet/minecraft/client/Minecraft;IIIF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/ItemEntityRenderer;renderItemOverlayIntoGUI(Lnet/minecraft/client/render/FontRenderer;Lnet/minecraft/client/render/RenderEngine;Lnet/minecraft/core/item/ItemStack;IIF)V"))
    private void fadeIntoGUIOverlay(ItemEntityRenderer instance, FontRenderer fontrenderer, RenderEngine renderengine, ItemStack itemstack, int i, int j, float alpha){
        instance.renderItemOverlayIntoGUI(fontrenderer, renderengine, itemstack, i, j, UtilGui.getHotbarAlpha());
    }
}
