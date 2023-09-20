package useless.legacyui.Settings;

import net.minecraft.client.Minecraft;
import turniplabs.halplibe.util.ConfigHandler;
import useless.legacyui.LegacyUI;
import useless.legacyui.ModModules.GuiModModule;
import useless.prismaticlibe.helper.ModCheckHelper;

import java.util.Properties;

public class ModSettings {
    public static final ConfigHandler config;
    public static final ILegacyOptions legacyOptions = (ILegacyOptions) Minecraft.getMinecraft(Minecraft.class).gameSettings;
    static {
        Properties props = new Properties();
        props.setProperty("GuiLabelColor", "404040");
        props.setProperty("GuiPromptColor", "FFFFFF");
        props.setProperty("HighlightColor", "FF0000");
        props.setProperty("GuiBackgroundColor", "90101010");
        config = new ConfigHandler(LegacyUI.MOD_ID, props);
    }
    public static class Colors {
        private static final int guiLabelColor;
        static {
            if (ModCheckHelper.checkForMod("guimod", ">=2.0.0")){
                guiLabelColor = GuiModModule.getColorFromMod();
            } else {
                guiLabelColor = Integer.decode("0X" + config.getString("GuiLabelColor"));
            }
        }
        private static final int highlightColor = Integer.decode("0X" + config.getString("HighlightColor"));
        private static final int guiBackgroundColor = ((Integer.decode("0X" + config.getString("GuiBackgroundColor").substring(0,2)) << 24) + Integer.decode("0X" + config.getString("GuiBackgroundColor").substring(2)));
        private static final int guiPromptColor = Integer.decode("0X" + config.getString("GuiPromptColor"));
        public static int GuiLabelColor(){
            return guiLabelColor;
        }
        public static int HighlightColor(){
            return highlightColor;
        }
        public static int GuiBackgroundColor(){
            return guiBackgroundColor;
        }
        public static int GuiPromptColor(){
            return guiPromptColor;
        }
    }
}
