package useless.legacyui.Gui.GuiScreens.Options;

import net.minecraft.core.util.helper.ITranslatable;

public enum ControllerType implements ITranslatable {
    GENERIC(0),
    DUAL_SENSE(1),
    DUAL_SHOCK_4(2),
    DUAL_SHOCK_3(3),
    XBOX_ONE(4),
    XBOX_360_BK(5),
    XBOX_360_WT(6),
    XBOX_SLIM(7),
    XBOX_DUKE(8),
    JOYCON_1(9),
    JOYCON_2(10),
    JOYCON_3(11),
    JOYCON_4(12),
    GAMECUBE(13),
    STEAM_CONTROLLER(14),
    STEAM_DECK(15);
    private int index;

    private ControllerType(int index) {
        this.index = index;
    }

    public int index() {
        return this.index;
    }

    public String getTranslationKey() {
        return this.name().toLowerCase();
    }
}
