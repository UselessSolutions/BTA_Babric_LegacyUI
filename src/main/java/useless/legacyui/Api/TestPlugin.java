package useless.legacyui.Api;

import net.minecraft.core.block.Block;
import net.minecraft.core.item.Item;
import useless.legacyui.Sorting.Item.ItemCategoryBuilder;
import useless.legacyui.Sorting.LegacyCategoryManager;

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
    }
    public static ItemCategoryBuilder testCategory1 = new ItemCategoryBuilder(modId)
            .addItem(Block.brickBasalt, true);
    public static ItemCategoryBuilder testCategory2 = new ItemCategoryBuilder(modId)
            .addItem(Item.ammoArrowGold, true);
}
