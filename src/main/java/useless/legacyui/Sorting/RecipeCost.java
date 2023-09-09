package useless.legacyui.Sorting;

import net.minecraft.core.crafting.CraftingManager;
import net.minecraft.core.crafting.recipe.RecipeShaped;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.ContainerGuidebookRecipeCrafting;
import useless.legacyui.Utils.InventoryUtil;

public class RecipeCost {
    private static CraftingManager craftingManager = CraftingManager.getInstance();
    public ItemStack[] itemStacks;
    public int[] quantity;

    public boolean useAlts;

    public RecipeCost(ItemStack[] itemStacks, int[] quantity){
        this.itemStacks = itemStacks;
        this.quantity = quantity;
    }
    public RecipeCost(ContainerGuidebookRecipeCrafting recipe){
        ItemStack[] items = new ItemStack[9];

        useAlts = false;
        if (recipe.recipe instanceof RecipeShaped){
            useAlts = ((RecipeShaped)(recipe.recipe)).useAlternatives;
        }


        for (int i = 1; i < items.length; i++){
            if (recipe.inventorySlots.size() > i){
                boolean itemInList = false;
                for (int j = 0; j < items.length; j++){
                    if (items[j] == null || recipe.inventorySlots.get(i).getStack() == null) {continue;}
                    if (recipe.inventorySlots.get(i).getStack().getItem() == items[j].getItem()){
                        itemInList = true;
                    }
                }
                if (!itemInList){
                    items[i] = recipe.inventorySlots.get(i).getStack();
                }
            }
        }

        int[] amounts = new int[9];
        for (int i = 0; i < amounts.length; i++){
            amounts[i] = InventoryUtil.itemsInInventory(recipe.inventorySlots, items[i]);
        }

        int realItemCount = 0;
        for (int i = 0; i < amounts.length; i++){
            if (items[i] != null){
                realItemCount++;
            }
        }

        this.itemStacks = new ItemStack[realItemCount];
        this.quantity = new int[realItemCount];
        int recipeIndex = 0;
        for (int i = 0; i < items.length; i++){
            if (items[i] != null){
                itemStacks[recipeIndex] = items[i];
                quantity[recipeIndex] = amounts[i];
                recipeIndex++;
            }
        }
    }

    @Override
    public String toString() {
        String returnString = "";
        int i = 0;
        for (ItemStack item: itemStacks) {
            returnString += item.getItem().getKey() + " | " + quantity[i++] + "\n";
        }
        return returnString;
    }
}
