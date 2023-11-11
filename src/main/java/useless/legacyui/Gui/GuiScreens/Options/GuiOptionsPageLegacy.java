package useless.legacyui.Gui.GuiScreens.Options;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.options.GuiOptionsPageOptionBase;
import net.minecraft.client.option.GameSettings;
import useless.legacyui.Settings.ILegacyOptions;

public class GuiOptionsPageLegacy extends GuiOptionsPageOptionBase {
    public GuiOptionsPageLegacy(GuiScreen parent, GameSettings settings) {
        super(parent, settings);
        ILegacyOptions legacyOptions = (ILegacyOptions)settings;
        this.addOptionsCategory("legacyui.options.gameplay",
                legacyOptions.getEnableAutoBridge());
        this.addOptionsCategory("legacyui.options.gui",
                legacyOptions.getEnableLegacyInventorySurvival(),
                legacyOptions.getEnableLegacyCrafting(),
                legacyOptions.getEnableLegacyInventoryCreative(),
                legacyOptions.getEnableLegacyFlag(),
                legacyOptions.getHideHotbarInGUIs(),
                legacyOptions.getGuiControllerType(),
                legacyOptions.getShowCraftingItemNamePreview(),
                legacyOptions.getCraftingHideUndiscoveredItems(),
                legacyOptions.getCoordsOnMaps(),
                legacyOptions.getForceButtonPrompts(),
                legacyOptions.getForceLegacyTooltip(),
                legacyOptions.getEnablePaperDoll());
        this.addOptionsCategory("legacyui.options.panorama",
                legacyOptions.getEnablePanorama(),
                legacyOptions.getPanoramaScrollLength(),
                legacyOptions.getMainMenuBrightness());
        this.addOptionsCategory("legacyui.options.sound",
                legacyOptions.getUseLegacySounds(),
                legacyOptions.getUseRandomPitch());
    }
}
