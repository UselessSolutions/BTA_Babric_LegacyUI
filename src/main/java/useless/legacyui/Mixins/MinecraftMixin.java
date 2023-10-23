package useless.legacyui.Mixins;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.client.gui.GuiGuidebook;
import net.minecraft.client.gui.GuiInventory;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.option.GameSettings;
import net.minecraft.client.player.controller.PlayerController;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.core.HitResult;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.gamemode.Gamemode;
import net.minecraft.core.util.helper.Axis;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import useless.legacyui.Api.LegacyUIApi;
import useless.legacyui.Api.LegacyUIPlugin;
import useless.legacyui.Gui.GuiScreens.GuiLegacyCrafting;
import useless.legacyui.Gui.GuiScreens.GuiLegacyCreative;
import useless.legacyui.Gui.GuiScreens.GuiLegacyInventory;
import useless.legacyui.LegacyUI;
import useless.legacyui.Settings.ILegacyOptions;
import useless.legacyui.Sorting.LegacyCategoryManager;

import java.util.List;

@Mixin(value = Minecraft.class, remap = false)
public class MinecraftMixin {
    @Shadow
    public EntityPlayerSP thePlayer;
    @Shadow public GameSettings gameSettings;

    @Shadow public HitResult objectMouseOver;

    @Shadow public PlayerController playerController;

    @Shadow public World theWorld;

    @Shadow public WorldRenderer worldRenderer;

    @Inject(method = "getGuiInventory()Lnet/minecraft/client/gui/GuiInventory;", at = @At("RETURN"), cancellable = true)
    private void useCustomInventoryGuis(CallbackInfoReturnable<GuiInventory> cir){
        if (thePlayer.getGamemode() == Gamemode.creative && LegacyUI.modSettings.getEnableLegacyInventoryCreative().value){
            cir.setReturnValue(new GuiLegacyCreative(thePlayer));
        }
        if (thePlayer.getGamemode() == Gamemode.survival && LegacyUI.modSettings.getEnableLegacyInventorySurvival().value){
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
                mc.displayGuiScreen(new GuiGuidebook(thePlayer));
            }
        }
    }
    @Inject(method = "clickMouse(IZZ)V", at = @At("HEAD"))
    private void autoBridge(int clickType, boolean attack, boolean repeat, CallbackInfo ci){
        if (objectMouseOver == null && doAutoBridge()) {
            if (clickType == 1) {
                if (thePlayer.xRot < 45) {return;}
                List<AABB> cubes = thePlayer.world.getCubes(thePlayer, thePlayer.bb.getOffsetBoundingBox(thePlayer.xd, -1.0, 0.0));
                if (cubes.size() < 1){return;}
                AABB cube = cubes.get(0);
                if (cube == null){return;}
                int blockX = (int) cube.minX;
                int blockY = (int) cube.minY;
                int blockZ = (int) cube.minZ;
                Direction playerDirection = Direction.getHorizontalDirection(thePlayer);
                Side side;
                switch (playerDirection){
                    case NORTH:
                        side = Side.NORTH;
                        break;
                    case SOUTH:
                        side = Side.SOUTH;
                        break;
                    case WEST:
                        side = Side.WEST;
                        break;
                    case EAST:
                        side = Side.EAST;
                        break;
                    default:
                        return;
                }

                double yPlaced = 0.5d;
                double xPlaced = 0.5d;
                ItemStack stack = this.thePlayer.inventory.getCurrentItem();
                int numItemsInStack = stack == null ? 0 : stack.stackSize;
                if (this.playerController.activateBlockOrUseItem(thePlayer, theWorld, stack, blockX, blockY, blockZ, side, xPlaced, yPlaced)) {
                    this.playerController.swingItem(true);
                }
                if (stack == null) {
                    return;
                }
                if (stack.stackSize <= 0) {
                    this.thePlayer.inventory.mainInventory[this.thePlayer.inventory.currentItem] = null;
                } else if (stack.stackSize != numItemsInStack) {
                    worldRenderer.itemRenderer.func_9449_b();
                }
            }
        }
    }
    @Inject(method = "run()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;startGame()V", shift = At.Shift.AFTER))
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
    @Unique
    private boolean doAutoBridge(){
        return LegacyUI.modSettings.getEnableAutoBridge().value;
    }
}
