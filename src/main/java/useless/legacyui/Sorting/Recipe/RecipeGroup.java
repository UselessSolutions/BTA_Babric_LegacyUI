package useless.legacyui.Sorting.Recipe;

import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.item.ItemStack;
import useless.legacyui.Helper.ArrayHelper;

import java.util.ArrayList;
import java.util.List;

public class RecipeGroup {
    private final List<RecipeEntryCrafting<?, ?>> recipes = new ArrayList<>();
    private final List<RecipeEntryCrafting<?, ?>> smallRecipes = new ArrayList<>();

    public RecipeGroup(List<RecipeEntryCrafting<?,?>> recipes){
        this.recipes.addAll(recipes);
        for (RecipeEntryCrafting<?, ?> recipe : recipes){
            if (recipe.getRecipeSize() <= 4){
                smallRecipes.add(recipe);
            }
        }

    }

    public RecipeEntryCrafting<?, ?> getRecipe(int index, boolean isSmall){
        if (isSmall){
            return smallRecipes.get(ArrayHelper.wrapAroundIndex(index, smallRecipes.size()));
        }
        else {
           return recipes.get(ArrayHelper.wrapAroundIndex(index, recipes.size()));
        }
    }
    public ItemStack getOutputStack(int index, boolean isSmall){
        return (ItemStack) getRecipe(index, isSmall).getOutput();
    }

    public int getSmallRecipesAmount(){
        return smallRecipes.size();
    }

    public List<RecipeEntryCrafting<?, ?>> getRecipes(boolean isSmall) {
        if (isSmall){
            return smallRecipes;
        }
        return recipes;
    }
}

