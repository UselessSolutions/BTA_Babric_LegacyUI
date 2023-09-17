package useless.legacyui.Mixins.Gamemodes;

import net.minecraft.core.player.gamemode.GamemodeSurvival;
import net.minecraft.core.player.inventory.ContainerPlayer;
import net.minecraft.core.player.inventory.InventoryPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import useless.legacyui.Gui.Containers.LegacyContainerPlayerSurvival;
import useless.legacyui.ModSettings;

@Mixin(value = GamemodeSurvival.class, remap = false)
public class GamemodeSurvivalMixin {
    @Inject(method = "getContainer(Lnet/minecraft/core/player/inventory/InventoryPlayer;Z)Lnet/minecraft/core/player/inventory/ContainerPlayer;", at = @At("RETURN"), cancellable = true)
    private void returnModdedContainer(InventoryPlayer inventory, boolean isMultiplayer, CallbackInfoReturnable<ContainerPlayer> cir){
        if (ModSettings.Gui.EnableLegacyInventorySurvival()){
            cir.setReturnValue(new LegacyContainerPlayerSurvival(inventory, isMultiplayer));
        }
    }
}
