package useless.legacyui.mixin;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiContainer;
import net.minecraft.client.gui.GuiInventory;
import net.minecraft.core.player.inventory.Container;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = GuiInventory.class, remap = false)
public class GuiInventoryMixin extends GuiContainer {
    @Shadow
    private GuiButton armorButton;
    @Shadow
    protected int armourButtonFloatX;

    public GuiInventoryMixin(Container container) {
        super(container);
    }

    @Redirect(method = "initGui()V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/GuiInventory;armorButton:Lnet/minecraft/client/gui/GuiButton;", opcode = Opcodes.PUTFIELD))
    private void moveStupidButtonUpwards(GuiInventory guiInventory, GuiButton guiButton){
        this.armorButton = new GuiButton(100, this.width / 2 - this.armourButtonFloatX, this.height / 2 - 74 -5, 9, 9, "");
    }


    @Shadow
    protected void drawGuiContainerBackgroundLayer(float f) {

    }
}
