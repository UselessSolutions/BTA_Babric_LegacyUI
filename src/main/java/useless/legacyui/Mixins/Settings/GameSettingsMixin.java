package useless.legacyui.Mixins.Settings;

import net.minecraft.client.Minecraft;
import net.minecraft.client.option.*;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.player.gamemode.Gamemode;
import net.minecraft.core.util.helper.Color;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import useless.legacyui.Gui.GuiScreens.Options.ControllerType;
import useless.legacyui.Settings.ILegacyOptions;

@Mixin(value = GameSettings.class, remap = false, priority = 2000)
public class GameSettingsMixin implements ILegacyOptions {
    @Shadow @Final public Minecraft mc;

    @Inject(method = "optionChanged(Lnet/minecraft/client/option/Option;)V", at = @At("TAIL"))
    private void onOptionChanged(Option<?> option, CallbackInfo ci){
        if (option == enableLegacyInventorySurvival){
            if (mc.thePlayer != null && mc.thePlayer.getGamemode() == Gamemode.survival){
                mc.thePlayer.inventorySlots = Gamemode.survival.getContainer(mc.thePlayer.inventory, !mc.thePlayer.world.isClientSide);
            }
        }
        if (option == enableLegacyInventoryCreative){
            if (mc.thePlayer != null && mc.thePlayer.getGamemode() == Gamemode.creative){
                mc.thePlayer.inventorySlots = Gamemode.creative.getContainer(mc.thePlayer.inventory, !mc.thePlayer.world.isClientSide);
            }
        }
    }
    @Inject(method = "getDisplayString(Lnet/minecraft/client/option/Option;)Ljava/lang/String;", at = @At("HEAD"), cancellable = true)
    private void displayString(Option<?> option, CallbackInfoReturnable<String> cir){
        I18n translator = I18n.getInstance();
        if (option == panoramaScrollLength){
            cir.setReturnValue("" + (panoramaScrollLength.value + 1) * 15 + " " + translator.translateKey("options.legacyui.panoramaSpeed.unit"));
        }
    }
    @Unique
    private final GameSettings thisAsGameSettings = (GameSettings) ((Object)this);
    @Unique
    public BooleanOption craftingHideUndiscoveredItems = new BooleanOption(thisAsGameSettings,"legacyui.craftingHideUndiscoveredItems", true);
    @Unique
    public BooleanOption overrideLabelModColor = new BooleanOption(thisAsGameSettings,"legacyui.overrideLabelModColor", false);
    @Unique
    public BooleanOption useLegacySounds = new BooleanOption(thisAsGameSettings,"legacyui.useLegacySounds", true);
    @Unique
    public BooleanOption hideHotbarInGUIs = new BooleanOption(thisAsGameSettings,"legacyui.hideHotbarInGUIs", true);
    @Unique
    public BooleanOption enableLegacyCrafting = new BooleanOption(thisAsGameSettings,"legacyui.enableLegacyCrafting", true);
    @Unique
    public BooleanOption enableLegacyInventorySurvival = new BooleanOption(thisAsGameSettings,"legacyui.enableLegacyInventorySurvival", true);
    @Unique
    public BooleanOption enableLegacyInventoryCreative = new BooleanOption(thisAsGameSettings,"legacyui.enableLegacyInventoryCreative", true);
    @Unique
    public BooleanOption enableLegacyFlag = new BooleanOption(thisAsGameSettings,"legacyui.enableLegacyFlag", true);
    @Unique
    public BooleanOption showCraftingItemNamePreview = new BooleanOption(thisAsGameSettings,"legacyui.showCraftingItemNamePreview", true);
    @Unique
    public BooleanOption useRandomPitch = new BooleanOption(thisAsGameSettings,"legacyui.useRandomPitch", false);
    @Unique
    public ColorOption guiLabelColor = new ColorOption(thisAsGameSettings, "legacyui.guiLabelColor", new Color().setARGB(0x404040));
    @Unique
    public ColorOption guiPromptColor = new ColorOption(thisAsGameSettings, "legacyui.guiPromptColor", new Color().setARGB(0xFFFFFF));
    @Unique
    public ColorOption highlightColor = new ColorOption(thisAsGameSettings, "legacyui.highlightColor", new Color().setARGB(0xFF0000));
    @Unique
    public ColorOption guiBackgroundColor = new ColorOption(thisAsGameSettings, "legacyui.guiBackgroundColor", new Color().setARGB(0x90101010));
    @Unique
    public EnumOption<ControllerType> guiControllerType = new EnumOption<>(thisAsGameSettings, "legacyui.guiControllerType", ControllerType.class, ControllerType.GENERIC);
    @Unique
    public BooleanOption enablePanorama = new BooleanOption(thisAsGameSettings, "legacyui.enablePanorama", true);
    @Unique
    public BooleanOption replaceStandardBackground = new BooleanOption(thisAsGameSettings, "legacyui.universalPanorama", true);
    @Unique
    public RangeOption panoramaScrollLength = new RangeOption(thisAsGameSettings, "legacyui.panoramaSpeed", 3, 12);
    @Unique
    public FloatOption mainMenuBrightness = new FloatOption(thisAsGameSettings, "legacyui.mainMenuBrightness", 1f);

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
    public BooleanOption getEnableLegacyFlag() {return enableLegacyFlag;}
    public BooleanOption getShowCraftingItemNamePreview() {
        return showCraftingItemNamePreview;
    }
    public BooleanOption getUseRandomPitch() {
        return useRandomPitch;
    }
    public ColorOption getGuiLabelColor() {
        return guiLabelColor;
    }
    public ColorOption getGuiPromptColor() {
        return guiPromptColor;
    }
    public ColorOption getHighlightColor() {
        return highlightColor;
    }
    public ColorOption getGuiBackgroundColor() {
        return guiBackgroundColor;
    }
    public EnumOption<ControllerType> getGuiControllerType() {
        return guiControllerType;
    }
    public BooleanOption getEnablePanorama() {
        return enablePanorama;
    }

    @Override
    public BooleanOption getReplaceStandardBackground() {
        return replaceStandardBackground;
    }

    public RangeOption getPanoramaScrollLength() {
        return panoramaScrollLength;
    }

    public FloatOption getMainMenuBrightness() {
        return mainMenuBrightness;
    }
}
