package useless.legacyui.Helper;

import java.util.HashMap;

public class RepeatInputHandler {
    public static HashMap<Integer, Long> timeMap = new HashMap<>();
    public static boolean doRepeatInput(int code, int delay){
        long delta = System.currentTimeMillis() - timeMap.getOrDefault(code, 0L);
        if (delta >= delay){
            manualSuccess(code);
            return true;
        }
        return false;
    }
    public static void manualSuccess(int code){
        timeMap.put(code, System.currentTimeMillis());
    }
}
