package useless.legacyui.Gui.Containers;

import net.minecraft.client.Minecraft;
import net.minecraft.core.InventoryAction;
import net.minecraft.core.block.entity.TileEntityFlag;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.ContainerFlag;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.core.player.inventory.slot.SlotDye;
import useless.legacyui.Gui.GuiScreens.GuiLegacyFlag;
import useless.legacyui.Gui.Slots.SlotCraftingDisplayLegacy;
import useless.legacyui.Gui.Slots.SlotNull;
import useless.legacyui.Helper.ArrayHelper;
import useless.legacyui.Helper.InventoryHelper;
import useless.legacyui.LegacyUI;

import java.util.ArrayList;
import java.util.List;

public class LegacyContainerFlag extends ContainerFlag {
    public IInventory inventory;
    public TileEntityFlag flag;
    public static List<Integer> dyesSlotsInventory = new ArrayList<>();
    public static List<Integer> dyesMetaAtSlot = new ArrayList<>();
    public static List<ItemStack> displayStacks = new ArrayList<>();
    private Minecraft mc = Minecraft.getMinecraft(this);
    public LegacyContainerFlag(IInventory inv, TileEntityFlag flag) {
        super(inv, flag);
        this.flag = flag;
        inventory = inv;
        setSlots();

    }
    public void setSlots(){
        inventorySlots.clear();
        this.addSlot(new SlotDye(flag, 36, 129, 62));
        this.addSlot(new SlotDye(flag, 37, 129, 81));
        this.addSlot(new SlotDye(flag, 38, 129, 100));
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlot(new SlotNull(inventory, x + y * 9 + 9, 8 + 170 + x * 18, 100 + y * 18));
            }
        }
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new SlotNull(inventory, i, 8 + 170 + i * 18, 158));
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
    public void swapDye(int dyeSelected){
        if (GuiLegacyFlag.selectedColor < 3 && dyeSelected < dyesSlotsInventory.size()){
            int slotId = dyesSlotsInventory.get(dyeSelected); // Finds Slot index of an inventory Slot with a desired item
            if (slotId < 9){
                slotId += 27;
            } else {
                slotId -= 9;
            }
            slotId += 3; // Accounts for the first three slots being the dye slots
            //slotId -= 4;

            mc.playerController.doInventoryAction(windowId, InventoryAction.MOVE_STACK, new int[]{GuiLegacyFlag.selectedColor}, mc.thePlayer);

            mc.playerController.doInventoryAction(windowId, InventoryAction.CLICK_LEFT, new int[]{slotId}, mc.thePlayer);
            mc.playerController.doInventoryAction(windowId, InventoryAction.CLICK_LEFT, new int[]{GuiLegacyFlag.selectedColor}, mc.thePlayer);
            mc.playerController.doInventoryAction(windowId, InventoryAction.CLICK_LEFT, new int[]{slotId}, mc.thePlayer);
        }
        setSlots();
    }

    public void findDyes(){
        dyesSlotsInventory.clear();
        dyesMetaAtSlot.clear();
        ItemStack[] mainInventory = mc.thePlayer.inventory.mainInventory;
        for (int i = 0; i < mainInventory.length; i++){
            if (mainInventory[i] != null && mainInventory[i].getItem() == Item.dye && !dyeAlreadyInList(mainInventory[i].getMetadata())){
                dyesSlotsInventory.add(i);
                dyesMetaAtSlot.add(mainInventory[i].getMetadata());
            }
        }
        displayStacks.clear();
        for (int i = 0; i < dyesSlotsInventory.size(); i++) {
            displayStacks.add(new ItemStack(Item.dye.id, 1, dyesMetaAtSlot.get(i)));
        }
    }
    private boolean dyeAlreadyInList(int meta){
        for (int listMeta: dyesMetaAtSlot) {
            if (listMeta == meta){
                return true;
            }
        }
        return false;
    }
    public List<Integer> getMoveSlots(InventoryAction action, Slot slot, int target, EntityPlayer player) {
        if (slot.id > 38) {return null;}
        if (slot.id >= 0 && slot.id <= 2) {
            return this.getSlots(0, 3, false);
        }
        if (action == InventoryAction.MOVE_SIMILAR) {
            return this.getSlots(3, 36, false);
        }
        if (action == InventoryAction.MOVE_ALL) {
            if (slot.id < 27) {
                return this.getSlots(3, 27, false);
            }
            return this.getSlots(30, 9, false);
        }
        return null;
    }

    @Override
    public List<Integer> getTargetSlots(InventoryAction action, Slot slot, int target, EntityPlayer player) {
        if (slot.id > 38) {return null;}
        if (slot.id >= 0 && slot.id <= 2) {
            return this.getSlots(3, 36, false);
        }
        return this.getSlots(0, 3, false);
    }
    public void handleHotbarSwap(int[] args, EntityPlayer player) {
        if (args[0] > 38){
            return;
        }
        super.handleHotbarSwap(args, player);
    }
    public int getHotbarSlotId(int number) {
        return 3 + 27 -1 + number;
    }
}
