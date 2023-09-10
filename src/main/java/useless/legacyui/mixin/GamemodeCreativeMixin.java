package useless.legacyui.mixin;

import net.minecraft.core.player.gamemode.GamemodeCreative;
import net.minecraft.core.player.inventory.ContainerPlayer;
import net.minecraft.core.player.inventory.InventoryPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import useless.legacyui.Gui.Container.ContainerCreativeLegacy;

@Mixin(value = GamemodeCreative.class, remap = false)
public class GamemodeCreativeMixin {
    @Inject(method = "getContainer(Lnet/minecraft/core/player/inventory/InventoryPlayer;Z)Lnet/minecraft/core/player/inventory/ContainerPlayer;", at = @At(value = "HEAD"), cancellable = true)
    private void customCreativeContainer(InventoryPlayer inventory, boolean isMultiplayer, CallbackInfoReturnable<ContainerPlayer> cir){
        cir.setReturnValue(new ContainerCreativeLegacy(inventory, isMultiplayer));
    }
}
