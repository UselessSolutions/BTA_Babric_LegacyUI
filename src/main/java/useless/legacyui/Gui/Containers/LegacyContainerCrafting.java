package useless.legacyui.Gui.Containers;

import net.minecraft.client.Minecraft;
import net.minecraft.core.InventoryAction;
import net.minecraft.core.achievement.stat.StatFileWriter;
import net.minecraft.core.achievement.stat.StatList;
import net.minecraft.core.block.Block;
import net.minecraft.core.crafting.CraftingManager;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.gamemode.Gamemode;
import net.minecraft.core.player.inventory.*;
import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.core.player.inventory.slot.SlotCrafting;
import net.minecraft.core.world.World;
import useless.legacyui.Gui.GuiScreens.GuiLegacyCrafting;
import useless.legacyui.Gui.Slots.SlotCraftingDisplayLegacy;
import useless.legacyui.Gui.Slots.SlotNull;
import useless.legacyui.Helper.InventoryHelper;
import useless.legacyui.LegacyUI;
import useless.legacyui.ModSettings;
import useless.legacyui.Sorting.LegacyCategoryManager;
import useless.legacyui.Sorting.Recipe.RecipeCategory;
import useless.legacyui.Sorting.Recipe.RecipeCost;
import useless.legacyui.Sorting.Recipe.RecipeGroup;
import useless.prismaticlibe.gui.slot.SlotCraftingDisplay;
import useless.prismaticlibe.gui.slot.SlotResizable;

