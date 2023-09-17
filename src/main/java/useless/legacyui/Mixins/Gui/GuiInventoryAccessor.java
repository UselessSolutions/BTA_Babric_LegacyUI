package useless.legacyui.Mixins.Gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = GuiInventory.class, remap = false)
public interface GuiInventoryAccessor {
    @Accessor
    GuiButton getArmorButton();
}
