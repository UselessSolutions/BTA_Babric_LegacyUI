package useless.legacyui.sorting;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.item.ItemStack;
import useless.legacyui.LegacyUI;

public class UtilSorting {
    public static boolean stackInClassList(final Iterable<Class<?>> classList, final ItemStack itemStack){

        if (itemStack.itemID < Blocks.blocksList.length){
            Block<?> b = Blocks.getBlock(itemStack.itemID);
            for (final Class<?> clazz : classList){
                if (Block.hasLogicClass(b, clazz)) {
                    return true;
                }
            }
        } else {
            Class<?> itemClass = itemStack.getItem().getClass();
            for (final Class<?> clazz : classList){
                if (clazz.isAssignableFrom(itemClass)) {
                    return true;
                }
            }
        }
        return false;
    }
    public static boolean stackInItemList(final Iterable<ItemStack> itemStackList, final ItemStack itemStack){
        for (final ItemStack stack : itemStackList){
            if (itemStack.itemID == stack.itemID && itemStack.getMetadata() == stack.getMetadata()){
                return true;
            }
        }
        return false;
    }
    public static boolean stackInKeywordList(final Iterable<String> keywordList, final ItemStack itemStack){
        for (final String keyword : keywordList){
            if (itemStack.getItem().getKey().contains(keyword)){
                return true;
            }
        }
        return false;
    }
    public static boolean recipeInRecipeList(final Iterable<? extends RecipeEntryCrafting<?, ?>> recipeList, final RecipeEntryCrafting<?,?> recipe){
        for (final RecipeEntryCrafting<?,?> groupRecipe : recipeList){
            if (groupRecipe.equals(recipe)){
                return true;
            }
        }
        return false;
    }
    public static void printRecipeList(final Iterable<? extends RecipeEntryCrafting<?, ?>> recipes){
        for (final RecipeEntryCrafting<?,?> recipe : recipes){
            LegacyUI.LOGGER.info("Output:{}", recipe.getOutput());
        }
    }
}
