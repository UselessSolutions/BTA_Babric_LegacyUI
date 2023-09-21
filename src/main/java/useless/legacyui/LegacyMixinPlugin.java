package useless.legacyui;

import com.google.common.collect.ImmutableMap;
import goocraft4evr.nonamedyes.client.gui.GuiBleacher;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.render.FontRenderer;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import useless.legacyui.Settings.ModSettings;
import useless.prismaticlibe.helper.ModCheckHelper;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public final class LegacyMixinPlugin implements IMixinConfigPlugin {
    private static final Supplier<Boolean> TRUE = () -> true;
    private static final Map<String, Supplier<Boolean>> CONDITIONS = ImmutableMap.of(
            "useless.legacyui.Mixins.Modded.NoNameDyes.GuiBleacherMixin", () -> FabricLoader.getInstance().isModLoaded("nonamedyes")
    );

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return CONDITIONS.getOrDefault(mixinClassName, TRUE).get();
    }
    @Override
    public void onLoad(String mixinPackage) {

    }
    @Override
    public String getRefMapperConfig() {
        return null;
    }
    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
