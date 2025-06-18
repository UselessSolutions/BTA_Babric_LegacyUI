package useless.legacyui.sorting;

import net.minecraft.core.block.Blocks;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.item.ItemStack;
import useless.legacyui.LegacyUI;

public class UtilSorting {
    public static boolean stackInClassList(final Iterable<Class<?>> classList, final ItemStack itemStack){
        for (final Class<?> clazz : classList){
            try {
                if (itemStack.itemID < Blocks.blocksList.length){
                    clazz.cast(Blocks.getBlock(itemStack.itemID));
                } else {
                    clazz.cast(itemStack.getItem());
                }
                return true;
            } catch (final ClassCastException ignored){
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
