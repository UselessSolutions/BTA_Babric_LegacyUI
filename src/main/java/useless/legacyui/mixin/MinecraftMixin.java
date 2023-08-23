package useless.legacyui.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiInventory;
import net.minecraft.client.gui.GuiInventoryCreative;
import net.minecraft.core.player.gamemode.Gamemode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


// Fixes a startup class when using some custom classes
@Mixin(value=Minecraft.class, remap=false)
public class MinecraftMixin {
    @Shadow
    private static Minecraft theMinecraft;
    @Inject(method="getMinecraft(Ljava/lang/Class;)Lnet/minecraft/client/Minecraft;", remap=false, at=@At("HEAD"), cancellable=true)
    private static void getMinecraft(Class<?> caller, CallbackInfoReturnable<Minecraft> cir) {
        cir.setReturnValue(theMinecraft);
    }
    /**
     * @author Useless
     * @reason Custom inventory GUIs for legacy ui
    @Overwrite
    private GuiInventory getGuiInventory() {
        if (theMinecraft.thePlayer.getGamemode() == Gamemode.creative) {
            return new GuiInventoryCreative(theMinecraft.thePlayer);
        }
        return new GuiInventory(theMinecraft.thePlayer);
    }*/
}