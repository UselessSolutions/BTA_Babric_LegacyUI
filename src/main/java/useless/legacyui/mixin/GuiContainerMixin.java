package useless.legacyui.mixin;

import net.minecraft.client.gui.GuiContainer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.core.player.inventory.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import useless.legacyui.Gui.SlotResizable;

@Mixin(value = GuiContainer.class, remap = false)
public class GuiContainerMixin extends GuiScreen {
    @Shadow
    private int xSize;
    @Shadow
    private int ySize;

    @Overwrite
    public boolean getIsMouseOverSlot(Slot slot, int i, int j) {
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        i -= k;
        j -= l;
        int slotSize = 16;
        if (slot instanceof SlotResizable){
            slotSize = ((SlotResizable) slot).width;
        }
        return i >= slot.xDisplayPosition - 1 && i < slot.xDisplayPosition + slotSize -2 + 1 && j >= slot.yDisplayPosition - 1 && j < slot.yDisplayPosition + slotSize -2 + 1;
    }
}
