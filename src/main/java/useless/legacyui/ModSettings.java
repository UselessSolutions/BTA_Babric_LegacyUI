package useless.legacyui;

import turniplabs.halplibe.util.ConfigHandler;
import useless.legacyui.ModModules.GuiModModule;
import useless.prismaticlibe.helper.ModCheckHelper;

import java.util.Properties;

public class ModSettings {
    public static final ConfigHandler config;
    static {
        Properties props = new Properties();
        props.setProperty("CraftingHideUndiscoveredItems","true"); //
        props.setProperty("ExperimentalQuickStackFix", "false"); //
        props.setProperty("ExperimentalQuickStackFixDelay", "50"); //
        props.setProperty("GuiLabelColor", "404040");
        props.setProperty("GuiPromptColor", "FFFFFF");//
        props.setProperty("HighlightColor", "FF0000"); //
        props.setProperty("GuiBackgroundColor", "90101010"); //
        props.setProperty("OverrideLabelModColor", "false"); //
        props.setProperty("UseLegacySounds", "true"); //
        props.setProperty("HideHotbarInGUIs", "true"); //
        props.setProperty("EnableLegacyCrafting", "true");
        props.setProperty("EnableLegacyInventorySurvival", "true");
        props.setProperty("EnableLegacyInventoryCreative", "true");
        props.setProperty("ShowCraftingItemNamePreview", "true");
        props.setProperty("UseRandomPitch", "false");
        props.setProperty("GuiControllerType", "4");
        config = new ConfigHandler(LegacyUI.MOD_ID, props);
    }
    public static class Gui {
        private static final boolean hideUndiscoveredItems = config.getBoolean("CraftingHideUndiscoveredItems");
        private static final boolean hideHotbarInGUIs = config.getBoolean("HideHotbarInGUIs");
        private static final boolean experimentalQuickStackFix = config.getBoolean("ExperimentalQuickStackFix");
        private static final int experimentalQuickStackFixDelay = config.getInt("ExperimentalQuickStackFixDelay");
        private static final boolean enableLegacyCrafting = config.getBoolean("EnableLegacyCrafting");
        private static final boolean enableLegacyInventorySurvival = config.getBoolean("EnableLegacyInventorySurvival");
        private static final boolean enableLegacyInventoryCreative = config.getBoolean("EnableLegacyInventoryCreative");
        private static final boolean showCraftingItemNamePreview = config.getBoolean("ShowCraftingItemNamePreview");
        private static final int guiControllerType = Math.max(Math.min(config.getInt("GuiControllerType"),16), 0);
        public static boolean HideUndiscoveredItems(){
            return hideUndiscoveredItems;
        }
        public static boolean HideHotbarInGUIs(){
            return hideHotbarInGUIs;
        }
        public static boolean ExperimentalQuickStackFix(){return experimentalQuickStackFix;}
        public static int ExperimentalQuickStackFixDelay(){return experimentalQuickStackFixDelay;}
        public static boolean EnableLegacyCrafting(){
            return enableLegacyCrafting;
        }
        public static boolean EnableLegacyInventorySurvival(){
            return enableLegacyInventorySurvival;
        }
        public static boolean EnableLegacyInventoryCreative(){
            return enableLegacyInventoryCreative;
        }
        public static boolean ShowCraftingItemNamePreview(){
            return showCraftingItemNamePreview;
        }
        public static int GuiControllerType() {return guiControllerType;}
    }
    public static class Colors {
        private static final boolean overrideLabelModColor = config.getBoolean("OverrideLabelModColor");
        private static final int guiLabelColor;
        static {
            if (ModCheckHelper.checkForMod("guimod", ">=2.0.0") && !overrideLabelModColor){
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
    public static class Sounds {
        private static final boolean useLegacySounds = config.getBoolean("UseLegacySounds");
        private static final boolean useRandomPitch = config.getBoolean("UseRandomPitch");
        public static boolean UseLegacySounds(){
            return useLegacySounds;
        }
        public static boolean UseRandomPitch() {return  useRandomPitch;}
    }
}
