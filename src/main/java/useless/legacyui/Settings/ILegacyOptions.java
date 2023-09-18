package useless.legacyui.Settings;

import net.minecraft.client.option.BooleanOption;
import net.minecraft.client.option.IntegerOption;
import net.minecraft.client.option.RangeOption;
import net.minecraft.client.option.StringOption;

public interface ILegacyOptions {
    BooleanOption getCraftingHideUndiscoveredItems();
    BooleanOption getOverrideLabelModColor();
    BooleanOption getUseLegacySounds();
    BooleanOption getHideHotbarInGUIs();
    BooleanOption getEnableLegacyCrafting();
    BooleanOption getEnableLegacyInventorySurvival();
    BooleanOption getEnableLegacyInventoryCreative();
    BooleanOption getShowCraftingItemNamePreview();
    BooleanOption getUseRandomPitch();
    IntegerOption getGuiLabelColor();
    IntegerOption getGuiPromptColor();
    IntegerOption getHighlightColor();
    IntegerOption getGuiBackgroundColor();
    RangeOption getGuiControllerType();
}
