package useless.legacyui.Helper;

import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCraftingShaped;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCraftingShapeless;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.core.player.inventory.slot.SlotGuidebook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryHelper {
    public static Map<RecipeEntryCrafting<?, ?>, List<SlotGuidebook>> recipeMap = new HashMap<>();
    static {
        // Would've just used the crafting pages' info for this but because their creation requires the player to exist (just to give the guidebook achievement) I instead have to copy the code here
        ArrayList<RecipeEntryCrafting<?, ?>> allRecipes = new ArrayList<>(Registries.RECIPES.getAllCraftingRecipes());
        int recipeAmount = 0;
        int xOffset = 32;
        int yOffset = 12;
        Map<RecipeEntryCrafting<?, ?>, List<SlotGuidebook>> map = new HashMap<>();
        for (RecipeEntryCrafting<?, ?> recipe : allRecipes) {
            int centerY;
            int i;
            int slotsAmount;
            RecipeEntryCrafting<?, ?> r;
            ArrayList<SlotGuidebook> recipeSlots = new ArrayList<SlotGuidebook>();
            if (recipe instanceof RecipeEntryCraftingShaped) {
                RecipeSymbol stack;
                int slotY;
                r = recipe;
                if (((RecipeEntryCraftingShaped)r).recipeHeight > 2 || ((RecipeEntryCraftingShaped)r).recipeWidth > 2) {
                    slotsAmount = 10;
                    for (i = 0; i < slotsAmount - 1; ++i) {
                        int slotX = i % 3;
                        slotY = i / 3;
                        if (slotX >= ((RecipeEntryCraftingShaped)r).recipeWidth || slotY >= ((RecipeEntryCraftingShaped)r).recipeHeight) {
                            recipeSlots.add(new SlotGuidebook(i, 1 + 18 * (i % 3) + xOffset, 1 + 18 * (i / 3 + recipeAmount * 3) + yOffset, null, false, recipe));
                            continue;
                        }
                        stack = ((RecipeSymbol[])r.getInput())[slotX + slotY * ((RecipeEntryCraftingShaped)r).recipeWidth];
                        recipeSlots.add(new SlotGuidebook(i, 1 + 18 * (i % 3) + xOffset, 1 + 18 * (i / 3 + recipeAmount * 3) + yOffset, stack, false, recipe));
                    }
                    centerY = (recipeSlots.get(recipeSlots.size() - 1).yDisplayPosition + recipeSlots.get(recipeSlots.size() - 8).yDisplayPosition) / 2;
                    recipeSlots.add(new SlotGuidebook(slotsAmount - 1, 81 + xOffset, centerY, new RecipeSymbol(((ItemStack)r.getOutput()).copy()), false, recipe).setAsOutput());
                } else {
                    slotsAmount = 5;
                    for (i = 0; i < slotsAmount - 1; ++i) {
                        int slotX = i % 2;
                        slotY = i / 2;
                        if (slotX >= ((RecipeEntryCraftingShaped)r).recipeWidth || slotY >= ((RecipeEntryCraftingShaped)r).recipeHeight) {
                            recipeSlots.add(new SlotGuidebook(i, 13 + 18 * (i % 2) + xOffset, 10 + 18 * (i / 2 + recipeAmount * 3) + yOffset, null, false, recipe));
                            continue;
                        }
                        stack = ((RecipeSymbol[])r.getInput())[slotX + slotY * ((RecipeEntryCraftingShaped)r).recipeWidth];
                        recipeSlots.add(new SlotGuidebook(i, 13 + 18 * (i % 2) + xOffset, 10 + 18 * (i / 2 + recipeAmount * 3) + yOffset, stack, false, recipe));
                    }
                    centerY = (recipeSlots.get(recipeSlots.size() - 1).yDisplayPosition + recipeSlots.get(recipeSlots.size() - 3).yDisplayPosition) / 2;
                    recipeSlots.add(new SlotGuidebook(slotsAmount - 1, 81 + xOffset, centerY, new RecipeSymbol((ItemStack)r.getOutput()), false, recipe).setAsOutput());
                }
                map.put(recipe, recipeSlots);
                yOffset += 10;
                ++recipeAmount;
                continue;
            }
            if (!(recipe instanceof RecipeEntryCraftingShapeless)) continue;
            r = recipe;
            if (((List<?>)r.getInput()).size() > 4) {
                slotsAmount = 10;
                for (i = 0; i < Math.max(((List<?>)r.getInput()).size(), 9); ++i) {
                    if (i >= ((List<?>)r.getInput()).size()) {
                        recipeSlots.add(new SlotGuidebook(i, 1 + 18 * (i % 3) + xOffset, 1 + 18 * (i / 3 + recipeAmount * 3) + yOffset, null, false, recipe));
                        continue;
                    }
                    RecipeSymbol stack = (RecipeSymbol)((List<?>)r.getInput()).get(i);
                    recipeSlots.add(new SlotGuidebook(i, 1 + 18 * (i % 3) + xOffset, 1 + 18 * (i / 3 + recipeAmount * 3) + yOffset, stack, false, recipe));
                }
                centerY = (recipeSlots.get(recipeSlots.size() - 1).yDisplayPosition + recipeSlots.get(recipeSlots.size() - 8).yDisplayPosition) / 2;
            } else {
                slotsAmount = 5;
                for (i = 0; i < Math.max(((List<?>)r.getInput()).size(), 4); ++i) {
                    if (i >= ((List<?>)r.getInput()).size()) {
                        recipeSlots.add(new SlotGuidebook(i, 13 + 18 * (i % 2) + xOffset, 10 + 18 * (i / 2 + recipeAmount * 3) + yOffset, null, false, recipe));
                        continue;
                    }
                    RecipeSymbol stack = (RecipeSymbol)((List<?>)r.getInput()).get(i);
                    recipeSlots.add(new SlotGuidebook(i, 13 + 18 * (i % 2) + xOffset, 10 + 18 * (i / 2 + recipeAmount * 3) + yOffset, stack, false, recipe));
                }
                centerY = (recipeSlots.get(recipeSlots.size() - 1).yDisplayPosition + recipeSlots.get(recipeSlots.size() - 3).yDisplayPosition) / 2;
            }
            recipeSlots.add(new SlotGuidebook(slotsAmount - 1, 81 + xOffset, centerY, new RecipeSymbol((ItemStack)r.getOutput()), false, recipe).setAsOutput());
            map.put(recipe, recipeSlots);
            yOffset += 10;
            ++recipeAmount;
        }
        recipeMap.putAll(map);
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
