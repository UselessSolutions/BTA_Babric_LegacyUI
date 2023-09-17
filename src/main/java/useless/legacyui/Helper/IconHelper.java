package useless.legacyui.Helper;

import turniplabs.halplibe.helper.TextureHelper;
import turniplabs.halplibe.util.TextureHandler;
import useless.legacyui.LegacyUI;

import java.util.HashMap;
import java.util.Map;

public class IconHelper {
    public static Map<String, int[]> registeredIconTextures = new HashMap<>();
    public static int ICON_ATLAS_WIDTH_TILES = 16;
    public static int ICON_RESOLUTION = 32;
    /**
     * Place mod textures in the <i>assets/mod_id/icon/</i> directory for them to be seen.
     */
    public static int[] getOrCreateIconTexture(String modId, String iconTexture) {
        int[] possibleCoords = registeredIconTextures.get(modId + ":" + iconTexture);
        if (possibleCoords != null) {
            return possibleCoords;
        }

        int[] newCoords = IconCoords.nextCoords();
        registeredIconTextures.put(modId + ":" + iconTexture, newCoords);
        addTextureToIcons(modId, iconTexture, newCoords[0], newCoords[1]);
        return newCoords;
    }
    public static void addTextureToIcons(String modId, String iconTexture, int x, int y) {
        TextureHelper.textureHandlers.add(new TextureHandler("/assets/legacyui/gui/icons.png", "/assets/" + modId + "/icon/" + iconTexture, texCoordToIndex(x, y), ICON_RESOLUTION, 1));
    }
    public static int texCoordToIndex(int x, int y) {
        return x + y * ICON_ATLAS_WIDTH_TILES;
    }
    public static class IconCoords {
        public static int lastX = 1;
        public static int lastY = 0;
        public static boolean outOfSpace = false;
        public static int[] nextCoords(){
            if (!outOfSpace){
                int x = lastX;
                int y = lastY;
                if (++lastX > ICON_ATLAS_WIDTH_TILES-1) {
                    lastX = 0;
                    if (++lastY > ICON_ATLAS_WIDTH_TILES-1) {
                        outOfSpace = true;
                        LegacyUI.LOGGER.info("Reached the end of icon texture space!");
                    }
                }
                return new int[]{x, y};
            }
            else {
                LegacyUI.LOGGER.info("No more icon texture spaces are available!");
                return new int[]{ICON_ATLAS_WIDTH_TILES, ICON_ATLAS_WIDTH_TILES};
            }
        }
    }
}
