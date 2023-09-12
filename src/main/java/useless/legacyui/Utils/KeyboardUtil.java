package useless.legacyui.Utils;

import org.lwjgl.input.Keyboard;

public class KeyboardUtil {
    private static final boolean[] keysPressed = new boolean[65536];
    public static boolean isKeyPressed(int keyCode) {
        if (Keyboard.isKeyDown(keyCode)) { // Key held down
            if (keysPressed[keyCode]) { // Key was already held down
                return false;
            } else { // Key Wasn't already held down
                keysPressed[keyCode] = true;
                return true;
            }
        } else { // Key not held
            keysPressed[keyCode] = false;
            return false;
        }
    }

    public static boolean isKeyHeld(int keyCode){
        if (Keyboard.isKeyDown(keyCode)) { // Key held down
            if (!keysPressed[keyCode]) { // Key Wasn't already held down
                keysPressed[keyCode] = true;
            }
        } else { // Key not held
            keysPressed[keyCode] = false;
        }
        return keysPressed[keyCode];
    }

}
