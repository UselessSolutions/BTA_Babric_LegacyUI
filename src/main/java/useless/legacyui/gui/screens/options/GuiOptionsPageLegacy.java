package useless.legacyui.gui.screens.options;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.options.ScreenOptions;
import net.minecraft.client.gui.options.components.BooleanOptionComponent;
import net.minecraft.client.gui.options.components.FloatOptionComponent;
import net.minecraft.client.gui.options.components.OptionsCategory;
import net.minecraft.client.gui.options.components.ToggleableOptionComponent;
import net.minecraft.client.gui.options.data.OptionsPage;
import net.minecraft.client.gui.options.data.OptionsPages;
import net.minecraft.client.option.GameSettings;
import net.minecraft.core.item.Items;
import turniplabs.halplibe.util.ClientStartEntrypoint;
import useless.legacyui.settings.ILegacyOptions;

public class GuiOptionsPageLegacy implements ClientStartEntrypoint {
    public static final GameSettings gameSettings = Minecraft.getMinecraft().gameSettings;
    public static final ILegacyOptions legacyOptions = (ILegacyOptions) gameSettings;
    public static final OptionsPage LegacyUIPage = OptionsPages.register(new OptionsPage("legacyui.options.title", Items.MAP.getDefaultStack())
            .withComponent(
                    new OptionsCategory("legacyui.options.gameplay")
                            .withComponent(new BooleanOptionComponent(legacyOptions.legacyui$getCoordsOnMaps())))
            .withComponent(new OptionsCategory("legacyui.options.hud")
                    .withComponent(new BooleanOptionComponent(legacyOptions.legacyui$getHideHotbarInGUIs()))
                    .withComponent(new BooleanOptionComponent(legacyOptions.legacyui$getEnablePaperDoll()))
                    .withComponent(new BooleanOptionComponent(legacyOptions.legacyui$getEnableHUDFadeout()))
                    .withComponent(new FloatOptionComponent(legacyOptions.legacyui$getHUDFadeoutDelay()))
                    .withComponent(new FloatOptionComponent(legacyOptions.legacyui$getHUDFadeoutAlpha())))
            .withComponent(new OptionsCategory("legacyui.options.gui")
                    .withComponent(new BooleanOptionComponent(legacyOptions.legacyui$getEnableLegacyInventorySurvival()))
                    .withComponent(new BooleanOptionComponent(legacyOptions.legacyui$getEnableLegacyCrafting()))
                    .withComponent(new BooleanOptionComponent(legacyOptions.legacyui$getEnableLegacyInventoryCreative()))
                    .withComponent(new BooleanOptionComponent(legacyOptions.legacyui$getEnableLegacyFlag()))
                    .withComponent(new ToggleableOptionComponent<>(legacyOptions.legacyui$getGuiControllerType()))
                    .withComponent(new BooleanOptionComponent(legacyOptions.legacyui$getShowCraftingItemNamePreview()))
                    .withComponent(new BooleanOptionComponent(legacyOptions.legacyui$getCraftingHideUndiscoveredItems()))
                    .withComponent(new BooleanOptionComponent(legacyOptions.legacyui$getForceButtonPrompts()))
                    .withComponent(new BooleanOptionComponent(legacyOptions.legacyui$getForceLegacyTooltip())))
            .withComponent(new OptionsCategory("legacyui.options.panorama")
                    .withComponent(new BooleanOptionComponent(legacyOptions.legacyui$getEnablePanorama()))
                    .withComponent(new ToggleableOptionComponent<>(legacyOptions.legacyui$getPanoramaScrollLength()))
                    .withComponent(new FloatOptionComponent(legacyOptions.legacyui$getMainMenuBrightness())))
            .withComponent(new OptionsCategory("legacyui.options.sound")
                    .withComponent(new BooleanOptionComponent(legacyOptions.legacyui$getUseLegacySounds()))
                    .withComponent(new BooleanOptionComponent(legacyOptions.legacyui$getUseRandomPitch()))));
    public static ScreenOptions legacyOptionsScreen(final Screen parent){
        return new ScreenOptions(parent, LegacyUIPage);
    }

    @Override
    public void beforeClientStart() {

    }

    @Override
    public void afterClientStart() {

    }
}
