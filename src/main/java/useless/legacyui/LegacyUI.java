package useless.legacyui;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import useless.legacyui.Sorting.LegacyCategoryManager;
import useless.prismaticlibe.helper.SoundHelper;

public class LegacyUI implements ModInitializer {
    static {
        //this is here to possibly fix some class loading issues, do not delete
        try {
            Class.forName("net.minecraft.core.block.Block");
            Class.forName("net.minecraft.core.item.Item");
        } catch (ClassNotFoundException ignored) {}
    }
    public static final String MOD_ID = "legacyui";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    @Override
    public void onInitialize() {
        SoundHelper.addSound(MOD_ID, "ui/back.wav");
        SoundHelper.addSound(MOD_ID, "ui/craft.wav");
        SoundHelper.addSound(MOD_ID, "ui/craftfail.wav");
        SoundHelper.addSound(MOD_ID, "ui/focus.wav");
        SoundHelper.addSound(MOD_ID, "ui/press.wav");
        SoundHelper.addSound(MOD_ID, "ui/scroll.wav");
        SoundHelper.addSound(MOD_ID, "ui/achievement.wav");
        LegacyCategoryManager.register();
        LOGGER.info("LegacyUI initialized.");
    }
}
