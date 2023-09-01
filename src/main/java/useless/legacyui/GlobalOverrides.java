package useless.legacyui;

import net.minecraft.client.gui.GuiScreen;

public class GlobalOverrides {
    private static boolean backOverride = false;
    public static GuiScreen currentGuiScreen = null;
    public static boolean getBackOverride(){
        if (backOverride){
            backOverride = false;
            return true;
        }
        return false;
    }
    public static void armBackOverride(){
        backOverride = true;
    }
}
