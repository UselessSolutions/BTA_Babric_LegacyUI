package useless.legacyui.Sorting.Items;

import net.minecraft.core.item.*;
import net.minecraft.core.item.tool.ItemTool;
import net.minecraft.core.item.tool.ItemToolSword;

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
        i++;
        itemCategories.add((new ItemCategory("Miscellaneous", new int[]{i++ * 16,0})));
        itemCategories.add((new ItemCategory("Modded", new int[]{i++ * 16,0})));

        ItemCategory natBlockCategory = CategoryManager.get(0);
        ItemCategory otherBlockCategory = CategoryManager.get(1);
        ItemCategory toolCategory = CategoryManager.get(2);
        ItemCategory foodCategory = CategoryManager.get(3);
        ItemCategory redstoneCategory = CategoryManager.get(4);
        ItemCategory miscCategory = CategoryManager.get(5);
        ItemCategory moddedCategory = CategoryManager.get(6);

        Object[] _stacks = ItemCategory.unsortedItemStacks.toArray();
        for (Object notstack : _stacks){
            ItemStack stack = (ItemStack)notstack;
            String name = stack.getItemName();
            if (stack.getItem().getClass().getPackage().toString().contains("net.minecraft.")){
                if (name.contains("tile.")){
                    if (Arrays.stream(new String[]{"rail"}).anyMatch(name.toLowerCase()::contains)){
                        redstoneCategory.addItem(stack);
                        continue;
                    }
                    if (Arrays.stream(new String[]{"ore"}).anyMatch(name.toLowerCase()::contains)){
                        natBlockCategory.addItem(stack);
                        continue;
                    }
                    if (Arrays.stream(new String[]{"button", "plate", "redstone", "dispenser", "tnt", "piston", "sensor", "noteblock", "lever", "trap"}).anyMatch(name.toLowerCase()::contains)){
                        redstoneCategory.addItem(stack);
                        continue;
                    }
                    if (Arrays.stream(new String[]{"brick", "furnace", "pillar", "polish", "carved", "capstone"}).anyMatch(name.toLowerCase()::contains)){
                        otherBlockCategory.addItem(stack);
                        continue;
                    }
                    if (Arrays.stream(new String[]{
                            "dirt", "grass", "gravel", "sand", "log", "ice", "stone", "cobble", "basalt", "granite", "slate", "marble", "permafrost", "leave", "bedrock",
                            "flower", "shroom","sponge", "mud", "sapling", "clay", "snow", "cactus", "pumpkin", "web", "obsidian", "spinifex", "deadbush", "algae", "nether"}).anyMatch(name.toLowerCase()::contains)){
                        natBlockCategory.addItem(stack);
                        continue;
                    }
                    otherBlockCategory.addItem(stack);
                    continue;
                }
                else {
                    if (Arrays.stream(new String[]{"sugarcane"}).anyMatch(name.toLowerCase()::contains)){
                        natBlockCategory.addItem(stack);
                        continue;
                    }
                    if (stack.getItem() instanceof ItemFood || stack.getItem() instanceof ItemBucketIceCream ||
                            Arrays.stream(new String[]{"cake"}).anyMatch(name.toLowerCase()::contains)){
                        foodCategory.addItem(stack);
                        continue;
                    }
                    if (
                            stack.getItem() instanceof ItemTool || stack.getItem() instanceof ItemArmor || stack.getItem() instanceof ItemToolSword ||
                                    stack.getItem() instanceof ItemFishingRod || stack.getItem() instanceof ItemBow || stack.getItem() instanceof ItemQuiver ||
                                    stack.getItem() instanceof ItemQuiverEndless || stack.getItem() instanceof ItemHandCannonLoaded || stack.getItem() instanceof ItemHandCannonUnloaded ||
                                    stack.getItem() instanceof ItemBucket || stack.getItem() instanceof ItemBucketEmpty || stack.getItem() instanceof ItemLabel ||
                                    Arrays.stream(new String[]{"tool", "map", "arrow", "explosive", "saddle"}).anyMatch(name.toLowerCase()::contains)){
                        toolCategory.addItem(stack);
                        continue;
                    }
                    if (Arrays.stream(new String[]{"boat","minecart"}).anyMatch(name.toLowerCase()::contains)){
                        redstoneCategory.addItem(stack);
                        continue;
                    }
                    if (Arrays.stream(new String[]{"redstone", "repeater"}).anyMatch(name.toLowerCase()::contains)){
                        redstoneCategory.addItem(stack);
                        continue;
                    }
                    if (stack.getItem() instanceof ItemPlaceable || stack.getItem() instanceof ItemDoor|| stack.getItem() instanceof ItemFlag
                            || stack.getItem() instanceof ItemBed || stack.getItem() instanceof ItemSign || stack.getItem() instanceof ItemPainting){
                        otherBlockCategory.addItem(stack);
                        continue;
                    }
                    miscCategory.addItem(stack);
                }
            }
            else { // modded items
                moddedCategory.addItem(stack);
            }

        }
    }
}
