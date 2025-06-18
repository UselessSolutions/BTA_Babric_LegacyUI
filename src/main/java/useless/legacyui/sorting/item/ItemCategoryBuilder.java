package useless.legacyui.sorting.item;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.menu.MenuInventoryCreative;
import net.minecraft.core.util.collection.NamespaceID;
import useless.legacyui.LegacyUI;
import useless.legacyui.sorting.UtilSorting;

import java.util.ArrayList;
import java.util.List;

public class ItemCategoryBuilder {
    private static final List<String> modList = new ArrayList<>();
    static {
        for (final ModContainer mod : FabricLoader.getInstance().getAllMods()) {
            modList.add(mod.getMetadata().getId());
        }
    }
    private static final List<ItemStack> allCreativeItems = MenuInventoryCreative.creativeItems;
    private static final List<ItemStack> unusedCreativeItems = new ArrayList<>(allCreativeItems);
    private String key = "default";
    private final String modid;
    public IconCoordinate iconCoordinate = TextureRegistry.getTexture("legacyui:gui/icon/unknown");
    private Boolean isDebug = false;
    private Boolean forModded = false;
    private Boolean excludeModdedItems = false;
    private final List<Class<?>> inclusiveClassList = new ArrayList<>();
    private final List<ItemStack> inclusiveItemList = new ArrayList<>();
    private final List<String> inclusiveKeywordList = new ArrayList<>();
    private final List<Class<?>> exclusiveClassList = new ArrayList<>();
    private final List<ItemStack> exclusiveItemList = new ArrayList<>();
    private final List<String> exclusiveKeywordList = new ArrayList<>();
    private final List<ItemStack> excludeItemList = new ArrayList<>();
    public ItemCategoryBuilder(final String modid){
        this.modid = modid;
    }
    public ItemCategoryBuilder excludeItem(final Item item){
        return excludeItem(new ItemStack(item));
    }
    public ItemCategoryBuilder excludeItem(final ItemStack stack){
        if (this.isDebug){
            LegacyUI.LOGGER.info(stack.toString());
        }
        this.excludeItemList.add(stack);
        return this;
    }
    public ItemCategoryBuilder addKeyword(final String keyword){
        return addKeyword(keyword, false);
    }
    public ItemCategoryBuilder addKeyword(final String keyword, final boolean isInclusive){
        if (this.isDebug){
            LegacyUI.LOGGER.info(keyword);
        }

        if (isInclusive){
            this.inclusiveKeywordList.add(keyword);
        } else {
            this.exclusiveKeywordList.add(keyword);
        }

        return this;
    }
    public ItemCategoryBuilder addItemsWithMetaRange(final Item item, final int metaStart, final int metaRange, final boolean isInclusive){
        for (int i = 0; i < metaRange; i++) {
            this.addItem(item, metaStart+i, isInclusive);
        }
        return this;
    }
    public ItemCategoryBuilder addItem(final Block block){
        return addItem(block.asItem(), false);
    }
    public ItemCategoryBuilder addItem(final Block block, final boolean isInclusive){
        return addItem(block.asItem(), isInclusive);
    }
    public ItemCategoryBuilder addItem(final int id, final int meta){
        return addItem(new ItemStack(id, 1, meta), false);
    }
    public ItemCategoryBuilder addItem(final int id, final int meta, final boolean isInclusive){
        return addItem(new ItemStack(id, 1, meta), isInclusive);
    }
    public ItemCategoryBuilder addItem(final Item item, final int meta){
        return addItem(new ItemStack(item, 1, meta), false);
    }
    public ItemCategoryBuilder addItem(final Item item, final int meta, final boolean isInclusive){
        return addItem(new ItemStack(item, 1, meta), isInclusive);
    }
    public ItemCategoryBuilder addItem(final Item item, final boolean isInclusive){
        return addItem(new ItemStack(item), isInclusive);
    }
    public ItemCategoryBuilder addItem(final Item item){
        return addItem(new ItemStack(item));
    }
    public ItemCategoryBuilder addItem(final ItemStack stack){
        return addItem(stack, false);
    }
    public ItemCategoryBuilder addItem(final ItemStack stack, final boolean isInclusive){
        if (this.isDebug){
            LegacyUI.LOGGER.info(stack.toString());
        }

        if (isInclusive){
            this.inclusiveItemList.add(stack);
        } else {
            this.exclusiveItemList.add(stack);
        }

        return this;
    }
    public ItemCategoryBuilder addClass(final Class clazz){
        return addClass(clazz, false);
    }
    public ItemCategoryBuilder addClass(final Class clazz, final boolean isInclusive){
        if (this.isDebug){
            LegacyUI.LOGGER.info(clazz.getName());
        }

        if (isInclusive){
            this.inclusiveClassList.add(clazz);
        } else {
            this.exclusiveClassList.add(clazz);
        }

        return this;
    }
    public ItemCategoryBuilder isDebug(){
        this.isDebug = true;
        return this;
    }
    public ItemCategoryBuilder isModded(){
        this.forModded = true;
        return this;
    }
    public ItemCategoryBuilder printCurrentConfig(){
        LegacyUI.LOGGER.info("isDebug:" + this.isDebug);
        for (final Class clazz : this.inclusiveClassList){
            LegacyUI.LOGGER.info("inclusiveClass:"+clazz.getName());
        }
        for (final ItemStack stack : this.inclusiveItemList){
            LegacyUI.LOGGER.info("inclusiveItemStack:"+stack);
        }
        for (final String keyword : this.inclusiveKeywordList){
            LegacyUI.LOGGER.info("inclusiveKeyword:"+keyword);
        }
        for (final Class clazz : this.exclusiveClassList){
            LegacyUI.LOGGER.info("exclusiveClass:"+clazz.getName());
        }
        for (final ItemStack stack : this.exclusiveItemList){
            LegacyUI.LOGGER.info("exclusiveItemStack:"+stack);
        }
        for (final String keyword : this.exclusiveKeywordList){
            LegacyUI.LOGGER.info("exclusiveKeyword:"+keyword);
        }
        return this;
    }
    public ItemCategoryBuilder setTranslationKey(final String key){
        this.key = key;
        return this;
    }
    public ItemCategoryBuilder setIcon(final String iconTexturePath){
        this.iconCoordinate = TextureRegistry.getTexture(iconTexturePath);
        return this;
    }
    public ItemCategoryBuilder setIcon(final NamespaceID iconCoordinate){
        this.iconCoordinate = TextureRegistry.getTexture(iconCoordinate);
        return this;
    }
    public ItemCategoryBuilder excludeModdedItems(){
        this.excludeModdedItems = true;
        return this;
    }
    public boolean stackIsModded(final ItemStack stack){
        final String itemModID = stack.getItemKey().split("[.]")[1];
        for (final String modId: modList) {
            if (itemModID.equals(modId)){
                return true;
            }
        }
        return false;
    }
    public ItemCategory build(){
        if (this.forModded){
            return buildForModded();
        }
        final List<ItemStack> unused_copy = new ArrayList<>(unusedCreativeItems);
        final List<ItemStack> categoryItems = new ArrayList<>();
        int removeOffset = 0;
        for (int i = 0; i < unused_copy.size(); i++) { // Add exclusive Recipes
            final ItemStack currentItem = unused_copy.get(i);
            if (UtilSorting.stackInItemList(this.excludeItemList,currentItem) || (this.excludeModdedItems && stackIsModded(currentItem))){
                continue;
            }
            if (UtilSorting.stackInClassList(this.exclusiveClassList,currentItem) || UtilSorting.stackInItemList(this.exclusiveItemList, currentItem) || UtilSorting.stackInKeywordList(this.exclusiveKeywordList, currentItem)) {
                categoryItems.add(currentItem);
                unusedCreativeItems.remove(i - removeOffset);
                removeOffset++;
                continue;

            }
        }
        for (int i = 0; i < allCreativeItems.size(); i++) { // Add inclusive recipes
            final ItemStack currentItem = allCreativeItems.get(i);
            if (UtilSorting.stackInItemList(this.excludeItemList,currentItem) || (this.excludeModdedItems && stackIsModded(currentItem))){
                continue;
            }
            if (UtilSorting.stackInItemList(categoryItems, currentItem)){ // Stack already in list
                continue;
            }
            if (UtilSorting.stackInClassList(this.inclusiveClassList, currentItem) || UtilSorting.stackInItemList(this.inclusiveItemList, currentItem) || UtilSorting.stackInKeywordList(this.inclusiveKeywordList, currentItem)){
                categoryItems.add(currentItem);
                continue;
            }
        }
        final ItemStack[] returnArray = new ItemStack[categoryItems.size()];
        for (int i = 0; i < returnArray.length; i++) {
            returnArray[i] = categoryItems.get(i);
        }
        return new ItemCategory(this.modid, this.key, this.iconCoordinate, returnArray);
    }
    public ItemCategory buildForModded(){
        final List<ItemStack> categoryItems = new ArrayList<>();
        for (int i = 0; i < allCreativeItems.size(); i++) { // Add inclusive recipes
            final ItemStack currentItem = allCreativeItems.get(i);
            if (stackIsModded(currentItem)){
                categoryItems.add(currentItem);
            }
        }
        final ItemStack[] returnArray = new ItemStack[categoryItems.size()];
        for (int i = 0; i < returnArray.length; i++) {
            returnArray[i] = categoryItems.get(i);
        }
        return new ItemCategory(this.modid, this.key, this.iconCoordinate, returnArray);
    }
}
