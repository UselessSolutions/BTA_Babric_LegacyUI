package useless.legacyui.gui.screens;

import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.ListLayout;
import net.minecraft.client.gui.container.ScreenInventory;
import net.minecraft.client.input.controller.ControllerInput;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.player.gamemode.Gamemode;
import org.lwjgl.opengl.GL11;
import useless.legacyui.gui.elements.GuiButtonPrompt;
import useless.legacyui.gui.IGuiController;
import useless.legacyui.LegacySoundManager;
import useless.legacyui.mixins.gui.GuiInventoryAccessor;

import java.util.ArrayList;
import java.util.List;

public class GuiLegacyInventory extends ScreenInventory implements IGuiController {
    private static int GUIx;
    private static int GUIy;

    protected ButtonElement craftButton;
    protected Player player;
    public List<GuiButtonPrompt> prompts = new ArrayList<>();
    public GuiLegacyInventory(final Player player) {
        super(player);
        this.xSize = 176;
        this.ySize = 176;
        this.overlayButtonsLayout = new ListLayout(this).setAlign(0.5, 0.5).setVertical(false).setElementSize(11, 11).setOffset(-(this.xSize / 2), -(this.ySize / 2) - 10).setMargin(1);
        this.player = player;
        this.inventorySlots = Gamemode.survival.getContainer(player.inventory, !player.world.isClientSide);
    }
    public void init() {
        super.init();

        // Setup size variables
        GUIx = (this.width - this.xSize) / 2;
        GUIy = (this.height - this.ySize) / 2;

        // Offset Armor Button
        final ButtonElement armorButton = ((GuiInventoryAccessor)this).getArmorButton();
        if (armorButton != null){
            armorButton.xPosition += 44;
            armorButton.yPosition -= 5;
        }

        // Create Crafting Button
        this.craftButton = new ButtonElement(10, GUIx + 138, GUIy + 33, 20, 21, "");
        this.craftButton.visible = false;
        this.buttons.add(this.craftButton);
        final I18n translator = I18n.getInstance();
        this.prompts.clear();
        this.prompts.add(new GuiButtonPrompt( 101, 50, this.height-30,  3,translator.translateKey("legacyui.prompt.select"), new int[]{0}));
        this.prompts.add(new GuiButtonPrompt( 102, this.prompts.get(this.prompts.size()-1).xPosition + this.prompts.get(this.prompts.size()-1).width + 3, this.height-30,  3,translator.translateKey("legacyui.prompt.back"), new int[]{1}));
        this.prompts.add(new GuiButtonPrompt( 103, this.prompts.get(this.prompts.size()-1).xPosition + this.prompts.get(this.prompts.size()-1).width + 3, this.height-30,  3,translator.translateKey("legacyui.prompt.movestack"), new int[]{2}));
        this.prompts.add(new GuiButtonPrompt( 104, this.prompts.get(this.prompts.size()-1).xPosition + this.prompts.get(this.prompts.size()-1).width + 3, this.height-30,  3,translator.translateKey("legacyui.prompt.halfstack"), new int[]{3}));
        if(this.player.getGamemode() == Gamemode.creative){
            this.prompts.add(new GuiButtonPrompt( 110, this.prompts.get(this.prompts.size()-1).xPosition + this.prompts.get(this.prompts.size()-1).width + 3, this.height-30,  3,translator.translateKey("legacyui.prompt.opencreative"), new int[]{GuiButtonPrompt.LEFT_TRIGGER}));
        }
        this.prompts.add(new GuiButtonPrompt( 105, this.prompts.get(this.prompts.size()-1).xPosition + this.prompts.get(this.prompts.size()-1).width + 3, this.height-30,  3,translator.translateKey("legacyui.prompt.opencrafting"), new int[]{GuiButtonPrompt.RIGHT_TRIGGER}));
    }
    @Override
    protected void buttonClicked(final ButtonElement guibutton) {
        super.buttonClicked(guibutton);
        if (guibutton == this.craftButton){
            openCrafting();
        }
    }
    protected void openCrafting(){
        LegacySoundManager.volume = 0;
        this.removed();
        this.mc.displayScreen(new GuiLegacyCrafting(this.player, 4));
        LegacySoundManager.volume = 1f;
    }
    protected void openCreative(){
        LegacySoundManager.volume = 0;
        this.removed();
        this.mc.displayScreen(new GuiLegacyCreative(this.player));
        LegacySoundManager.volume = 1f;
    }
    public void render(final int mx, final int my, final float partialTick) {
        super.render(mx,my,partialTick);

        UtilGui.bindTexture("/assets/legacyui/gui/legacyinventory.png");
        this.drawTexturedModalRect(this.craftButton.xPosition, this.craftButton.yPosition, 177, this.craftButton.isHovered(mx,my) ? 77:54, this.craftButton.width, this.craftButton.height); // Crafting Button Render
        for (final GuiButtonPrompt prompt: this.prompts) {
            prompt.drawPrompt(this.mc, mx, my);
        }
    }
    protected void drawGuiContainerForegroundLayer() {
    }
    protected void drawGuiContainerBackgroundLayer(final float renderPartialTick) {
        UtilGui.bindTexture("/assets/legacyui/gui/legacyinventory.png");
        this.drawTexturedModalRect(GUIx, GUIy, 0, 0, this.xSize, this.ySize);
        renderPlayerDoll();
        drawStringNoShadow(this.font, I18n.getInstance().translateKey("legacyui.guilabel.inventory"),GUIx + 8,GUIy +82, 0x404040);
    }
    private void renderPlayerDoll(){
        GL11.glEnable(32826);
        GL11.glEnable(2903);
        GL11.glEnable(2929);
        GL11.glPushMatrix();
        GL11.glTranslatef(GUIx + 51 + 44, GUIy + 75 - 5, 50.0f);
        final float f1 = 30.0f;
        GL11.glScalef(-f1, f1, f1);
        GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
        final float f2 = this.mc.thePlayer.yBodyRot;
        final float f3 = this.mc.thePlayer.yRot;
        final float f4 = this.mc.thePlayer.xRot;
        final float f5 = (float)(GUIx + 51 + 44) - this.xSize_lo;
        final float f6 = (float)(GUIy + 75 - 50 - 5) - this.ySize_lo;
        GL11.glRotatef(135.0f, 0.0f, 1.0f, 0.0f);
        Lighting.enableLight();
        GL11.glRotatef(-135.0f, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(-((float)Math.atan(f6 / 40.0f)) * 20.0f, 1.0f, 0.0f, 0.0f);
        this.mc.thePlayer.yBodyRot = (float)Math.atan(f5 / 40.0f) * 20.0f;
        this.mc.thePlayer.yRot = (float)Math.atan(f5 / 40.0f) * 40.0f;
        this.mc.thePlayer.xRot = -((float)Math.atan(f6 / 40.0f)) * 20.0f;
        this.mc.thePlayer.entityBrightness = 1.0f;
        GL11.glTranslatef(0.0f, this.mc.thePlayer.heightOffset, 0.0f);
        EntityRenderDispatcher.instance.viewLerpYaw = 180.0f;
        EntityRenderDispatcher.instance.renderEntityWithPosYaw(Tessellator.instance, this.mc.thePlayer, 0.0, 0.0, 0.0, 0.0f, 1.0f);
        this.mc.thePlayer.entityBrightness = 0.0f;
        this.mc.thePlayer.yBodyRot = f2;
        this.mc.thePlayer.yRot = f3;
        this.mc.thePlayer.xRot = f4;
        GL11.glPopMatrix();
        Lighting.disable();
        GL11.glDisable(32826);
    }

    @Override
    public void guiSpecificControllerInput(final ControllerInput controllerInput) {
        if (controllerInput.buttonLeftTrigger.pressedThisFrame() && this.player.getGamemode() == Gamemode.creative){
            openCreative();
        }
        if (controllerInput.buttonRightTrigger.pressedThisFrame()){
            openCrafting();
        }
    }

    @Override
    public boolean playDefaultPressSound() {
        return true;
    }

    @Override
    public boolean enableDefaultSnapping() {
        return true;
    }
}
