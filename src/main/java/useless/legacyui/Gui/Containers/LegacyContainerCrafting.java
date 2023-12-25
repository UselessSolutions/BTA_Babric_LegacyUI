package useless.legacyui.Gui.Containers;

import net.minecraft.client.Minecraft;
import net.minecraft.core.InventoryAction;
import net.minecraft.core.achievement.stat.StatList;
import net.minecraft.core.achievement.stat.StatsCounter;
import net.minecraft.core.block.Block;
import net.minecraft.core.crafting.legacy.CraftingManager;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.data.registry.recipe.RecipeSymbol;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.gamemode.Gamemode;
import net.minecraft.core.player.inventory.Container;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.InventoryCraftResult;
import net.minecraft.core.player.inventory.InventoryCrafting;
import net.minecraft.core.player.inventory.InventoryPlayer;
import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.core.player.inventory.slot.SlotCrafting;
import net.minecraft.core.world.World;
import useless.legacyui.Gui.GuiScreens.GuiLegacyCrafting;
import useless.legacyui.Gui.Slots.SlotCraftingDisplayLegacy;
import useless.legacyui.Gui.Slots.SlotNull;
import useless.legacyui.Gui.Slots.SlotResizable;
import useless.legacyui.Helper.InventoryHelper;
import useless.legacyui.LegacyUI;
import useless.legacyui.Sorting.LegacyCategoryManager;
import useless.legacyui.Sorting.Recipe.RecipeCategory;
import useless.legacyui.Sorting.Recipe.RecipeCost;
import useless.legacyui.Sorting.Recipe.RecipeGroup;

import java.util.List;
import java.util.Map;

