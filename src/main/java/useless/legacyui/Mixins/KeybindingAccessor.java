package useless.legacyui.Mixins;

import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = KeyBinding.class, remap = false)
public interface KeybindingAccessor {
    @Accessor
    int getKeyCode();
}
