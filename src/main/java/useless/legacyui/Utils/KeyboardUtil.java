package useless.legacyui.Utils;

import org.lwjgl.input.Keyboard;

public class KeyboardUtil {
    private static final boolean[] keysPressed = new boolean[65536];
    public static boolean isKeyPressed(int keyCode) {
        if (Keyboard.isKeyDown(keyCode)) {
            if (keysPressed[keyCode]) {
                return false;
            } else {
                keysPressed[keyCode] = true;
                return true;
            }
        } else {
            keysPressed[keyCode] = false;
            return false;
        }
    }
}
