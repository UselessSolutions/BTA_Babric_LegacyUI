package useless.legacyui.gui.containers;

import net.minecraft.client.Minecraft;
import net.minecraft.core.InventoryAction;
import net.minecraft.core.block.entity.TileEntityFlag;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.player.inventory.menu.MenuFlag;
import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.core.player.inventory.slot.SlotDye;
import useless.legacyui.gui.screens.GuiLegacyFlag;
import useless.legacyui.gui.slots.SlotCraftingDisplayLegacy;
import useless.legacyui.helper.ArrayHelper;

import java.util.ArrayList;
import java.util.List;

public class LegacyContainerFlag extends MenuFlag {
    public Container inventory;
    public TileEntityFlag flag;
    public static List<Integer> dyesSlotsInventory = new ArrayList<>();
    public static List<Integer> dyesMetaAtSlot = new ArrayList<>();
    public static List<ItemStack> displayStacks = new ArrayList<>();
    private final Minecraft mc = Minecraft.getMinecraft();
    public LegacyContainerFlag(final Container inv, final TileEntityFlag flag) {
        super(inv, flag);
        this.flag = flag;
        this.inventory = inv;
        setSlots();

    }
    public void setSlots(){
        this.slots.clear();
        this.addSlot(new SlotDye(this.flag, 36, 129, 62));
        this.addSlot(new SlotDye(this.flag, 37, 129, 81));
        this.addSlot(new SlotDye(this.flag, 38, 129, 100));
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlot(new Slot(this.inventory, x + y * 9 + 9, 8 + 17000 + x * 18, 100 + y * 18));
            }
        }
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(this.inventory, i, 8 + 17000 + i * 18, 158));
        }
        findDyes();
        for (int i = 0; i < 6; i++) {
            ItemStack stack = null;
            if (i < displayStacks.size()){
                stack = displayStacks.get(ArrayHelper.wrapAroundIndex(i + GuiLegacyFlag.dyeScroll, displayStacks.size()));
            }
            addSlot(new SlotCraftingDisplayLegacy(100 + i, 15 + 18 * i, 38, stack, true, false, -1));
        }
    }
    public void swapDye(final int dyeSelected){
        if (GuiLegacyFlag.selectedColor < 3 && dyeSelected < dyesSlotsInventory.size()){
            int slotId = dyesSlotsInventory.get(dyeSelected); // Finds Slot index of an inventory Slot with a desired item
            if (slotId < 9){
                slotId += 27;
            } else {
                slotId -= 9;
            }
            slotId += 3; // Accounts for the first three slots being the dye slots
            //slotId -= 4;

            this.mc.playerController.handleInventoryMouseClick(this.containerId, InventoryAction.MOVE_STACK, new int[]{GuiLegacyFlag.selectedColor}, this.mc.thePlayer);

            this.mc.playerController.handleInventoryMouseClick(this.containerId, InventoryAction.CLICK_LEFT, new int[]{slotId}, this.mc.thePlayer);
            this.mc.playerController.handleInventoryMouseClick(this.containerId, InventoryAction.CLICK_LEFT, new int[]{GuiLegacyFlag.selectedColor}, this.mc.thePlayer);
            this.mc.playerController.handleInventoryMouseClick(this.containerId, InventoryAction.CLICK_LEFT, new int[]{slotId}, this.mc.thePlayer);
        }
        setSlots();
    }

    public void findDyes(){
        dyesSlotsInventory.clear();
        dyesMetaAtSlot.clear();
        final ItemStack[] mainInventory = this.mc.thePlayer.inventory.mainInventory;
        for (int i = 0; i < mainInventory.length; i++){
            if (mainInventory[i] != null && mainInventory[i].getItem() == Items.DYE && !dyeAlreadyInList(mainInventory[i].getMetadata())){
                dyesSlotsInventory.add(i);
                dyesMetaAtSlot.add(mainInventory[i].getMetadata());
            }
        }
        displayStacks.clear();
        for (int i = 0; i < dyesSlotsInventory.size(); i++) {
            displayStacks.add(new ItemStack(Items.DYE.id, 1, dyesMetaAtSlot.get(i)));
        }
    }
    private boolean dyeAlreadyInList(final int meta){
        for (final int listMeta: dyesMetaAtSlot) {
            if (listMeta == meta){
                return true;
            }
        }
        return false;
    }
    @Override
    public List<Integer> getMoveSlots(final InventoryAction action, final Slot slot, final int target, final Player player) {
        if (slot.index > 38) {return null;}
        if (slot.index >= 0 && slot.index <= 2) {
            return this.getSlots(0, 3, false);
        }
        if (action == InventoryAction.MOVE_SIMILAR) {
            return this.getSlots(3, 36, false);
        }
        if (action == InventoryAction.MOVE_ALL) {
            if (slot.index < 27) {
                return this.getSlots(3, 27, false);
            }
            return this.getSlots(30, 9, false);
        }
        return null;
    }

    @Override
    public List<Integer> getTargetSlots(final InventoryAction action, final Slot slot, final int target, final Player player) {
        if (slot.index > 38) {return null;}
        if (slot.index >= 0 && slot.index <= 2) {
            return this.getSlots(3, 36, false);
        }
        return this.getSlots(0, 3, false);
    }

    @Override
    public void handleHotbarSwap(final int[] args, final Player player) {
        if (args[0] > 38){
            return;
        }
        super.handleHotbarSwap(args, player);
    }

    @Override
    public int getHotbarSlotId(final int number) {
        return 3 + 27 -1 + number;
    }
}
