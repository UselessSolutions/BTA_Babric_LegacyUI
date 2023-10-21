package useless.legacyui.Api;

import useless.legacyui.LegacyUI;
import useless.legacyui.Sorting.LegacyCategoryManager;

public class LegacyUIPlugin implements LegacyUIApi{
    private static boolean hasRegistered = false;
    @Override
    public String getModId() {
        return LegacyUI.MOD_ID;
    }

    @Override
    public void register() {
        if (hasRegistered){return;}
        hasRegistered = true;
        LegacyCategoryManager.recipeCategoryBuilders.add(LegacyCategoryManager.recipeBasics.category);
        LegacyCategoryManager.recipeCategoryBuilders.add(LegacyCategoryManager.recipeBricks.category);
        LegacyCategoryManager.recipeCategoryBuilders.add(LegacyCategoryManager.recipeTools.category);
        LegacyCategoryManager.recipeCategoryBuilders.add(LegacyCategoryManager.recipeFood.category);
        LegacyCategoryManager.recipeCategoryBuilders.add(LegacyCategoryManager.recipeRedstone.category);
        LegacyCategoryManager.recipeCategoryBuilders.add(LegacyCategoryManager.recipeTransit.category);
        LegacyCategoryManager.recipeCategoryBuilders.add(LegacyCategoryManager.recipeMisc.category);
        //recipeCategoryBuilders.add(new RecipeCategory(MOD_ID, "modded", IconHelper.getOrCreateIconTexture(MOD_ID, "modded.png"), recipeMisc.category.getRecipeGroups(false)));

        LegacyCategoryManager.creativeCategoriesBuilders.add(LegacyCategoryManager.creative.natural);
        LegacyCategoryManager.creativeCategoriesBuilders.add(LegacyCategoryManager.creative.otherBlocks);
        LegacyCategoryManager.creativeCategoriesBuilders.add(LegacyCategoryManager.creative.equipment);
        LegacyCategoryManager.creativeCategoriesBuilders.add(LegacyCategoryManager.creative.food);
        LegacyCategoryManager.creativeCategoriesBuilders.add(LegacyCategoryManager.creative.redstoneTransit);
        LegacyCategoryManager.creativeCategoriesBuilders.add(LegacyCategoryManager.creative.misc);
        LegacyCategoryManager.creativeCategoriesBuilders.add(LegacyCategoryManager.creative.modded);
        new TestPlugin().register();
    }
}
