package useless.legacyui.mixins.gui;

import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.container.ScreenInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(value = ScreenInventory.class, remap = false)
public interface GuiInventoryAccessor {
    @Accessor
    ButtonElement getArmorButton();
    @Accessor
    List<Integer> getUvs();
}
