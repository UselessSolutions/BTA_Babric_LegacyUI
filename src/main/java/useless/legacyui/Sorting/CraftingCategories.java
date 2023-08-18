package useless.legacyui.Sorting;

import net.minecraft.core.crafting.CraftingManager;
import net.minecraft.core.crafting.recipe.IRecipe;
import net.minecraft.core.crafting.recipe.RecipeShaped;
import net.minecraft.core.crafting.recipe.RecipeShapeless;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemDye;
import net.minecraft.core.item.tool.ItemTool;
import useless.legacyui.LegacyUI;

import java.util.ArrayList;
import java.util.List;

public class CraftingCategories {
    private static final CraftingCategories instance = new CraftingCategories();
    public static CraftingCategories getInstance() {
        return instance;
    }

    private List<SortingCategory> categories = new ArrayList<SortingCategory>();
    private CraftingCategories(){
        CraftingManager manager = CraftingManager.getInstance();

        int j = 0;
        List<Object> _dyes = new ArrayList<Object>();
        List<Object> _tools = new ArrayList<Object>();
        List<Object> _others = new ArrayList<Object>();
        for (IRecipe recipe: manager.getRecipeList()){
            if (recipe instanceof RecipeShaped || recipe instanceof RecipeShapeless){
                LegacyUI.LOGGER.debug("" + j + " | " + recipe.getRecipeOutput().getItemName());
                Item recipeItem = recipe.getRecipeOutput().getItem();
                if (recipeItem instanceof ItemDye){
                    _dyes.add(recipe);
                } else if (recipeItem instanceof ItemTool) {
                    _tools.add(recipe);
                } else{
                    _others.add(recipe);
                }
                j++;
            }
        }

        // Misc
        RecipeGroup dyesGroup = new RecipeGroup(_dyes.toArray());
        RecipeGroup othersGroup = new RecipeGroup(_others.toArray());
        SortingCategory misc = new SortingCategory(new RecipeGroup[]{dyesGroup, othersGroup});
        addCategory(misc);
        // Tools
        RecipeGroup toolsGroup = new RecipeGroup(_tools.toArray());
        SortingCategory tools = new SortingCategory(new RecipeGroup[]{toolsGroup});
        addCategory(tools);

    }

    public int addCategory(SortingCategory category){
        int index = categories.size();
        categories.add(category);
        return index;
    }


    public List<SortingCategory> getCategories(){
        return categories;
    }
}
