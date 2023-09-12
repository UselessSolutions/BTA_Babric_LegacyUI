package useless.legacyui.Gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiInventory;
import net.minecraft.client.input.InputType;
import net.minecraft.core.InventoryAction;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.core.sound.SoundType;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import turniplabs.halplibe.helper.RecipeHelper;
import useless.legacyui.ConfigTranslations;
import useless.legacyui.Gui.Container.ContainerCreativeLegacy;
import useless.legacyui.LegacyUI;
import useless.legacyui.Sorting.Items.CategoryManager;
import useless.legacyui.Sorting.Items.ItemCategory;
import useless.legacyui.Utils.KeyboardUtil;
import useless.prismaticlibe.gui.GuiAuditoryButtons;

import java.util.Arrays;

public class GuiLegacyCreative extends GuiInventory {
    protected ContainerCreativeLegacy container;
    protected int tab; // Current page of tabs
    protected final int maxDisplayedTabs = 8; // Total amount of tab pages, zero index
    protected GuiAuditoryButtons[] tabButtons = new GuiAuditoryButtons[maxDisplayedTabs];
    protected GuiAuditoryButtons scrollBar;
    protected GuiAuditoryButtons clearInventory;
    protected int currentCursorColumn;
    protected int currentCursorRow;
    protected long lastKeyEvent = 0;
    protected long timeCursorEnabled = 0;
    private boolean clearIsHovered = false;
    private boolean _renderCursor = false;
    public GuiLegacyCreative(EntityPlayer player) {
        super(player);
        this.container = (ContainerCreativeLegacy)inventorySlots;
    }
    public void initGui() {
        this.xSize = 273; // width of texture plus the 17px strip that was cut off
        this.ySize = 175; // height of Gui window
        int j = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;
        for (int i = 0; i < tabButtons.length; i++) {
            tabButtons[i] = new GuiAuditoryButtons(controlList.size(),  j + 34 * i, k, 34, 24, "");
            tabButtons[i].visible = false;
            tabButtons[i].setMuted(true);
            this.controlList.add(tabButtons[i]);
        }

        scrollBar = new GuiAuditoryButtons(controlList.size(), j + 251, k + 35, 15, 112, "");
        scrollBar.visible = false;
        this.controlList.add(scrollBar);

        clearInventory = new GuiAuditoryButtons(controlList.size(), j + 209, k + 150, 18, 18, "X");
        clearInventory.visible = false;
        this.controlList.add(clearInventory);
        tab = 0;
        updatePages();
    }
    public void drawScreen(int x, int y, float renderPartialTicks) {
        super.drawScreen(x,y,renderPartialTicks);
        clearIsHovered = (clearInventory.isHovered(x,y) || (mc.inputType == InputType.CONTROLLER && clearInventory.isHovered((int)mc.controllerInput.cursorX, (int)mc.controllerInput.cursorY)));

        if (scrollBar.isHovered(x,y)){
            if (Mouse.isButtonDown(0)){
                float scrollProgress = (y-scrollBar.getY())/ (float)scrollBar.getHeight();
                ContainerCreativeLegacy.currentRow = Math.round(ContainerCreativeLegacy.getTotalRows() * scrollProgress);
            }
        }
        if (mc.inputType == InputType.CONTROLLER && scrollBar.isHovered((int)mc.controllerInput.cursorX, (int)mc.controllerInput.cursorY)) {
            if (mc.controllerInput.buttonA.isPressed()){
                float scrollProgress = (((int)mc.controllerInput.cursorY)-scrollBar.getY())/ (float)scrollBar.getHeight();
                ContainerCreativeLegacy.currentRow = Math.round(ContainerCreativeLegacy.getTotalRows() * scrollProgress);
            }
        }

    }
    protected void buttonPressed(GuiButton guibutton) {
        //LegacyUI.LOGGER.info("" + currentScroll);
        int i = 0;
        for (GuiButton button : tabButtons) {
            if (guibutton == button) {
                selectTab(i);
            }
            i++;
        }
        if (guibutton == clearInventory) {
            if (mc.inputType == InputType.KEYBOARD){
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){ // Clear inventory with shift click
                    clearInventory();
                } else { // Clear Hotbar
                    clearHotbar();
                }

            } else if (mc.inputType == InputType.CONTROLLER){
                boolean hotbarNotEmpty = false;
                for (i = 0; i < 9; ++i) {
                    hotbarNotEmpty = hotbarNotEmpty || container.playerInv.mainInventory[i] != null;
                }
                if (!hotbarNotEmpty){
                    clearInventory();
                } else {
                    clearHotbar();
                }
            }


        }
    }
    private void clearInventory(){
        for (int i = 0; i < this.container.playerInv.getSizeInventory(); ++i) {
            this.container.playerInv.setInventorySlotContents(i, null);
        }
        Arrays.fill(this.container.playerInv.armorInventory, null);
    }
    private void clearHotbar(){
        for (int i = 0; i < 9; ++i) {
            this.container.playerInv.setInventorySlotContents(i, null);
        }
    }
    public void setControllerCursorPosition() {
        if (this.mc.inputType == InputType.CONTROLLER) {
            int j = (this.width - this.xSize) / 2;
            int k = (this.height - this.ySize) / 2;
            Slot slot = container.inventorySlots.get(9);
            this.mc.controllerInput.cursorX = j + slot.xDisplayPosition + 9;
            this.mc.controllerInput.cursorY = k + slot.yDisplayPosition + 9;
        }
    }
    public void selectTab(int tabIndex) {
        resetCursorPosition();
        uiSound("legacyui.ui.focus");
        if (tabIndex < 0) {
            tabIndex += CategoryManager.size();
        } else if (tabIndex > CategoryManager.size() - 1) {
            tabIndex -= CategoryManager.size();
        }
        tab = tabIndex;
        setControllerCursorPosition();
        updatePages();
    }

    public void scrollTab(int direction) {
        if (direction > 0) {
            while (direction > 0) {
                selectTab(tab + 1);
                direction--;
            }
        } else if (direction < 0) {
            while (direction < 0) {
                selectTab(tab - 1);
                direction++;
            }
        }
    }

    public void scrollCursorColumn(int direction){
        enableCursor();
        if (direction > 0) {
            while (direction > 0) {
                selectCursorColumn(currentCursorColumn + 1);
                direction--;
            }
        } else if (direction < 0) {
            while (direction < 0) {
                selectCursorColumn(currentCursorColumn - 1);
                direction++;
            }
        }
    }
    public void selectCursorColumn(int columnIndex) {
        uiSound("legacyui.ui.focus");
        if (columnIndex < 0) {
            columnIndex += ContainerCreativeLegacy.slotsWide;
        } else if (columnIndex >= ContainerCreativeLegacy.slotsWide) {
            columnIndex -= ContainerCreativeLegacy.slotsWide;
        }
        currentCursorColumn = columnIndex;
        setControllerCursorPosition();
        updatePages();
    }
    public void scrollCursorRow(int direction){
        enableCursor();
        if (direction > 0) {
            while (direction > 0) {
                selectCursorRow(currentCursorRow + 1);
                direction--;
            }
        } else if (direction < 0) {
            while (direction < 0) {
                selectCursorRow(currentCursorRow - 1);
                direction++;
            }
        }
    }
    public void selectCursorRow(int rowIndex) {
        uiSound("legacyui.ui.focus");
        if (rowIndex < 0) {
            rowIndex = 0;
            ContainerCreativeLegacy.scrollRow(-1);
        } else if (rowIndex >= ContainerCreativeLegacy.slotsTall) {
            rowIndex = ContainerCreativeLegacy.slotsTall - 1;
            ContainerCreativeLegacy.scrollRow(1);
        }
        currentCursorRow = rowIndex;
        setControllerCursorPosition();
        updatePages();
    }
    protected void checkInputs(){
        if (isCursorEnabled() && System.currentTimeMillis() - timeCursorEnabled > 2000){
            disableCursor();
        }
        int inputDelay = 1000/8;
        if (repeatedInput(inputDelay)) {
            if (KeyboardUtil.isKeyHeld(mc.gameSettings.keyForward.keyCode()) || KeyboardUtil.isKeyHeld(mc.gameSettings.keyLookUp.keyCode())) {
                if (isCursorEnabled()){
                    scrollCursorRow(-1);
                }
                enableCursor();
                repeatedInputSuccess();
            }
            if (KeyboardUtil.isKeyHeld(mc.gameSettings.keyBack.keyCode()) || KeyboardUtil.isKeyHeld(mc.gameSettings.keyLookDown.keyCode())) {
                if (isCursorEnabled()){
                    scrollCursorRow(1);
                }
                enableCursor();
                repeatedInputSuccess();
            }
        }

        if ((KeyboardUtil.isKeyPressed(mc.gameSettings.keyRight.keyCode()) || KeyboardUtil.isKeyPressed(mc.gameSettings.keyLookRight.keyCode())) && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            scrollTab(1);
        }
        else if (repeatedInput(inputDelay) && (KeyboardUtil.isKeyHeld(mc.gameSettings.keyRight.keyCode()) || KeyboardUtil.isKeyHeld(mc.gameSettings.keyLookRight.keyCode())) && !Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            if (isCursorEnabled()){
                scrollCursorColumn(1);
            }
            enableCursor();
            repeatedInputSuccess();
        }


        if ((KeyboardUtil.isKeyPressed(mc.gameSettings.keyLeft.keyCode()) || KeyboardUtil.isKeyPressed(mc.gameSettings.keyLookLeft.keyCode())) && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
            scrollTab(-1);
        }
        else if (repeatedInput(inputDelay) && (KeyboardUtil.isKeyHeld(mc.gameSettings.keyLeft.keyCode()) || KeyboardUtil.isKeyHeld(mc.gameSettings.keyLookLeft.keyCode())) && !Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            if (isCursorEnabled()){
                scrollCursorColumn(-1);
            }
            enableCursor();
            repeatedInputSuccess();
        }

        if (KeyboardUtil.isKeyPressed(mc.gameSettings.keyJump.keyCode())){
            if (isCursorEnabled()){
                takeStack(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL));
            }
            enableCursor();
        }

        int mouseScroll = Mouse.getDWheel();
        if (mouseScroll/10 != 0){
            disableCursor();
        }
        ContainerCreativeLegacy.scrollRow(-mouseScroll/10);
        updatePages();
    }
    private void takeStack(boolean isShifted){
        uiSound("legacyui.ui.craft");
        int slotNumber = 9+(ContainerCreativeLegacy.slotsWide * currentCursorRow + currentCursorColumn);
        mc.playerController.doInventoryAction(inventorySlots.windowId, InventoryAction.CREATIVE_MOVE, new int[]{slotNumber, isShifted ? 64:1}, mc.thePlayer);
    }

    private boolean repeatedInput(int delayMillis){
        if (System.currentTimeMillis() - lastKeyEvent > delayMillis) {
            return true;
        }
        return false;
    }
    protected void resetCursorPosition(){
        currentCursorColumn = 0;
        currentCursorRow = 0;
    }
    protected void enableCursor(){
        timeCursorEnabled = System.currentTimeMillis();
        _renderCursor = true;
    }
    protected void disableCursor(){
        _renderCursor = false;
    }
    protected boolean isCursorEnabled(){
        return _renderCursor;
    }
    private void repeatedInputSuccess(){
        lastKeyEvent = System.currentTimeMillis();
    }
    protected void updatePages(){
        ((ContainerCreativeLegacy)inventorySlots).updatePage(tab);
    }
    public void drawGuiContainerForegroundLayer() {
        int i = this.mc.renderEngine.getTexture("/assets/legacyui/gui/legacycreative.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(i);

        // Cursor
        if (isCursorEnabled()){
            this.drawTexturedModalRect(8 + currentCursorColumn * 18, 33 + currentCursorRow * 18, 36, 175, 24, 24);
        }


        // Scroll Bar
        int scrollTopY = 36;
        int scrollBottomY = 143 - 15;
        int scrollYPos = (int)((scrollBottomY-scrollTopY) * ((float)ContainerCreativeLegacy.currentRow/ContainerCreativeLegacy.getTotalRows()));
        this.drawTexturedModalRect(251, scrollTopY + scrollYPos, 131, 175, 15, 15);
    }
    public void drawGuiContainerBackgroundLayer(float f) {
        checkInputs();

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

        // Renders selected bookmark
        int bookMarkWidth = 34;
        this.drawTexturedModalRect(j + bookMarkWidth * tab, k - 2, 0, 175, bookMarkWidth + 1, 30);

        drawTexturedModalRect(clearInventory.xPosition, clearInventory.yPosition, clearIsHovered ? 146 + clearInventory.width: 146, 175, clearInventory.width, clearInventory.height);
        drawStringCentered(fontRenderer, "X", clearInventory.xPosition + clearInventory.width/2, clearInventory.yPosition + 1 + (clearInventory.height - fontRenderer.fontHeight)/2, 0xFFFFFF);

        i = this.mc.renderEngine.getTexture("/assets/legacyui/gui/icons.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(i);
        double scale = 1.5D;
        GL11.glScaled(scale, scale, scale);
        for (int v = 0; v < maxDisplayedTabs; v++){
            ItemCategory category = CategoryManager.get(v);
            if (category != null){
                int[] icon = category.iconCoord;
                int x = (int)((j + 6 + bookMarkWidth * v)/scale);
                int y = (int)((k + 3)/scale);
                this.drawTexturedModalRect(x, y, icon[0], icon[1], 16, 16);
            }

        }
        GL11.glScaled(1/scale,1/scale,1/scale);

    }
    private void uiSound(String soundDir){
        if (LegacyUI.config.getBoolean(ConfigTranslations.USE_LEGACY_SOUNDS.getKey())){
            mc.sndManager.playSound(soundDir, SoundType.GUI_SOUNDS, 1, 1);
        }
        else {
            mc.sndManager.playSound("random.click", SoundType.GUI_SOUNDS, 1, 1);
        }
    }
}
