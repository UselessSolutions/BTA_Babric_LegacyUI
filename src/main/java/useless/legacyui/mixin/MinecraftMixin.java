package useless.legacyui.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiInventory;
import net.minecraft.client.gui.GuiInventoryCreative;
import net.minecraft.core.player.gamemode.Gamemode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import useless.legacyui.Gui.GuiLegacyInventory;


// Fixes a startup class when using some custom classes
@Mixin(value=Minecraft.class, remap=false)
public class MinecraftMixin {
    @Redirect(method = "Lnet/minecraft/client/Minecraft;runTick()V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;getGuiInventory()Lnet/minecraft/client/gui/GuiInventory;"))
    private GuiInventory loadInventory(Minecraft minecraft){
        if (minecraft.thePlayer.getGamemode() == Gamemode.creative) {
            return new GuiInventoryCreative(minecraft.thePlayer);
        }
        return new GuiLegacyInventory(minecraft.thePlayer);
    }


}