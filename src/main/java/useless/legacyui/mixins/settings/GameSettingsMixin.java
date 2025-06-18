package useless.legacyui.mixins.settings;

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
import useless.legacyui.gui.screens.options.ControllerType;
import useless.legacyui.settings.ILegacyOptions;

@Mixin(value = GameSettings.class, remap = false, priority = 2000)
public class GameSettingsMixin implements ILegacyOptions {
    @Shadow @Final public Minecraft mc;
    @Inject(method = "optionChanged(Lnet/minecraft/client/option/Option;)V", at = @At("TAIL"))
    private void onOptionChanged(final Option<?> option, final CallbackInfo ci){
        if (option == this.enableLegacyInventorySurvival){
            if (this.mc.thePlayer != null && this.mc.thePlayer.getGamemode() == Gamemode.survival){
                this.mc.thePlayer.inventorySlots = Gamemode.survival.getContainer(this.mc.thePlayer.inventory, !this.mc.thePlayer.world.isClientSide);
            }
        }
        if (option == this.enableLegacyInventoryCreative){
            if (this.mc.thePlayer != null && this.mc.thePlayer.getGamemode() == Gamemode.creative){
                this.mc.thePlayer.inventorySlots = Gamemode.creative.getContainer(this.mc.thePlayer.inventory, !this.mc.thePlayer.world.isClientSide);
            }
        }
    }
    @Inject(method = "getDisplayString(Lnet/minecraft/client/option/Option;)Ljava/lang/String;", at = @At("HEAD"), cancellable = true)
    private void displayString(final Option<?> option, final CallbackInfoReturnable<String> cir){
        final I18n translator = I18n.getInstance();
        if (option == this.panoramaScrollLength){
            cir.setReturnValue((this.panoramaScrollLength.value + 1) * 15 + " " + translator.translateKey("options.legacyui.panoramaSpeed.unit"));
        }
        if (option == this.HUDFadeoutDelay){
            cir.setReturnValue(String.format("%.1f", this.HUDFadeoutDelay.value * 10) + " " + translator.translateKey("options.legacyui.hudFadeout.unit"));
        }
    }
    @Unique
    private final GameSettings thisAs = (GameSettings) ((Object)this);
    @Unique
    public OptionBoolean craftingHideUndiscoveredItems = new OptionBoolean(this.thisAs,"legacyui.craftingHideUndiscoveredItems", true);
    @Unique
    public OptionBoolean useLegacySounds = new OptionBoolean(this.thisAs,"legacyui.useLegacySounds", true);
    @Unique
    public OptionBoolean hideHotbarInGUIs = new OptionBoolean(this.thisAs,"legacyui.hideHotbarInGUIs", true);
    @Unique
    public OptionBoolean enableLegacyCrafting = new OptionBoolean(this.thisAs,"legacyui.enableLegacyCrafting", true);
    @Unique
    public OptionBoolean enableLegacyInventorySurvival = new OptionBoolean(this.thisAs,"legacyui.enableLegacyInventorySurvival", true);
    @Unique
    public OptionBoolean enableLegacyInventoryCreative = new OptionBoolean(this.thisAs,"legacyui.enableLegacyInventoryCreative", true);
    @Unique
    public OptionBoolean enableLegacyFlag = new OptionBoolean(this.thisAs,"legacyui.enableLegacyFlag", true);
    @Unique
    public OptionBoolean showCraftingItemNamePreview = new OptionBoolean(this.thisAs,"legacyui.showCraftingItemNamePreview", true);
    @Unique
    public OptionBoolean useRandomPitch = new OptionBoolean(this.thisAs,"legacyui.useRandomPitch", false);
    @Unique
    public OptionColor guiPromptColor = new OptionColor(this.thisAs, "legacyui.guiPromptColor", new Color().setARGB(0xFFFFFF));
    @Unique
    public OptionColor highlightColor = new OptionColor(this.thisAs, "legacyui.highlightColor", new Color().setARGB(0xFF0000));
    @Unique
    public OptionColor guiBackgroundColor = new OptionColor(this.thisAs, "legacyui.guiBackgroundColor", new Color().setARGB(0x90101010));
    @Unique
    public OptionEnum<ControllerType> guiControllerType = new OptionEnum<>(this.thisAs, "legacyui.guiControllerType", ControllerType.class, ControllerType.GENERIC);
    @Unique
    public OptionBoolean enablePanorama = new OptionBoolean(this.thisAs, "legacyui.enablePanorama", true);
    @Unique
    public OptionRange panoramaScrollLength = new OptionRange(this.thisAs, "legacyui.panoramaSpeed", 3, 12);
    @Unique
    public OptionFloat mainMenuBrightness = new OptionFloat(this.thisAs, "legacyui.mainMenuBrightness", 1f);
    @Unique
    public OptionBoolean coordsOnMaps = new OptionBoolean(this.thisAs, "legacyui.coordsOnMaps", true);
    @Unique
    public OptionBoolean forceButtonPrompts = new OptionBoolean(this.thisAs, "legacyui.forceButtonPrompts", false);
    @Unique
    public OptionBoolean forceLegacyTooltip = new OptionBoolean(this.thisAs, "legacyui.forceLegacyTooltip", false);
    @Unique
    public OptionBoolean enablePaperDoll = new OptionBoolean(this.thisAs, "legacyui.enablePaperDoll", false);
    @Unique
    public OptionBoolean enableHUDFadeout = new OptionBoolean(this.thisAs, "legacyui.enableHUDFadeout", true);
    @Unique
    public OptionFloat HUDFadeoutDelay = new OptionFloat(this.thisAs, "legacyui.hudFadeoutDelay", 0.5f);
    @Unique
    public OptionFloat HUDFadeoutAlpha = new OptionFloat(this.thisAs, "legacyui.hudFadeoutAlpha", 0.20f);
    public OptionBoolean legacyui$getCraftingHideUndiscoveredItems() {
        return this.craftingHideUndiscoveredItems;
    }
    public OptionBoolean legacyui$getUseLegacySounds() {
        return this.useLegacySounds;
    }
    public OptionBoolean legacyui$getHideHotbarInGUIs() {
        return this.hideHotbarInGUIs;
    }
    public OptionBoolean legacyui$getEnableLegacyCrafting() {
        return this.enableLegacyCrafting;
    }
    public OptionBoolean legacyui$getEnableLegacyInventorySurvival() {
        return this.enableLegacyInventorySurvival;
    }
    public OptionBoolean legacyui$getEnableLegacyInventoryCreative() {
        return this.enableLegacyInventoryCreative;
    }
    public OptionBoolean legacyui$getEnableLegacyFlag() {return this.enableLegacyFlag;}
    public OptionBoolean legacyui$getShowCraftingItemNamePreview() {
        return this.showCraftingItemNamePreview;
    }
    public OptionBoolean legacyui$getUseRandomPitch() {
        return this.useRandomPitch;
    }
    public OptionColor legacyui$getGuiPromptColor() {
        return this.guiPromptColor;
    }
    public OptionColor legacyui$getHighlightColor() {
        return this.highlightColor;
    }
    public OptionColor legacyui$getGuiBackgroundColor() {
        return this.guiBackgroundColor;
    }
    public OptionEnum<ControllerType> legacyui$getGuiControllerType() {
        return this.guiControllerType;
    }
    public OptionBoolean legacyui$getEnablePanorama() {
        return this.enablePanorama;
    }
    public OptionRange legacyui$getPanoramaScrollLength() {
        return this.panoramaScrollLength;
    }
    public OptionFloat legacyui$getMainMenuBrightness() {
        return this.mainMenuBrightness;
    }
    public OptionBoolean legacyui$getCoordsOnMaps() {
        return this.coordsOnMaps;
    }
    public OptionBoolean legacyui$getForceButtonPrompts() {
        return this.forceButtonPrompts;
    }

    public OptionBoolean legacyui$getForceLegacyTooltip() {
        return this.forceLegacyTooltip;
    }
    public OptionBoolean legacyui$getEnablePaperDoll() {
        return this.enablePaperDoll;
    }
    public OptionBoolean legacyui$getEnableHUDFadeout() {
        return this.enableHUDFadeout;
    }
    public OptionFloat legacyui$getHUDFadeoutDelay() {
        return this.HUDFadeoutDelay;
    }
    public OptionFloat legacyui$getHUDFadeoutAlpha() {
        return this.HUDFadeoutAlpha;
    }
}
