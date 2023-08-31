package useless.legacyui.Sorting;



public class SortingCategory {
    private RecipeGroup[] recipeGroups;
    private RecipeGroup[] smallGroups;
    public SortingCategory(RecipeGroup[] recipeGroups){
        this.recipeGroups = recipeGroups;
        int numberSmallGroups = 0;
        for (RecipeGroup group : recipeGroups){
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
    }

    public RecipeGroup[] getRecipeGroups(boolean isSmall){
        if (!isSmall){
            return this.recipeGroups;
        }
        return this.smallGroups;

    }
}
