package useless.legacyui.Settings;

import net.minecraft.client.option.*;
import useless.legacyui.Gui.GuiScreens.Options.ControllerType;

public interface ILegacyOptions {
    BooleanOption getCraftingHideUndiscoveredItems();
    BooleanOption getOverrideLabelModColor();
    BooleanOption getUseLegacySounds();
    BooleanOption getHideHotbarInGUIs();
    BooleanOption getEnableLegacyCrafting();
    BooleanOption getEnableLegacyInventorySurvival();
    BooleanOption getEnableLegacyInventoryCreative();
    BooleanOption getEnableLegacyFlag();
    BooleanOption getShowCraftingItemNamePreview();
    BooleanOption getUseRandomPitch();
    ColorOption getGuiLabelColor();
    ColorOption getGuiPromptColor();
    ColorOption getHighlightColor();
    ColorOption getGuiBackgroundColor();
    EnumOption<ControllerType> getGuiControllerType();
    BooleanOption getEnablePanorama();
    BooleanOption getReplaceStandardBackground();
    RangeOption getPanoramaScrollLength();
    FloatOption getMainMenuBrightness();
    BooleanOption getCoordsOnMaps();
}
