package useless.legacyui.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.net.handler.NetClientHandler;
import net.minecraft.client.player.controller.PlayerController;
import net.minecraft.client.player.controller.PlayerControllerMP;
import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.packet.Packet102WindowClick;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import useless.legacyui.GlobalOverrides;
import useless.legacyui.Gui.GuiLegacyInventory;
import useless.legacyui.SlotRemaps;
import useless.legacyui.utils.PacketUtil;

@Mixin(value = PlayerControllerMP.class, remap = false)
public class PlayerControllerMPMixin extends PlayerController {

    @Shadow
    private NetClientHandler netHandler;

    public PlayerControllerMPMixin(Minecraft minecraft) {
        super(minecraft);
    }

    @Inject(method = "doInventoryAction(ILnet/minecraft/core/InventoryAction;[ILnet/minecraft/core/entity/player/EntityPlayer;)Lnet/minecraft/core/item/ItemStack;", at = @At("HEAD"), cancellable = true)
    public void lieToServer(int windowId, InventoryAction action, int[] args, EntityPlayer entityplayer, CallbackInfoReturnable<ItemStack> cir) {
        if (GlobalOverrides.currentGuiScreen instanceof GuiLegacyInventory){
            short actionId = entityplayer.craftingInventory.getActionId(entityplayer.inventory);
            if (args != null){
                args = new int[]{SlotRemaps.remapSurvivalInventory(args[0], false), args[1]};
            }
            ItemStack itemstack = super.doInventoryAction(windowId, action, args, entityplayer);
            this.netHandler.addToSendQueue(new Packet102WindowClick(windowId, action, args, itemstack, actionId));
            cir.setReturnValue(itemstack);
        }

    }
}