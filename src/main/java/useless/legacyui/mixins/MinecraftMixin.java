package useless.legacyui.mixins;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.PlayerLocal;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.container.ScreenInventory;
import net.minecraft.client.option.GameSettings;
import net.minecraft.core.player.gamemode.Gamemode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import useless.legacyui.api.LegacyUIApi;
import useless.legacyui.api.LegacyUIPlugin;
import useless.legacyui.gui.screens.GuiLegacyCrafting;
import useless.legacyui.gui.screens.GuiLegacyCreative;
import useless.legacyui.gui.screens.GuiLegacyInventory;
import useless.legacyui.LegacyUI;
import useless.legacyui.settings.ILegacyOptions;
import useless.legacyui.sorting.LegacyCategoryManager;

@Mixin(value = Minecraft.class, remap = false, priority = 1001)
public class MinecraftMixin {
    @Shadow
    public PlayerLocal thePlayer;
    @Shadow public GameSettings gameSettings;

    @Inject(method = "getInventoryScreen", at = @At("RETURN"), cancellable = true)
    private void useCustomInventoryGuis(final CallbackInfoReturnable<ScreenInventory> cir){
        if (this.thePlayer.getGamemode() == Gamemode.creative && LegacyUI.modSettings.legacyui$getEnableLegacyInventoryCreative().value){
            cir.setReturnValue(new GuiLegacyCreative(this.thePlayer));
        }
        if (this.thePlayer.getGamemode() == Gamemode.survival && LegacyUI.modSettings.legacyui$getEnableLegacyInventorySurvival().value){
            cir.setReturnValue(new GuiLegacyInventory(this.thePlayer));
        }
    }
    @Redirect(method = "handleControllerInput()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;displayScreen(Lnet/minecraft/client/gui/Screen;)V", ordinal = 0))
    private void xToCraft(final Minecraft minecraft, final Screen guiscreen){
        if (minecraft.controllerInput.buttonX.pressedThisFrame()){
            minecraft.displayScreen(new GuiLegacyCrafting(minecraft.thePlayer, 4));
        }
        else {
            minecraft.displayScreen(guiscreen);
        }
    }

    @Inject(method = "startGame", at = @At(value = "TAIL"))
    private void startOfGameInit(final CallbackInfo ci){
        new LegacyUIPlugin().register();
        FabricLoader.getInstance().getEntrypoints("legacyui", LegacyUIApi.class).forEach(api -> {
            try {
                api.getClass().getDeclaredMethod("register"); // Make sure the method is implemented
                api.register();
            } catch (final NoSuchMethodException ignored) {
            }
        });
        LegacyCategoryManager.build();
        LegacyUI.modSettings = (ILegacyOptions) this.gameSettings;
    }
}