public class LegacyContainerCrafting extends Container {
    public InventoryCrafting craftMatrix;
    public IInventory craftResult = new InventoryCraftResult();
    private final World world;
    private final int x;
    private final int y;
    private final int z;
    private final int craftingSize;
    private final InventoryPlayer inventoryPlayer;
    public static int inventorySlotsStart = 10;
    public LegacyContainerCrafting(InventoryPlayer inventoryplayer, int craftingSize) {
        this(inventoryplayer, null, 0, 0, 0, craftingSize);
    }
    public LegacyContainerCrafting(InventoryPlayer inventoryplayer, World world, int x, int y, int z, int craftingSize) {
        if (craftingSize <= 4){
            craftMatrix = new InventoryCrafting(this, 2, 2);
        } else {
            craftMatrix = new InventoryCrafting(this, 3, 3);
        }
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.craftingSize = craftingSize;
        this.inventoryPlayer = inventoryplayer;
    }
    public void craftingSlots(boolean showCraftingPreview) {
        this.addSlot(new SlotCrafting(this.inventoryPlayer.player, this.craftMatrix, this.craftResult, 0, showCraftingPreview ? -5000 : 107, showCraftingPreview ? -5000 : 127));
        int baseIterator;
        int subIterator;

        if (craftingSize <= 4){
            // 2x2 Crafting
            for (baseIterator = 0; baseIterator < 2; ++baseIterator) {
                for (subIterator = 0; subIterator < 2; ++subIterator) {
                    this.addSlot(new Slot(this.craftMatrix, subIterator + baseIterator * 2, 29 + subIterator * 18, 118 + baseIterator * 18));
                }
            }
            for (baseIterator = 0; baseIterator < 4; ++baseIterator) {
                this.addSlot(new SlotNull(inventoryPlayer, inventoryPlayer.getSizeInventory() - 1 - baseIterator, -5000, -5000));
            }
        }
        else {
            // 3x3 Crafting
            for (baseIterator = 0; baseIterator < 3; ++baseIterator) {
                for (subIterator = 0; subIterator < 3; ++subIterator) {
                    int x = showCraftingPreview ? -5000 : 20 + subIterator * 18;
                    int y = showCraftingPreview ? -5000 : 109 + baseIterator * 18;
                    this.addSlot(new Slot(this.craftMatrix, subIterator + baseIterator * 3, x, y));
                }
            }
        }
        inventorySlotsStart = inventorySlots.size();
        // 3x9 inventory
        for (baseIterator = 0; baseIterator < 3; ++baseIterator) {
            for (subIterator = 0; subIterator < 9; ++subIterator) {
                this.addSlot(new SlotResizable(this.inventoryPlayer, subIterator + baseIterator * 9 + 9, 152 + subIterator * 12, 112 + baseIterator * 12, 12));
            }
        }
        // 1x9 hotbar
        for (baseIterator = 0; baseIterator < 9; ++baseIterator) {
            this.addSlot(new SlotResizable(this.inventoryPlayer, baseIterator, 152 + baseIterator * 12, 154, 12));
        }

        this.onCraftMatrixChanged(this.craftMatrix);
    }
    public void setRecipes(EntityPlayer player, StatsCounter statCounter, boolean showCraftingPreview){
        boolean isInInventory = craftingSize <= 4;

        int currentSlotId = GuiLegacyCrafting.currentSlot;
        int currentScrollAmount = GuiLegacyCrafting.currentScroll;
        int categoryIndex = GuiLegacyCrafting.currentTab;

        RecipeCategory category = LegacyCategoryManager.getRecipeCategories().get(categoryIndex);

        for (RecipeGroup group : category.getRecipeGroups(isInInventory)){
            LegacyUI.LOGGER.debug("CategoryGroup: " + group.getOutputStack(0, isInInventory).getItemName());
        }
        LegacyUI.LOGGER.debug("Category: " + category + " | slotId: " + currentSlotId + " | currentScroll: " + currentScrollAmount + " | craftPreview: " + showCraftingPreview);
        this.inventorySlots.clear();
        craftingSlots(showCraftingPreview);

        RecipeGroup[] craftingGroups = category.getRecipeGroups(isInInventory);
        boolean discovered;
        boolean highlighted;
        ItemStack item;

        int index = 0;
        for (RecipeGroup group : craftingGroups){
            RecipeEntryCrafting<?, ?> recipe;
            boolean craftable;
            if (index == currentSlotId){ // special rendering for scrolling and recipe preview
                recipe = group.getRecipe(currentScrollAmount, isInInventory);
                craftable = canCraft(player, new RecipeCost(recipe));

                // Recipebar preview
                item = (ItemStack) recipe.getOutput();
                discovered = isDicovered(item, statCounter, player);
                this.addSlot(new SlotCraftingDisplayLegacy(this.inventorySlots.size(), 12 + 18 * index, 56, item, discovered, !craftable, LegacyUI.modSettings.getHighlightColor().value.value));

                if (group.getRecipes(isInInventory).size() > 1) { // If multiple Items in recipe group
                    int idUpper = currentScrollAmount + 1; // Next item in group
                    int idLower = currentScrollAmount - 1; // Last item in group

                    // Next item preview
                    item = group.getOutputStack(idUpper, isInInventory);
                    discovered = isDicovered(item, statCounter, player);
                    craftable = canCraft(player, new RecipeCost(group.getRecipe(idUpper, isInInventory)));
                    this.addSlot(new SlotCraftingDisplayLegacy(this.inventorySlots.size(), 12 + 18 * index, 56 + 21, item, discovered, !craftable, LegacyUI.modSettings.getHighlightColor().value.value));

                    // Previous item preview
                    item = group.getOutputStack(idUpper, isInInventory);
                    discovered = isDicovered(item, statCounter, player);
                    craftable = canCraft(player, new RecipeCost(group.getRecipe(idLower, isInInventory)));
                    this.addSlot(new SlotCraftingDisplayLegacy(this.inventorySlots.size(), 12 + 18 * index, 56 - 21, item, discovered, !craftable, LegacyUI.modSettings.getHighlightColor().value.value));

                }

                // Crafting table result preview
                int offset = showCraftingPreview ? 0:5000;
                RecipeCost cost = new RecipeCost(recipe);
                craftable = canCraft(player, cost);
                item = (ItemStack) recipe.getOutput();
                discovered = isDicovered(item, statCounter, player);
                this.addSlot(new SlotCraftingDisplayLegacy(this.inventorySlots.size(), 103, 123+offset, item, discovered, !craftable, LegacyUI.modSettings.getHighlightColor().value.value, 26));

                for (int j = 1; j < recipe.getRecipeSize(); j++) {
                    item = (ItemStack) recipe.getOutput();
                    discovered = isDicovered(item, statCounter, player);

                    RecipeSymbol[] costSymbols = cost.costMap.keySet().toArray(new RecipeSymbol[0]);
                    int k = 0;
                    if (item != null){
                        for (int i = 0; i < costSymbols.length; i++){
                            if (costSymbols[i].matches(item)){
                                cost.costMap.put(costSymbols[i], cost.costMap.get(costSymbols[i]) - 1);
                                k = i;
                                break;
                            }
                        }
                    }
                    int num = cost.costMap.get(costSymbols[k]);
                    if (recipe.getRecipeSize() > 5){
                        // Render 3x3 crafting grid
                        this.addSlot(new SlotCraftingDisplayLegacy(this.inventorySlots.size(), 20 + 18 * ((j - 1) % 3), 109 + offset + 18 * ((j - 1) / 3), item, discovered, InventoryHelper.itemsInInventory(inventoryPlayer, item) <= num && item != null, LegacyUI.modSettings.getHighlightColor().value.value));
                    }
                    else {
                        // Render 2x2 crafting gird
                        this.addSlot(new SlotCraftingDisplayLegacy(this.inventorySlots.size(), 29 + 18 * ((j - 1) % 2), 118 + offset + 18 * ((j - 1) / 2), item, discovered, InventoryHelper.itemsInInventory(inventoryPlayer, item) <= num && item != null, LegacyUI.modSettings.getHighlightColor().value.value));
                    }


                }
            }
            else { // Renders first Slot of none selected groups
                item = group.getOutputStack(0, isInInventory);
                discovered = isDicovered(item, statCounter, player);
                craftable = canCraft(player, new RecipeCost(group.getRecipe(0, isInInventory)));
                this.addSlot(new SlotCraftingDisplayLegacy(this.inventorySlots.size(), 12 + 18 * index, 56, item, discovered, !craftable, LegacyUI.modSettings.getHighlightColor().value.value));
            }

            index++;
        }
    }
    public boolean craft(Minecraft mc, int windowId){
        boolean isInInventory = craftingSize <= 4;

        int currentSlotId = GuiLegacyCrafting.currentSlot;
        int currentScrollAmount = GuiLegacyCrafting.currentScroll;
        int categoryIndex = GuiLegacyCrafting.currentTab;

        RecipeCategory category = LegacyCategoryManager.getRecipeCategories().get(categoryIndex);

        RecipeEntryCrafting<?, ?> recipe = category.getRecipeGroups(isInInventory)[currentSlotId].getRecipe(currentScrollAmount, isInInventory);
        RecipeCost recipeCost = new RecipeCost(recipe);

        RecipeSymbol[] recipeInput = InventoryHelper.getRecipeInput(recipe);
        if (canCraft(mc.thePlayer, recipeCost)){
            for (int i = 0; i < recipeInput.length; i++){
                int slotId = InventoryHelper.findStackIndex(mc.thePlayer.inventory.mainInventory, recipeInput[i]); // Finds Slot index of an inventory Slot with a desired item
                if (slotId == -1) {continue;}
                if (slotId < 9){ slotId += 36;}


                if (isInInventory){
                    mc.playerController.handleInventoryMouseClick(windowId, InventoryAction.CLICK_LEFT, new int[]{slotId}, mc.thePlayer); // Picks up stack
                    mc.playerController.handleInventoryMouseClick(windowId, InventoryAction.CLICK_RIGHT, new int[]{i}, mc.thePlayer); // Places one item
                    mc.playerController.handleInventoryMouseClick(windowId, InventoryAction.CLICK_LEFT, new int[]{slotId}, mc.thePlayer); // Puts down stack
                }
                else if (recipe.getRecipeSize() > 5){// 3x3 crafting
                    int offset = 1;
                    mc.playerController.handleInventoryMouseClick(windowId, InventoryAction.CLICK_LEFT, new int[]{slotId + offset}, mc.thePlayer); // Picks up stack
                    mc.playerController.handleInventoryMouseClick(windowId, InventoryAction.CLICK_RIGHT, new int[]{i}, mc.thePlayer); // Places one item
                    mc.playerController.handleInventoryMouseClick(windowId, InventoryAction.CLICK_LEFT, new int[]{slotId + offset}, mc.thePlayer); // Puts down stack
                }
                else {// 2x2 crafting
                    mc.playerController.handleInventoryMouseClick(windowId, InventoryAction.CLICK_LEFT, new int[]{slotId + 1}, mc.thePlayer); // Picks up stack
                    mc.playerController.handleInventoryMouseClick(windowId, InventoryAction.CLICK_RIGHT, new int[]{i + (i/3)}, mc.thePlayer); // Places one item
                    mc.playerController.handleInventoryMouseClick(windowId, InventoryAction.CLICK_LEFT, new int[]{slotId + 1}, mc.thePlayer); // Puts down stack
                }
            }
            mc.playerController.handleInventoryMouseClick(windowId, InventoryAction.MOVE_STACK, new int[]{0}, mc.thePlayer);
            mc.playerController.handleInventoryMouseClick(windowId, InventoryAction.DROP, new int[]{0}, mc.thePlayer);
            return true; // Craft succeeded
        }
        return false; // Craft failed
    }
    private boolean canCraft(EntityPlayer player, RecipeCost cost){
        boolean canCraft = true;
        for (Map.Entry<RecipeSymbol, Integer> entry : cost.costMap.entrySet()){
            canCraft = canCraft && InventoryHelper.itemsInInventory(player.inventory, entry.getKey()) >= entry.getValue();
        }
        return canCraft;
    }
    public static boolean isDicovered(ItemStack item, StatsCounter statWriter, EntityPlayer player){
        if (!LegacyUI.modSettings.getCraftingHideUndiscoveredItems().value){
            return true;
        }
        if (player.getGamemode() == Gamemode.creative) {
            return true;
        }
        if (item == null) {
            return false;
        } else {
            return statWriter.readStat(StatList.pickUpItemStats[item.itemID]) > 0;
        }
    }
    public void onCraftMatrixChanged(IInventory iinventory) {
        this.craftResult.setInventorySlotContents(0, Registries.RECIPES.findMatchingRecipe(this.craftMatrix));
    }
    public void onCraftGuiClosed(EntityPlayer player) {
        super.onCraftGuiClosed(player);
        boolean insert = false;
        for (int i = 0; i < 9; ++i) {
            ItemStack itemstack = this.craftMatrix.getStackInSlot(i);
            if (itemstack == null) continue;
            this.storeOrDropItem(player, itemstack);
            insert = true;
        }
        if (insert) {
            player.world.playSoundAtEntity(player, "random.insert", 0.1f, 1.0f);
        }
    }
    public boolean isUsableByPlayer(EntityPlayer entityplayer) {
        if (craftingSize <=4 ){ // Inventory Crafting
            return true;
        }
        if (this.world.getBlockId(this.x, this.y, this.z) != Block.workbench.id) { // Supplied coords are not a crafting table
            return false;
        }
        return entityplayer.distanceToSqr((double)this.x + 0.5, (double)this.y + 0.5, (double)this.z + 0.5) <= 64.0; // Is close enough to table
    }
    public List<Integer> getMoveSlots(InventoryAction action, Slot slot, int target, EntityPlayer player) {
        if (craftingSize > 4){
            if (slot.id == 0) {
                return this.getSlots(0, 1, false);
            }
            if (slot.id >= 1 && slot.id < 9) {
                return this.getSlots(1, 9, false);
            }
            if (action == InventoryAction.MOVE_SIMILAR) {
                if (slot.id >= 10 && slot.id <= 45) {
                    return this.getSlots(10, 36, false);
                }
            } else {
                if (slot.id >= 10 && slot.id <= 36) {
                    return this.getSlots(10, 27, false);
                }
                if (slot.id >= 37 && slot.id <= 45) {
                    return this.getSlots(37, 9, false);
                }
            }
        } else {
            if (slot.id == 0) {
                return this.getSlots(0, 1, false);
            }
            if (slot.id >= 1 && slot.id <= 4) {
                return this.getSlots(1, 4, false);
            }
            if (slot.id >= 5 && slot.id <= 8) {
                return this.getSlots(5, 4, false);
            }
            if (action == InventoryAction.MOVE_SIMILAR) {
                if (slot.id >= 9 && slot.id <= 44) {
                    return this.getSlots(9, 36, false);
                }
            } else {
                if (slot.id >= 9 && slot.id <= 35) {
                    return this.getSlots(9, 27, false);
                }
                if (slot.id >= 36 && slot.id <= 44) {
                    return this.getSlots(36, 9, false);
                }
            }
        }
        return null;

    }
    public List<Integer> getTargetSlots(InventoryAction action, Slot slot, int target, EntityPlayer player) {
        if (craftingSize > 4){
            if (slot.id >= 10 && slot.id <= 45) {
                if (target == 1) {
                    return this.getSlots(1, 9, false);
                }
                if (slot.id >= 10 && slot.id <= 36) {
                    return this.getSlots(37, 9, false);
                }
                if (slot.id >= 37 && slot.id <= 45) {
                    return this.getSlots(10, 27, false);
                }
            } else {
                if (slot.id == 0) {
                    return this.getSlots(10, 36, true);
                }
                return this.getSlots(10, 36, false);
            }
            return null;
        } {
            if (slot.id >= 9 && slot.id <= 44) {
                if (target == 1) {
                    return this.getSlots(1, 4, false);
                }
                if (target == 2) {
                    return this.getSlots(5, 4, false);
                }
                if (slot.id < 36) {
                    return this.getSlots(36, 9, false);
                }
                return this.getSlots(9, 27, false);
            }
            if (slot.id == 0) {
                return this.getSlots(9, 36, true);
            }
            return this.getSlots(9, 36, false);
        }
    }
    public void handleHotbarSwap(int[] args, EntityPlayer player) {
        if (args[0] > ((craftingSize <= 4) ? (5 + 4 + 27 -1):(10 + 27 -1))){
            return;
        }
        super.handleHotbarSwap(args, player);
    }
    public int getHotbarSlotId(int number) {
        if (craftingSize <= 4){
            return 5 + 4 + 27 -1 + number;
        } else {
            return 10 + 27 -1 + number;
        }
    }
}
