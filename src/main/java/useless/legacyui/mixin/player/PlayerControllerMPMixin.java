package useless.legacyui.mixin.player;

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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import useless.legacyui.gui.GuiLegacyCrafting;
import useless.legacyui.gui.GuiLegacyInventory;
import useless.legacyui.LegacyUI;
import useless.legacyui.SlotRemaps;

import java.util.Arrays;

@Mixin(value = PlayerControllerMP.class, remap = false)
public class PlayerControllerMPMixin extends PlayerController {
    @Shadow
    protected NetClientHandler netHandler;

    public PlayerControllerMPMixin(Minecraft minecraft) {
        super(minecraft);
    }
    @Inject(method = "doInventoryAction(ILnet/minecraft/core/InventoryAction;[ILnet/minecraft/core/entity/player/EntityPlayer;)Lnet/minecraft/core/item/ItemStack;", at = @At("HEAD"), cancellable = true)
    public void lieToServer(int windowId, InventoryAction action, int[] args, EntityPlayer entityplayer, CallbackInfoReturnable<ItemStack> cir) {
        LegacyUI.LOGGER.info("Action" + action);
        LegacyUI.LOGGER.info("Args" + Arrays.toString(args));
        if (mc.currentScreen instanceof GuiLegacyInventory){
            short actionId = entityplayer.craftingInventory.getActionId(entityplayer.inventory);
            if (args != null){
                for (int i = 0; i < args.length; i++){
                    args[i]= SlotRemaps.remapSurvivalInventory(args[i], false);
                }
            }
            ItemStack itemstack = super.doInventoryAction(windowId, action, args, entityplayer);
            LegacyUI.LOGGER.info("Item: " + itemstack);
            this.netHandler.addToSendQueue(new Packet102WindowClick(windowId, action, args, itemstack, actionId));
            cir.setReturnValue(itemstack);
        }
        else if (mc.currentScreen instanceof GuiLegacyCrafting && ((GuiLegacyCrafting)mc.currentScreen).isInInventory){
            short actionId = entityplayer.craftingInventory.getActionId(entityplayer.inventory);
            ItemStack itemstack = super.doInventoryAction(windowId, action, args, entityplayer);
            if (args != null){
                for (int i = 0; i < args.length; i++){
                    args[i]= SlotRemaps.remapInventoryCrafting(args[i], false);
                }
            }
            this.netHandler.addToSendQueue(new Packet102WindowClick(windowId, action, args, itemstack, actionId));
            cir.setReturnValue(itemstack);
        }

    }

}
