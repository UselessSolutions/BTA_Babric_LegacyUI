package useless.legacyui.Mixins.Gui;

import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = GuiIngame.class, remap = false)
public interface GuiIngameAccessor {
    @Accessor
    ItemEntityRenderer getItemRenderer();
}
