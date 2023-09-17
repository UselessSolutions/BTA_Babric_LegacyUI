package useless.legacyui.Sorting.Item;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.core.block.Block;
import net.minecraft.core.crafting.CraftingManager;
import net.minecraft.core.crafting.recipe.IRecipe;
import net.minecraft.core.crafting.recipe.RecipeShaped;
import net.minecraft.core.crafting.recipe.RecipeShapeless;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.ContainerPlayerCreative;
import useless.legacyui.Helper.IconHelper;
import useless.legacyui.LegacyUI;
import useless.legacyui.Sorting.UtilSorting;
import useless.prismaticlibe.PrismaticLibe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ItemCategoryBuilder {
    private static final List<String> modList = new ArrayList<>();
    static {
        Iterator modITerator = FabricLoader.getInstance().getAllMods().iterator();
        while(modITerator.hasNext()) {
            ModContainer mod = (ModContainer)modITerator.next();
            modList.add(mod.getMetadata().getId());
        }
    }
    private static final List<ItemStack> allCreativeItems = ContainerPlayerCreative.creativeItems;
    private static final List<ItemStack> unusedCreativeItems = new ArrayList<>(allCreativeItems);
    private String key = "default";
    private final String modid;
    public int[] iconCoordinate = new int[]{0,0};
    private Boolean isDebug = false;
    private Boolean excludeModdedItems = false;
    private final List<Class> inclusiveClassList = new ArrayList<>();
    private final List<ItemStack> inclusiveItemList = new ArrayList<>();
    private final List<String> inclusiveKeywordList = new ArrayList<>();
    private final List<Class> exclusiveClassList = new ArrayList<>();
    private final List<ItemStack> exclusiveItemList = new ArrayList<>();
    private final List<String> exclusiveKeywordList = new ArrayList<>();
    private final List<ItemStack> excludeItemList = new ArrayList<>();
    public ItemCategoryBuilder(String modid){
        this.modid = modid;
    }
    public ItemCategoryBuilder excludeItem(Item item){
        return excludeItem(new ItemStack(item));
    }
    public ItemCategoryBuilder excludeItem(ItemStack stack){
        if (isDebug){
            LegacyUI.LOGGER.info(stack.toString());
        }
        excludeItemList.add(stack);
        return this;
    }
    public ItemCategoryBuilder addKeyword(String keyword){
        return addKeyword(keyword, false);
    }
    public ItemCategoryBuilder addKeyword(String keyword, boolean isInclusive){
        if (isDebug){
            LegacyUI.LOGGER.info(keyword);
        }

        if (isInclusive){
            inclusiveKeywordList.add(keyword);
        } else {
            exclusiveKeywordList.add(keyword);
        }

        return this;
    }
    public ItemCategoryBuilder addItemsWithMetaRange(Item item, int metaStart, int metaRange, boolean isInclusive){
        for (int i = 0; i < metaRange; i++) {
            this.addItem(item, metaStart+i, isInclusive);
        }
        return this;
    }
    public ItemCategoryBuilder addItem(Block block){
        return addItem(block.asItem(), false);
    }
    public ItemCategoryBuilder addItem(Block block, boolean isInclusive){
        return addItem(block.asItem(), isInclusive);
    }
    public ItemCategoryBuilder addItem(int id, int meta){
        return addItem(new ItemStack(id, 1, meta), false);
    }
    public ItemCategoryBuilder addItem(int id, int meta, boolean isInclusive){
        return addItem(new ItemStack(id, 1, meta), isInclusive);
    }
    public ItemCategoryBuilder addItem(Item item, int meta){
        return addItem(new ItemStack(item, 1, meta), false);
    }
    public ItemCategoryBuilder addItem(Item item, int meta, boolean isInclusive){
        return addItem(new ItemStack(item, 1, meta), isInclusive);
    }
    public ItemCategoryBuilder addItem(Item item, boolean isInclusive){
        return addItem(new ItemStack(item), isInclusive);
    }
    public ItemCategoryBuilder addItem(Item item){
        return addItem(new ItemStack(item));
    }
    public ItemCategoryBuilder addItem(ItemStack stack){
        return addItem(stack, false);
    }
    public ItemCategoryBuilder addItem(ItemStack stack, boolean isInclusive){
        if (isDebug){
            LegacyUI.LOGGER.info(stack.toString());
        }

        if (isInclusive){
            inclusiveItemList.add(stack);
        } else {
            exclusiveItemList.add(stack);
        }

        return this;
    }
    public ItemCategoryBuilder addClass(Class clazz){
        return addClass(clazz, false);
    }
    public ItemCategoryBuilder addClass(Class clazz, boolean isInclusive){
        if (isDebug){
            LegacyUI.LOGGER.info(clazz.getName());
        }

        if (isInclusive){
            inclusiveClassList.add(clazz);
        } else {
            exclusiveClassList.add(clazz);
        }

        return this;
    }
    public ItemCategoryBuilder isDebug(){
        isDebug = true;
        return this;
    }
    public ItemCategoryBuilder printCurrentConfig(){
        LegacyUI.LOGGER.info("isDebug:" + isDebug);
        for (Class clazz : inclusiveClassList){
            LegacyUI.LOGGER.info("inclusiveClass:"+clazz.getName());
        }
        for (ItemStack stack : inclusiveItemList){
            LegacyUI.LOGGER.info("inclusiveItemStack:"+stack);
        }
        for (String keyword : inclusiveKeywordList){
            LegacyUI.LOGGER.info("inclusiveKeyword:"+keyword);
        }
        for (Class clazz : exclusiveClassList){
            LegacyUI.LOGGER.info("exclusiveClass:"+clazz.getName());
        }
        for (ItemStack stack : exclusiveItemList){
            LegacyUI.LOGGER.info("exclusiveItemStack:"+stack);
        }
        for (String keyword : exclusiveKeywordList){
            LegacyUI.LOGGER.info("exclusiveKeyword:"+keyword);
        }
        return this;
    }
    public ItemCategoryBuilder setTranslationKey(String key){
        this.key = key;
        return this;
    }
    public ItemCategoryBuilder setIcon(String iconTexturePath){
        this.iconCoordinate = IconHelper.getOrCreateIconTexture(modid, iconTexturePath);
        return this;
    }
    public ItemCategoryBuilder setIcon(int[] iconCoordinate){
        this.iconCoordinate = iconCoordinate;
        return this;
    }
    public ItemCategoryBuilder excludeModdedItems(){
        this.excludeModdedItems = true;
        return this;
    }
    public boolean stackIsModded(ItemStack stack){
        String itemModID = stack.getItemName().split("[.]")[1];
        for (String modId: modList) {
            if (itemModID.equals(modId)){
                return true;
            }
        }
        return false;
    }
    public ItemCategory build(){
        List<ItemStack> unused_copy = new ArrayList<>(unusedCreativeItems);
        List<ItemStack> categoryItems = new ArrayList<>();
        int removeOffset = 0;
        for (int i = 0; i < unused_copy.size(); i++) { // Add exclusive Recipes
            ItemStack currentItem = unused_copy.get(i);
            if (UtilSorting.stackInItemList(excludeItemList,currentItem) || (excludeModdedItems && stackIsModded(currentItem))){
                continue;
            }
            if (UtilSorting.stackInClassList(exclusiveClassList,currentItem) || UtilSorting.stackInItemList(exclusiveItemList, currentItem) || UtilSorting.stackInKeywordList(exclusiveKeywordList, currentItem)) {
                categoryItems.add(currentItem);
                unusedCreativeItems.remove(i - removeOffset);
                removeOffset++;
                continue;

            }
        }
        for (int i = 0; i < allCreativeItems.size(); i++) { // Add inclusive recipes
            ItemStack currentItem = allCreativeItems.get(i);
            if (UtilSorting.stackInItemList(excludeItemList,currentItem) || (excludeModdedItems && stackIsModded(currentItem))){
                continue;
            }
            if (UtilSorting.stackInItemList(categoryItems, currentItem)){ // Stack already in list
                continue;
            }
            if (UtilSorting.stackInClassList(inclusiveClassList, currentItem) || UtilSorting.stackInItemList(inclusiveItemList, currentItem) || UtilSorting.stackInKeywordList(inclusiveKeywordList, currentItem)){
                categoryItems.add(currentItem);
                continue;
            }
        }
        ItemStack[] returnArray = new ItemStack[categoryItems.size()];
        for (int i = 0; i < returnArray.length; i++) {
            returnArray[i] = categoryItems.get(i);
        }
        return new ItemCategory(modid, key, iconCoordinate, returnArray);
    }
    public ItemCategory buildForModded(){
        List<ItemStack> categoryItems = new ArrayList<>();
        for (int i = 0; i < allCreativeItems.size(); i++) { // Add inclusive recipes
            ItemStack currentItem = allCreativeItems.get(i);
            if (stackIsModded(currentItem)){
                categoryItems.add(currentItem);
            }
        }
        ItemStack[] returnArray = new ItemStack[categoryItems.size()];
        for (int i = 0; i < returnArray.length; i++) {
            returnArray[i] = categoryItems.get(i);
        }
        return new ItemCategory(modid, key, iconCoordinate, returnArray);
    }
}
