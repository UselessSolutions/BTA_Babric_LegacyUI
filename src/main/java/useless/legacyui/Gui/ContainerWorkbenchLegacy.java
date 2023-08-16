package useless.legacyui.Gui;

import net.minecraft.core.InventoryAction;
import net.minecraft.core.block.Block;
import net.minecraft.core.crafting.CraftingManager;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.*;
import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.core.player.inventory.slot.SlotCrafting;
import net.minecraft.core.world.World;

import java.util.List;

public class ContainerWorkbenchLegacy extends Container {
    public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
    public IInventory craftResult = new InventoryCraftResult();
    private World field_20133_c;
    private int x;
    private int y;
    private int z;

    public ContainerWorkbenchLegacy(InventoryPlayer inventoryplayer, World world, int i, int j, int k) {
        this.field_20133_c = world;
        this.x = i;
        this.y = j;
        this.z = k;
        this.addSlot(new SlotCrafting(inventoryplayer.player, this.craftMatrix, this.craftResult, 0, 107, 127));

        int j1;
        int l1;
        // 3x3 Crafting
        for(j1 = 0; j1 < 3; ++j1) {
            for(l1 = 0; l1 < 3; ++l1) {
                this.addSlot(new Slot(this.craftMatrix, l1 + j1 * 3, 20 + l1 * 18, 109 + j1 * 18));
            }
        }

        // 3x9 inventory
        for(j1 = 0; j1 < 3; ++j1) {
            for(l1 = 0; l1 < 9; ++l1) {
                this.addSlot(new SlotResizable(inventoryplayer, l1 + j1 * 9 + 9, 153 + l1 * 12, 112 + j1 * 12, 12));
            }
        }

        // 1x9 hotbar
        for(j1 = 0; j1 < 9; ++j1) {
            this.addSlot(new SlotResizable(inventoryplayer, j1, 153 + j1 * 12, 154, 12));
        }

        this.onCraftMatrixChanged(this.craftMatrix);
    }

    public void onCraftMatrixChanged(IInventory iinventory) {
        this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix));
    }

    public void onCraftGuiClosed(EntityPlayer player) {
        super.onCraftGuiClosed(player);
        boolean insert = false;

        for(int i = 0; i < 9; ++i) {
            ItemStack itemstack = this.craftMatrix.getStackInSlot(i);
            if (itemstack != null) {
                this.storeOrDropItem(player, itemstack);
                insert = true;
            }
        }

        if (insert) {
            player.world.playSoundAtEntity(player, "random.insert", 0.1F, 1.0F);
        }

    }

    public boolean isUsableByPlayer(EntityPlayer entityplayer) {
        if (this.field_20133_c.getBlockId(this.x, this.y, this.z) != Block.workbench.id) {
            return false;
        } else {
            return entityplayer.distanceToSqr((double)this.x + 0.5, (double)this.y + 0.5, (double)this.z + 0.5) <= 64.0;
        }
    }

    public List<Integer> getMoveSlots(InventoryAction action, Slot slot, int target, EntityPlayer player) {
        if (slot.id == 0) {
            return this.getSlots(0, 1, false);
        } else if (slot.id >= 1 && slot.id < 9) {
            return this.getSlots(1, 9, false);
        } else {
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

            return null;
        }
    }

    public List<Integer> getTargetSlots(InventoryAction action, Slot slot, int target, EntityPlayer player) {
        if (slot.id >= 10 && slot.id <= 45) {
            if (target == 1) {
                return this.getSlots(1, 9, false);
            } else if (slot.id >= 10 && slot.id <= 36) {
                return this.getSlots(37, 9, false);
            } else {
                return slot.id >= 37 && slot.id <= 45 ? this.getSlots(10, 27, false) : null;
            }
        } else {
            return slot.id == 0 ? this.getSlots(10, 36, true) : this.getSlots(10, 36, false);
        }
    }
}
