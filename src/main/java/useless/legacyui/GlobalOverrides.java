package useless.legacyui;

public class GlobalOverrides {
    public static int iconTileWidth = 32;
    public static int imagesTilesLength = 16;
    private static boolean backOverride = false;
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
