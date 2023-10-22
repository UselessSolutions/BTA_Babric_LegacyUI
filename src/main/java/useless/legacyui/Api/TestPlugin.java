package useless.legacyui.Api;

import net.minecraft.core.block.Block;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemDye;
import useless.legacyui.Sorting.Item.ItemCategoryBuilder;
import useless.legacyui.Sorting.LegacyCategoryManager;
import useless.legacyui.Sorting.Recipe.RecipeCategoryBuilder;
import useless.legacyui.Sorting.Recipe.RecipeGroupBuilder;

public class TestPlugin implements LegacyUIApi{
    public static String modId = "test";
    @Override
    public String getModId() {
        return modId;
    }

    @Override
    public void register() {
        LegacyCategoryManager.creativeCategoriesBuilders.add(testCategory1);
        LegacyCategoryManager.creativeCategoriesBuilders.add(testCategory2);
        LegacyCategoryManager.creativeCategoriesBuilders.add(testCategory3);
        LegacyCategoryManager.creativeCategoriesBuilders.add(testCategory1);
        LegacyCategoryManager.creativeCategoriesBuilders.add(testCategory2);
        LegacyCategoryManager.creativeCategoriesBuilders.add(testCategory3);
        LegacyCategoryManager.creativeCategoriesBuilders.add(testCategory1);
        LegacyCategoryManager.creativeCategoriesBuilders.add(testCategory2);
        LegacyCategoryManager.creativeCategoriesBuilders.add(testCategory3);
        LegacyCategoryManager.creativeCategoriesBuilders.add(testCategory1);
        LegacyCategoryManager.creativeCategoriesBuilders.add(testCategory2);
        LegacyCategoryManager.creativeCategoriesBuilders.add(testCategory3);
        LegacyCategoryManager.creativeCategoriesBuilders.add(testCategory1);
        LegacyCategoryManager.creativeCategoriesBuilders.add(testCategory2);
        LegacyCategoryManager.creativeCategoriesBuilders.add(testCategory3);

        LegacyCategoryManager.recipeCategoryBuilders.add(category);
        LegacyCategoryManager.recipeCategoryBuilders.add(category);
        LegacyCategoryManager.recipeCategoryBuilders.add(category);
        LegacyCategoryManager.recipeCategoryBuilders.add(category);
        LegacyCategoryManager.recipeCategoryBuilders.add(category);
        LegacyCategoryManager.recipeCategoryBuilders.add(category);
        LegacyCategoryManager.recipeCategoryBuilders.add(category);
        LegacyCategoryManager.recipeCategoryBuilders.add(category);
        LegacyCategoryManager.recipeCategoryBuilders.add(category);
        LegacyCategoryManager.recipeCategoryBuilders.add(category);
        LegacyCategoryManager.recipeCategoryBuilders.add(category);
    }
    public static ItemCategoryBuilder testCategory1 = new ItemCategoryBuilder(modId)
            .addItem(Block.brickBasalt, true);
    public static ItemCategoryBuilder testCategory2 = new ItemCategoryBuilder(modId)
            .addItem(Item.ammoArrowGold, true);
    public static ItemCategoryBuilder testCategory3 = new ItemCategoryBuilder(modId)
            .addItem(Item.toolSwordDiamond, true);
    public static RecipeGroupBuilder dyes = new RecipeGroupBuilder()
            .addClass(ItemDye.class, true);
    public static RecipeGroupBuilder allBlocks = new RecipeGroupBuilder()
            .addClass(Block.class, true);
    public static RecipeGroupBuilder allItems = new RecipeGroupBuilder()
            .addClass(Item.class, true);
    public static RecipeGroupBuilder all = new RecipeGroupBuilder()
            .addKeyword(".", true);
    public static RecipeCategoryBuilder category = new RecipeCategoryBuilder(modId)
            .addRecipeGroupBuilders(new RecipeGroupBuilder[]{dyes,allBlocks,allItems,all});
}
