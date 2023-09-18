package useless.legacyui.Mixins.Settings;

import net.minecraft.client.option.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import useless.legacyui.Settings.ILegacyOptions;

@Mixin(value = GameSettings.class, remap = false)
public class GameSettingsMixin implements ILegacyOptions {
    @Unique
    private final GameSettings thisAsGameSettings = (GameSettings) ((Object)this);
    @Unique
    public BooleanOption craftingHideUndiscoveredItems = new BooleanOption(thisAsGameSettings,"craftingHideUndiscoveredItems", true);
    @Unique
    public BooleanOption overrideLabelModColor = new BooleanOption(thisAsGameSettings,"overrideLabelModColor", false);
    @Unique
    public BooleanOption useLegacySounds = new BooleanOption(thisAsGameSettings,"useLegacySounds", true);
    @Unique
    public BooleanOption hideHotbarInGUIs = new BooleanOption(thisAsGameSettings,"hideHotbarInGUIs", true);
    @Unique
    public BooleanOption enableLegacyCrafting = new BooleanOption(thisAsGameSettings,"enableLegacyCrafting", true);
    @Unique
    public BooleanOption enableLegacyInventorySurvival = new BooleanOption(thisAsGameSettings,"enableLegacyInventorySurvival", true);
    @Unique
    public BooleanOption enableLegacyInventoryCreative = new BooleanOption(thisAsGameSettings,"enableLegacyInventoryCreative", true);
    @Unique
    public BooleanOption showCraftingItemNamePreview = new BooleanOption(thisAsGameSettings,"showCraftingItemNamePreview", true);
    @Unique
    public BooleanOption useRandomPitch = new BooleanOption(thisAsGameSettings,"useRandomPitch", false);
    @Unique
    public IntegerOption guiLabelColor = new IntegerOption(thisAsGameSettings, "guiLabelColor", 0x404040);
    @Unique
    public IntegerOption guiPromptColor = new IntegerOption(thisAsGameSettings, "guiPromptColor", 0xFFFFFF);
    @Unique
    public IntegerOption highlightColor = new IntegerOption(thisAsGameSettings, "highlightColor", 0xFF0000);
    @Unique
    private String strGuiBackgroundColor = "90101010";
    @Unique
    public IntegerOption guiBackgroundColor = new IntegerOption(thisAsGameSettings, "guiBackgroundColor", ((Integer.decode("0X" + strGuiBackgroundColor.substring(0,2)) << 24) + Integer.decode("0X" + strGuiBackgroundColor.substring(2))));
    @Unique
    public RangeOption guiControllerType = new RangeOption(thisAsGameSettings, "guiControllerType", 4, 13);

    public BooleanOption getCraftingHideUndiscoveredItems() {
        return craftingHideUndiscoveredItems;
    }
    public BooleanOption getOverrideLabelModColor() {
        return overrideLabelModColor;
    }
    public BooleanOption getUseLegacySounds() {
        return useLegacySounds;
    }
    public BooleanOption getHideHotbarInGUIs() {
        return hideHotbarInGUIs;
    }
    public BooleanOption getEnableLegacyCrafting() {
        return enableLegacyCrafting;
    }
    public BooleanOption getEnableLegacyInventorySurvival() {
        return enableLegacyInventorySurvival;
    }
    public BooleanOption getEnableLegacyInventoryCreative() {
        return enableLegacyInventoryCreative;
    }
    public BooleanOption getShowCraftingItemNamePreview() {
        return showCraftingItemNamePreview;
    }
    public BooleanOption getUseRandomPitch() {
        return useRandomPitch;
    }
    public IntegerOption getGuiLabelColor() {
        return guiLabelColor;
    }
    public IntegerOption getGuiPromptColor() {
        return guiPromptColor;
    }
    public IntegerOption getHighlightColor() {
        return highlightColor;
    }
    public IntegerOption getGuiBackgroundColor() {
        return guiBackgroundColor;
    }
    public RangeOption getGuiControllerType() {
        return guiControllerType;
    }
}
