package useless.legacyui.Sorting.Recipe;

import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCraftingShaped;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCraftingShapeless;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeCost {
    public HashMap<RecipeSymbol, Integer> costMap = new HashMap<>();

    public RecipeCost(RecipeEntryCrafting<?, ?> recipe){
        if (recipe instanceof RecipeEntryCraftingShaped){
            RecipeEntryCraftingShaped shaped = (RecipeEntryCraftingShaped) recipe;
            RecipeSymbol[] input = shaped.getInput();
            for (RecipeSymbol symbol : input){
                if (symbol == null || costMap.containsKey(symbol)){
                    continue;
                }
                int amount = 0;
                for (RecipeSymbol symbol2 : input){
                    if (symbol.equals(symbol2)) {
                        amount++;
                    }
                }
                costMap.put(symbol, amount);
            }
            return;
        }
        if (recipe instanceof RecipeEntryCraftingShapeless){
            RecipeEntryCraftingShapeless shapeless = (RecipeEntryCraftingShapeless) recipe;
            List<RecipeSymbol> input = shapeless.getInput();
            for (RecipeSymbol symbol : input){
                if (costMap.containsKey(symbol)){
                    continue;
                }
                int amount = 0;
                for (RecipeSymbol symbol2 : input){
                    if (symbol.equals(symbol2)) {
                        amount++;
                    }
                }
                costMap.put(symbol, amount);
            }
            return;
        }
        throw new IllegalArgumentException("Invalid recipe: " + recipe.getClass().getSimpleName());
    }

    @Override
    public String toString() {
        StringBuilder returnString = new StringBuilder();
        for (Map.Entry<RecipeSymbol, Integer> entry : costMap.entrySet()) {
            returnString.append(entry.getKey()).append(" | ").append(entry.getValue()).append("\n");
        }
        return returnString.toString();
    }
}
