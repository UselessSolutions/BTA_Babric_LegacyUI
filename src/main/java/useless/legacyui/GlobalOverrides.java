package useless.legacyui;


import net.minecraft.client.gui.GuiScreen;

public class GlobalOverrides {
    private static boolean backOverride = false;
    private static int deleteSlots = 0;
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

    public static boolean overrideCrafting(){
        return deleteSlots-- > 0;
    }
    public static void armOverrideCrafting(){
        deleteSlots = 5;
    }
}
