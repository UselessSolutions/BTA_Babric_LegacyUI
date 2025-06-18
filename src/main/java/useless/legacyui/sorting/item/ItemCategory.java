package useless.legacyui.sorting.item;

import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;

public class ItemCategory {
    private final String key;
    public IconCoordinate iconCoordinate;
    public ItemStack[] itemStacks;
    public ItemCategory(final String modid, final String translationKey , final IconCoordinate iconCoordinate, final ItemStack[] itemStacks){
        assert itemStacks.length > 0;
        this.key = (modid + ".categories.creative." + translationKey).replace("..", ".");
        this.iconCoordinate = iconCoordinate;
        this.itemStacks = itemStacks;
    }
    public String getKey(){
        return this.key;
    }
    public String getTranslatedKey(){
        return I18n.getInstance().translateKey(this.key);
    }

}
