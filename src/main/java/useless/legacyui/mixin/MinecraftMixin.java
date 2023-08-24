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
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import useless.legacyui.Gui.GuiLegacyInventory;


// Fixes a startup class when using some custom classes
@Mixin(value=Minecraft.class, remap=false)
public class MinecraftMixin {
    @Shadow
    private static Minecraft theMinecraft;
    @Inject(method="getMinecraft(Ljava/lang/Class;)Lnet/minecraft/client/Minecraft;", remap=false, at=@At("HEAD"), cancellable=true)
    private static void getMinecraft(Class<?> caller, CallbackInfoReturnable<Minecraft> cir) {
        cir.setReturnValue(theMinecraft);
    }


    @Redirect(method = "Lnet/minecraft/client/Minecraft;runTick()V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;getGuiInventory()Lnet/minecraft/client/gui/GuiInventory;"))
    private GuiInventory loadInventory(Minecraft minecraft){
        if (minecraft.thePlayer.getGamemode() == Gamemode.creative) {
            return new GuiInventoryCreative(minecraft.thePlayer);
        }
        return new GuiLegacyInventory(minecraft.thePlayer);
    }


}