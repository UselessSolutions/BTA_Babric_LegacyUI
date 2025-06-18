package useless.legacyui.settings;

import net.minecraft.client.option.*;
import useless.legacyui.gui.screens.options.ControllerType;

public interface ILegacyOptions {
    OptionBoolean legacyui$getCraftingHideUndiscoveredItems();
    OptionBoolean legacyui$getUseLegacySounds();
    OptionBoolean legacyui$getHideHotbarInGUIs();
    OptionBoolean legacyui$getEnableLegacyCrafting();
    OptionBoolean legacyui$getEnableLegacyInventorySurvival();
    OptionBoolean legacyui$getEnableLegacyInventoryCreative();
    OptionBoolean legacyui$getEnableLegacyFlag();
    OptionBoolean legacyui$getShowCraftingItemNamePreview();
    OptionBoolean legacyui$getUseRandomPitch();
    OptionColor legacyui$getGuiPromptColor();
    OptionColor legacyui$getHighlightColor();
    OptionColor legacyui$getGuiBackgroundColor();
    OptionEnum<ControllerType> legacyui$getGuiControllerType();
    OptionBoolean legacyui$getEnablePanorama();
    OptionRange legacyui$getPanoramaScrollLength();
    OptionFloat legacyui$getMainMenuBrightness();
    OptionBoolean legacyui$getCoordsOnMaps();
    OptionBoolean legacyui$getForceButtonPrompts();
    OptionBoolean legacyui$getEnableAutoBridge();
    OptionBoolean legacyui$getForceLegacyTooltip();
    OptionBoolean legacyui$getEnablePaperDoll();
    OptionBoolean legacyui$getEnableHUDFadeout();
    OptionFloat legacyui$getHUDFadeoutDelay();
    OptionFloat legacyui$getHUDFadeoutAlpha();
}
