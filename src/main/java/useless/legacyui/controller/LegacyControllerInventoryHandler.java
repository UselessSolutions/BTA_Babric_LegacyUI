package useless.legacyui.controller;

import com.b100.utils.interfaces.Condition;
import net.minecraft.client.gui.GuiContainer;
import net.minecraft.client.input.controller.ControllerInput;
import net.minecraft.client.input.controller.ControllerInventoryHandler;
import net.minecraft.core.player.inventory.slot.Slot;
import useless.legacyui.gui.GuiLegacyCrafting;

public class LegacyControllerInventoryHandler extends ControllerInventoryHandler {
    private Slot lastSlot;
    public LegacyControllerInventoryHandler(ControllerInput controllerInput) {
        super(controllerInput);
    }
    public void handleAbstractCrafting(GuiContainer container, Condition<Slot> isInventorySlot, Condition<Slot> isCraftingSlot, Condition<Slot> isResultSlot, int craftingSlot, int resultSlot, int invSlot) {
        Slot slot = this.controllerInput.getSlotAtCursor(container);
        if (this.controllerInput.buttonA.pressedThisFrame()) {
            for (Slot slot2 : container.inventorySlots.inventorySlots) {
                System.out.println(slot2.id + ": " + slot2.getStack());
            }
        }
        if (slot != null && isCraftingSlot.isTrue(slot) && slot.hasStack() && this.controllerInput.buttonA.isPressed() && this.controllerInput.buttonA.getHoldTime() > 10 && this.controllerInput.buttonA.getHoldTime() % 2 == 1) {
            this.click(container, 0);
        }
        if (this.controllerInput.buttonZL.pressedThisFrame()) {
            if (slot != null) {
                if (isCraftingSlot.isTrue(slot)) {
                    if (this.lastSlot != null) {
                        this.controllerInput.snapToSlot(container, this.lastSlot);
                    } else {
                        this.controllerInput.snapToSlot(container, invSlot);
                    }
                } else {
                    if (slot != null && slot.id > 9) {
                        this.lastSlot = slot;
                    }
                    this.controllerInput.snapToSlot(container, craftingSlot);
                }
            } else {
                this.controllerInput.snapToSlot(container, invSlot);
            }
        }
        if (this.controllerInput.buttonZR.pressedThisFrame()) {
            if (slot != null) {
                if (isResultSlot.isTrue(slot)) {
                    if (this.lastSlot != null) {
                        this.controllerInput.snapToSlot(container, this.lastSlot);
                    } else {
                        this.controllerInput.snapToSlot(container, invSlot);
                    }
                } else {
                    if (isInventorySlot.isTrue(slot)) {
                        this.lastSlot = slot;
                    }
                    this.controllerInput.snapToSlot(container, resultSlot);
                }
            } else {
                this.controllerInput.snapToSlot(container, resultSlot);
            }
        }
    }
    public void handleCrafting(GuiLegacyCrafting crafting) {
        if (controllerInput.buttonL.pressedThisFrame()) {
            crafting.scrollTab(-1);
        }
        if (controllerInput.buttonR.pressedThisFrame()) {
            crafting.scrollTab(1);
        }
        if (controllerInput.digitalPad.up.pressedThisFrame()) {
            crafting.scrollSlot(-1);
        }
        if (controllerInput.digitalPad.down.pressedThisFrame()) {
            crafting.scrollSlot(1);
        }
        if (controllerInput.digitalPad.right.pressedThisFrame()) {
            crafting.scrollDisplaySlot(1);
        }
        if (controllerInput.digitalPad.left.pressedThisFrame()) {
            crafting.scrollDisplaySlot(-1);
        }
        /*if (controllerInput.buttonA.pressedThisFrame()) {
            crafting.craft();
        }*/

        this.handleAbstractCrafting(crafting, e -> e.id > 9, e -> e.id > 0 && e.id < 10, e -> e.id == 0, 5, 0, 24);
    }
}
