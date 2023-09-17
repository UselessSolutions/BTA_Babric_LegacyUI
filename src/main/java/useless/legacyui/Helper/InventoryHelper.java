package useless.legacyui.Helper;

import net.minecraft.core.block.Block;
import net.minecraft.core.crafting.CraftingManager;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.slot.Slot;

import java.util.List;

public class InventoryHelper {
    public static int itemsInInventory(IInventory inventory, ItemStack itemToFind){
        return itemsInInventory(inventory, itemToFind, false);
    }
    public static int itemsInInventory(IInventory inventory, ItemStack itemToFind, boolean useAlts){
        if (inventory == null) {return -1;}
        if (itemToFind == null) {return -1;}
        int itemCount = 0;
        int stackCount = 0;
        Block[] altGroup = getAltGroup(itemToFind);
        if (useAlts && altGroup != null){
            for (int i = 0; i < inventory.getSizeInventory(); ++i) {
                ItemStack itemStack = inventory.getStackInSlot(i);
                if (itemStack == null) continue;
                for (Block block : altGroup){
                    if (itemStack.getItem() == block.asItem()){
                        itemCount += itemStack.stackSize;
                        ++stackCount;
                    }
                }
            }
        }
        else {
            for (int i = 0; i < inventory.getSizeInventory(); ++i) {
                ItemStack itemStack = inventory.getStackInSlot(i);
                if (itemStack == null || itemStack.itemID != itemToFind.itemID || itemStack.getMetadata() != itemToFind.getMetadata()) continue;
                itemCount += itemStack.stackSize;
                ++stackCount;
            }
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
        return findStackIndex(inventory,itemToFind,false);
    }

    public static int findStackIndex(ItemStack[] inventory, ItemStack itemToFind, boolean useAlts){
        Block[] altGroup = getAltGroup(itemToFind);
        if (useAlts && altGroup != null){
            for (int i = 0; i < inventory.length; i++){
                if (inventory[i] != null){
                    for (Block block : altGroup){
                        if (inventory[i].itemID == block.id){
                            return  i;
                        }
                    }
                }
            }
        }
        else {
            for (int i = 0; i < inventory.length; i++){
                if (inventory[i] != null && inventory[i].getItem() == itemToFind.getItem() && inventory[i].getMetadata() == itemToFind.getMetadata()){
                    return i;
                }
            }
        }

        return -1;
    }

    public static Block[] getAltGroup(ItemStack itemStack){
        for (int i = 0; i < CraftingManager.blockAlternatives.length; i++){
            for (int j = 0; j < CraftingManager.blockAlternatives[i].length; j++){
                if(CraftingManager.blockAlternatives[i][j].id == itemStack.itemID){
                    return CraftingManager.blockAlternatives[i];
                }
            }
        }
        return null;
    }

}
