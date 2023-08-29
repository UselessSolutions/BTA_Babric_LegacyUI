package useless.legacyui.mixin;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.input.InputHandler;
import net.minecraft.client.input.controller.*;
import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.core.sound.SoundType;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import useless.legacyui.ConfigTranslations;
import useless.legacyui.Controller.LegacyControllerInventoryHandler;
import useless.legacyui.Gui.GuiLegacyCrafting;
import useless.legacyui.LegacyUI;

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
    @Shadow public DigitalPad digitalPad;
    @Shadow @Final public ControllerInventoryHandler craftingGuiHandler;
    @Unique
    public LegacyControllerInventoryHandler legacyControllerInventoryHandler;
    @Inject(method = "<init>(Lnet/minecraft/client/Minecraft;Lnet/java/games/input/Controller;)V", at = @At("TAIL"))
    public void constructor(Minecraft minecraft, Controller controller, CallbackInfo cbi){
        legacyControllerInventoryHandler = new LegacyControllerInventoryHandler(craftingGuiHandler.controllerInput);
    }

    @Inject(method = "inventoryControls(Lnet/minecraft/client/gui/GuiContainer;)V", at = @At("HEAD"), cancellable = true)
    public void inventoryControlsInject(GuiContainer guiContainer, CallbackInfo cbi) {
        if ((guiContainer instanceof GuiLegacyCrafting)) {
            if (this.buttonA.pressedThisFrame() || this.buttonX.pressedThisFrame() || this.buttonY.pressedThisFrame()) {
                if (LegacyUI.config.getBoolean(ConfigTranslations.USE_LEGACY_SOUNDS.getKey())){
                    this.minecraft.sndManager.playSound("legacyui.ui.press", SoundType.GUI_SOUNDS, 1.0f, 1.0f);
                }
                else {
                    this.minecraft.sndManager.playSound("random.ui_click", SoundType.GUI_SOUNDS, 1.0f, 1.0f);
                }

            }
            if (this.buttonB.pressedThisFrame()) {
                if (LegacyUI.config.getBoolean(ConfigTranslations.USE_LEGACY_SOUNDS.getKey())){
                    this.minecraft.sndManager.playSound("legacyui.ui.back", SoundType.GUI_SOUNDS, 1.0f, 1.0f);
                }
                else {
                    this.minecraft.sndManager.playSound("random.ui_back", SoundType.GUI_SOUNDS, 1.0f, 1.0f);
                }
            }
            legacyControllerInventoryHandler.handleCrafting((GuiLegacyCrafting)guiContainer);
            cbi.cancel();
        }
    }
}
