package useless.legacyui.Gui.GuiScreens.Options;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.options.GuiOptionsPageOptionBase;
import net.minecraft.client.option.GameSettings;
import useless.legacyui.Settings.ILegacyOptions;

public class GuiOptionsPageLegacy extends GuiOptionsPageOptionBase {
    public GuiOptionsPageLegacy(GuiScreen parent, GameSettings settings) {
        super(parent, settings);
        ILegacyOptions legacyOptions = (ILegacyOptions)settings;
        this.addOptionsCategory("legacyui.options.gui",
                legacyOptions.getEnableLegacyInventorySurvival(),
                legacyOptions.getEnableLegacyCrafting(),
                legacyOptions.getEnableLegacyInventoryCreative(),
                legacyOptions.getHideHotbarInGUIs(),
                legacyOptions.getGuiControllerType(),
                legacyOptions.getShowCraftingItemNamePreview(),
                legacyOptions.getCraftingHideUndiscoveredItems());
        this.addOptionsCategory("legacyui.options.sound",
                legacyOptions.getUseLegacySounds(),
                legacyOptions.getUseRandomPitch());
    }
}
