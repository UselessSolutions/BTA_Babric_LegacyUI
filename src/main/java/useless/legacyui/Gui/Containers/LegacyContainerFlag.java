package useless.legacyui.Gui.Containers;

import net.minecraft.client.Minecraft;
import net.minecraft.core.block.entity.TileEntityFlag;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.ContainerFlag;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.core.player.inventory.slot.SlotDye;
import useless.legacyui.Gui.GuiScreens.GuiLegacyFlag;
import useless.legacyui.Gui.Slots.SlotCraftingDisplayLegacy;
import useless.legacyui.LegacyUI;

import java.util.ArrayList;
import java.util.List;

public class LegacyContainerFlag extends ContainerFlag {
    public IInventory inventory;
    public TileEntityFlag flag;
    public static List<Integer> dyesSlotsInventory = new ArrayList<>();
    public static List<Integer> dyesMetaAtSlot = new ArrayList<>();
    private Minecraft mc = Minecraft.getMinecraft(this);
    public LegacyContainerFlag(IInventory inv, TileEntityFlag flag) {
        super(inv, flag);
        this.flag = flag;
        inventory = inv;
        setSlots();

    }
    public void setSlots(){
        inventorySlots.clear();
        this.addSlot(new SlotDye(flag, 36, 44 + 170, 40));
        this.addSlot(new SlotDye(flag, 37, 77 + 170, 40));
        this.addSlot(new SlotDye(flag, 38, 110 + 170, 40));
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlot(new Slot(inventory, x + y * 9 + 9, 8 + 170 + x * 18, 100 + y * 18));
            }
        }
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inventory, i, 8 + 170 + i * 18, 158));
        }
        switch (GuiLegacyFlag.selectedDyeSlot){
            case 1:
                this.addSlot(new SlotCraftingDisplayLegacy(101, 44, 40, flag.getStackInSlot(36), true, false, -1));
                this.addSlot(new SlotCraftingDisplayLegacy(103, 110, 40, flag.getStackInSlot(38), true, false, -1));
                scrollBarSlots();
                break;
            case 2:
                this.addSlot(new SlotCraftingDisplayLegacy(102, 77, 40, flag.getStackInSlot(37), true, false, -1));
                scrollBarSlots();
                break;
            case 3:
                this.addSlot(new SlotCraftingDisplayLegacy(101, 44, 40, flag.getStackInSlot(36), true, false, -1));
                this.addSlot(new SlotCraftingDisplayLegacy(103, 110, 40, flag.getStackInSlot(38), true, false, -1));
                scrollBarSlots();
                break;
            default:
                this.addSlot(new SlotCraftingDisplayLegacy(101, 44, 40, flag.getStackInSlot(36), true, false, -1));
                this.addSlot(new SlotCraftingDisplayLegacy(102, 77, 40, flag.getStackInSlot(37), true, false, -1));
                this.addSlot(new SlotCraftingDisplayLegacy(103, 110, 40, flag.getStackInSlot(38), true, false, -1));
                break;
        }

    }
    private void scrollBarSlots(){
        findDyes();
        int offset = GuiLegacyFlag.selectedDyeSlot - 1;
        if (offset < 0) {return;}
        ItemStack[] stacks = new ItemStack[4];
        for (int i = 0; i < stacks.length; i++) {
            if (i < dyesSlotsInventory.size()){
                stacks[i] = new ItemStack(Item.dye.id, 1, dyesMetaAtSlot.get(i));
            } else {
                stacks[i] = null;
            }
        }
        addSlot(new SlotCraftingDisplayLegacy(105, 8 + 33 * offset, 40, stacks[0], true, false, -1));
        addSlot(new SlotCraftingDisplayLegacy(106, 26 + 33 * offset, 40, stacks[1], true, false, -1));
        addSlot(new SlotCraftingDisplayLegacy(107, 62 + 33 * offset, 40, stacks[2], true, false, -1));
        addSlot(new SlotCraftingDisplayLegacy(108, 80 + 33 * offset, 40, stacks[3], true, false, -1));
    }
    public void findDyes(){
        dyesSlotsInventory.clear();
        dyesMetaAtSlot.clear();
        ItemStack[] mainInventory = mc.thePlayer.inventory.mainInventory;
        for (int i = 0; i < mainInventory.length; i++){
            if (mainInventory[i] != null && mainInventory[i].getItem() == Item.dye){
                dyesSlotsInventory.add(i);
                dyesMetaAtSlot.add(mainInventory[i].getMetadata());
            }
        }
        for (int i = 0; i < dyesSlotsInventory.size(); i++) {
            LegacyUI.LOGGER.info("Slot: " + dyesSlotsInventory.get(i) + " | Meta: " + dyesMetaAtSlot.get(i));
        }
    }
}
