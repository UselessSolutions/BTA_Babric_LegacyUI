package useless.legacyui.utils;

import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.InventoryPlayer;
import net.minecraft.core.player.inventory.slot.Slot;
import useless.legacyui.LegacyUI;

import java.util.List;

public class InventoryUtil {
    public static int itemsInInventory(IInventory inventory, ItemStack itemToFind){
        if (inventory == null) {return -1;}
        if (itemToFind == null) {return -1;}
        int itemCount = 0;
        int stackCount = 0;
        for (int i = 0; i < inventory.getSizeInventory(); ++i) {
            ItemStack itemStack = inventory.getStackInSlot(i);
            if (itemStack == null || itemStack.itemID != itemToFind.itemID || itemStack.getMetadata() != itemToFind.getMetadata()) continue;
            itemCount += itemStack.stackSize;
            ++stackCount;
        }
        return Math.max(itemCount, stackCount);
    }

    public static int itemsInInventory(List<Slot> slots, ItemStack itemToFind){
        if (slots == null) {return -1;}
        if (itemToFind == null) {return -1;}
        int itemCount = 0;
        int stackCount = 0;
        for (int i = 0; i < slots.size(); ++i) {
            ItemStack itemStack = slots.get(i).getStack();
            if (itemStack == null || itemStack.itemID != itemToFind.itemID || itemStack.getMetadata() != itemToFind.getMetadata()) continue;
            itemCount += itemStack.stackSize;
            ++stackCount;
        }
        return Math.max(itemCount, stackCount);
    }
    public static int findStackIndex(ItemStack[] inventory, ItemStack itemToFind){
        for (int i = 0; i < inventory.length; i++){
            if (inventory[i] != null && inventory[i].getItem() == itemToFind.getItem()){
                return i;
            }
        }
        return -1;
    }


}
