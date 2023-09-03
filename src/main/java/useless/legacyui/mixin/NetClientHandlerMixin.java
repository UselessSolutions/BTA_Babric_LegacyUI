package useless.legacyui.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.net.handler.NetClientHandler;
import net.minecraft.core.net.handler.NetHandler;
import net.minecraft.core.net.packet.Packet104WindowItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import useless.legacyui.GlobalOverrides;
import useless.legacyui.Gui.GuiLegacyInventory;
import useless.legacyui.utils.PacketUtil;

@Mixin(value = NetClientHandler.class, remap = false)
public class NetClientHandlerMixin extends NetHandler {
    @Unique
    private final Minecraft mc = Minecraft.getMinecraft(this);

    @Inject(method = "handleWindowItems(Lnet/minecraft/core/net/packet/Packet104WindowItems;)V", at = @At("HEAD"), cancellable = true)
    public void handleWindowItems(Packet104WindowItems packet104windowitems, CallbackInfo ci) {
        if (mc.currentScreen instanceof GuiLegacyInventory){
            standardHandleWindowItems(PacketUtil.packet104Converter(packet104windowitems));
            ci.cancel();
        }
    }

    @Unique
    private void standardHandleWindowItems(Packet104WindowItems packet104windowitems) {
        if (packet104windowitems.windowId == 0) {
            this.mc.thePlayer.inventorySlots.putStacksInSlots(packet104windowitems.itemStack);
        } else if (packet104windowitems.windowId == this.mc.thePlayer.craftingInventory.windowId) {
            this.mc.thePlayer.craftingInventory.putStacksInSlots(packet104windowitems.itemStack);
        }
    }
    @Shadow
    public boolean isServerHandler() {
        return false;
    }
}
