package useless.legacyui.Mixins.Gamemodes;

import net.minecraft.core.player.gamemode.GamemodeCreative;
import net.minecraft.core.player.inventory.ContainerPlayer;
import net.minecraft.core.player.inventory.InventoryPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import useless.legacyui.Gui.Containers.LegacyContainerPlayerCreative;
import useless.legacyui.ModSettings;

@Mixin(value = GamemodeCreative.class, remap = false)
public class GamemodeCreativeMixin {
    @Inject(method = "getContainer(Lnet/minecraft/core/player/inventory/InventoryPlayer;Z)Lnet/minecraft/core/player/inventory/ContainerPlayer;", at = @At("RETURN"), cancellable = true)
    private void returnModdedContainer(InventoryPlayer inventory, boolean isMultiplayer, CallbackInfoReturnable<ContainerPlayer> cir){
        if (ModSettings.Gui.EnableLegacyInventoryCreative()){
            cir.setReturnValue(new LegacyContainerPlayerCreative(inventory, isMultiplayer));
        }
    }
}
