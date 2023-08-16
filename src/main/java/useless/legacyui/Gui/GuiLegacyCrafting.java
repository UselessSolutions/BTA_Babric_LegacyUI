package useless.legacyui.Gui;

import net.minecraft.client.gui.GuiContainer;
import net.minecraft.client.render.TextureFX;
import net.minecraft.core.player.inventory.InventoryPlayer;
import net.minecraft.core.world.World;
import org.lwjgl.opengl.GL11;

public class GuiLegacyCrafting extends GuiContainer {
    public GuiLegacyCrafting(InventoryPlayer inventoryplayer, World world, int i, int j, int k) {
        super(new ContainerWorkbenchLegacy(inventoryplayer, world, i, j, k));
    }

    public void initGui() {
        super.initGui();
        this.xSize = 256+17;
        this.ySize = 175;
    }

    public void onGuiClosed() {
        super.onGuiClosed();
        this.inventorySlots.onCraftGuiClosed(this.mc.thePlayer);
    }

    public void drawGuiContainerForegroundLayer() {
        this.fontRenderer.drawCenteredString("Inventory", 205, this.ySize - 78, 0XFFFFFF);
        this.fontRenderer.drawCenteredString("Crafting", 72, this.ySize - 78, 0XFFFFFF);

    }

    public void drawGuiContainerBackgroundLayer(float f) {
        int i = this.mc.renderEngine.getTexture("assets/gui/legacycrafting.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(i);
        int j = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(j, k, 0, 0, 256, this.ySize);
        this.drawTexturedModalRect(j + 256, k+this.ySize-81, 205, 175, 17, 81);
        this.drawTexturedModalRect(j + 256, k+this.ySize-81-81 , 222, 175, 17, 81);
        this.drawTexturedModalRect(j + 256, k+this.ySize-81-81-13, 239, 175, 17, 13);

        int item = 0;
        this.drawTexturedModalRect(j + 9 + 34*item,k + 6, 16 * item++, 256-16, 16, 16);
        this.drawTexturedModalRect(j + 9 + 34*item,k + 6, 16 * item++, 256-16, 16, 16);
        this.drawTexturedModalRect(j + 9 + 34*item,k + 6, 16 * item++, 256-16, 16, 16);
        this.drawTexturedModalRect(j + 9 + 34*item,k + 6, 16 * item++, 256-16, 16, 16);
        this.drawTexturedModalRect(j + 9 + 34*item,k + 6, 16 * item++, 256-16, 16, 16);
        this.drawTexturedModalRect(j + 9 + 34*item,k + 6, 16 * item++, 256-16, 16, 16);
        this.drawTexturedModalRect(j + 9 + 34*item,k + 6, 16 * item++, 256-16, 16, 16);
        this.drawTexturedModalRect(j + 9 + 34*item,k + 6, 16 * item++, 256-16, 16, 16);
    }
}
