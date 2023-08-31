package useless.legacyui.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiInventory;
import net.minecraft.client.gui.GuiInventoryCreative;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.core.player.gamemode.Gamemode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import useless.legacyui.Gui.GuiLegacyCrafting;
import useless.legacyui.Gui.GuiLegacyInventory;


// Fixes a startup class when using some custom classes
@Mixin(value=Minecraft.class, remap=false)
public class MinecraftMixin {
    /**
     * @author Useless
     * @reason Completely replaced old inventory
     */
    @Overwrite
    private GuiInventory getGuiInventory() {
        Minecraft minecraft = Minecraft.getMinecraft(this);
        if (minecraft.thePlayer.getGamemode() == Gamemode.creative) {
            return new GuiInventoryCreative(minecraft.thePlayer);
        }
        return new GuiLegacyInventory(minecraft.thePlayer);
    }

    @Redirect(method = "handleControllerInput()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;displayGuiScreen(Lnet/minecraft/client/gui/GuiScreen;)V", ordinal = 0))
    private void xToCraft(Minecraft minecraft, GuiScreen guiscreen){
        if (minecraft.controllerInput.buttonX.pressedThisFrame()){
            minecraft.displayGuiScreen(new GuiLegacyCrafting(minecraft.thePlayer));
        }
        else {
            minecraft.displayGuiScreen(guiscreen);
        }
    }

}