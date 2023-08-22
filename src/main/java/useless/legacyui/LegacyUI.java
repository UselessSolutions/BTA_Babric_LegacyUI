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
        config = new ConfigHandler(MOD_ID, props);
    }
    public static int GuiLabelColor = 0x404040;

    @Override
    public void onInitialize() {
        for (ModContainer mod : FabricLoader.getInstance().getAllMods()){
            if (mod.toString().contains("guimod")){
                GuiLabelColor = ModMenuConfigManager.getConfig().getLabelColor();
            }
        }
        LOGGER.info("LegacyUI initialized.");
    }
}
