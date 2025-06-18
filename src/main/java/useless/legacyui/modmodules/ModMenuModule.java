package useless.legacyui.modmodules;

import io.github.prospector.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.GuiScreen;
import useless.legacyui.gui.screens.options.GuiOptionsPageLegacy;
import useless.legacyui.LegacyUI;

import java.util.function.Function;

public class ModMenuModule implements ModMenuApi {
    @Override
    public String getModId() {
        return LegacyUI.MOD_ID;
    }

    @Override
    public Function<GuiScreen, ? extends GuiScreen> getConfigScreenFactory() {
        return (screenBase -> GuiOptionsPageLegacy.legacyOptionsScreen(screenBase));
    }
}
