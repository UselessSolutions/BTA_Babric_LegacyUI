package useless.legacyui.Gui.GuiScreens;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiInventory;
import net.minecraft.client.input.InputType;
import net.minecraft.client.input.controller.ControllerInput;
import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.lang.I18n;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import useless.legacyui.Gui.Containers.LegacyContainerPlayerCreative;
import useless.legacyui.Gui.GuiElements.Buttons.GuiAuditoryButton;
import useless.legacyui.Gui.GuiElements.GuiButtonPrompt;
import useless.legacyui.Gui.GuiElements.GuiRegion;
import useless.legacyui.Gui.IGuiController;
import useless.legacyui.Helper.KeyboardHelper;
import useless.legacyui.LegacySoundManager;
import useless.legacyui.ModSettings;
import useless.legacyui.Sorting.LegacyCategoryManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GuiLegacyCreative extends GuiInventory implements IGuiController {
    private EntityPlayer player;
    private static int GUIx;
    private static int GUIy;
    private static final int guiTextureWidth = 512;
    private static final int tabWidth = 35;
    public static int currentTab = 0;
    public static int currentRow = 0;
    private static float scrollProgress = 0f;
    public static LegacyContainerPlayerCreative container;
    protected GuiRegion scrollBar;
    protected GuiRegion bottomCreativeSlots;
    protected GuiRegion topCreativeSlots;
    protected GuiAuditoryButton clearButton;
    protected GuiAuditoryButton craftButton;
    protected GuiAuditoryButton[] tabButtons = new GuiAuditoryButton[8];
    public List<GuiButtonPrompt> prompts = new ArrayList<>();
    public GuiLegacyCreative(EntityPlayer player) {
        super(player);
        this.player = player;
        container = (LegacyContainerPlayerCreative)player.inventorySlots;
    }
    public void scrollTab(int direction){
        if (direction > 0){
            while (direction > 0){
                selectTab(currentTab + 1);
                direction--;
            }
        } else if (direction < 0){
            while (direction < 0){
                selectTab(currentTab - 1);
                direction++;
            }
        }
    }
    public void selectTab(int value){
        if (currentTab != value){
            LegacySoundManager.play.focus(true);
        }
        currentTab = value;
        int tabAmount = Math.min(8, LegacyCategoryManager.creativeCategories.size());
        if (currentTab > tabAmount-1){
            currentTab -= tabAmount;
        } else if (currentTab < 0){
            currentTab += tabAmount;
        }
        currentTab = Math.min(currentTab, tabAmount-1);
        selectRow(0);
        setContainerSlots();
    }
    public void selectRow(int value){
        boolean doContainer = false;
        if (value != currentRow){
            doContainer = true;
        }
        currentRow = value;
        currentRow = Math.min(currentRow, (LegacyContainerPlayerCreative.getTotalRows()-6));
        currentRow = Math.max(currentRow,0);
        if (doContainer){
            setContainerSlots();
        }
    }
    public void handleInputs(){
        selectRow(currentRow + (Mouse.getDWheel()/-120));
        boolean shifted = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
        if (KeyboardHelper.isKeyPressedThisFrame(mc.gameSettings.keyRight.keyCode()) || KeyboardHelper.isKeyPressedThisFrame(mc.gameSettings.keyLookRight.keyCode())){
            if (shifted){
                scrollTab(1);
            }
        }
        if (KeyboardHelper.isKeyPressedThisFrame(mc.gameSettings.keyLeft.keyCode()) || KeyboardHelper.isKeyPressedThisFrame(mc.gameSettings.keyLookLeft.keyCode())){
            if (shifted){
                scrollTab(-1);
            }
        }
    }
    protected void buttonPressed(GuiButton guibutton) {
        super.buttonPressed(guibutton);
        boolean shifted = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
        if (guibutton == clearButton){
            if (shifted){
                clearInventory();
            } else {
                clearHotbar();
            }
        }
        if (guibutton == craftButton){
            openCrafting();
        }
        for (int i = 0; i < tabButtons.length; i++) {
            if (tabButtons[i] == guibutton){
                selectTab(i);
            }
        }
    }
    private void clearInventory(){
        for (int i = 0; i < container.getCreativeSlotsStart(); ++i) {
            mc.playerController.doInventoryAction(container.windowId, InventoryAction.CREATIVE_DELETE, new int[]{i}, player);
        }

    }
    private void clearHotbar(){
        for (int i = container.getCreativeSlotsStart()-10; i < container.getCreativeSlotsStart(); ++i) {
            mc.playerController.doInventoryAction(container.windowId, InventoryAction.CREATIVE_DELETE, new int[]{i}, player);
        }
    }
    protected void openCrafting(){
        LegacySoundManager.volume = 0;
        this.onGuiClosed();
        mc.displayGuiScreen(new GuiLegacyCrafting(player, 4));
        LegacySoundManager.volume = 1f;
    }
    public void setContainerSlots(){
        for (int i = 0; i < tabButtons.length; i++) { // Only enable buttons if there is a corresponding recipe group
            tabButtons[i].enabled = i < LegacyCategoryManager.creativeCategories.size();
        }
        container.setSlots();
    }
    @Override
    public void initGui() {
        this.controlList.clear();
        // Setup size variables
        this.xSize = 273;
        this.ySize = 184;
        GUIx = (this.width - this.xSize) / 2;
        GUIy = (this.height - this.ySize) / 2;

        for (int i = 0; i < tabButtons.length; i++) {
            tabButtons[i] = new GuiAuditoryButton(controlList.size() + 2, GUIx + (tabWidth-1)*i, GUIy, tabWidth-1, 24, "");
            tabButtons[i].setMuted(true);
            tabButtons[i].visible = false;
            controlList.add(tabButtons[i]);
        }

        scrollBar = new GuiRegion(100, GUIx + 251, GUIy + 43, 15, 112);
        bottomCreativeSlots = new GuiRegion(101, GUIx + 11, GUIy + 135, 234, 18);
        topCreativeSlots = new GuiRegion(101, GUIx + 11, GUIy + 45, 234, 18);
        clearButton = new GuiAuditoryButton(controlList.size() + 2, GUIx + 221, GUIy + 158, 20, 20, "X");
        clearButton.visible = false;
        controlList.add(clearButton);
        craftButton = new GuiAuditoryButton(controlList.size() + 2, GUIx + 31, GUIy + 158, 20, 20, "");
        craftButton.visible = false;
        controlList.add(craftButton);

        I18n translator = I18n.getInstance();
        prompts.add(new GuiButtonPrompt( 101, 50, this.height-30, 0, 3,translator.translateKey("legacyui.prompt.takeone")));
        prompts.add(new GuiButtonPrompt( 102, prompts.get(prompts.size()-1).xPosition + prompts.get(prompts.size()-1).width + 3, this.height-30, 2, 3,translator.translateKey("legacyui.prompt.takestack")));
        prompts.add(new GuiButtonPrompt( 102, prompts.get(prompts.size()-1).xPosition + prompts.get(prompts.size()-1).width + 3, this.height-30, 1, 3,translator.translateKey("legacyui.prompt.back")));
        prompts.add(new GuiButtonPrompt( 102, prompts.get(prompts.size()-1).xPosition + prompts.get(prompts.size()-1).width + 3, this.height-30, 9,10, 3,translator.translateKey("legacyui.prompt.tabselect")));

        selectTab(0);
        selectRow(0);
        setContainerSlots();
    }
    public void drawScreen(int x, int y, float renderPartialTicks) {
        handleInputs();
        if (scrollBar.isHovered(x,y)){
            if (Mouse.isButtonDown(0)){
                scrollProgress = (y-scrollBar.getY())/ (float)scrollBar.getHeight();
                selectRow(Math.round((LegacyContainerPlayerCreative.getTotalRows() - LegacyContainerPlayerCreative.slotsTall) * scrollProgress));
                if (LegacyContainerPlayerCreative.getTotalRows() <= LegacyContainerPlayerCreative.slotsTall){
                    scrollProgress = 0f;
                }
                setContainerSlots();
            }
        }
        if (mc.inputType == InputType.CONTROLLER){
            if (scrollBar.isHovered((int) mc.controllerInput.cursorX, (int) mc.controllerInput.cursorY)){
                if (mc.controllerInput.buttonA.isPressed()){
                    scrollProgress = (float) ((mc.controllerInput.cursorY-scrollBar.getY())/scrollBar.getHeight());
                    selectRow(Math.round((LegacyContainerPlayerCreative.getTotalRows() - LegacyContainerPlayerCreative.slotsTall) * scrollProgress));
                    if (LegacyContainerPlayerCreative.getTotalRows() <= LegacyContainerPlayerCreative.slotsTall){
                        scrollProgress = 0f;
                    }
                    setContainerSlots();
                }
            }
        }

        super.drawScreen(x,y, renderPartialTicks);
        UtilGui.bindTexture("/assets/legacyui/gui/legacycreative.png");
        UtilGui.drawTexturedModalRect(this, craftButton.xPosition, craftButton.yPosition, craftButton.isHovered(x, y) ? 186+craftButton.width:186, 184, craftButton.width, craftButton.height, 1f/guiTextureWidth); // draw craftButton
        UtilGui.drawTexturedModalRect(this, clearButton.xPosition, clearButton.yPosition, clearButton.isHovered(x, y) ? 146+clearButton.width:146, 184, clearButton.width, clearButton.height, 1f/guiTextureWidth); // draw clearbutton
        drawStringCentered(fontRenderer, clearButton.displayString, clearButton.xPosition + (clearButton.width/2), clearButton.yPosition + 6, ModSettings.Colors.GuiPromptColor());
        if (mc.inputType == InputType.CONTROLLER){
            for (GuiButtonPrompt prompt: prompts) {
                prompt.drawPrompt(mc, x, y);
            }
        }
    }
    protected void drawGuiContainerForegroundLayer(){
    }
    protected void drawGuiContainerBackgroundLayer(float renderPartialTick) {
        UtilGui.bindTexture("/assets/legacyui/gui/legacycreative.png");
        UtilGui.drawTexturedModalRect(this, GUIx,GUIy, 0, 0, xSize, ySize,1f/guiTextureWidth); // GUI Background
        UtilGui.drawTexturedModalRect(this, GUIx + (tabWidth - 1) * currentTab, GUIy - 2, 0,184, tabWidth, 30, 1f/guiTextureWidth); // Render Selected Tab

        float scrollProgressLimited = ((float) currentRow) /(LegacyContainerPlayerCreative.getTotalRows()-LegacyContainerPlayerCreative.slotsTall);
        UtilGui.drawTexturedModalRect(this,scrollBar.xPosition, (scrollBar.yPosition + (int) ((scrollBar.height-15)*scrollProgressLimited)),131,184,15,15,1f/guiTextureWidth);

        UtilGui.bindTexture("/assets/legacyui/gui/icons.png");
        for (int i = 0; i < Math.min(LegacyCategoryManager.creativeCategories.size(), 8); i++) {
            UtilGui.drawIconTexture(this, GUIx + 5 + (tabWidth - 1) * i, GUIy + 2, LegacyCategoryManager.creativeCategories.get(i).iconCoordinate, 0.75f); // Render Icon
        }

        drawStringCenteredNoShadow(fontRenderer, LegacyCategoryManager.creativeCategories.get(currentTab).getTranslatedKey(), GUIx + xSize/2, GUIy + 32, ModSettings.Colors.GuiLabelColor());
    }

    @Override
    public void GuiControls(ControllerInput controllerInput) {
        if (controllerInput.buttonR.pressedThisFrame()){
            scrollTab(1);
        }
        if (controllerInput.buttonL.pressedThisFrame()){
            scrollTab(-1);
        }
        if (controllerInput.joyRight.getY() >= 0.8f){
            selectRow(currentRow + 1);
        }
        if (controllerInput.joyRight.getY() <= -0.8f){
            selectRow(currentRow - 1);
        }
        if (bottomCreativeSlots.isHovered((int) mc.controllerInput.cursorX, (int) mc.controllerInput.cursorY)){
            if (mc.controllerInput.digitalPad.down.pressedThisFrame()){
                selectRow(currentRow + 1);
            }
        }
        if (topCreativeSlots.isHovered((int) mc.controllerInput.cursorX, (int) mc.controllerInput.cursorY)){
            if (mc.controllerInput.digitalPad.up.pressedThisFrame()){
                selectRow(currentRow - 1);
            }
        }
    }

    @Override
    public boolean playDefaultPressSound() {
        return false;
    }

    @Override
    public boolean enableDefaultSnapping() {
        if (bottomCreativeSlots.isHovered((int) mc.controllerInput.cursorX, (int) mc.controllerInput.cursorY)){
            if (mc.controllerInput.digitalPad.down.pressedThisFrame()){
                return false;
            }
        }
        if (topCreativeSlots.isHovered((int) mc.controllerInput.cursorX, (int) mc.controllerInput.cursorY)){
            if (mc.controllerInput.digitalPad.up.pressedThisFrame()){
                return false;
            }
        }
        return true;
    }
}
