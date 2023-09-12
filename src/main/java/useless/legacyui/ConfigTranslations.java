package useless.legacyui;

public enum ConfigTranslations {
    GUI_LABEL_COLOR,
    HIGHLIGHT_COLOR,
    GUI_BACKGROUND_COLOR,
    OVERRIDE_LABEL_COLOR,
    CRAFTING_HIDE_UNDISCOVERED,
    EXPERIMENTAL_STACK_FIX,
    EXPERIMENTAL_STACK_DELAY,
    USE_LEGACY_SOUNDS,
    HIDE_HOTBAR_IN_GUIS;
    private static final ConfigTranslations[] configs;
    private int id;
    private String key;
    private static void setId(ConfigTranslations config, int id, String key) {
        ConfigTranslations.configs[id] = config;
        config.id = id;
        config.key = key;
    }

    public String getKey() {
        return this.key;
    }

    public static ConfigTranslations get(int id) {
        if (id < 0 || id >= configs.length) {
            return null;
        }
        return configs[id];
    }
    static {
        configs = new ConfigTranslations[32];
        ConfigTranslations.setId(GUI_LABEL_COLOR, 0,"GuiLabelColor");
        ConfigTranslations.setId(HIGHLIGHT_COLOR, 1, "HighlightColor");
        ConfigTranslations.setId(OVERRIDE_LABEL_COLOR, 2, "OverrideLabelModColor");
        ConfigTranslations.setId(CRAFTING_HIDE_UNDISCOVERED, 3, "CraftingHideUndiscoveredItems");
        ConfigTranslations.setId(EXPERIMENTAL_STACK_FIX, 4, "ExperimentalQuickStackFix");
        ConfigTranslations.setId(EXPERIMENTAL_STACK_DELAY, 5, "ExperimentalQuickStackFixDelay");
        ConfigTranslations.setId(USE_LEGACY_SOUNDS, 6, "UseLegacySounds");
        ConfigTranslations.setId(HIDE_HOTBAR_IN_GUIS, 7, "HideHotbarInGUIs");
        ConfigTranslations.setId(GUI_BACKGROUND_COLOR, 8, "GuiBackgroundColor");
    }
}