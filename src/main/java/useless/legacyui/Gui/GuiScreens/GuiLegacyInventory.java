package useless.legacyui.Gui.GuiScreens;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiInventory;
import net.minecraft.client.input.InputType;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.Lighting;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.lang.I18n;
import org.lwjgl.opengl.GL11;
import useless.legacyui.Gui.GuiElements.Buttons.GuiAuditoryButton;
import useless.legacyui.Gui.GuiElements.GuiButtonPrompt;
import useless.legacyui.LegacySoundManager;
import useless.legacyui.LegacyUI;
import useless.legacyui.Mixins.Gui.GuiInventoryAccessor;
import useless.legacyui.ModSettings;

import java.util.ArrayList;
import java.util.List;

public class GuiLegacyInventory extends GuiInventory {
    private static int GUIx;
    private static int GUIy;
    protected GuiAuditoryButton craftButton;
    protected EntityPlayer player;
    public List<GuiButtonPrompt> prompts = new ArrayList<>();
    public GuiLegacyInventory(EntityPlayer player) {
        super(player);
        this.player = player;
    }
    public void initGui() {
        super.initGui();

        // Setup size variables
        xSize = 176;
        ySize = 176;
        GUIx = (this.width - this.xSize) / 2;
        GUIy = (this.height - this.ySize) / 2;

        // Offset Armor Button
        GuiButton armorButton = ((GuiInventoryAccessor)this).getArmorButton();
        if (armorButton != null){
            armorButton.xPosition += 44;
            armorButton.yPosition -= 5;
        } else {
            LegacyUI.LOGGER.info("Armor Button is null!");
        }

        // Create Crafting Button
        craftButton = new GuiAuditoryButton(10, GUIx + 138, GUIy + 33, 20, 21, "");
        craftButton.visible = false;
        controlList.add(craftButton);
        I18n translator = I18n.getInstance();
        prompts.add(new GuiButtonPrompt( 101, 50, this.height-30, 0, 3,translator.translateKey("legacyui.prompt.select")));
        prompts.add(new GuiButtonPrompt( 102, prompts.get(prompts.size()-1).xPosition + prompts.get(prompts.size()-1).width + 3, this.height-30, 1, 3,translator.translateKey("legacyui.prompt.back")));
        prompts.add(new GuiButtonPrompt( 102, prompts.get(prompts.size()-1).xPosition + prompts.get(prompts.size()-1).width + 3, this.height-30, 2, 3,translator.translateKey("legacyui.prompt.movestack")));
        prompts.add(new GuiButtonPrompt( 102, prompts.get(prompts.size()-1).xPosition + prompts.get(prompts.size()-1).width + 3, this.height-30, 3, 3,translator.translateKey("legacyui.prompt.halfstack")));
    }
    protected void buttonPressed(GuiButton guibutton) {
        super.buttonPressed(guibutton);
        if (guibutton == craftButton){
            openCrafting();
        }
    }
    protected void openCrafting(){
        LegacySoundManager.volume = 0;
        this.onGuiClosed();
        mc.displayGuiScreen(new GuiLegacyCrafting(player, 4));
        LegacySoundManager.volume = 1f;
    }
    public void drawScreen(int x, int y, float renderPartialTicks) {
        super.drawScreen(x,y,renderPartialTicks);

        UtilGui.bindTexture("/assets/legacyui/gui/legacyinventory.png");
        this.drawTexturedModalRect(craftButton.xPosition, craftButton.yPosition, 177, craftButton.isHovered(x,y) ? 77:54, craftButton.width, craftButton.height); // Crafting Button Render
        if (mc.inputType == InputType.CONTROLLER){
            for (GuiButtonPrompt prompt: prompts) {
                prompt.drawPrompt(mc, x, y);
            }
        }
    }
    protected void drawGuiContainerForegroundLayer() {
    }
    protected void drawGuiContainerBackgroundLayer(float renderPartialTick) {
        UtilGui.bindTexture("/assets/legacyui/gui/legacyinventory.png");
        this.drawTexturedModalRect(GUIx, GUIy, 0, 0, this.xSize, this.ySize);
        renderPlayerDoll();
        drawStringNoShadow(fontRenderer, I18n.getInstance().translateKey("legacyui.guilabel.inventory"),GUIx + 8,GUIy +82, ModSettings.Colors.GuiLabelColor());
    }
    private void renderPlayerDoll(){
        GL11.glEnable(32826);
        GL11.glEnable(2903);
        GL11.glEnable(2929);
        GL11.glPushMatrix();
        GL11.glTranslatef(GUIx + 51 + 44, GUIy + 75 - 5, 50.0f);
        float f1 = 30.0f;
        GL11.glScalef(-f1, f1, f1);
        GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
        float f2 = this.mc.thePlayer.renderYawOffset;
        float f3 = this.mc.thePlayer.yRot;
        float f4 = this.mc.thePlayer.xRot;
        float f5 = (float)(GUIx + 51 + 44) - this.xSize_lo;
        float f6 = (float)(GUIy + 75 - 50 - 5) - this.ySize_lo;
        GL11.glRotatef(135.0f, 0.0f, 1.0f, 0.0f);
        Lighting.enableLight();
        GL11.glRotatef(-135.0f, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(-((float)Math.atan(f6 / 40.0f)) * 20.0f, 1.0f, 0.0f, 0.0f);
        this.mc.thePlayer.renderYawOffset = (float)Math.atan(f5 / 40.0f) * 20.0f;
        this.mc.thePlayer.yRot = (float)Math.atan(f5 / 40.0f) * 40.0f;
        this.mc.thePlayer.xRot = -((float)Math.atan(f6 / 40.0f)) * 20.0f;
        this.mc.thePlayer.entityBrightness = 1.0f;
        GL11.glTranslatef(0.0f, this.mc.thePlayer.heightOffset, 0.0f);
        EntityRenderDispatcher.instance.viewLerpYaw = 180.0f;
        EntityRenderDispatcher.instance.renderEntityWithPosYaw(this.mc.thePlayer, 0.0, 0.0, 0.0, 0.0f, 1.0f);
        this.mc.thePlayer.entityBrightness = 0.0f;
        this.mc.thePlayer.renderYawOffset = f2;
        this.mc.thePlayer.yRot = f3;
        this.mc.thePlayer.xRot = f4;
        GL11.glPopMatrix();
        Lighting.disable();
        GL11.glDisable(32826);
    }
}
