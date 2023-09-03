package useless.legacyui.sorting;

import net.minecraft.core.crafting.recipe.IRecipe;
import net.minecraft.core.player.inventory.ContainerGuidebookRecipeCrafting;
import useless.legacyui.utils.ArrayUtil;


public class RecipeGroup {
    private Object[] recipes;
    private Object[] smallRecipes;

    private short fourSlotRecipes; // Number of 2x2 recipes in the group

    public RecipeGroup(Object[] recipes){
        fourSlotRecipes = 0;
        this.recipes = recipes;
        for (int i = 0; i < recipes.length; i++){
            ContainerGuidebookRecipeCrafting currentContainer = getContainer(i, false);
            if (currentContainer.inventorySlots.size() < 6){
                fourSlotRecipes++;
            }
        }
        smallRecipes = new Object[fourSlotRecipes];
        int mapIndex = 0;
        for (int i = 0; i < recipes.length; i++){
            ContainerGuidebookRecipeCrafting currentContainer = getContainer(i, false);
            if (currentContainer.inventorySlots.size() < 6){
                smallRecipes[mapIndex++] = recipes[i];
            }
        }

    }

    public ContainerGuidebookRecipeCrafting getContainer(int index, boolean isSmall){
        if (!isSmall){
            index = ArrayUtil.wrapAroundIndex(index, recipes.length);
            return new ContainerGuidebookRecipeCrafting(((IRecipe)recipes[index]));
        }
        else {
            index = ArrayUtil.wrapAroundIndex(index, smallRecipes.length);
            return new ContainerGuidebookRecipeCrafting(((IRecipe)smallRecipes[index]));
        }

    }

    public int getSmallRecipesAmount(){
        return fourSlotRecipes;
    }

    public Object[] getRecipes(boolean isSmall) {
        if (!isSmall){
            return recipes;
        }
        return smallRecipes;
    }
}