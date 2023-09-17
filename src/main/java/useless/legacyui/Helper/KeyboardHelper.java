package useless.legacyui.Helper;

import org.lwjgl.input.Keyboard;

import java.util.BitSet;

public class KeyboardHelper {
    private static final BitSet keysPressed = new BitSet(65536);
    public static boolean isKeyPressedThisFrame(int keyCode) {
        if (Keyboard.isKeyDown(keyCode)) { // Key held down
            if (keysPressed.get(keyCode)) { // Key was already held down
                return false;
            } else { // Key Wasn't already held down
                keysPressed.set(keyCode, true);
                return true;
            }
        } else { // Key not held
            keysPressed.set(keyCode, false);
            return false;
        }
    }

    public static boolean isKeyHeld(int keyCode){
        if (Keyboard.isKeyDown(keyCode)) { // Key held down
            if (!keysPressed.get(keyCode)) { // Key Wasn't already held down
                keysPressed.set(keyCode, true);
            }
        } else { // Key not held
            keysPressed.set(keyCode, false);
        }
        return keysPressed.get(keyCode);
    }

}
