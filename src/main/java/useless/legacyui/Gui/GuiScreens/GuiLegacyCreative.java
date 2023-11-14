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
import useless.legacyui.Helper.IconHelper;
import useless.legacyui.Helper.KeyboardHelper;
import useless.legacyui.LegacySoundManager;
import useless.legacyui.LegacyUI;
import useless.legacyui.Sorting.LegacyCategoryManager;

import java.util.ArrayList;
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
    protected GuiAuditoryButton lastPageButton;
    protected GuiAuditoryButton nextPageButton;
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
        int tabAmount = LegacyCategoryManager.getCreativeCategories().size();
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

        if (KeyboardHelper.repeatInput(mc.gameSettings.keyRight.keyCode(), UtilGui.tabScrollRepeatDelay, UtilGui.tabScrollInitialDelay) || KeyboardHelper.repeatInput(mc.gameSettings.keyLookRight.keyCode(), UtilGui.tabScrollRepeatDelay, UtilGui.tabScrollInitialDelay)){
            if (shifted){
                scrollTab(1);
            }
        }
        if (KeyboardHelper.repeatInput(mc.gameSettings.keyLeft.keyCode(), UtilGui.tabScrollRepeatDelay, UtilGui.tabScrollInitialDelay) || KeyboardHelper.repeatInput(mc.gameSettings.keyLookLeft.keyCode(), UtilGui.tabScrollRepeatDelay, UtilGui.tabScrollInitialDelay)){
            if (shifted){
                scrollTab(-1);
            }
        }
    }
    protected void buttonPressed(GuiButton guibutton) {
        super.buttonPressed(guibutton);
        boolean shifted = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
        if (guibutton == clearButton){
            if (mc.inputType == InputType.CONTROLLER){
                boolean hotbarCleared = true;
                for (int i = 0; i < 9; ++i) {
                    hotbarCleared = hotbarCleared && player.inventory.mainInventory[i] == null;
                }
                if (hotbarCleared){
                    clearInventory();
                } else {
                    clearHotbar();
                }
            } else {
                if (shifted){
                    clearInventory();
                } else {
                    clearHotbar();
                }
            }

        }
        if (guibutton == craftButton){
            openInventory();
        }
        for (int i = 0; i < tabButtons.length; i++) {
            if (tabButtons[i] == guibutton){
                selectTab(getPageNumber()*8+i);
            }
        }
        if (guibutton == nextPageButton){
            selectPage(getPageNumber() + 1);
        }
        if (guibutton == lastPageButton){
            selectPage(getPageNumber() - 1);
        }
    }
    private void clearInventory(){
        for (int i = 0; i < container.getCreativeSlotsStart(); ++i) {
            mc.playerController.doInventoryAction(container.windowId, InventoryAction.CREATIVE_DELETE, new int[]{i}, player);
        }

    }
    private void clearHotbar(){
        for (int i = container.getCreativeSlotsStart()-9; i < container.getCreativeSlotsStart(); ++i) {
            mc.playerController.doInventoryAction(container.windowId, InventoryAction.CREATIVE_DELETE, new int[]{i}, player);
        }
    }
    protected void openInventory(){
        LegacySoundManager.volume = 0;
        this.onGuiClosed();
        mc.displayGuiScreen(new GuiLegacyInventory(player));
        LegacySoundManager.volume = 1f;
    }
    public void setContainerSlots(){
        for (int i = 0; i < tabButtons.length; i++) { // Only enable buttons if there is a corresponding item group
            tabButtons[i].enabled = (getPageNumber() * 8 + i) < LegacyCategoryManager.getCreativeCategories().size();
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
        clearButton = new GuiAuditoryButton(controlList.size() + 1, GUIx + 221, GUIy + 158, 20, 20, "X");
        clearButton.visible = false;
        controlList.add(clearButton);
        craftButton = new GuiAuditoryButton(controlList.size() + 1, GUIx + 31, GUIy + 158, 20, 20, "");
        craftButton.visible = false;
        controlList.add(craftButton);
        nextPageButton = new GuiAuditoryButton(controlList.size() + 1, GUIx + xSize + 2, GUIy + 4, 20, 20, ">");
        nextPageButton.visible = LegacyCategoryManager.getCreativeCategories().size() > 8;
        controlList.add(nextPageButton);
        lastPageButton = new GuiAuditoryButton(controlList.size() + 1, GUIx - 22, GUIy + 4, 20, 20, "<");
        lastPageButton.visible = LegacyCategoryManager.getCreativeCategories().size() > 8;
        controlList.add(lastPageButton);

        I18n translator = I18n.getInstance();
        prompts.clear();
        prompts.add(new GuiButtonPrompt( 101, 50, this.height-30, 3,translator.translateKey("legacyui.prompt.select"), new int[]{GuiButtonPrompt.A}));
        prompts.add(new GuiButtonPrompt( 102, prompts.get(prompts.size()-1).xPosition + prompts.get(prompts.size()-1).width + 3, this.height-30,3,translator.translateKey("legacyui.prompt.takestack"), new int[]{GuiButtonPrompt.X}));
        prompts.add(new GuiButtonPrompt( 103, prompts.get(prompts.size()-1).xPosition + prompts.get(prompts.size()-1).width + 3, this.height-30,3,translator.translateKey("legacyui.prompt.back"), new int[]{GuiButtonPrompt.B}));
        prompts.add(new GuiButtonPrompt( 104, prompts.get(prompts.size()-1).xPosition + prompts.get(prompts.size()-1).width + 3, this.height-30,3,translator.translateKey("legacyui.prompt.tabselect"), new int[]{GuiButtonPrompt.LEFT_BUMPER, GuiButtonPrompt.RIGHT_BUMPER}));
        prompts.add(new GuiButtonPrompt( 105, prompts.get(prompts.size()-1).xPosition + prompts.get(prompts.size()-1).width + 3, this.height-30,3,translator.translateKey("legacyui.prompt.openinventory"), new int[]{GuiButtonPrompt.LEFT_TRIGGER}));

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
        drawStringCentered(fontRenderer, clearButton.displayString, clearButton.xPosition + (clearButton.width/2), clearButton.yPosition + 6, LegacyUI.modSettings.getGuiPromptColor().value.value);
        for (GuiButtonPrompt prompt: prompts) {
            prompt.drawPrompt(mc, x, y);
        }
    }
    protected void drawGuiContainerForegroundLayer(){
    }
    protected void drawGuiContainerBackgroundLayer(float renderPartialTick) {
        UtilGui.bindTexture("/assets/legacyui/gui/legacycreative.png");
        UtilGui.drawTexturedModalRect(this, GUIx,GUIy, 0, 0, xSize, ySize,1f/guiTextureWidth); // GUI Background
        UtilGui.drawTexturedModalRect(this, GUIx + (tabWidth - 1) * (currentTab % 8), GUIy - 2, (tabWidth-1) * (currentTab % 8),215, tabWidth, 30, 1f/guiTextureWidth); // Render Selected Tab

        float scrollProgressLimited = ((float) currentRow) /(LegacyContainerPlayerCreative.getTotalRows()-LegacyContainerPlayerCreative.slotsTall);
        UtilGui.drawTexturedModalRect(this,scrollBar.xPosition, (scrollBar.yPosition + (int) ((scrollBar.height-15)*scrollProgressLimited)),131,184,15,15,1f/guiTextureWidth);

        UtilGui.bindTexture(IconHelper.ICON_TEXTURE);
        int iconAmountToDraw = Math.min(LegacyCategoryManager.getCreativeCategories().size() - (getPageNumber() * 8), 8);
        for (int i = 0; i < iconAmountToDraw; i++) {
            boolean isSelected = (currentTab % 8) == i;
            if (isSelected){
                UtilGui.drawIconTexture(this, GUIx + 3 + (tabWidth - 1) * i, GUIy - 1, LegacyCategoryManager.getCreativeCategories().get(getPageNumber()*8 + i).iconCoordinate, 0.9f); // Render Icon
            } else {
                UtilGui.drawIconTexture(this, GUIx + 5.5 + (tabWidth - 1) * i, GUIy + 2, LegacyCategoryManager.getCreativeCategories().get(getPageNumber()*8 + i).iconCoordinate, 0.75f); // Render Icon
            }
        }

        drawStringCenteredNoShadow(fontRenderer, LegacyCategoryManager.getCreativeCategories().get(currentTab).getTranslatedKey(), GUIx + xSize/2, GUIy + 32, LegacyUI.modSettings.getGuiLabelColor().value.value);
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
        if (controllerInput.buttonZL.pressedThisFrame()){
            openInventory();
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
    public static int getPageNumber(){
        return currentTab/8;
    }
    public void selectPage(int pageNumber){
        int desiredPage = pageNumber;
        if (desiredPage < 0){
            desiredPage = LegacyCategoryManager.getCreativeCategories().size()/8;
        }
        if (desiredPage > LegacyCategoryManager.getCreativeCategories().size()/8){
            desiredPage = 0;
        }
        selectTab(desiredPage * 8);
    }
}