import java.util.Arrays;
import java.util.List;

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
        if (craftingSize <= 4){
            craftMatrix = new InventoryCrafting(this, 2, 2);
        } else {
            craftMatrix = new InventoryCrafting(this, 3, 3);
        }
        this.world = null;
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.craftingSize = craftingSize;
        this.inventoryPlayer = inventoryplayer;
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
    public void craftingSlots() {
        this.addSlot(new SlotCrafting(this.inventoryPlayer.player, this.craftMatrix, this.craftResult, 0, 107, 127));
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
                    this.addSlot(new Slot(this.craftMatrix, subIterator + baseIterator * 3, 20 + subIterator * 18, 109 + baseIterator * 18));
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
    public void setRecipes(EntityPlayer player, StatFileWriter statWriter, boolean showCraftingPreview){
        boolean isInInventory = craftingSize <= 4;

        int currentSlotId = GuiLegacyCrafting.currentSlot;
        int currentScrollAmount = GuiLegacyCrafting.currentScroll;
        int categoryIndex = GuiLegacyCrafting.currentTab;

        RecipeCategory category = LegacyCategoryManager.recipeCategories.get(categoryIndex);

        for (RecipeGroup group : category.getRecipeGroups(isInInventory)){
            LegacyUI.LOGGER.debug("CategoryGroup: " + group.getContainer(0, isInInventory).inventorySlots.get(0).getStack().getItem().getKey());
        }
        LegacyUI.LOGGER.debug("Category: " + category + " | slotId: " + currentSlotId + " | currentScroll: " + currentScrollAmount + " | craftPreview: " + showCraftingPreview);
        this.inventorySlots.clear();
        craftingSlots();

        RecipeGroup[] craftingGroups = category.getRecipeGroups(isInInventory);
        boolean discovered;
        boolean highlighted;
        ItemStack item;

        int index = 0;
        for (RecipeGroup group : craftingGroups){
            ContainerGuidebookRecipeCrafting currentContainer;
            boolean craftable;
            if (index == currentSlotId){ // special rendering for scrolling and recipe preview
                currentContainer = group.getContainer(currentScrollAmount, isInInventory);
                craftable = canCraft(player, new RecipeCost(currentContainer));

                // Recipebar preview
                item = currentContainer.inventorySlots.get(0).getStack();
                discovered = isDicovered(item, statWriter, player);
                this.addSlot(new SlotCraftingDisplayLegacy(this.inventorySlots.size(), 12 + 18 * index, 56, currentContainer.inventorySlots.get(0).getStack(), discovered || craftable, !craftable, ModSettings.Colors.HighlightColor()));

                if (group.getRecipes(isInInventory).length > 1) { // If multiple Items in recipe group
                    int idUpper = currentScrollAmount + 1; // Next item in group
                    int idLower = currentScrollAmount - 1; // Last item in group

                    // Next item preview
                    item = group.getContainer(idUpper, isInInventory).inventorySlots.get(0).getStack();
                    discovered = isDicovered(item, statWriter, player);
                    craftable = canCraft(player, new RecipeCost(group.getContainer(idUpper, isInInventory)));
                    this.addSlot(new SlotCraftingDisplayLegacy(this.inventorySlots.size(), 12 + 18 * index, 56 + 21, item, discovered || craftable, !craftable, ModSettings.Colors.HighlightColor()));

                    // Previous item preview
                    item = group.getContainer(idLower, isInInventory).inventorySlots.get(0).getStack();
                    discovered = isDicovered(item, statWriter, player);
                    craftable = canCraft(player, new RecipeCost(group.getContainer(idLower, isInInventory)));
                    this.addSlot(new SlotCraftingDisplayLegacy(this.inventorySlots.size(), 12 + 18 * index, 56 - 21, item, discovered || craftable, !craftable, ModSettings.Colors.HighlightColor()));

                }

                // Crafting table result preview
                int offset = showCraftingPreview ? 0:5000;
                RecipeCost cost = new RecipeCost(currentContainer);
                craftable = canCraft(player, cost);
                item = currentContainer.inventorySlots.get(0).getStack();
                discovered = isDicovered(item, statWriter, player);
                this.addSlot(new SlotCraftingDisplayLegacy(this.inventorySlots.size(), 103, 123+offset, item, discovered || craftable, !craftable, ModSettings.Colors.HighlightColor(), 26));

                for (int j = 1; j < currentContainer.inventorySlots.size(); j++) {
                    item = currentContainer.inventorySlots.get(j).getStack();
                    discovered = isDicovered(item, statWriter, player);

                    int k = 0;
                    if (item != null){
                        for (int i = 0; i < cost.itemStacks.length; i++){
                            if (cost.itemStacks[i].getItem() == item.getItem()){
                                cost.quantity[i] -= 1;
                                k = i;
                                break;
                            }
                        }
                    }

                    if (currentContainer.inventorySlots.size() > 5){
                        // Render 3x3 crafting grid
                        this.addSlot(new SlotCraftingDisplayLegacy(this.inventorySlots.size(), 20 + 18 * ((j - 1) % 3), 109 + offset + 18 * ((j - 1) / 3), item, discovered, InventoryHelper.itemsInInventory(inventoryPlayer, item, cost.useAlts) <= cost.quantity[k] && item != null, ModSettings.Colors.HighlightColor()));
                    }
                    else {
                        // Render 2x2 crafting gird
                        this.addSlot(new SlotCraftingDisplayLegacy(this.inventorySlots.size(), 29 + 18 * ((j - 1) % 2), 118 + offset + 18 * ((j - 1) / 2), item, discovered, InventoryHelper.itemsInInventory(inventoryPlayer, item, cost.useAlts) <= cost.quantity[k] && item != null, ModSettings.Colors.HighlightColor()));
                    }


                }
            }
            else { // Renders first Slot of none selected groups
                item = group.getContainer(0, isInInventory).inventorySlots.get(0).getStack();
                discovered = isDicovered(item, statWriter, player);
                craftable = canCraft(player, new RecipeCost(group.getContainer(0, isInInventory)));
                this.addSlot(new SlotCraftingDisplayLegacy(this.inventorySlots.size(), 12 + 18 * index, 56, item, discovered || craftable, !craftable, ModSettings.Colors.HighlightColor()));
            }

            index++;
        }
    }
    public boolean craft(Minecraft mc, int windowId){
        boolean isInInventory = craftingSize <= 4;

        int currentSlotId = GuiLegacyCrafting.currentSlot;
        int currentScrollAmount = GuiLegacyCrafting.currentScroll;
        int categoryIndex = GuiLegacyCrafting.currentTab;

        RecipeCategory category = LegacyCategoryManager.recipeCategories.get(categoryIndex);

        ContainerGuidebookRecipeCrafting recipe = category.getRecipeGroups(isInInventory)[currentSlotId].getContainer(currentScrollAmount, isInInventory);
        RecipeCost recipeCost = new RecipeCost(recipe);

        if (canCraft(mc.thePlayer, recipeCost)){
            for (int i = 1; i < recipe.inventorySlots.size(); i++){
                ItemStack itemStack = recipe.inventorySlots.get(i).getStack();
                if (itemStack != null){
                    int slotId = InventoryHelper.findStackIndex(mc.thePlayer.inventory.mainInventory, itemStack, recipeCost.useAlts); // Finds Slot index of an inventory Slot with a desired item
                    if (slotId == -1) {continue;}
                    if (slotId < 9){ slotId += 36;}


                    if (isInInventory){
                        mc.playerController.doInventoryAction(windowId, InventoryAction.CLICK_LEFT, new int[]{slotId}, mc.thePlayer); // Picks up stack
                        mc.playerController.doInventoryAction(windowId, InventoryAction.CLICK_RIGHT, new int[]{i}, mc.thePlayer); // Places one item
                        mc.playerController.doInventoryAction(windowId, InventoryAction.CLICK_LEFT, new int[]{slotId}, mc.thePlayer); // Puts down stack
                    }
                    else if (recipe.inventorySlots.size() > 5){// 3x3 crafting
                        int offset = 1;
                        mc.playerController.doInventoryAction(windowId, InventoryAction.CLICK_LEFT, new int[]{slotId + offset}, mc.thePlayer); // Picks up stack
                        mc.playerController.doInventoryAction(windowId, InventoryAction.CLICK_RIGHT, new int[]{i}, mc.thePlayer); // Places one item
                        mc.playerController.doInventoryAction(windowId, InventoryAction.CLICK_LEFT, new int[]{slotId + offset}, mc.thePlayer); // Puts down stack
                    }
                    else {// 2x2 crafting
                        mc.playerController.doInventoryAction(windowId, InventoryAction.CLICK_LEFT, new int[]{slotId + 1}, mc.thePlayer); // Picks up stack
                        mc.playerController.doInventoryAction(windowId, InventoryAction.CLICK_RIGHT, new int[]{i + (i/3)}, mc.thePlayer); // Places one item
                        mc.playerController.doInventoryAction(windowId, InventoryAction.CLICK_LEFT, new int[]{slotId + 1}, mc.thePlayer); // Puts down stack
                    }
                }
            }
            mc.playerController.doInventoryAction(windowId, InventoryAction.MOVE_STACK, new int[]{0}, mc.thePlayer);
            return true; // Craft succeeded
        }
        return false; // Craft failed
    }
    private boolean canCraft(EntityPlayer player, RecipeCost cost){
        boolean canCraft = true;
        for (int i = 0; i < cost.itemStacks.length; i++){
            canCraft = canCraft && InventoryHelper.itemsInInventory(player.inventory, cost.itemStacks[i], cost.useAlts) >= cost.quantity[i];
        }
        return canCraft;
    }
    public static boolean isDicovered(ItemStack item, StatFileWriter statWriter, EntityPlayer player){
        if (!ModSettings.Gui.HideUndiscoveredItems()){
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
        this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix));
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
