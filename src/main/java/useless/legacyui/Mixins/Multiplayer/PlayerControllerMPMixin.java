package useless.legacyui.Mixins.Multiplayer;

import net.minecraft.client.net.handler.NetClientHandler;
import net.minecraft.client.player.controller.PlayerControllerMP;
import net.minecraft.core.InventoryAction;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.net.packet.Packet102WindowClick;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import useless.legacyui.Gui.GuiScreens.GuiLegacyCreative;

@Mixin(value = PlayerControllerMP.class, remap = false)
public class PlayerControllerMPMixin {
    @Redirect(method = "doInventoryAction(ILnet/minecraft/core/InventoryAction;[ILnet/minecraft/core/entity/player/EntityPlayer;)Lnet/minecraft/core/item/ItemStack;", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/net/handler/NetClientHandler;addToSendQueue(Lnet/minecraft/core/net/packet/Packet;)V"))
    private void getCorrectCreativeItem(NetClientHandler instance, Packet packet){
        Packet102WindowClick packet102 = (Packet102WindowClick)packet;
        int[] args = packet102.args;
        if (packet102.action == InventoryAction.CREATIVE_GRAB || packet102.action == InventoryAction.CREATIVE_MOVE){
            GuiLegacyCreative.container.playerInv.player.updateCreativeInventory(0, packet102.itemStack.getDisplayName());
            args[0] = GuiLegacyCreative.container.getCreativeSlotsStart();
        }
        instance.addToSendQueue(new Packet102WindowClick(packet102.window_Id, packet102.action, args, packet102.itemStack, packet102.actionId));
    }
}
