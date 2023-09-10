package useless.legacyui.Gui.Container;

import net.minecraft.core.InventoryAction;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.crafting.CraftingManager;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.*;
import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.core.player.inventory.slot.SlotArmor;
import net.minecraft.core.player.inventory.slot.SlotCreative;
import useless.legacyui.LegacyUI;
import useless.legacyui.Sorting.CraftingCategories;
import useless.legacyui.Sorting.Items.CategoryManager;
import useless.legacyui.Sorting.Items.ItemCategory;

import java.util.ArrayList;
import java.util.List;

public class ContainerCreativeLegacy extends ContainerPlayer {
    public static int slotsWide = 13;
    public static int slotsTall = 6;
    public static int currentRow = 0;
    protected static ItemCategory currentCategory = CategoryManager.get(0);
    protected int creativeSlotsStart;
    public ContainerCreativeLegacy(InventoryPlayer inventoryplayer) {
        this(inventoryplayer, true);
    }
    public ContainerCreativeLegacy(InventoryPlayer inventory, boolean isSinglePlayer) {
        super(inventory, isSinglePlayer);
        inventorySlots.clear();
        int i;
        for (i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventory, i, 57 + i * 18, 151));
        }
        this.creativeSlotsStart = this.inventorySlots.size();
        for (i = 0; i < slotsWide * slotsTall; ++i) {
            int x = i % slotsWide;
            int y = i / slotsWide;
            this.addSlot(new SlotCreative(this.creativeSlotsStart + i, 12 + x * 18, 37 + y * 18, ContainerPlayerCreative.creativeItems.get(i)));
        }
    }
    public void updatePage(int tab){
        currentCategory = CategoryManager.get(tab);
        for (int i = 0; i < slotsWide * slotsTall; ++i) {
            ItemStack item;
            int index = i + (currentRow) * slotsWide;
            if (index < currentCategory.size()){
                item = currentCategory.get(index);
            }
            else {
                item = null;
            }
            ((SlotCreative) this.inventorySlots.get(9+i)).item = item;
        }
    }

    public static void scrollRow(int direction){
        if (direction > 0){
            currentRow++;
        } else if (direction < 0){
            currentRow--;
        }
        if (currentRow < 0){
            currentRow = 0;
        } else if (currentRow > getTotalRows()){
            currentRow = getTotalRows();
        }
    }
    public static int getTotalRows(){
        int size = (currentCategory.size()/slotsWide) - slotsTall + 1;
        if (size < 0){
            size = 0;
        }
        return size;
    }
}
