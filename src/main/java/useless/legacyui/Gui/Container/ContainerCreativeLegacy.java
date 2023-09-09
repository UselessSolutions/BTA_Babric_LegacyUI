package useless.legacyui.Gui.Container;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.*;
import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.core.player.inventory.slot.SlotArmor;
import net.minecraft.core.player.inventory.slot.SlotCreative;

import java.util.ArrayList;
import java.util.List;

public class ContainerCreativeLegacy extends ContainerPlayer {
    public static int slotsWide = 13;
    public static int slotsTall = 6;
    public static int currentRow = 0;
    public static int totalRows = ContainerPlayerCreative.creativeItemsCount/slotsWide;
    public ContainerCreativeLegacy(InventoryPlayer inventoryplayer, InventoryCrafting craftMatrix, IInventory craftResult) {
        this(inventoryplayer, true, craftMatrix, craftResult);
    }
    public ContainerCreativeLegacy(InventoryPlayer inventory, boolean isSinglePlayer, InventoryCrafting craftMatrix, IInventory craftResult) {
        super(inventory, isSinglePlayer);
        inventorySlots.clear();
        this.craftMatrix = craftMatrix;
        this.craftResult = craftResult;
        int i;
        for (i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventory, i, 57 + i * 18, 151));
        }
        for (i = 0; i < slotsWide * slotsTall; ++i) {
            int x = i % slotsWide;
            int y = i / slotsWide;
            this.addSlot(new SlotCreative(inventorySlots.size() + i, 12 + x * 18, 37 + y * 18, ContainerPlayerCreative.creativeItems.get(i)));
        }
    }
    public void updatePage(){
        for (int i = 0; i < slotsWide * slotsTall; ++i) {
            ItemStack item;
            int index = i + currentRow * slotsWide;
            if (index < ContainerPlayerCreative.creativeItemsCount){
                item = ContainerPlayerCreative.creativeItems.get(index);
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
        } else if (currentRow > totalRows){
            currentRow = totalRows;
        }
    }
}