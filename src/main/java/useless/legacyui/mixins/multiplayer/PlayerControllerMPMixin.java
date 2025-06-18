package useless.legacyui.mixins.multiplayer;

import net.minecraft.client.net.handler.PacketHandlerClient;
import net.minecraft.client.player.controller.PlayerControllerMP;
import net.minecraft.core.InventoryAction;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.net.packet.PacketContainerClick;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import useless.legacyui.gui.containers.LegacyContainerPlayerCreative;
import useless.legacyui.gui.screens.GuiLegacyCreative;
import useless.legacyui.LegacyUI;

@Mixin(value = PlayerControllerMP.class, remap = false)
public class PlayerControllerMPMixin {
    @Redirect(method = "handleInventoryMouseClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/net/handler/PacketHandlerClient;addToSendQueue(Lnet/minecraft/core/net/packet/Packet;)V"))
    private void getCorrectCreativeItem(final PacketHandlerClient instance, final Packet packet){
        if (LegacyUI.modSettings.legacyui$getEnableLegacyInventoryCreative().value) {
            final PacketContainerClick packetContainerClick = (PacketContainerClick)packet;
            final int[] args = packetContainerClick.args;
            if (packetContainerClick.action == InventoryAction.CREATIVE_GRAB || packetContainerClick.action == InventoryAction.CREATIVE_MOVE){
                LegacyContainerPlayerCreative.inventory.player.updateCreativeInventory(0, packetContainerClick.itemStack.getDisplayName());
                args[0] = GuiLegacyCreative.container.getCreativeSlotsStart();
            }
            instance.addToSendQueue(new PacketContainerClick(packetContainerClick.window_Id, packetContainerClick.action, args, packetContainerClick.itemStack, packetContainerClick.actionId));
        } else {
            instance.addToSendQueue(packet);
        }
    }
}
