package useless.legacyui.gui.screens;

import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.container.ScreenInventory;
import net.minecraft.client.input.InputType;
import net.minecraft.client.input.controller.ControllerInput;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.lang.I18n;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import useless.legacyui.gui.containers.LegacyContainerPlayerCreative;
import useless.legacyui.gui.elements.GuiButtonPrompt;
import useless.legacyui.gui.elements.GuiRegion;
import useless.legacyui.gui.IGuiController;
import useless.legacyui.helper.RepeatInputHandler;
import useless.legacyui.LegacySoundManager;
import useless.legacyui.LegacyUI;
import useless.legacyui.sorting.LegacyCategoryManager;

import java.util.ArrayList;
import java.util.List;

import static useless.legacyui.helper.KeyboardHelper.repeatInput;

public class GuiLegacyCreative extends ScreenInventory implements IGuiController {
    private final Player player;
    private int GUIx;
    private int GUIy;
    private static final int guiTextureWidth = 512;
    private static final int tabWidth = 35;
    public static int currentTab = 0;
    public static int currentRow = 0;
    private static float scrollProgress = 0f;
    public static LegacyContainerPlayerCreative container;
    protected GuiRegion scrollBar;
    protected GuiRegion bottomCreativeSlots;
    protected GuiRegion topCreativeSlots;
    protected ButtonElement clearButton;
    protected ButtonElement craftButton;
    protected ButtonElement[] tabButtons = new ButtonElement[8];
    protected ButtonElement lastPageButton;
    protected ButtonElement nextPageButton;
    public List<GuiButtonPrompt> prompts = new ArrayList<>();
    public GuiLegacyCreative(final Player player) {
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
    public void selectTab(final int value){
        if (currentTab != value){
            LegacySoundManager.play.focus(true);
        }
        currentTab = value;
        final int tabAmount = LegacyCategoryManager.getCreativeCategories().size();
        if (currentTab > tabAmount-1){
            currentTab -= tabAmount;
        } else if (currentTab < 0){
            currentTab += tabAmount;
        }
        currentTab = Math.min(currentTab, tabAmount-1);
        selectRow(0);
        setContainerSlots();
    }
    public void selectRow(final int value){
        final boolean doContainer = value != currentRow;
        currentRow = value;
        currentRow = Math.min(currentRow, (LegacyContainerPlayerCreative.getTotalRows()-6));
        currentRow = Math.max(currentRow,0);
        if (doContainer){
            setContainerSlots();
        }
    }
    public void handleInputs(){
        selectRow(currentRow + (-Mouse.getDWheel()));
        final boolean shifted = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);

        if (repeatInput(this.mc.gameSettings.keyRight.getKeyCode(), UtilGui.tabScrollRepeatDelay, UtilGui.tabScrollInitialDelay) || repeatInput(this.mc.gameSettings.keyLookRight.getKeyCode(), UtilGui.tabScrollRepeatDelay, UtilGui.tabScrollInitialDelay)){
            if (shifted){
                scrollTab(1);
            }
        }
        if (repeatInput(this.mc.gameSettings.keyLeft.getKeyCode(), UtilGui.tabScrollRepeatDelay, UtilGui.tabScrollInitialDelay) || repeatInput(this.mc.gameSettings.keyLookLeft.getKeyCode(), UtilGui.tabScrollRepeatDelay, UtilGui.tabScrollInitialDelay)){
            if (shifted){
                scrollTab(-1);
            }
        }
    }

