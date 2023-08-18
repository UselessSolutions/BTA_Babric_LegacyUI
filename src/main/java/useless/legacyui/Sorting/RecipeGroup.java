package useless.legacyui.Sorting;

import net.minecraft.core.crafting.recipe.IRecipe;
import net.minecraft.core.player.inventory.ContainerGuidebookRecipeCrafting;
import useless.legacyui.LegacyUI;


public class RecipeGroup {
    public Object[] recipes;
    public RecipeGroup(Object[] recipes){
        this.recipes = recipes;
        if (recipes.length < 1){
            throw new IllegalArgumentException("recipes.length should always be greater than 0!");
        }
    }

    public ContainerGuidebookRecipeCrafting getContainer(int index){
        return new ContainerGuidebookRecipeCrafting(((IRecipe)recipes[index]));
    }
}
