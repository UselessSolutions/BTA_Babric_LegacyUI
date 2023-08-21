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
    @Overwrite
    public void inventoryControls(GuiContainer guiContainer) {
        Slot slot = this.getSlotAt((int)this.cursorX, (int)this.cursorY, guiContainer);
        if (slot != null && !(guiContainer instanceof GuiLegacyCrafting)) {
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
        if (guiContainer instanceof GuiInventoryCreative) {
            GuiInventoryCreative inventoryCreative = (GuiInventoryCreative)guiContainer;
            if (this.buttonL.pressedThisFrame()) {
                inventoryCreative.scroll(1);
            }
            if (this.buttonR.pressedThisFrame()) {
                inventoryCreative.scroll(-1);
            }
        }
        if (guiContainer instanceof GuiGuidebook) {
            GuiGuidebook guidebook = (GuiGuidebook)guiContainer;
            if (this.buttonL.pressedThisFrame()) {
                guidebook.scroll(1);
            }
            if (this.buttonR.pressedThisFrame()) {
                guidebook.scroll(-1);
            }
        }
        if (guiContainer instanceof GuiCrafting) {
            this.craftingGuiHandler.handleCrafting((GuiCrafting)guiContainer);
        }
        if (guiContainer instanceof GuiInventory) {
            this.craftingGuiHandler.handleInventory((GuiInventory)guiContainer);
        }
        if (guiContainer instanceof GuiFurnace) {
            this.craftingGuiHandler.handleFurnace((GuiFurnace)guiContainer);
        }
        if (this.buttonA.pressedThisFrame() || this.buttonX.pressedThisFrame() || this.buttonY.pressedThisFrame()) {
            this.minecraft.sndManager.playSound("random.ui_click", SoundType.GUI_SOUNDS, 1.0f, 1.0f);
        }
        if (this.buttonB.pressedThisFrame()) {
            this.minecraft.sndManager.playSound("random.ui_back", SoundType.GUI_SOUNDS, 1.0f, 1.0f);
        }
        if (guiContainer instanceof GuiLegacyCrafting) {
            legacyControllerInventoryHandler.handleCrafting((GuiLegacyCrafting)guiContainer);

        }
    }

    @Shadow
    public Slot getSlotAt(int x, int y, GuiContainer guiContainer) {return null;}
    @Shadow
    public Slot getSlotAtCursor(GuiContainer guiContainer) {return null;}
    @Shadow
    public void snapToSlot(GuiContainer guiContainer, Slot slot) {}
}
