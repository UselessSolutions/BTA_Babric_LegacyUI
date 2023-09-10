package useless.legacyui.Sorting.Items;

import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.ContainerPlayerCreative;
import net.minecraft.core.player.inventory.CreativeInventoryCategories;
import useless.legacyui.LegacyUI;

import java.util.ArrayList;

public class ItemCategory {
    public int[] iconCoord;
    public String key;
    public static ArrayList<ItemStack> unsortedItemStacks = new ArrayList<ItemStack>();

    private ArrayList<ItemStack> itemStacks = new ArrayList<ItemStack>();

    public ItemCategory(String key, int[] icon){
        this.key = key;
        this.iconCoord = icon;
    }
    public ItemStack get(int index){
        if (index < size()){
            return itemStacks.get(index);
        }
        return null;
    }
    public int size(){
        return itemStacks.size();
    }

    public void addItem(Item item, int meta){
        for (ItemStack stack : this.itemStacks) {
            if (stack.getItem() != item || stack.getMetadata() != meta) continue;
            return;
        }
        ItemStack stack = new ItemStack(item, 1, meta);
        this.removeFromUnsorted(stack);
        this.itemStacks.add(stack);
    }
    public void addItem(Item item){
        addItem(item, 0);
    }

    public void addItem(ItemStack stack){
        addItem(stack.getItem(), stack.getMetadata());
    }
    private void removeFromUnsorted(ItemStack itemStack){
        for (int i = 0; i < unsortedItemStacks.size(); i++){
            if (unsortedItemStacks.get(i) != null && unsortedItemStacks.get(i).getItemName().equals(itemStack.getItemName())){
                unsortedItemStacks.remove(i);
            }
        }
    }
    public static void printUnsorted(){
        for (ItemStack item : unsortedItemStacks){
            if (item != null){
                LegacyUI.LOGGER.info(item.getItemName());
            }
        }
    }

    static {
        for (ItemStack item : ContainerPlayerCreative.creativeItems){
            if (item != null){
                unsortedItemStacks.add(item);
                //
            }
        }
    }
}