    @Override
    protected void buttonClicked(final ButtonElement guibutton) {
        super.buttonClicked(guibutton);
        final boolean shifted = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
        if (guibutton == this.clearButton){
            if (this.mc.inputType == InputType.CONTROLLER){
                boolean hotbarCleared = true;
                for (int i = 0; i < 9; ++i) {
                    hotbarCleared = hotbarCleared && this.player.inventory.mainInventory[i] == null;
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
        if (guibutton == this.craftButton){
            openInventory();
        }
        for (int i = 0; i < this.tabButtons.length; i++) {
            if (this.tabButtons[i] == guibutton){
                selectTab(getPageNumber()*8+i);
            }
        }
        if (guibutton == this.nextPageButton){
            selectPage(getPageNumber() + 1);
        }
        if (guibutton == this.lastPageButton){
            selectPage(getPageNumber() - 1);
        }
    }
    private void clearInventory(){
        for (int i = 0; i < container.getCreativeSlotsStart(); ++i) {
            this.mc.playerController.handleInventoryMouseClick(container.containerId, InventoryAction.CREATIVE_DELETE, new int[]{i}, this.player);
        }

    }
    private void clearHotbar(){
        for (int i = container.getCreativeSlotsStart()-9; i < container.getCreativeSlotsStart(); ++i) {
            this.mc.playerController.handleInventoryMouseClick(container.containerId, InventoryAction.CREATIVE_DELETE, new int[]{i}, this.player);
        }
    }
    protected void openInventory(){
        LegacySoundManager.volume = 0;
        this.removed();
        this.mc.displayScreen(new GuiLegacyInventory(this.player));
        LegacySoundManager.volume = 1f;
    }
    public void setContainerSlots(){
        for (int i = 0; i < this.tabButtons.length; i++) { // Only enable buttons if there is a corresponding item group
            this.tabButtons[i].enabled = (getPageNumber() * 8 + i) < LegacyCategoryManager.getCreativeCategories().size();
        }
        container.setSlots();
    }
    @Override
    public void init() {
        super.init();
        for (final ButtonElement button : this.buttons){
            button.visible = false;
            button.enabled = false;
        }
        this.buttons.clear();
        // Setup size variables
        this.xSize = 273;
        this.ySize = 184;
        GUIx = (this.width - this.xSize) / 2;
        GUIy = (this.height - this.ySize) / 2;

        for (int i = 0; i < this.tabButtons.length; i++) {
            this.tabButtons[i] = new ButtonElement(this.buttons.size() + 2, GUIx + (tabWidth-1)*i, GUIy, tabWidth-1, 24, "");
            this.tabButtons[i].mute();
            this.tabButtons[i].visible = false;
            this.buttons.add(this.tabButtons[i]);
        }

        this.scrollBar = new GuiRegion(100, GUIx + 251, GUIy + 43, 15, 112);
        this.bottomCreativeSlots = new GuiRegion(101, GUIx + 11, GUIy + 135, 234, 18);
        this.topCreativeSlots = new GuiRegion(101, GUIx + 11, GUIy + 45, 234, 18);
        this.clearButton = new ButtonElement(this.buttons.size() + 1, GUIx + 221, GUIy + 158, 20, 20, "X");
        this.clearButton.visible = false;
        this.buttons.add(this.clearButton);
        this.craftButton = new ButtonElement(this.buttons.size() + 1, GUIx + 31, GUIy + 158, 20, 20, "");
        this.craftButton.visible = false;
        this.buttons.add(this.craftButton);
        this.nextPageButton = new ButtonElement(this.buttons.size() + 1, GUIx + this.xSize + 2, GUIy + 4, 20, 20, ">");
        this.nextPageButton.visible = LegacyCategoryManager.getCreativeCategories().size() > 8;
        this.buttons.add(this.nextPageButton);
        this.lastPageButton = new ButtonElement(this.buttons.size() + 1, GUIx - 22, GUIy + 4, 20, 20, "<");
        this.lastPageButton.visible = LegacyCategoryManager.getCreativeCategories().size() > 8;
        this.buttons.add(this.lastPageButton);

        final I18n translator = I18n.getInstance();
        this.prompts.clear();
        this.prompts.add(new GuiButtonPrompt( 101, 50, this.height-30, 3,translator.translateKey("legacyui.prompt.select"), new int[]{GuiButtonPrompt.A}));
        this.prompts.add(new GuiButtonPrompt( 102, this.prompts.get(this.prompts.size()-1).xPosition + this.prompts.get(this.prompts.size()-1).width + 3, this.height-30,3,translator.translateKey("legacyui.prompt.takestack"), new int[]{GuiButtonPrompt.X}));
        this.prompts.add(new GuiButtonPrompt( 103, this.prompts.get(this.prompts.size()-1).xPosition + this.prompts.get(this.prompts.size()-1).width + 3, this.height-30,3,translator.translateKey("legacyui.prompt.back"), new int[]{GuiButtonPrompt.B}));
        this.prompts.add(new GuiButtonPrompt( 104, this.prompts.get(this.prompts.size()-1).xPosition + this.prompts.get(this.prompts.size()-1).width + 3, this.height-30,3,translator.translateKey("legacyui.prompt.tabselect"), new int[]{GuiButtonPrompt.LEFT_BUMPER, GuiButtonPrompt.RIGHT_BUMPER}));
        this.prompts.add(new GuiButtonPrompt( 105, this.prompts.get(this.prompts.size()-1).xPosition + this.prompts.get(this.prompts.size()-1).width + 3, this.height-30,3,translator.translateKey("legacyui.prompt.openinventory"), new int[]{GuiButtonPrompt.LEFT_TRIGGER}));

        selectTab(0);
        selectRow(0);
        setContainerSlots();
    }

    @Override
    public void render(final int mx, final int my, final float partialTick) {
        handleInputs();
        if (this.scrollBar.isHovered(mx,my)){
            if (Mouse.isButtonDown(0)){
                scrollProgress = (my- this.scrollBar.getY())/ (float) this.scrollBar.getHeight();
                selectRow(Math.round((LegacyContainerPlayerCreative.getTotalRows() - LegacyContainerPlayerCreative.slotsTall) * scrollProgress));
                if (LegacyContainerPlayerCreative.getTotalRows() <= LegacyContainerPlayerCreative.slotsTall){
                    scrollProgress = 0f;
                }
                setContainerSlots();
            }
        }
        if (this.mc.inputType == InputType.CONTROLLER){
            if (this.scrollBar.isHovered((int) this.mc.controllerInput.cursorX, (int) this.mc.controllerInput.cursorY)){
                if (this.mc.controllerInput.buttonA.isPressed()){
                    scrollProgress = (float) ((this.mc.controllerInput.cursorY- this.scrollBar.getY())/ this.scrollBar.getHeight());
                    selectRow(Math.round((LegacyContainerPlayerCreative.getTotalRows() - LegacyContainerPlayerCreative.slotsTall) * scrollProgress));
                    if (LegacyContainerPlayerCreative.getTotalRows() <= LegacyContainerPlayerCreative.slotsTall){
                        scrollProgress = 0f;
                    }
                    setContainerSlots();
                }
            }
        }

        super.render(mx,my, partialTick);
        GL11.glColor4f(1, 1, 1, 1);
        this.mc.textureManager.loadTexture("/assets/legacyui/textures/gui/legacycreative.png").bind();
        UtilGui.drawTexturedModalRect(this, this.craftButton.xPosition, this.craftButton.yPosition, this.craftButton.isHovered(mx, my) ? 186 + this.craftButton.width : 186, 184, this.craftButton.width, this.craftButton.height, 1f/guiTextureWidth); // draw craftButton
        UtilGui.drawTexturedModalRect(this, this.clearButton.xPosition, this.clearButton.yPosition, this.clearButton.isHovered(mx, my) ? 146 + this.clearButton.width : 146, 184, this.clearButton.width, this.clearButton.height, 1f/guiTextureWidth); // draw clearbutton
        drawStringCentered(this.font, this.clearButton.displayString, this.clearButton.xPosition + (this.clearButton.width/2), this.clearButton.yPosition + 6, LegacyUI.modSettings.legacyui$getGuiPromptColor().value.value);
        for (final GuiButtonPrompt prompt: this.prompts) {
            prompt.drawPrompt(this.mc, mx, my);
        }
    }
    protected void drawGuiContainerForegroundLayer(){
    }
    protected void drawGuiContainerBackgroundLayer(final float renderPartialTick) {
        GL11.glColor4f(1, 1, 1, 1);
        this.mc.textureManager.loadTexture("/assets/legacyui/textures/gui/legacycreative.png").bind();
        UtilGui.drawTexturedModalRect(this, GUIx,GUIy, 0, 0, this.xSize, this.ySize,1f/guiTextureWidth); // GUI Background
        UtilGui.drawTexturedModalRect(this, GUIx + (tabWidth - 1) * (currentTab % 8), GUIy - 2, (tabWidth) * (currentTab % 8),215, tabWidth, 30, 1f/guiTextureWidth); // Render Selected Tab

        final float scrollProgressLimited = ((float) currentRow) /(LegacyContainerPlayerCreative.getTotalRows()-LegacyContainerPlayerCreative.slotsTall);
        UtilGui.drawTexturedModalRect(this, this.scrollBar.xPosition, (this.scrollBar.yPosition + (int) ((this.scrollBar.height-15)*scrollProgressLimited)),131,184,15,15,1f/guiTextureWidth);

        final int iconAmountToDraw = Math.min(LegacyCategoryManager.getCreativeCategories().size() - (getPageNumber() * 8), 8);
        for (int i = 0; i < iconAmountToDraw; i++) {
            final boolean isSelected = (currentTab % 8) == i;
            if (isSelected){
                final double x0 = GUIx + 3 + (tabWidth - 1) * i;
                final double y0 = GUIy - 1;
                final IconCoordinate coordinate = LegacyCategoryManager.getCreativeCategories().get(getPageNumber()*8 + i).iconCoordinate;
                drawIconTextureDouble(x0, y0, x0 + 32 * 0.9f, y0 + 32 * 0.9f, 0, 0, coordinate.width, coordinate.height, coordinate);
            } else {
                final double x0 = GUIx + 5.5 + (tabWidth - 1) * i;
                final double y0 = GUIy + 2;
                final IconCoordinate coordinate = LegacyCategoryManager.getCreativeCategories().get(getPageNumber()*8 + i).iconCoordinate;
                drawIconTextureDouble(x0, y0, x0 + 32 * 0.75f, y0 + 32 * 0.75f, 0, 0, coordinate.width, coordinate.height, coordinate);
            }
        }

        drawStringCenteredNoShadow(this.font, LegacyCategoryManager.getCreativeCategories().get(currentTab).getTranslatedKey(), GUIx + this.xSize /2, GUIy + 32, 0x404040);
    }

    @Override
    public void guiSpecificControllerInput(final ControllerInput controllerInput) {
        if (controllerInput.buttonRightShoulder.pressedThisFrame() || controllerInput.buttonRightShoulder.isPressed() && RepeatInputHandler.doRepeatInput(-2, UtilGui.tabScrollRepeatDelay) && controllerInput.buttonRightShoulder.getHoldTime() > 3){
            RepeatInputHandler.manualSuccess(-2);
            scrollTab(1);
        }
        if (controllerInput.buttonLeftShoulder.pressedThisFrame() || controllerInput.buttonLeftShoulder.isPressed() && RepeatInputHandler.doRepeatInput(-2, UtilGui.tabScrollRepeatDelay) && controllerInput.buttonLeftShoulder.getHoldTime() > 3){
            RepeatInputHandler.manualSuccess(-2);
            scrollTab(-1);
        }
        if (controllerInput.joyRight.getY() >= 0.8f){
            selectRow(currentRow + 1);
        }
        if (controllerInput.joyRight.getY() <= -0.8f){
            selectRow(currentRow - 1);
        }
        if (this.bottomCreativeSlots.isHovered((int) this.mc.controllerInput.cursorX, (int) this.mc.controllerInput.cursorY)){
            if (this.mc.controllerInput.digitalPad.down.pressedThisFrame()){
                selectRow(currentRow + 1);
            }
        }
        if (this.topCreativeSlots.isHovered((int) this.mc.controllerInput.cursorX, (int) this.mc.controllerInput.cursorY)){
            if (this.mc.controllerInput.digitalPad.up.pressedThisFrame()){
                selectRow(currentRow - 1);
            }
        }
        if (controllerInput.buttonLeftTrigger.pressedThisFrame()){
            openInventory();
        }
    }

    @Override
    public boolean playDefaultPressSound() {
        return false;
    }

    @Override
    public boolean enableDefaultSnapping() {
        if (this.bottomCreativeSlots.isHovered((int) this.mc.controllerInput.cursorX, (int) this.mc.controllerInput.cursorY)){
            if (this.mc.controllerInput.digitalPad.down.pressedThisFrame()){
                return false;
            }
        }
        if (this.topCreativeSlots.isHovered((int) this.mc.controllerInput.cursorX, (int) this.mc.controllerInput.cursorY)){
            return !this.mc.controllerInput.digitalPad.up.pressedThisFrame();
        }
        return true;
    }
    public static int getPageNumber(){
        return currentTab/8;
    }
    public void selectPage(final int pageNumber){
        int desiredPage = pageNumber;
        if (desiredPage < 0){
            desiredPage = LegacyCategoryManager.getCreativeCategories().size()/8;
        }
        if (desiredPage > LegacyCategoryManager.getCreativeCategories().size()/8){
            desiredPage = 0;
        }
        selectTab(desiredPage * 8);
    }
    @Override
    public void updateOverlayButtons() {}
    @Override
    protected void checkForArmor() {}
}
