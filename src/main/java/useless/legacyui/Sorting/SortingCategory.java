package useless.legacyui.Sorting;


import java.util.ArrayList;
import java.util.List;

public class SortingCategory {
    private RecipeGroup[] recipeGroups;
    private RecipeGroup[] smallGroups;
    public SortingCategory(RecipeGroup[] recipeGroups){
        this.recipeGroups = recipeGroups;
        List<RecipeGroup> _groups = new ArrayList<RecipeGroup>();
        int numberSmallGroups = 0;
        for (RecipeGroup group : recipeGroups){
            if(group.getRecipes(false).length > 0){ // Discard empty groups
                _groups.add(group);
            }
            else {
                continue;
            }
            if (group.getSmallRecipesAmount() > 0){
                numberSmallGroups++;
            }
        }
        smallGroups = new RecipeGroup[numberSmallGroups];
        int i = 0;
        for (RecipeGroup group : recipeGroups){
            if (group.getSmallRecipesAmount() > 0){
                smallGroups[i++] = group;
            }
        }
        this.recipeGroups = _groups.toArray(new RecipeGroup[0]);
    }

    public RecipeGroup[] getRecipeGroups(boolean isSmall){
        if (!isSmall){
            return this.recipeGroups;
        }
        return this.smallGroups;

    }
}
