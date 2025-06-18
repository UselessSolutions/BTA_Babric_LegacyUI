package useless.legacyui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.util.ClientStartEntrypoint;
import useless.legacyui.settings.ILegacyOptions;

public class LegacyUI implements ClientStartEntrypoint {
    public static final String MOD_ID = "legacyui";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static ILegacyOptions modSettings;

    @Override
    public void beforeClientStart() {
        LOGGER.info("LegacyUI initialized.");
    }

    @Override
    public void afterClientStart() {

    }

}
