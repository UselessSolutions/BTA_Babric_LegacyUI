package useless.legacyui.Gui.Containers;

import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.ContainerPlayerCreative;
import net.minecraft.core.player.inventory.InventoryPlayer;
import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.core.player.inventory.slot.SlotArmor;
import net.minecraft.core.player.inventory.slot.SlotCreative;
import useless.legacyui.Gui.GuiScreens.GuiLegacyCreative;
import useless.legacyui.Gui.Slots.SlotNull;
import useless.legacyui.Sorting.Item.ItemCategory;
import useless.legacyui.Sorting.LegacyCategoryManager;

import java.util.Arrays;

public class LegacyContainerPlayerCreative extends ContainerPlayerCreative {
    public static int slotsWide = 13;
    public static int slotsTall = 6;
    public static InventoryPlayer inventory;
    public LegacyContainerPlayerCreative(InventoryPlayer inventory, boolean isSinglePlayer) {
        super(inventory, isSinglePlayer);
        this.inventory = inventory;
        createSlots();
        setSlots();
    }
    public void createSlots() {
        inventorySlots.clear(); // Remove all slots made in super class
        for (int index = 0; index < 5; ++index){ // Null Slots to keep alignment with server
            this.addSlot(new SlotNull(this.playerInv,index, -5000, -5000));
        }
        for (int index = 0; index < 4; ++index) { // Create Armor Slots
            this.addSlot(new SlotArmor(this, inventory, inventory.getSizeInventory() - 1 - index, -5000, -5000, index));
        }
        for (int row = 0; row < 3; ++row) { // Create Main Inventory Slots
            for (int column = 0; column < 9; ++column) {
                this.addSlot(new Slot(this.playerInv, row * 9 + column + 9, -5000,-5000));
            }
        }
        for (int column = 0; column < 9; ++column) { // Create Hotbar slots
            this.addSlot(new Slot(inventory, column, 56 + column * 18, 160));
        }
        this.creativeSlotsStart = this.inventorySlots.size();
        for (int i = 0; i < slotsWide * slotsTall; ++i) {
            int x = i % slotsWide;
            int y = i / slotsWide;
            this.addSlot(new SlotCreative(this.creativeSlotsStart + i, 12 + x * 18, 46 + y * 18, null));
        }
        this.addSlot(new SlotNull(this.playerInv,inventorySlots.size(), 223, 160));
        this.addSlot(new SlotNull(this.playerInv,inventorySlots.size(), 33, 160));
    }
    public int getCreativeSlotsStart(){
        return creativeSlotsStart;
    }
    public static int getTotalRows(){
        ItemCategory currentCategory = LegacyCategoryManager.creativeCategories.get(GuiLegacyCreative.currentTab);
        return (int) Math.ceil((double) currentCategory.itemStacks.length / slotsWide);
    }
    public void setSlots(){
        ItemCategory currentCategory = LegacyCategoryManager.creativeCategories.get(GuiLegacyCreative.currentTab);
        for (int i = 0; i < slotsWide * slotsTall; ++i) {
            ItemStack item;
            int index = i +  + (GuiLegacyCreative.currentRow * slotsWide);
            if (index < currentCategory.itemStacks.length && index >= 0){
                item = currentCategory.itemStacks[index];
            } else {
                item = null;
            }
            ((SlotCreative) this.inventorySlots.get(creativeSlotsStart+i)).item = item;
        }
    }
}
