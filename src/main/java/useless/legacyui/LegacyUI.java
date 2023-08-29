package useless.legacyui;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.util.ConfigHandler;
import useless.config.ModMenuConfigManager;

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

    private static boolean guimodExists = false;

    @Override
    public void onInitialize() {
        for (ModContainer mod : FabricLoader.getInstance().getAllMods()){
            if (mod.toString().contains("guimod")){
                LegacyUI.LOGGER.info("Found Mod: " + mod);
                guimodExists = true;
            }
        }
        SoundHelper soundHelper = SoundHelper.getInstance();
        soundHelper.addSound(MOD_ID, "ui/back.wav");
        soundHelper.addSound(MOD_ID, "ui/craft.wav");
        soundHelper.addSound(MOD_ID, "ui/craftfail.wav");
        soundHelper.addSound(MOD_ID, "ui/focus.wav");
        soundHelper.addSound(MOD_ID, "ui/press.wav");
        soundHelper.addSound(MOD_ID, "ui/scroll.wav");
        LOGGER.info("LegacyUI initialized.");
    }
    public static int getGuiLabelColor(){
        if (GuiLabelColor == -1){
            if (guimodExists && !config.getBoolean(ConfigTranslations.OVERRIDE_LABEL_COLOR.getKey())){
                try {
                    GuiLabelColor = ModMenuConfigManager.getConfig().getLabelColor();
                }
                catch (NoClassDefFoundError error){
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
