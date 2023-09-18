package useless.legacyui.ModModules;

import io.github.prospector.modmenu.api.ModMenuApi;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.options.GuiOptionsPageGeneral;
import useless.legacyui.LegacyUI;

import java.util.function.Function;

public class ModMenuModule implements ModMenuApi {
    @Override
    public String getModId() {
        return LegacyUI.MOD_ID;
    }

    @Override
    public Function<GuiScreen, ? extends GuiScreen> getConfigScreenFactory() {
        return (screenBase -> new GuiOptionsPageGeneral(screenBase, ((Minecraft) FabricLoader.getInstance().getGameInstance()).gameSettings));
    }
}
