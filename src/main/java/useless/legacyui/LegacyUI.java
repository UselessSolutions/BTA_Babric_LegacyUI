package useless.legacyui;

import net.minecraft.client.render.texture.stitcher.AtlasStitcher;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.util.ClientStartEntrypoint;
import useless.legacyui.settings.ILegacyOptions;

import java.io.IOException;
import java.net.URISyntaxException;

public class LegacyUI implements ClientStartEntrypoint {
    public static final String MOD_ID = "legacyui";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static ILegacyOptions modSettings;

    @Override
    public void beforeClientStart() {
        LOGGER.info("LegacyUI initialized.");
        for (AtlasStitcher stitcher : TextureRegistry.stitcherMap.values()) {
            try {
                TextureRegistry.initializeAllFiles(MOD_ID, stitcher, true);
            } catch (URISyntaxException | IOException e) {
                LOGGER.error("Failed to initialize textures in {} atlas!", TextureRegistry.stitcherMapReverse.get(stitcher), e);
            }
        }
    }

    @Override
    public void afterClientStart() {

    }

}
