package useless.legacyui.Sorting.Items;

import net.minecraft.core.item.ItemArmor;
import net.minecraft.core.item.ItemFood;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.tool.ItemTool;

import java.util.ArrayList;
import java.util.Arrays;

public class CategoryManager {
    private static ArrayList<ItemCategory> itemCategories = new ArrayList<ItemCategory>();

    public static ItemCategory get(int index){
        if (index < itemCategories.size()){
            return itemCategories.get(index);
        }
        return null;
    }

    public static int size(){
        return itemCategories.size();
    }

    static {
        int i = 0;
        itemCategories.add((new ItemCategory("Natural Blocks", new int[]{i++ * 16,16})));
        itemCategories.add((new ItemCategory("Other Blocks", new int[]{i++ * 16,0})));
        itemCategories.add((new ItemCategory("Tools and Equipment", new int[]{i++ * 16,0})));
        itemCategories.add((new ItemCategory("Food", new int[]{i++ * 16,0})));
        itemCategories.add((new ItemCategory("Redstone", new int[]{i++ * 16,0})));
        itemCategories.add((new ItemCategory("Travel", new int[]{i++ * 16,0})));
        itemCategories.add((new ItemCategory("Miscellaneous", new int[]{i++ * 16,0})));

        ItemCategory natBlockCategory = CategoryManager.get(0);
        ItemCategory otherBlockCategory = CategoryManager.get(1);
        ItemCategory toolCategory = CategoryManager.get(2);
        ItemCategory foodCategory = CategoryManager.get(3);
        ItemCategory redstoneCategory = CategoryManager.get(4);
        ItemCategory travelCategory = CategoryManager.get(5);
        ItemCategory miscCategory = CategoryManager.get(6);

        Object[] _stacks = ItemCategory.unsortedItemStacks.toArray();
        for (Object notstack : _stacks){
            ItemStack stack = (ItemStack)notstack;
            String name = stack.getItemName();
            if (name.contains("tile.")){
                if (Arrays.stream(new String[]{"rail"}).anyMatch(name.toLowerCase()::contains)){
                    travelCategory.addItem(stack);
                    continue;
                }
                if (Arrays.stream(new String[]{"ore"}).anyMatch(name.toLowerCase()::contains)){
                    natBlockCategory.addItem(stack);
                    continue;
                }
                if (Arrays.stream(new String[]{"button", "plate", "redstone", "dispenser", "tnt", "piston", "sensor", "noteblock"}).anyMatch(name.toLowerCase()::contains)){
                    redstoneCategory.addItem(stack);
                    continue;
                }
                if (Arrays.stream(new String[]{"brick", "furnace", "pillar", "polish"}).anyMatch(name.toLowerCase()::contains)){
                    otherBlockCategory.addItem(stack);
                    continue;
                }
                if (Arrays.stream(new String[]{
                        "dirt", "grass", "gravel", "sand", "log", "ice", "stone", "cobble", "basalt", "granite", "slate", "marble", "permafrost", "leave", "bedrock",
                        "flower", "shroom","sponge", "mud", "sapling", "clay", "snow", "cactus", "pumpkin", "web", "obsidian", "spinifex", "deadbush", "algae"}).anyMatch(name.toLowerCase()::contains)){
                    natBlockCategory.addItem(stack);
                    continue;
                }
                otherBlockCategory.addItem(stack);
                continue;
            }
            else {
                if (stack.getItem() instanceof ItemFood){
                    foodCategory.addItem(stack);
                    continue;
                }
                if (stack.getItem() instanceof ItemTool || stack.getItem() instanceof ItemArmor){
                    toolCategory.addItem(stack);
                    continue;
                }
                if (Arrays.stream(new String[]{"boat","minecart"}).anyMatch(name.toLowerCase()::contains)){
                    travelCategory.addItem(stack);
                    continue;
                }
                miscCategory.addItem(stack);
            }
        }
    }
}
