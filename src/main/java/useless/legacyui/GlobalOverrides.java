package useless.legacyui;

public class GlobalOverrides {
    public static int offset = 0;
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
