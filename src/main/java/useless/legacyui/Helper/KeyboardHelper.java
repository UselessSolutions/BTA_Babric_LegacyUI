package useless.legacyui.Helper;

import net.minecraft.client.option.KeyBinding;
import org.lwjgl.input.Keyboard;

import java.util.BitSet;

public class KeyboardHelper {
    private static final int keysSize = 256;
    private static final BitSet keysPressed = new BitSet(keysSize);
    private static final int[] holdTimes = new int[keysSize];
    private static final int[] repeatSuccessTimes = new int[keysSize];
    private static long prevUpdateTime = -1;
    public static boolean isKeyPressedThisFrame(int keyCode) {
        return keysPressed.get(keyCode) && holdTimes[keyCode] == 0;
    }

    public static boolean isKeyHeld(int keyCode){
        return keysPressed.get(keyCode);
    }
    public static int getKeyHoldTime(int keycode){
        return holdTimes[keycode];
    }
    public static boolean repeatInput(int keycode, int repeatDelay, int initialDelay){
        if (isKeyPressedThisFrame(keycode)){
            return true;
        }
        if (getKeyHoldTime(keycode) < initialDelay) return false;
        if (getKeyHoldTime(keycode) - initialDelay > repeatDelay * (repeatSuccessTimes[keycode] + 1)){
            repeatSuccessTimes[keycode]++;
            return true;
        }
        return false;
    }
    public static void resetKey(int keycode){
        repeatSuccessTimes[keycode] = 0;
        holdTimes[keycode] = 0;
        keysPressed.set(keycode, false);
    }
    public static void update(){
        if (prevUpdateTime == -1){
            prevUpdateTime = System.currentTimeMillis();
        }
        long deltaTime = System.currentTimeMillis() - prevUpdateTime;
        prevUpdateTime = System.currentTimeMillis();
        for (int i = 0; i < keysSize; i++) {
            if (keysPressed.get(i)){
                holdTimes[i] += deltaTime;
            } else {
                holdTimes[i] = 0;
                repeatSuccessTimes[i] = 0; // Reset success counter
            }
            keysPressed.set(i, Keyboard.isKeyDown(i));
        }
    }
}
