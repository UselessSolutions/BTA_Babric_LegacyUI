package useless.legacyui.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import useless.legacyui.Gui.GuiLegacyCrafting;

@Mixin(value = EntityPlayerSP.class, remap = false)
public class PlayerSPMixin extends EntityPlayer {

    @Shadow
    protected Minecraft mc;
    public PlayerSPMixin(World world) {
        super(world);
    }

    @Shadow
    public void func_6420_o() {

    }

    /**
     * @author Useless
     * @reason Custom Legacy UI
     */
    @Overwrite
    public void displayGUIWorkbench(int i, int j, int k) {
        this.mc.displayGuiScreen(new GuiLegacyCrafting(this.inventory, this.world, i, j, k));
    }
}
