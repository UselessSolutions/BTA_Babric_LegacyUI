package useless.legacyui.mixin;

import net.java.games.input.Controller;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.input.controller.*;
import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.core.sound.SoundType;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import useless.legacyui.Controller.LegacyControllerInventoryHandler;
import useless.legacyui.Gui.GuiLegacyCrafting;

@Mixin(value = ControllerInput.class, remap = false)
public class ControllerInputMixin {
    @Final
    @Shadow
    public Minecraft minecraft;
    @Shadow
    public Button buttonA;
    @Shadow
    public Button buttonB;
    @Shadow
    public Button buttonX;
    @Shadow
    public Button buttonY;

    @Final
    @Shadow
    public ControllerInventoryHandler craftingGuiHandler;

    @Unique
    public LegacyControllerInventoryHandler legacyControllerInventoryHandler;

    @Inject(method = "Lnet/minecraft/client/input/controller/ControllerInput;<init>(Lnet/minecraft/client/Minecraft;Lnet/java/games/input/Controller;)V", at = @At("TAIL"))
    public void constructor(Minecraft minecraft, Controller controller, CallbackInfo cbi){
        legacyControllerInventoryHandler = new LegacyControllerInventoryHandler(craftingGuiHandler.controllerInput);
    }

    /**
     * @author Useless
     * @reason Need to overwrite dpad controls for legacy UI
     */
    @Inject(method = "Lnet/minecraft/client/input/controller/ControllerInput;inventoryControls(Lnet/minecraft/client/gui/GuiContainer;)V", at = @At("HEAD"))
    public void inventoryControlsInject(GuiContainer guiContainer, CallbackInfo cbi) {
        if ((guiContainer instanceof GuiLegacyCrafting)) {
            if (this.buttonA.pressedThisFrame() || this.buttonX.pressedThisFrame() || this.buttonY.pressedThisFrame()) {
                this.minecraft.sndManager.playSound("random.ui_click", SoundType.GUI_SOUNDS, 1.0f, 1.0f);
            }
            if (this.buttonB.pressedThisFrame()) {
                this.minecraft.sndManager.playSound("random.ui_back", SoundType.GUI_SOUNDS, 1.0f, 1.0f);
            }
            legacyControllerInventoryHandler.handleCrafting((GuiLegacyCrafting)guiContainer);
            return;
        }
    }
}
