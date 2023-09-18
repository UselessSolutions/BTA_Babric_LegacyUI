package useless.legacyui.Gui.GuiScreens;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.options.GuiOptionsPageOptionBase;
import net.minecraft.client.option.GameSettings;
import useless.legacyui.Settings.ILegacyOptions;

public class GuiOptionsPageLegacy extends GuiOptionsPageOptionBase {
    public GuiOptionsPageLegacy(GuiScreen parent, GameSettings settings) {
        super(parent, settings);
        ILegacyOptions legacyOptions = (ILegacyOptions)settings;
        this.addOptionsCategory("legacyui.options",
                legacyOptions.getEnableLegacyInventorySurvival(),
                legacyOptions.getEnableLegacyCrafting(),
                legacyOptions.getEnableLegacyInventoryCreative(),
                legacyOptions.getUseLegacySounds(),
                legacyOptions.getHideHotbarInGUIs(),
                legacyOptions.getUseRandomPitch(),
                legacyOptions.getShowCraftingItemNamePreview(),
                legacyOptions.getGuiControllerType(),
                legacyOptions.getCraftingHideUndiscoveredItems(),
                legacyOptions.getOverrideLabelModColor(),
                legacyOptions.getGuiPromptColor(),
                legacyOptions.getGuiBackgroundColor(),
                legacyOptions.getGuiLabelColor(),
                legacyOptions.getHighlightColor());
    }
}
