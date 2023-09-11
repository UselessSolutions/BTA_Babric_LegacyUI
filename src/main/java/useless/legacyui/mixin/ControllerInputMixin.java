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
import useless.legacyui.ConfigTranslations;
import useless.legacyui.Controller.LegacyControllerInventoryHandler;
import useless.legacyui.Gui.GuiLegacyCrafting;
import useless.legacyui.Gui.GuiLegacyCreative;
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
    @Shadow
    public Joystick joyLeft;
    @Shadow
    public Joystick joyRight;
    @Shadow
    public Button buttonZL;
    @Shadow
    public Button buttonZR;
    @Shadow
    public Button buttonMinus;
    @Shadow
    public Button buttonPlus;
    @Shadow
    public Button buttonL;
    @Shadow
    public Button buttonR;
    @Shadow
    public Button[] buttons;
    @Shadow
    public Joystick[] joysticks;
    @Shadow
    public double cursorX;
    @Shadow
    public double cursorY;
    @Shadow
    public DigitalPad digitalPad;
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
            legacyControllerInventoryHandler.handleCrafting((GuiLegacyCrafting)guiContainer);
            cbi.cancel();
        } else if (guiContainer instanceof GuiInventory) {
            dpadSelect(guiContainer);
            legacyControllerInventoryHandler.handleInventory((GuiInventory)guiContainer);
            cbi.cancel();
        }
    }

    @Unique
    private void dpadSelect(GuiContainer guiContainer){
        Slot slot = this.getSlotAt((int)this.cursorX, (int)this.cursorY, guiContainer);
        if (slot != null) {
            Slot slot3;
            if (this.digitalPad.right.pressedThisFrame()) {
                slot3 = null;
                for (Slot slot2 : guiContainer.inventorySlots.inventorySlots) {
                    if (slot2.xDisplayPosition <= slot.xDisplayPosition || Math.abs(slot2.yDisplayPosition - slot.yDisplayPosition) >= 12 || slot3 != null && slot2.xDisplayPosition >= slot3.xDisplayPosition) continue;
                    slot3 = slot2;
                }
                if (slot3 == null) {
                    for (Slot slot2 : guiContainer.inventorySlots.inventorySlots) {
                        if (Math.abs(slot2.yDisplayPosition - slot.yDisplayPosition) >= 12 || slot3 != null && slot2.xDisplayPosition >= slot3.xDisplayPosition) continue;
                        slot3 = slot2;
                    }
                }
                this.snapToSlot(guiContainer, slot3);
            }
            if (this.digitalPad.left.pressedThisFrame()) {
                slot3 = null;
                for (Slot slot2 : guiContainer.inventorySlots.inventorySlots) {
                    if (slot2.xDisplayPosition >= slot.xDisplayPosition || Math.abs(slot2.yDisplayPosition - slot.yDisplayPosition) >= 12 || slot3 != null && slot2.xDisplayPosition <= slot3.xDisplayPosition) continue;
                    slot3 = slot2;
                }
                if (slot3 == null) {
                    for (Slot slot2 : guiContainer.inventorySlots.inventorySlots) {
                        if (Math.abs(slot2.yDisplayPosition - slot.yDisplayPosition) >= 12 || slot3 != null && slot2.xDisplayPosition <= slot3.xDisplayPosition) continue;
                        slot3 = slot2;
                    }
                }
                this.snapToSlot(guiContainer, slot3);
            }
            if (this.digitalPad.up.pressedThisFrame()) {
                slot3 = null;
                for (Slot slot2 : guiContainer.inventorySlots.inventorySlots) {
                    if (slot2.yDisplayPosition >= slot.yDisplayPosition || Math.abs(slot2.xDisplayPosition - slot.xDisplayPosition) >= 12 || slot3 != null && slot2.yDisplayPosition <= slot3.yDisplayPosition) continue;
                    slot3 = slot2;
                }
                if (slot3 == null) {
                    for (Slot slot2 : guiContainer.inventorySlots.inventorySlots) {
                        if (Math.abs(slot2.xDisplayPosition - slot.xDisplayPosition) >= 12 || slot3 != null && slot2.yDisplayPosition <= slot3.yDisplayPosition) continue;
                        slot3 = slot2;
                    }
                }
                this.snapToSlot(guiContainer, slot3);
            }
            if (this.digitalPad.down.pressedThisFrame()) {
                slot3 = null;
                for (Slot slot2 : guiContainer.inventorySlots.inventorySlots) {
                    if (slot2.yDisplayPosition <= slot.yDisplayPosition || Math.abs(slot2.xDisplayPosition - slot.xDisplayPosition) >= 12 || slot3 != null && slot2.yDisplayPosition >= slot3.yDisplayPosition) continue;
                    slot3 = slot2;
                }
                if (slot3 == null) {
                    for (Slot slot2 : guiContainer.inventorySlots.inventorySlots) {
                        if (Math.abs(slot2.xDisplayPosition - slot.xDisplayPosition) >= 12 || slot3 != null && slot2.yDisplayPosition >= slot3.yDisplayPosition) continue;
                        slot3 = slot2;
                    }
                }
                this.snapToSlot(guiContainer, slot3);
            }
        }
        if (this.buttonR.pressedThisFrame() && (slot = this.getSlotAtCursor(guiContainer)) != null) {
            System.out.println("SLOT: " + slot.id);
        }
    }

    @Shadow
    private Slot getSlotAtCursor(GuiContainer guiContainer) {
        return null;
    }

    @Shadow
    private void snapToSlot(GuiContainer guiContainer, Slot slot3) {
    }

    @Shadow
    private Slot getSlotAt(int i, int i1, GuiContainer guiContainer) {
        return null;
    }

}
