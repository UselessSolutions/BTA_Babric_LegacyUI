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
                legacyOptions.getCoordsOnMaps(),
                legacyOptions.getEnableAutoBridge());
        this.addOptionsCategory("legacyui.options.hud",
                legacyOptions.getHideHotbarInGUIs(),
                legacyOptions.getEnablePaperDoll(),
                legacyOptions.getEnableHUDFadeout(),
                legacyOptions.getHUDFadeoutDelay(),
                legacyOptions.getHUDFadeoutAlpha());
        this.addOptionsCategory("legacyui.options.gui",
                legacyOptions.getEnableLegacyInventorySurvival(),
                legacyOptions.getEnableLegacyCrafting(),
                legacyOptions.getEnableLegacyInventoryCreative(),
                legacyOptions.getEnableLegacyFlag(),
                legacyOptions.getGuiControllerType(),
                legacyOptions.getShowCraftingItemNamePreview(),
                legacyOptions.getCraftingHideUndiscoveredItems(),
                legacyOptions.getForceButtonPrompts(),
                legacyOptions.getForceLegacyTooltip());
        this.addOptionsCategory("legacyui.options.panorama",
                legacyOptions.getEnablePanorama(),
                legacyOptions.getPanoramaScrollLength(),
                legacyOptions.getMainMenuBrightness());
        this.addOptionsCategory("legacyui.options.sound",
                legacyOptions.getUseLegacySounds(),
                legacyOptions.getUseRandomPitch());
    }
}
