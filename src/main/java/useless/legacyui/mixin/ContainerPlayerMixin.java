package useless.legacyui.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.player.gamemode.Gamemode;
import net.minecraft.core.player.inventory.Container;
import net.minecraft.core.player.inventory.ContainerPlayer;
import net.minecraft.core.player.inventory.InventoryCrafting;
import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.core.player.inventory.slot.SlotCrafting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(value = ContainerPlayer.class, remap = false)
public class ContainerPlayerMixin extends Container {

    @Redirect(method = "<init>(Lnet/minecraft/core/player/inventory/InventoryPlayer;Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/player/inventory/ContainerPlayer;addSlot(Lnet/minecraft/core/player/inventory/slot/Slot;)V"))
    private void craftingSlotRemover(ContainerPlayer containerPlayer, Slot slot){
        Minecraft mc = Minecraft.getMinecraft(this);
        EntityPlayer player = mc.thePlayer;
        boolean isCreative;
        if (player != null){
            isCreative = player.getGamemode() == Gamemode.creative;

        }
        else {
            isCreative = true;

        }

        if (mc.isMultiplayerWorld()){
            addSlot(containerPlayer, slot);
        }
        else {
            if (slot instanceof SlotCrafting && (!isCreative)){
                return; // Remove crafting output
            } else if (slot.getInventory() instanceof InventoryCrafting && (!isCreative)) {
                return; // Remove crafting grid
            } else {
                addSlot(containerPlayer, slot);
            }
        }



    }

    @Unique
    private void addSlot(ContainerPlayer containerPlayer, Slot slot){
        slot.id = containerPlayer.inventorySlots.size(); // Add slot is private so manually doing it instead
        containerPlayer.inventorySlots.add(slot);
        containerPlayer.inventoryItemStacks.add(null);
    }

    /**
     * @author Useless
     * @reason Legacy ui survival inventory has fewer slots
     */
    @Overwrite
    public List<Integer> getMoveSlots(InventoryAction action, Slot slot, int target, EntityPlayer player) {
        if (player.getGamemode() == Gamemode.survival){
            return survivalGetMoveSlots(action, slot, target, player);
        }
        return creativeGetMoveSlots(action, slot, target, player);

    }

    @Unique
    private List<Integer> survivalGetMoveSlots(InventoryAction action, Slot slot, int target, EntityPlayer player){
        if (slot.id >= 5-5 && slot.id <= 8-5) { // armor slots
            return this.getSlots(5-5, 4, false);
        }
        if (action == InventoryAction.MOVE_SIMILAR) {
            if (slot.id >= 9-5 && slot.id <= 44-5) { // Entire inventory
                return this.getSlots(9-5, 36, false);
            }
        } else {
            if (slot.id >= 9-5 && slot.id <= 35-5) { // Main inventory
                return this.getSlots(9-5, 27, false);
            }
            if (slot.id >= 36-5 && slot.id <= 44-5) { // Hotbar
                return this.getSlots(36-5, 9, false);
            }
        }
        return null;
    }
    @Unique
    public List<Integer> creativeGetMoveSlots(InventoryAction action, Slot slot, int target, EntityPlayer player) {
        if (slot.id == 0) {
            return this.getSlots(0, 1, false);
        }
        if (slot.id >= 1 && slot.id <= 4) {
            return this.getSlots(1, 4, false);
        }
        if (slot.id >= 5 && slot.id <= 8) {
            return this.getSlots(5, 4, false);
        }
        if (action == InventoryAction.MOVE_SIMILAR) {
            if (slot.id >= 9 && slot.id <= 44) {
                return this.getSlots(9, 36, false);
            }
        } else {
            if (slot.id >= 9 && slot.id <= 35) {
                return this.getSlots(9, 27, false);
            }
            if (slot.id >= 36 && slot.id <= 44) {
                return this.getSlots(36, 9, false);
            }
        }
        return null;
    }



    /**
     * @author Useless
     * @reason Legacy ui survival inventory has fewer slots
     */
    @Overwrite
    public List<Integer> getTargetSlots(InventoryAction action, Slot slot, int target, EntityPlayer player) {
        if (player.getGamemode() == Gamemode.survival){
            return survivalGetTargetSlots(action, slot, target, player);
        }
        return creativeGetTargetSlots(action, slot, target, player);

    }
    @Unique
    public List<Integer> survivalGetTargetSlots(InventoryAction action, Slot slot, int target, EntityPlayer player) {
        if (slot.id >= 9-5 && slot.id <= 44-5) {
            if (target == 2) { // Armor Slots
                return this.getSlots(5-5, 4, false);
            }
            if (slot.id < 36-5) { // Hotbar
                return this.getSlots(36-5, 9, false);
            }
            return this.getSlots(9-5, 27, false); // Main Inventory
        }
        return this.getSlots(9-5, 36, false); // Entire Inventory
    }
    @Unique
    public List<Integer> creativeGetTargetSlots(InventoryAction action, Slot slot, int target, EntityPlayer player) {
        if (slot.id >= 9 && slot.id <= 44) {
            if (target == 1) {
                return this.getSlots(1, 4, false);
            }
            if (target == 2) {
                return this.getSlots(5, 4, false);
            }
            if (slot.id < 36) {
                return this.getSlots(36, 9, false);
            }
            return this.getSlots(9, 27, false);
        }
        if (slot.id == 0) {
            return this.getSlots(9, 36, true);
        }
        return this.getSlots(9, 36, false);
    }

    @Shadow
    public boolean isUsableByPlayer(EntityPlayer entityPlayer) {
        return false;
    }
}
