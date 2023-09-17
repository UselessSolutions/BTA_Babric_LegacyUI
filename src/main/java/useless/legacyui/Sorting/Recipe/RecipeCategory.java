package useless.legacyui.Sorting.Recipe;

import net.minecraft.core.lang.I18n;

import java.util.ArrayList;
import java.util.List;

public class RecipeCategory {
    private String key;
    public int[] iconCoordinate;

    private RecipeGroup[] recipeGroups;
    private RecipeGroup[] smallGroups;
    public RecipeCategory(String modid, String translationKey , int[] iconCoordinate, RecipeGroup[]recipes){
        assert recipes.length > 0;
        this.key = (modid + ".categories.recipe." + translationKey).replace("..", ".");
        this.iconCoordinate = iconCoordinate;
        this.recipeGroups = recipes;
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
    public String getKey(){
        return key;
    }
    public String getTranslatedKey(){
        return I18n.getInstance().translateKey(key);
    }
}
