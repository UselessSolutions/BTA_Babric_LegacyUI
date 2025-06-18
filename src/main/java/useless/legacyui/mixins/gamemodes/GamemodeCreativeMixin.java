package useless.legacyui.mixins.gamemodes;

import net.minecraft.core.player.gamemode.GamemodeCreative;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.menu.MenuInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import useless.legacyui.gui.containers.LegacyContainerPlayerCreative;
import useless.legacyui.LegacyUI;

@Mixin(value = GamemodeCreative.class, remap = false)
public class GamemodeCreativeMixin {
    @Inject(method = "getContainer(Lnet/minecraft/core/player/inventory/container/ContainerInventory;Z)Lnet/minecraft/core/player/inventory/menu/MenuInventory;", at = @At("RETURN"), cancellable = true)
    private void returnModdedContainer(final ContainerInventory inventory, final boolean isNotClientSide, final CallbackInfoReturnable<MenuInventory> cir){
        if (LegacyUI.modSettings.legacyui$getEnableLegacyInventoryCreative().value){
            cir.setReturnValue(new LegacyContainerPlayerCreative(inventory, isNotClientSide));
        }
    }
}
