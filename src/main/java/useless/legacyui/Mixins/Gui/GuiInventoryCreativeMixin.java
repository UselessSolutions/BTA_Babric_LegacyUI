package useless.legacyui.Mixins.Gui;

import net.minecraft.client.gui.*;
import net.minecraft.client.render.FontRenderer;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.player.inventory.ContainerPlayerCreative;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import useless.legacyui.LegacyUI;

@Mixin(value = GuiInventoryCreative.class, remap = false)
public class GuiInventoryCreativeMixin extends GuiInventory{
    @Shadow protected ContainerPlayerCreative container;
    @Shadow protected GuiTextField searchField;

    public GuiInventoryCreativeMixin(EntityPlayer player) {
        super(player);
    }
    @Redirect(method = "drawGuiContainerForegroundLayer()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiInventoryCreative;drawStringCenteredNoShadow(Lnet/minecraft/client/render/FontRenderer;Ljava/lang/String;III)V"))
    public void changeColor(GuiInventoryCreative instance, FontRenderer fontRenderer, String s, int x, int y, int color) {
        instance.drawStringCenteredNoShadow(fontRenderer, s, x, y, LegacyUI.modSettings.getGuiLabelColor().value.value);
    }
    @Redirect(method = "initGui()V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/GuiInventoryCreative;searchField:Lnet/minecraft/client/gui/GuiTextField;", opcode = Opcodes.PUTFIELD))
    public void changeColorSearch(GuiInventoryCreative instance, GuiTextField value) {
        this.searchField = new GuiTextField(this, this.fontRenderer, value.xPosition, value.yPosition, value.width, value.height, container.searchText, "§8Search...");
    }
}
