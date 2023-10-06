package useless.legacyui.Mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.MapItemRenderer;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.gamemode.Gamemode;
import net.minecraft.core.world.saveddata.maps.ItemMapSavedData;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import useless.legacyui.Helper.InventoryHelper;
import useless.legacyui.Settings.ModSettings;

import java.awt.*;

@Mixin(value = MapItemRenderer.class, remap = false)
public class MapItemRendererMixin {
    @Inject(method = "renderMap(Lnet/minecraft/client/render/RenderEngine;Lnet/minecraft/core/world/saveddata/maps/ItemMapSavedData;F)V", at = @At("TAIL"))
    private void renderInject(RenderEngine renderengine, ItemMapSavedData mapdata, float brightness, CallbackInfo ci){
        Minecraft mc = Minecraft.getMinecraft(this);
        EntityPlayer player = mc.thePlayer;
        if (ModSettings.legacyOptions.getCoordsOnMaps().value && (InventoryHelper.inInventory(player.inventory.mainInventory, new ItemStack(Item.toolCompass)) || player.getGamemode() == Gamemode.creative)){
            GL11.glTranslatef(0.0f, 0.0f, -0.4f);
            String text = "X: " + (int)player.x + ", Y: " + (int)player.y + ", Z: " + (int)player.z;
            mc.fontRenderer.drawString(text, 0, 0, new Color(brightness, brightness, brightness).getRGB());
        }
    }
}
