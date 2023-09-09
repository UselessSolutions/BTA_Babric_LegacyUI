package useless.legacyui.Gui;

import net.minecraft.client.gui.GuiInventory;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.player.inventory.ContainerPlayer;
import net.minecraft.core.player.inventory.ContainerPlayerCreative;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import useless.legacyui.Gui.Container.ContainerCreativeLegacy;

public class GuiLegacyCreative extends GuiInventory {
    protected ContainerCreativeLegacy container;
    public GuiLegacyCreative(EntityPlayer player) {
        super(player);
        this.inventorySlots = new ContainerCreativeLegacy(player.inventory, ((ContainerPlayer)inventorySlots).craftMatrix, ((ContainerPlayer)inventorySlots).craftResult);
    }
    public void initGui() {
        this.xSize = 273; // width of texture plus the 17px strip that was cut off
        this.ySize = 175; // height of Gui window
    }
    public void drawGuiContainerForegroundLayer() {
        ContainerCreativeLegacy.scrollRow(-Mouse.getDWheel()/10);
        ((ContainerCreativeLegacy)inventorySlots).updatePage();

        int i = this.mc.renderEngine.getTexture("/assets/legacyui/gui/legacycreative.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(i);

        // Scroll Bar
        int scrollTopY = 36;
        int scrollBottomY = 143 - 15;
        int scrollYPos = (int)((scrollBottomY-scrollTopY) * ((float)ContainerCreativeLegacy.currentRow/ContainerCreativeLegacy.totalRows));
        this.drawTexturedModalRect(251, scrollTopY + scrollYPos, 131, 175, 15, 15);
    }
    public void drawGuiContainerBackgroundLayer(float f) {
        int i = this.mc.renderEngine.getTexture("/assets/legacyui/gui/legacycreative.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(i);
        int j = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;

        // Draws base Gui background
        this.drawTexturedModalRect(j, k, 0, 0, 256, this.ySize);
        this.drawTexturedModalRect(j + 256, k + this.ySize - 13, 205, 175, 17, 13);
        this.drawTexturedModalRect(j + 256, k + this.ySize - 81 - 13, 222, 175, 17, 81);
        this.drawTexturedModalRect(j + 256, k + this.ySize - 81 - 81 - 13, 239, 175, 17, 81);
    }
}
