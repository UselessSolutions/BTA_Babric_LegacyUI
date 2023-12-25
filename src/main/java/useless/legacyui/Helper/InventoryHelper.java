package useless.legacyui.Helper;

import net.minecraft.core.block.Block;
import net.minecraft.core.crafting.legacy.CraftingManager;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCraftingShaped;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCraftingShapeless;
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
        for (Slot slot : slots) {
            ItemStack itemStack = slot.getStack();
            if (itemStack == null || itemStack.itemID != itemToFind.itemID || itemStack.getMetadata() != itemToFind.getMetadata())
                continue;
            itemCount += itemStack.stackSize;
            ++stackCount;
        }
        return Math.max(itemCount, stackCount);
    }
    public static int itemsInInventory(IInventory inventory, RecipeSymbol matchingSymbol){
        if (inventory == null) {return -1;}
        if (matchingSymbol == null) {return -1;}
        int itemCount = 0;
        int stackCount = 0;
        int size = inventory.getSizeInventory();
        for (int i = 0; i < size; i++) {
            ItemStack itemStack = inventory.getStackInSlot(i);
            if (!matchingSymbol.matches(itemStack))
                continue;
            itemCount += itemStack.stackSize;
            ++stackCount;
        }
        return Math.max(itemCount, stackCount);
    }


    public static int findStackIndex(ItemStack[] inventory, RecipeSymbol matchingSymbol){
        for (int i = 0; i < inventory.length; i++){
            if (matchingSymbol.matches(inventory[i])){
                return i;
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
    public static boolean inInventory(ItemStack[] stacks, ItemStack item){
        for (ItemStack stack: stacks) {
            if (stack != null && stack.isItemEqual(item)){
                return true;
            }
        }
        return false;
    }
    public static RecipeSymbol[] getRecipeInput(RecipeEntryCrafting<?, ?> recipe){
        RecipeSymbol[] recipeInput;
        if (recipe instanceof RecipeEntryCraftingShaped){
            return ((RecipeEntryCraftingShaped)recipe).getInput();
        }
        if (recipe instanceof RecipeEntryCraftingShapeless){
            return ((RecipeEntryCraftingShapeless) recipe).getInput().toArray(new RecipeSymbol[0]);
        }
        throw new IllegalArgumentException("Recipe not of valid type! " + recipe.getClass().getSimpleName());
    }
}
