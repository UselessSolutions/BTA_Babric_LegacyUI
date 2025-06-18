package useless.legacyui.mixins;

import net.minecraft.client.input.controller.*;
import org.spongepowered.asm.mixin.*;

@Mixin(value = ControllerInput.class, remap = false)
public class ControllerInputMixin {

//    /**
//     * @author Useless
//     * @reason I need to fundamentally restructure the inventory controls system to work with how I want to setup controller support
//     */
//    @Overwrite
//    public void inventoryControls(GuiContainer guiContainer){
//        thisAsController = (ControllerInput)(Object)(this);
//        boolean hasDoneGuiSpecificControls = false;
//        boolean playDefaultSound = true;
//        boolean doDefaultSlotSnap = true;
//        if (guiContainer instanceof IGuiController){
//            IGuiController iGuiController = ((IGuiController) guiContainer);
//            iGuiController.GuiControls(thisAsController);
//            playDefaultSound = iGuiController.playDefaultPressSound();
//            doDefaultSlotSnap = iGuiController.enableDefaultSnapping();
//            hasDoneGuiSpecificControls = true;
//        }
//        Slot currentSlot = this.getSlotAt((int)this.cursorX, (int)this.cursorY, guiContainer);
//        if (doDefaultSlotSnap){
//            if (currentSlot != null) {
//                Slot slot3;
//                if (this.digitalPad.right.pressedThisFrame()) {
//                    snapRight(guiContainer, currentSlot);
//                }
//                if (this.digitalPad.left.pressedThisFrame()) {
//                    snapLeft(guiContainer, currentSlot);
//                }
//                if (this.digitalPad.up.pressedThisFrame()) {
//                    snapUp(guiContainer, currentSlot);
//                }
//                if (this.digitalPad.down.pressedThisFrame()) {
//                    snapDown(guiContainer, currentSlot);
//                }
//            }
//        }
//        if (this.buttonR.pressedThisFrame() && (currentSlot = this.getSlotAtCursor(guiContainer)) != null) {
//            System.out.println("SLOT: " + currentSlot.id);
//        }
//        if (!hasDoneGuiSpecificControls) {
//            if (guiContainer instanceof GuiInventoryCreative) {
//                GuiInventoryCreative inventoryCreative = (GuiInventoryCreative) guiContainer;
//                if (this.buttonL.pressedThisFrame()) {
//                    inventoryCreative.scroll(1);
//                }
//                if (this.buttonR.pressedThisFrame()) {
//                    inventoryCreative.scroll(-1);
//                }
//            }
////            if (guiContainer instanceof GuiGuidebook) { TODO add controller support to new guidebook
////                GuiGuidebook guidebook = (GuiGuidebook) guiContainer;
////                if (this.buttonL.pressedThisFrame()) {
////                    guidebook.scroll(1);
////                }
////                if (this.buttonR.pressedThisFrame()) {
////                    guidebook.scroll(-1);
////                }
////            }
//            if (guiContainer instanceof GuiCrafting) {
//                this.craftingGuiHandler.handleCrafting((GuiCrafting) guiContainer);
//            }
//            if (guiContainer instanceof GuiInventory) {
//                this.craftingGuiHandler.handleInventory((GuiInventory) guiContainer);
//            }
//            if (guiContainer instanceof GuiFurnace) {
//                this.craftingGuiHandler.handleFurnace((GuiFurnace) guiContainer);
//            }
//        }
//        if (!playDefaultSound){
//            return;
//        }
//        if (this.buttonA.pressedThisFrame() || this.buttonX.pressedThisFrame() || this.buttonY.pressedThisFrame()) {
//            LegacySoundManager.play.press(true);
//        }
//        if (this.buttonB.pressedThisFrame()) {
//            LegacySoundManager.play.back(true);
//        }
//    }
}
