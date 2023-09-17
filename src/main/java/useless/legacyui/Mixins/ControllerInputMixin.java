package useless.legacyui.Mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.input.controller.*;
import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.core.sound.SoundType;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import useless.legacyui.Gui.IGuiController;
import useless.legacyui.LegacySoundManager;
import useless.legacyui.LegacyUI;
import useless.legacyui.ModSettings;

@Mixin(value = ControllerInput.class, remap = false)
public class ControllerInputMixin {
    @Shadow
    public Joystick joyLeft;
    @Shadow
    public Joystick joyRight;
    @Shadow
    public Button buttonA;
    @Shadow
    public Button buttonB;
    @Shadow
    public Button buttonX;
    @Shadow
    public Button buttonY;
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
    public DigitalPad digitalPad;
    @Shadow
    public double cursorX;
    @Shadow
    public double cursorY;
    @Unique
    public ControllerInput thisAsController = (ControllerInput)(Object)(this);
    @Final
    @Shadow
    public final ControllerInventoryHandler craftingGuiHandler = new ControllerInventoryHandler(thisAsController);
    @Final
    @Shadow
    public final Minecraft minecraft = Minecraft.getMinecraft(this);
    /**
     * @author Useless
     * @reason I need to fundamentally restructure the inventory controls system to work with how I want to setup controller support
     */
    @Overwrite
    public void inventoryControls(GuiContainer guiContainer){
        thisAsController = (ControllerInput)(Object)(this);
        boolean hasDoneGuiSpecificControls = false;
        boolean playDefaultSound = true;
        boolean doDefaultSlotSnap = true;
        if (guiContainer instanceof IGuiController){
            IGuiController iGuiController = ((IGuiController) guiContainer);
            iGuiController.GuiControls(thisAsController);
            playDefaultSound = iGuiController.playDefaultPressSound();
            doDefaultSlotSnap = iGuiController.enableDefaultSnapping();
            hasDoneGuiSpecificControls = true;
        }
        Slot currentSlot = this.getSlotAt((int)this.cursorX, (int)this.cursorY, guiContainer);
        if (doDefaultSlotSnap){
            if (currentSlot != null) {
                Slot slot3;
                if (this.digitalPad.right.pressedThisFrame()) {
                    snapRight(guiContainer, currentSlot);
                }
                if (this.digitalPad.left.pressedThisFrame()) {
                    snapLeft(guiContainer, currentSlot);
                }
                if (this.digitalPad.up.pressedThisFrame()) {
                    snapUp(guiContainer, currentSlot);
                }
                if (this.digitalPad.down.pressedThisFrame()) {
                    snapDown(guiContainer, currentSlot);
                }
            }
        }
        if (this.buttonR.pressedThisFrame() && (currentSlot = this.getSlotAtCursor(guiContainer)) != null) {
            System.out.println("SLOT: " + currentSlot.id);
        }
        if (!hasDoneGuiSpecificControls) {
            if (guiContainer instanceof GuiInventoryCreative) {
                GuiInventoryCreative inventoryCreative = (GuiInventoryCreative) guiContainer;
                if (this.buttonL.pressedThisFrame()) {
                    inventoryCreative.scroll(1);
                }
                if (this.buttonR.pressedThisFrame()) {
                    inventoryCreative.scroll(-1);
                }
            }
            if (guiContainer instanceof GuiGuidebook) {
                GuiGuidebook guidebook = (GuiGuidebook) guiContainer;
                if (this.buttonL.pressedThisFrame()) {
                    guidebook.scroll(1);
                }
                if (this.buttonR.pressedThisFrame()) {
                    guidebook.scroll(-1);
                }
            }
            if (guiContainer instanceof GuiCrafting) {
                this.craftingGuiHandler.handleCrafting((GuiCrafting) guiContainer);
            }
            if (guiContainer instanceof GuiInventory) {
                this.craftingGuiHandler.handleInventory((GuiInventory) guiContainer);
            }
            if (guiContainer instanceof GuiFurnace) {
                this.craftingGuiHandler.handleFurnace((GuiFurnace) guiContainer);
            }
        }
        if (!playDefaultSound){
            return;
        }
        if (this.buttonA.pressedThisFrame() || this.buttonX.pressedThisFrame() || this.buttonY.pressedThisFrame()) {
            LegacySoundManager.play.press(true);
        }
        if (this.buttonB.pressedThisFrame()) {
            LegacySoundManager.play.back(true);
        }
    }
    @Shadow
    public Slot getSlotAtCursor(GuiContainer guiContainer) {
        return null;
    }

    @Shadow
    public Slot getSlotAt(int x, int y, GuiContainer guiContainer) {
        return null;
    }
    @Unique
    private void snapLeft(GuiContainer guiContainer, Slot slot) {
        Slot slot3 = null;
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

    @Unique
    private void snapRight(GuiContainer guiContainer, Slot slot){
        Slot slot3 = null;
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
    @Unique
    private void snapUp(GuiContainer guiContainer, Slot slot){
        Slot slot3 = null;
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
    @Unique
    private void snapDown(GuiContainer guiContainer, Slot slot){
        Slot slot3 = null;
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
    @Shadow
    public void snapToSlot(GuiContainer guiContainer, Slot slot3) {
    }
}
