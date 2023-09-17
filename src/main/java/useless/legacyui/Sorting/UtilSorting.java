package useless.legacyui.Sorting;

import net.minecraft.core.block.Block;
import net.minecraft.core.crafting.recipe.IRecipe;
import net.minecraft.core.item.ItemStack;
import useless.legacyui.LegacyUI;

import java.util.List;

public class UtilSorting {
    public static boolean stackInClassList(List<Class> classList, ItemStack itemStack){
        for (Class clazz : classList){
            try {
                if (itemStack.itemID < Block.blocksList.length){
                    clazz.cast(Block.getBlock(itemStack.itemID));
                } else {
                    clazz.cast(itemStack.getItem());
                }
                return true;
            } catch (ClassCastException e){
            }
        }
        return false;
    }
    public static boolean stackInItemList(List<ItemStack> itemStackList, ItemStack itemStack){
        for (ItemStack stack : itemStackList){
            if (itemStack.itemID == stack.itemID && itemStack.getMetadata() == stack.getMetadata()){
                return true;
            }
        }
        return false;
    }
    public static boolean stackInKeywordList(List<String> keywordList, ItemStack itemStack){
        for (String keyword : keywordList){
            if (itemStack.getItem().getKey().contains(keyword)){
                return true;
            }
        }
        return false;
    }
    public static boolean recipeInRecipeList(List<IRecipe> recipeList, IRecipe recipe){
        for (IRecipe groupRecipe : recipeList){
            if (groupRecipe.equals(recipe)){
                return true;
            }
        }
        return false;
    }
    public static void printRecipeList(List<IRecipe> recipes){
        for (IRecipe recipe : recipes){
            LegacyUI.LOGGER.info("Output:" + recipe.getRecipeOutput());
        }
    }
}
