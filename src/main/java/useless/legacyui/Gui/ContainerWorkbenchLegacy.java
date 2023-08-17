package useless.legacyui.Gui;

import net.minecraft.core.InventoryAction;
import net.minecraft.core.achievement.stat.StatFileWriter;
import net.minecraft.core.achievement.stat.StatList;
import net.minecraft.core.block.Block;
import net.minecraft.core.crafting.CraftingManager;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.gamemode.Gamemode;
import net.minecraft.core.player.inventory.*;
import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.core.player.inventory.slot.SlotCrafting;
import net.minecraft.core.player.inventory.slot.SlotGuidebook;
import net.minecraft.core.world.World;
import useless.legacyui.LegacyUI;

import java.util.List;

public class ContainerWorkbenchLegacy extends Container {
    public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
    public IInventory craftResult = new InventoryCraftResult();
    private World field_20133_c;
    private int x;
    private int y;
    private int z;
    private InventoryPlayer inventoryPlayer;

    public ContainerWorkbenchLegacy(InventoryPlayer inventoryplayer, World world, int i, int j, int k) {
        this.field_20133_c = world;
        this.x = i;
        this.y = j;
        this.z = k;
        this.inventoryPlayer = inventoryplayer;
    }

    public void craftingSlots(){
        this.addSlot(new SlotCrafting(this.inventoryPlayer.player, this.craftMatrix, this.craftResult, 0, 107, 127));
        int baseIterator;
        int subIterator;
        // 3x3 Crafting
        for(baseIterator = 0; baseIterator < 3; ++baseIterator) {
            for(subIterator = 0; subIterator < 3; ++subIterator) {
                this.addSlot(new Slot(this.craftMatrix, subIterator + baseIterator * 3, 20 + subIterator * 18, 109 + baseIterator * 18));
            }
        }

        // 3x9 inventory
        for(baseIterator = 0; baseIterator < 3; ++baseIterator) {
            for(subIterator = 0; subIterator < 9; ++subIterator) {
                this.addSlot(new SlotResizable(this.inventoryPlayer, subIterator + baseIterator * 9 + 9, 153 + subIterator * 12, 112 + baseIterator * 12, 12));
            }
        }

        // 1x9 hotbar
        for(baseIterator = 0; baseIterator < 9; ++baseIterator) {
            this.addSlot(new SlotResizable(this.inventoryPlayer, baseIterator, 153 + baseIterator * 12, 154, 12));
        }

        this.onCraftMatrixChanged(this.craftMatrix);
    }

    public void setRecipes(EntityPlayer player, ContainerGuidebookRecipeBase[] recipes, StatFileWriter statWriter) {
        this.inventorySlots.clear();
        craftingSlots();
        int i = 0;
        for (ContainerGuidebookRecipeBase container : recipes){
            SlotGuidebook slot = (SlotGuidebook)container.inventorySlots.get(0);
            this.addSlot(new SlotGuidebook(this.inventorySlots.size(), 12 + 18*i, 56, slot.item, true));
            i++;
        }

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
        } else if (slot.id < 10){
            return slot.id == 0 ? this.getSlots(10, 36, true) : this.getSlots(10, 36, false);
        } else {
            return null;
        }
    }
    public void handleHotbarSwap(int[] args, EntityPlayer player) {
        // Dont hotbar swap the crafting guide!
        if (args[0] > 45){
            return;
        }

        if (args.length < 2) {
            return;
        }
        int hotbarSlotNumber = args[1];
        if (hotbarSlotNumber < 1 || hotbarSlotNumber > 9) {
            return;
        }
        Slot slot = this.getSlot(args[0]);
        Slot hotbarSlot = this.getSlot(this.getHotbarSlotId(hotbarSlotNumber));
        if (hotbarSlot == null || slot == hotbarSlot) {
            return;
        }
        ItemStack slotStack = slot.getStack();
        ItemStack hotbarStack = hotbarSlot.getStack();
        if (slotStack != null) {
            slot.putStack(null);
            slot.onPickupFromSlot(slotStack);
        }
        if (hotbarStack != null) {
            hotbarSlot.putStack(null);
            hotbarSlot.onPickupFromSlot(hotbarStack);
        }
        this.mergeItems(slotStack, hotbarSlot.id);
        this.storeOrDropItem(player, slotStack);
        this.mergeItems(hotbarStack, slot.id);
        this.storeOrDropItem(player, hotbarStack);
        slot.onSlotChanged();
        hotbarSlot.onSlotChanged();
    }

    public int getHotbarSlotId(int number) {
        // - 14 to account for the 14 display items
        return this.inventorySlots.size() - 10 - 14 + number;
    }

}
