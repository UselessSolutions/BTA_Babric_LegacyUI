package useless.legacyui.Sorting.Recipe;

import net.minecraft.core.crafting.recipe.IRecipe;
import net.minecraft.core.player.inventory.ContainerGuidebookRecipeCrafting;
import useless.legacyui.Helper.ArrayHelper;

public class RecipeGroup {
    private final Object[] recipes;
    private final Object[] smallRecipes;

    public RecipeGroup(Object[] recipes){
        // Number of 2x2 recipes in the group
        short fourSlotRecipes = 0;
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
            index = ArrayHelper.wrapAroundIndex(index, recipes.length);
            return new ContainerGuidebookRecipeCrafting(((IRecipe)recipes[index]));
        }
        else {
            index = ArrayHelper.wrapAroundIndex(index, smallRecipes.length);
            return new ContainerGuidebookRecipeCrafting(((IRecipe)smallRecipes[index]));
        }

    }

    public int getSmallRecipesAmount(){
        return smallRecipes.length;
    }

    public Object[] getRecipes(boolean isSmall) {
        if (!isSmall){
            return recipes;
        }
        return smallRecipes;
    }
}

