package useless.legacyui.Helper;

import net.minecraft.client.gui.guidebook.GuidebookPage;
import net.minecraft.client.gui.guidebook.GuidebookSections;
import net.minecraft.client.gui.guidebook.crafting.CraftingPage;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.core.player.inventory.slot.SlotGuidebook;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryHelper {
    public static Map<RecipeEntryCrafting<?, ?>, List<SlotGuidebook>> recipeMap = new HashMap<>();
    static {
        GuidebookSections.init();
        for (GuidebookPage page: GuidebookSections.CRAFTING.getPages()){
            CraftingPage craftingPage = (CraftingPage) page;
            recipeMap.putAll(craftingPage.map);
        }
    }
    public static int itemsInInventory(IInventory inventory, ItemStack itemToFind){
        if (inventory == null) {return -1;}
        if (itemToFind == null) {return -1;}
        int itemCount = 0;
        int stackCount = 0;
        for (int i = 0; i < inventory.getSizeInventory(); ++i) {
            ItemStack itemStack = inventory.getStackInSlot(i);
            if (itemStack == null) continue;
            if (itemStack.isItemEqual(itemToFind)){
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
        if (matchingSymbol != null){
            for (int i = 0; i < inventory.length; i++){
                if (matchingSymbol.matches(inventory[i])){
                    return i;
                }
            }
        }
        return -1;
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
        List<SlotGuidebook> slots = recipeMap.get(recipe);
        RecipeSymbol[] output = new RecipeSymbol[slots.size()-1];
        for (int i = 0; i < output.length; i++) {
            output[i] = slots.get(i).symbol;
        }
        return output;
    }
}
