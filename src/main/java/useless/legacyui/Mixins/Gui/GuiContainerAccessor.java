package useless.legacyui.Mixins.Gui;

import net.minecraft.client.gui.GuiContainer;
import net.minecraft.client.gui.GuiRenderItem;
import net.minecraft.client.gui.GuiTooltip;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = GuiContainer.class, remap = false)
public interface GuiContainerAccessor {
    @Accessor
    GuiTooltip getGuiTooltip();
    @Accessor
    GuiRenderItem getGuiRenderItem();
}
