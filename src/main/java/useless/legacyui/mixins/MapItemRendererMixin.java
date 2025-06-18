package useless.legacyui.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.MapItemRenderer;
import net.minecraft.client.render.TextureManager;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.player.gamemode.Gamemode;
import net.minecraft.core.util.helper.Color;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.saveddata.maps.ItemMapSavedData;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import useless.legacyui.helper.InventoryHelper;
import useless.legacyui.LegacyUI;

@Mixin(value = MapItemRenderer.class, remap = false)
public class MapItemRendererMixin {
    @Inject(method = "renderMap", at = @At("TAIL"))
    private void renderInject(final TextureManager renderengine, final ItemMapSavedData mapdata, final float brightness, final CallbackInfo ci){
        final Minecraft mc = Minecraft.getMinecraft();
        final Player player = mc.thePlayer;
        if (LegacyUI.modSettings.legacyui$getCoordsOnMaps().value && (InventoryHelper.inInventory(player.inventory.mainInventory, new ItemStack(Items.TOOL_COMPASS)) || player.getGamemode() == Gamemode.creative)){
            GL11.glTranslatef(0.0f, 0.0f, -0.4f);
            mc.font.drawString(String.format("X: %s, Y: %s, Z: %s", MathHelper.round(player.x), MathHelper.round(player.y), MathHelper.round(player.z)), 0, 0, Color.floatToIntARGB(1, brightness, brightness, brightness));
        }
    }
}
