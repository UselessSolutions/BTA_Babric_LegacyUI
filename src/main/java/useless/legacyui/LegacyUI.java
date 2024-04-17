package useless.legacyui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.helper.SoundHelper;
import turniplabs.halplibe.util.ClientStartEntrypoint;
import useless.legacyui.Helper.IconHelper;
import useless.legacyui.Settings.ILegacyOptions;

public class LegacyUI implements ClientStartEntrypoint {
    public static final String MOD_ID = "legacyui";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static ILegacyOptions modSettings;

    @Override
    public void beforeClientStart() {
        SoundHelper.Client.addSound(MOD_ID, "ui/back.wav");
        SoundHelper.Client.addSound(MOD_ID, "ui/craft.wav");
        SoundHelper.Client.addSound(MOD_ID, "ui/craftfail.wav");
        SoundHelper.Client.addSound(MOD_ID, "ui/focus.wav");
        SoundHelper.Client.addSound(MOD_ID, "ui/press.wav");
        SoundHelper.Client.addSound(MOD_ID, "ui/scroll.wav");
        SoundHelper.Client.addSound(MOD_ID, "ui/achievement.wav");
        String[] iconTextures = new String[]{"armor.png", "bricks.png", "grass.png", "health.png", "lever.png", "modded.png", "painting.png", "planks.png", "rail.png", "redstonerail.png", "tools.png"};
        for (String texture: iconTextures) {
            IconHelper.getOrCreateIconTexture(MOD_ID, texture);
        }
        LOGGER.info("LegacyUI initialized.");
    }

    @Override
    public void afterClientStart() {

    }

}
