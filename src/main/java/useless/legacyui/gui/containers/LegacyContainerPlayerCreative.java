package useless.legacyui.gui.containers;

import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.menu.MenuInventoryCreative;
import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.core.player.inventory.slot.SlotArmor;
import net.minecraft.core.player.inventory.slot.SlotCreative;
import useless.legacyui.gui.screens.GuiLegacyCreative;
import useless.legacyui.gui.slots.SlotNull;
import useless.legacyui.sorting.item.ItemCategory;
import useless.legacyui.sorting.LegacyCategoryManager;

public class LegacyContainerPlayerCreative extends MenuInventoryCreative {
    public static int slotsWide = 13;
    public static int slotsTall = 6;
    public static ContainerInventory inventory;
    public LegacyContainerPlayerCreative(final ContainerInventory inventory, final boolean isSinglePlayer) {
        super(inventory, isSinglePlayer);
        LegacyContainerPlayerCreative.inventory = inventory;
        createSlots();
        setSlots();
    }
    public void createSlots() {
        this.slots.clear(); // Remove all slots made in super class
        for (int index = 0; index < 5; ++index){ // Null Slots to keep alignment with server
            this.addSlot(new SlotNull(inventory,index, -5000, -5000));
        }
        for (int index = 0; index < 4; ++index) { // Create Armor Slots
            this.addSlot(new SlotArmor(this, inventory, inventory.getContainerSize() - 1 - index, -5000, -5000, index));
        }
        for (int row = 0; row < 3; ++row) { // Create Main Inventory Slots
            for (int column = 0; column < 9; ++column) {
                this.addSlot(new Slot(inventory, row * 9 + column + 9, -5000,-5000));
            }
        }
        for (int column = 0; column < 9; ++column) { // Create Hotbar slots
            this.addSlot(new Slot(inventory, column, 56 + column * 18, 160));
        }
        this.creativeSlotsStart = this.slots.size();
        for (int i = 0; i < slotsWide * slotsTall; ++i) {
            final int x = i % slotsWide;
            final int y = i / slotsWide;
            this.addSlot(new SlotCreative(this.creativeSlotsStart + i, 12 + x * 18, 46 + y * 18, null));
        }
        this.addSlot(new SlotNull(inventory, this.slots.size(), 223, 160));
        this.addSlot(new SlotNull(inventory, this.slots.size(), 33, 160));
    }
    public int getCreativeSlotsStart(){
        return this.creativeSlotsStart;
    }
    public static int getTotalRows(){
        final ItemCategory currentCategory = LegacyCategoryManager.getCreativeCategories().get(GuiLegacyCreative.currentTab);
        return (int) Math.ceil((double) currentCategory.itemStacks.length / slotsWide);
    }
    public void setSlots(){
        final ItemCategory currentCategory = LegacyCategoryManager.getCreativeCategories().get(GuiLegacyCreative.currentTab);
        for (int i = 0; i < slotsWide * slotsTall; ++i) {
            final ItemStack item;
            final int index = i +  + (GuiLegacyCreative.currentRow * slotsWide);
            if (index < currentCategory.itemStacks.length && index >= 0){
                item = currentCategory.itemStacks[index];
            } else {
                item = null;
            }
            ((SlotCreative) this.slots.get(this.creativeSlotsStart +i)).item = item;
        }
    }
}
