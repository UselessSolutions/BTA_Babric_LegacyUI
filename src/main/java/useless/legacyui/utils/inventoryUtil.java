package useless.legacyui.utils;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;

public class inventoryUtil {
    public static int itemsInInventory(EntityPlayer thePlayer, ItemStack itemToFind){
        int itemCount = 0;
        int stackCount = 0;
        for (int i = 0; i < thePlayer.inventory.getSizeInventory(); ++i) {
            ItemStack itemStack = thePlayer.inventory.getStackInSlot(i);
            if (itemStack == null || itemStack.itemID != itemToFind.itemID || itemStack.getMetadata() != itemToFind.getMetadata()) continue;
            itemCount += itemStack.stackSize;
            ++stackCount;
        }
        return Math.max(itemCount, stackCount);
    }
}
