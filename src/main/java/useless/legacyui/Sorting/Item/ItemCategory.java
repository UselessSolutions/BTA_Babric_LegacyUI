package useless.legacyui.Sorting.Item;

import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;

public class ItemCategory {
    private String key;
    public int[] iconCoordinate;
    public ItemStack[] itemStacks;
    public ItemCategory(String modid, String translationKey , int[] iconCoordinate, ItemStack[]itemStacks){
        assert itemStacks.length > 0;
        this.key = (modid + ".categories.creative." + translationKey).replace("..", ".");
        this.iconCoordinate = iconCoordinate;
        this.itemStacks = itemStacks;
    }
    public String getKey(){
        return key;
    }
    public String getTranslatedKey(){
        return I18n.getInstance().translateKey(key);
    }

}
