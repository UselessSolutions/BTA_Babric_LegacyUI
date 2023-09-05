package useless.legacyui;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.util.ConfigHandler;
import useless.prismaticlibe.helper.ModCheckHelper;
import useless.prismaticlibe.helper.SoundHelper;

import java.util.Properties;


public class LegacyUI implements ModInitializer {
    public static final String MOD_ID = "legacyui";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final ConfigHandler config;
    static {
        Properties props = new Properties();
        props.setProperty("CraftingHideUndiscoveredItems","true");
        props.setProperty("ExperimentalQuickStackFix", "false");
        props.setProperty("ExperimentalQuickStackFixDelay", "50");
        props.setProperty("GuiLabelColor", "404040");
        props.setProperty("HighlightColor", "FF0000");
        props.setProperty("OverrideLabelModColor", "false");
        props.setProperty("UseLegacySounds", "true");
        config = new ConfigHandler(MOD_ID, props);
    }
    private static int GuiLabelColor = -1;
    private static final int HighlightColor = Integer.decode("0X" + config.getString(ConfigTranslations.HIGHLIGHT_COLOR.getKey()));

    private static final boolean guimodExists = ModCheckHelper.checkForMod("guimod", ">=2.0.0");

    @Override
    public void onInitialize() {
        SoundHelper.addSound(MOD_ID, "ui/back.wav");
        SoundHelper.addSound(MOD_ID, "ui/craft.wav");
        SoundHelper.addSound(MOD_ID, "ui/craftfail.wav");
        SoundHelper.addSound(MOD_ID, "ui/focus.wav");
        SoundHelper.addSound(MOD_ID, "ui/press.wav");
        SoundHelper.addSound(MOD_ID, "ui/scroll.wav");
        SoundHelper.addSound(MOD_ID, "ui/achievement.wav");

        LOGGER.info("LegacyUI initialized.");
    }
    public static int getGuiLabelColor(){
        if (GuiLabelColor == -1){
            if (guimodExists && !config.getBoolean(ConfigTranslations.OVERRIDE_LABEL_COLOR.getKey())){
                try {
                    GuiLabelColor = ModuleGuiLabels.getColorFromMod();
                }
                catch (Exception error){
                    GuiLabelColor = Integer.decode("0X" + config.getString(ConfigTranslations.GUI_LABEL_COLOR.getKey()));
                }
            }
            else {
                GuiLabelColor = Integer.decode("0X" + config.getString(ConfigTranslations.GUI_LABEL_COLOR.getKey()));
            }
        }
        return GuiLabelColor;
    }

    public static int getHighlightColor(){
        return HighlightColor;
    }

}
