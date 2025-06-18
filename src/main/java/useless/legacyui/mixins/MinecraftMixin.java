package useless.legacyui.mixins;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.PlayerLocal;
import net.minecraft.client.gui.container.ScreenInventory;
import net.minecraft.client.option.GameSettings;
import net.minecraft.client.player.controller.PlayerController;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.core.player.gamemode.Gamemode;
import net.minecraft.core.util.phys.HitResult;
import net.minecraft.core.world.World;
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

    @Shadow public HitResult objectMouseOver;

    @Shadow public PlayerController playerController;

    @Shadow public World theWorld;

    @Shadow public WorldRenderer worldRenderer;

    @Inject(method = "getInventoryScreen", at = @At("RETURN"), cancellable = true)
    private void useCustomInventoryGuis(CallbackInfoReturnable<ScreenInventory> cir){
        if (thePlayer.getGamemode() == Gamemode.creative && LegacyUI.modSettings.legacyui$getEnableLegacyInventoryCreative().value){
            cir.setReturnValue(new GuiLegacyCreative(thePlayer));
        }
        if (thePlayer.getGamemode() == Gamemode.survival && LegacyUI.modSettings.legacyui$getEnableLegacyInventorySurvival().value){
            cir.setReturnValue(new GuiLegacyInventory(thePlayer));
        }
    }
    @Redirect(method = "handleControllerInput()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;displayGuiScreen(Lnet/minecraft/client/gui/GuiScreen;)V", ordinal = 0))
    private void xToCraft(Minecraft minecraft, GuiScreen guiscreen){
        if (minecraft.controllerInput.buttonX.pressedThisFrame()){
            minecraft.displayGuiScreen(new GuiLegacyCrafting(minecraft.thePlayer, 4));
        }
        else {
            minecraft.displayGuiScreen(guiscreen);
        }
    }
    @Inject(method = "handleControllerInput()V", at = @At("TAIL"))
    private void dpRightToOpenGuidebook(CallbackInfo ci){
        Minecraft mc = Minecraft.getMinecraft(this);
        if (mc.currentScreen == null){
            if (mc.controllerInput.digitalPad.right.pressedThisFrame()){
                mc.displayGuiScreen(new GuiGuidebook());
            }
        }
    }

    @Inject(method = "startGame", at = @At(value = "TAIL"))
    private void startOfGameInit(CallbackInfo ci){
        new LegacyUIPlugin().register();
        FabricLoader.getInstance().getEntrypoints("legacyui", LegacyUIApi.class).forEach(api -> {
            try {
                api.getClass().getDeclaredMethod("register"); // Make sure the method is implemented
                api.register();
            } catch (NoSuchMethodException ignored) {
            }
        });
        LegacyCategoryManager.build();
        LegacyUI.modSettings = (ILegacyOptions) gameSettings;
    }
}
