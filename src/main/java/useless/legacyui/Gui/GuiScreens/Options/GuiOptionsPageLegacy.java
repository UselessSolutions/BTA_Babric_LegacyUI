package useless.legacyui.Gui.GuiScreens.Options;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.options.GuiOptions;
import net.minecraft.client.gui.options.components.BooleanOptionComponent;
import net.minecraft.client.gui.options.components.FloatOptionComponent;
import net.minecraft.client.gui.options.components.OptionsCategory;
import net.minecraft.client.gui.options.components.ToggleableOptionComponent;
import net.minecraft.client.gui.options.data.OptionsPage;
import net.minecraft.client.gui.options.data.OptionsPages;
import net.minecraft.client.option.GameSettings;
import useless.legacyui.Settings.ILegacyOptions;

public class GuiOptionsPageLegacy {
    public static GameSettings gameSettings = ((Minecraft) FabricLoader.getInstance().getGameInstance()).gameSettings;
    public static ILegacyOptions legacyOptions = (ILegacyOptions) gameSettings;
    public static final OptionsPage LegacyUIPage = OptionsPages.register(new OptionsPage("legacyui.options.title")
            .withComponent(
                    new OptionsCategory("legacyui.options.gameplay")
                            .withComponent(new BooleanOptionComponent(legacyOptions.getCoordsOnMaps()))
                            .withComponent(new BooleanOptionComponent(legacyOptions.getEnableAutoBridge())))
            .withComponent(new OptionsCategory("legacyui.options.hud")
                    .withComponent(new BooleanOptionComponent(legacyOptions.getHideHotbarInGUIs()))
                    .withComponent(new BooleanOptionComponent(legacyOptions.getEnablePaperDoll()))
                    .withComponent(new BooleanOptionComponent(legacyOptions.getEnableHUDFadeout()))
                    .withComponent(new FloatOptionComponent(legacyOptions.getHUDFadeoutDelay()))
                    .withComponent(new FloatOptionComponent(legacyOptions.getHUDFadeoutAlpha())))
            .withComponent(new OptionsCategory("legacyui.options.gui")
                    .withComponent(new BooleanOptionComponent(legacyOptions.getEnableLegacyInventorySurvival()))
                    .withComponent(new BooleanOptionComponent(legacyOptions.getEnableLegacyCrafting()))
                    .withComponent(new BooleanOptionComponent(legacyOptions.getEnableLegacyInventoryCreative()))
                    .withComponent(new BooleanOptionComponent(legacyOptions.getEnableLegacyFlag()))
                    .withComponent(new ToggleableOptionComponent<>(legacyOptions.getGuiControllerType()))
                    .withComponent(new BooleanOptionComponent(legacyOptions.getShowCraftingItemNamePreview()))
                    .withComponent(new BooleanOptionComponent(legacyOptions.getCraftingHideUndiscoveredItems()))
                    .withComponent(new BooleanOptionComponent(legacyOptions.getForceButtonPrompts()))
                    .withComponent(new BooleanOptionComponent(legacyOptions.getForceLegacyTooltip())))
            .withComponent(new OptionsCategory("legacyui.options.panorama")
                    .withComponent(new BooleanOptionComponent(legacyOptions.getEnablePanorama()))
                    .withComponent(new ToggleableOptionComponent<>(legacyOptions.getPanoramaScrollLength()))
                    .withComponent(new FloatOptionComponent(legacyOptions.getMainMenuBrightness())))
            .withComponent(new OptionsCategory("legacyui.options.sound")
                    .withComponent(new BooleanOptionComponent(legacyOptions.getUseLegacySounds()))
                    .withComponent(new BooleanOptionComponent(legacyOptions.getUseRandomPitch()))));
    public static GuiOptions legacyOptionsScreen(GuiScreen parent){
        return new GuiOptions(parent, gameSettings, LegacyUIPage);
    }
}
