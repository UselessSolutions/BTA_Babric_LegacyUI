package useless.legacyui.Mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.core.block.entity.TileEntityFlag;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.player.inventory.InventoryPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import useless.legacyui.Gui.GuiScreens.GuiLegacyCrafting;
import useless.legacyui.Gui.GuiScreens.GuiLegacyFlag;
import useless.legacyui.LegacyUI;

@Mixin(value = EntityPlayerSP.class, remap = false)
public class EntityPlayerSPMixin{
    @Shadow
    protected Minecraft mc;
    @Unique
    public InventoryPlayer inventory = ((EntityPlayer)(Object)this).inventory;
    @Inject(method = "displayGUIWorkbench(III)V", at = @At("HEAD"), cancellable = true)
    private void displayLegacyCrafting(int x, int y, int z, CallbackInfo ci){
        if (LegacyUI.modSettings.getEnableLegacyCrafting().value){
            mc.displayGuiScreen(new GuiLegacyCrafting(mc.thePlayer, x, y, z, 9));
            ci.cancel();
        }
    }
    @Inject(method = "displayGUIEditFlag(Lnet/minecraft/core/block/entity/TileEntityFlag;)V", at = @At("HEAD"), cancellable = true)
    private void displayLegacyFlag(TileEntityFlag tileEntityFlag, CallbackInfo ci){
        if (LegacyUI.modSettings.getEnableLegacyFlag().value){
            mc.displayGuiScreen(new GuiLegacyFlag(mc.thePlayer, tileEntityFlag, this.mc.renderEngine));
            ci.cancel();
        }
    }
}
