package useless.legacyui.Sorting.Recipe;

import useless.legacyui.Helper.IconHelper;
import useless.legacyui.Sorting.Item.ItemCategoryBuilder;

import java.util.ArrayList;
import java.util.List;

public class RecipeCategoryBuilder {
    private List<RecipeGroupBuilder> recipeGroupBuilderList = new ArrayList<>();
    private String key = "default";
    private final String modid;
    public int[] iconCoordinate = new int[]{0,0};
    public RecipeCategoryBuilder(String modid){
        this.modid = modid;
    }
    public RecipeCategoryBuilder addRecipeGroupBuilder(RecipeGroupBuilder recipeGroupBuilder){
        recipeGroupBuilderList.add(recipeGroupBuilder);
        return this;
    }
    public RecipeCategoryBuilder addRecipeGroupBuilders(RecipeGroupBuilder[] recipeGroupBuilders){
        for (RecipeGroupBuilder builder: recipeGroupBuilders) {
            addRecipeGroupBuilder(builder);
        }
        return this;
    }
    public RecipeCategoryBuilder setTranslationKey(String key){
        this.key = key;
        return this;
    }
    public RecipeCategoryBuilder setIcon(String iconTexturePath){
        this.iconCoordinate = IconHelper.getOrCreateIconTexture(modid, iconTexturePath);
        return this;
    }
    public RecipeCategoryBuilder setIcon(int[] iconCoordinate){
        this.iconCoordinate = iconCoordinate;
        return this;
    }
    public RecipeCategory build(){
        RecipeGroup[] recipeGroups = new RecipeGroup[recipeGroupBuilderList.size()];
        for (int i = 0; i < recipeGroups.length; i++) {
            recipeGroups[i] = recipeGroupBuilderList.get(i).build();
        }
        return new RecipeCategory(modid, key, iconCoordinate, recipeGroups);
    }
}
