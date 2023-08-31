package useless.legacyui.Sorting;

import net.minecraft.core.crafting.recipe.IRecipe;
import net.minecraft.core.crafting.recipe.RecipeShaped;
import net.minecraft.core.crafting.recipe.RecipeShapeless;
import net.minecraft.core.player.inventory.ContainerGuidebookRecipeCrafting;
import useless.legacyui.LegacyUI;
import useless.legacyui.utils.ArrayUtil;


public class RecipeGroup {
    public Object[] recipes;

    private short fourSlotRecipes;
    private int[] indexMap;
    public RecipeGroup(Object[] recipes){
        fourSlotRecipes = 0;
        this.recipes = recipes;
        if (recipes.length < 1){
            throw new IllegalArgumentException("recipes.length should always be greater than 0!");
        }
        for (int i = 0; i < recipes.length; i++){
            ContainerGuidebookRecipeCrafting currentContainer = getContainer(i, false);
            if (currentContainer.inventorySlots.size() < 6){
                fourSlotRecipes++;
            }
        }
        indexMap = new int[fourSlotRecipes];
        int mapIndex = 0;
        for (int i = 0; i < recipes.length; i++){
            ContainerGuidebookRecipeCrafting currentContainer = getContainer(i, false);
            if (currentContainer.inventorySlots.size() < 6){
                indexMap[mapIndex++] = i;
            }
        }
    }

    public ContainerGuidebookRecipeCrafting getContainer(int index, boolean isSmall){
        if (!isSmall){
            index = ArrayUtil.wrapAroundIndex(index, recipes.length);
            return new ContainerGuidebookRecipeCrafting(((IRecipe)recipes[index]));
        }
        if (fourSlotRecipes <= 0){
            return null;
        }
        index = ArrayUtil.wrapAroundIndex(index, fourSlotRecipes);
        return new ContainerGuidebookRecipeCrafting(((IRecipe)recipes[indexMap[index]]));
    }

    public short getSmallRecipes(){
        return fourSlotRecipes;
    }
}
