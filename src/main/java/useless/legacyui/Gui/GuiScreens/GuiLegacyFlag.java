package useless.legacyui.Gui.GuiScreens;

import net.minecraft.client.gui.*;
import net.minecraft.client.gui.drawing.DrawableEditor;
import net.minecraft.client.gui.drawing.IDrawableSurface;
import net.minecraft.client.input.InputType;
import net.minecraft.client.input.controller.ControllerInput;
import net.minecraft.client.render.FlagRenderer;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.core.block.entity.TileEntityFlag;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.net.packet.Packet141UpdateFlag;
import net.minecraft.core.util.helper.Colors;
import net.minecraft.core.util.helper.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import useless.legacyui.Gui.Containers.LegacyContainerFlag;
import useless.legacyui.Gui.GuiElements.Buttons.GuiAudioTextureButton;
import useless.legacyui.Gui.GuiElements.Buttons.GuiAuditoryButton;
import useless.legacyui.Gui.GuiElements.GuiButtonPrompt;
import useless.legacyui.Gui.GuiElements.GuiRegion;
import useless.legacyui.Gui.IGuiController;
import useless.legacyui.Helper.ArrayHelper;
import useless.legacyui.Helper.KeyboardHelper;
import useless.legacyui.Helper.RepeatInputHandler;
import useless.legacyui.LegacySoundManager;

import java.util.ArrayList;
import java.util.List;

public class GuiLegacyFlag extends GuiContainer
        implements IDrawableSurface<Byte>, IGuiController {
    private final TileEntityFlag tileEntity;
    private final int CANVAS_SCALE = 4;
    private final int CANVAS_WIDTH = 24;
    private final int CANVAS_HEIGHT = 16;
    private final RenderEngine renderEngineInstance;
    private int xLast = 0;
    private int yLast = 0;
    private int mouseButton;
    private boolean isDrawing = false;
    private int canvasX = 0;
    private int canvasY = 0;
    public static int selectedColor = 0;
    public static int dyeScroll = 0;
    public static int cursorX = 0;
    private FlagRenderer flagRenderer;
    private final DrawableEditor<Byte> flagSurfaceEditor;
    private final DrawableEditor<Byte> drawOverlaySurfaceEditor;
    private GuiAudioTextureButton[] toolBtns;
    private GuiAudioTextureButton eraseButton;
    private GuiAuditoryButton buttonRight;
    private GuiAuditoryButton buttonLeft;
    private int activeTool = 0;
    GuiSurface flagSurface;
    GuiSurface drawOverlaySurface;
    private final LegacyContainerFlag containerFlag;
    private int GUIx;
    private int GUIy;
    protected GuiAuditoryButton[] dyeButtons;
    protected GuiRegion flagRegion;
    private int pixelX = 0;
    private int pixelY = 0;
    public List<GuiButtonPrompt> promptsDraw = new ArrayList<>();
    public List<GuiButtonPrompt> promptsSelect = new ArrayList<>();
    public I18n translator = I18n.getInstance();
    public GuiLegacyFlag(EntityPlayer player, TileEntityFlag flagTileEntity, RenderEngine renderEngine) {
        super(new LegacyContainerFlag(player.inventory, flagTileEntity));
        containerFlag = (LegacyContainerFlag)inventorySlots;
        this.renderEngineInstance = renderEngine;
        this.tileEntity = flagTileEntity;
        this.xSize = 159;
        this.ySize = 148;
        flagTileEntity.owner = player.username;
        this.flagRenderer = new FlagRenderer(renderEngine);
        this.flagSurface = new GuiSurface(24, 16, 4, flagTileEntity.flagColors);
        this.flagSurfaceEditor = new DrawableEditor<Byte>(this.flagSurface);
        this.drawOverlaySurface = new GuiSurface(24, 16, 4);
        this.drawOverlaySurfaceEditor = new DrawableEditor<Byte>(this.drawOverlaySurface);
    }

    @Override
    public void initGui() {
        GUIx = (width - xSize) / 2;
        GUIy = (height - ySize) / 2;
        canvasX = GUIx + 20;
        canvasY = GUIy + 66;
        super.initGui();
        controlList.clear();
        toolBtns = new GuiAudioTextureButton[6];
        for (int i = 0; i < 6; ++i) {
            toolBtns[i] = new GuiAudioTextureButton(i, "/assets/legacyui/gui/legacyflag.png", GUIx + 12 + 23 * i, GUIy + 12, 20 * i, 196, 20, 20);
            toolBtns[i].setMuted(true);
            if (i == activeTool) {
                toolBtns[i].enabled = false;
            }
            controlList.add(toolBtns[i]);
        }
        eraseButton = new GuiAudioTextureButton(6, "/assets/legacyui/gui/legacyflag.png", GUIx + 128, GUIy + 118, 120, 196, 18, 18);
        eraseButton.setMuted(true);
        controlList.add(eraseButton);
        dyeButtons = new GuiAuditoryButton[6];
        for (int i = 0; i < dyeButtons.length; i++) {
            dyeButtons[i] = new GuiAuditoryButton(10 + i, GUIx + 14 + 18 * i, GUIy + 37, 18, 18, "");
            dyeButtons[i].setMuted(true);
            dyeButtons[i].visible = false;
            controlList.add(dyeButtons[i]);
        }
        buttonRight = new GuiAuditoryButton(30, GUIx + 122, GUIy + 37, 11, 18, "");
        buttonRight.setMuted(false);
        buttonRight.visible = false;
        controlList.add(buttonRight);
        buttonLeft = new GuiAuditoryButton(31, GUIx + 3, GUIy + 37, 11, 18, "");
        buttonLeft.setMuted(false);
        buttonLeft.visible = false;
        controlList.add(buttonLeft);
        flagRegion = new GuiRegion(200, canvasX - CANVAS_SCALE, canvasY - CANVAS_SCALE, CANVAS_WIDTH * CANVAS_SCALE + 2 * CANVAS_SCALE, CANVAS_HEIGHT * CANVAS_SCALE + 2 * CANVAS_SCALE);

        promptsDraw.clear();
        promptsDraw.add(new GuiButtonPrompt( 101, 50, this.height-30, 3,translator.translateKey("legacyui.prompt.draw"), new int[]{GuiButtonPrompt.A}));
        promptsDraw.add(new GuiButtonPrompt( 102, promptsDraw.get(0).xPosition + promptsDraw.get(0).width + 3, this.height-30,  3,translator.translateKey("legacyui.prompt.back"), new int[]{GuiButtonPrompt.B}));
        promptsDraw.add(new GuiButtonPrompt( 103, promptsDraw.get(1).xPosition + promptsDraw.get(1).width + 3, this.height-30,  3,translator.translateKey("legacyui.prompt.pickcolor"), new int[]{GuiButtonPrompt.Y}));
        promptsDraw.add(new GuiButtonPrompt( 104, promptsDraw.get(2).xPosition + promptsDraw.get(2).width + 3, this.height-30,  3,translator.translateKey("legacyui.prompt.colorselect"), new int[]{GuiButtonPrompt.LEFT_BUMPER,GuiButtonPrompt.RIGHT_BUMPER}));
        promptsDraw.add(new GuiButtonPrompt( 105, promptsDraw.get(3).xPosition + promptsDraw.get(3).width + 3, this.height-30,  3,translator.translateKey("legacyui.prompt.toolselect"), new int[]{GuiButtonPrompt.LEFT_TRIGGER,GuiButtonPrompt.RIGHT_TRIGGER}));

        promptsSelect.clear();
        promptsSelect.add(new GuiButtonPrompt( 101, 50, this.height-30, 3,translator.translateKey("legacyui.prompt.select"), new int[]{GuiButtonPrompt.A}));
        promptsSelect.add(new GuiButtonPrompt( 102, promptsSelect.get(0).xPosition + promptsSelect.get(0).width + 3, this.height-30,  3,translator.translateKey("legacyui.prompt.back"), new int[]{GuiButtonPrompt.B}));
        promptsSelect.add(new GuiButtonPrompt( 103, promptsSelect.get(1).xPosition + promptsSelect.get(1).width + 3, this.height-30,  3,translator.translateKey("legacyui.prompt.pickdraw"), new int[]{GuiButtonPrompt.Y}));
        promptsSelect.add(new GuiButtonPrompt( 104, promptsSelect.get(2).xPosition + promptsSelect.get(2).width + 3, this.height-30,  3,translator.translateKey("legacyui.prompt.colorselect"), new int[]{GuiButtonPrompt.LEFT_BUMPER,GuiButtonPrompt.RIGHT_BUMPER}));
        promptsSelect.add(new GuiButtonPrompt( 105, promptsSelect.get(3).xPosition + promptsSelect.get(3).width + 3, this.height-30,  3,translator.translateKey("legacyui.prompt.toolselect"), new int[]{GuiButtonPrompt.LEFT_TRIGGER,GuiButtonPrompt.RIGHT_TRIGGER}));
        setSlots();
    }

    private void renderCanvas() {
        int[] colors = new int[5];
        for (int i = 1; i < 4; ++i) {
            ItemStack stack = this.tileEntity.getStackInSlot(35 + i);
            if (stack == null || stack.getItem() != Item.dye) continue;
            colors[i] = Colors.allFlagColors[TextFormatting.get((int)(15 - stack.getMetadata())).id].getARGB();
        }
        colors[4] = -1;
        this.flagSurface.colors = colors;
        this.drawOverlaySurface.colors = colors;
        GL11.glEnable(3042);
        GL11.glBlendFunc(769, 768);
        this.flagSurface.render(this.canvasX, this.canvasY);
        GL11.glDisable(3042);
        this.drawOverlaySurface.render(this.canvasX, this.canvasY);
    }

    @Override
    public void mouseClicked(int x, int y, int mouseButton) {
        super.mouseClicked(x, y, mouseButton);
        if (mc.inputType == InputType.CONTROLLER && mc.controllerInput.buttonY.isPressed()){
            return;
        }
        if (this.tileEntity.getStackInSlot(36 + selectedColor) == null && selectedColor != 3) {
            return;
        }
        if (this.activeTool >= 0 && this.activeTool < 3) {
            if (!this.isDrawing) {
                int xInCanvas = (x - this.canvasX) / 4;
                int yInCanvas = (y - this.canvasY) / 4;
                if (xInCanvas >= 0 && xInCanvas < 24 && yInCanvas >= 0 && yInCanvas < 16) {
                    this.isDrawing = true;
                    this.xLast = xInCanvas;
                    this.yLast = yInCanvas;
                    this.mouseButton = mouseButton;
                    byte color = 0;
                    if (this.mouseButton == 0 && selectedColor != 3) {
                        color = (byte)(selectedColor + 1);
                    }
                    this.flagSurfaceEditor.drawLine(xInCanvas, yInCanvas, xInCanvas, yInCanvas, color, this.activeTool + 1);
                }
            }
        } else if (this.activeTool == 3) {
            if (!this.isDrawing) {
                int xInCanvas = (x - this.canvasX) / 4;
                int yInCanvas = (y - this.canvasY) / 4;
                if (xInCanvas >= 0 && xInCanvas < 24 && yInCanvas >= 0 && yInCanvas < 16) {
                    this.isDrawing = true;
                    this.mouseButton = mouseButton;
                    byte color = 0;
                    if (this.mouseButton == 0 && selectedColor != 3) {
                        color = (byte)(selectedColor + 1);
                    }
                    this.flagSurfaceEditor.floodFill(xInCanvas, yInCanvas, color);
                }
            }
        } else if (!(this.activeTool != 4 && this.activeTool != 5 || this.isDrawing)) {
            int xInCanvas = (x - this.canvasX) / 4;
            int yInCanvas = (y - this.canvasY) / 4;
            if (xInCanvas >= 0 && xInCanvas < 24 && yInCanvas >= 0 && yInCanvas < 16) {
                this.isDrawing = true;
                this.mouseButton = mouseButton;
                this.xLast = xInCanvas;
                this.yLast = yInCanvas;
            }
        }
    }

    @Override
    protected void buttonPressed(GuiButton guibutton) {
        super.buttonPressed(guibutton);
        if (guibutton.id >= 0 && guibutton.id < 6) {
            setActiveTool(guibutton.id);
        }
        if (guibutton == eraseButton){
            selectColor(3);
        }
        for (int i = 0; i < dyeButtons.length; i++) {
            if (guibutton == dyeButtons[i]){
                selectDye(i);
            }
        }
        if (guibutton == buttonRight){
            selectDyeOffset(dyeScroll + 1);
        }
        if (guibutton == buttonLeft){
            selectDyeOffset(dyeScroll - 1);
        }
    }
    public void handleInputs(){
        boolean shifted = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
        if (KeyboardHelper.repeatInput(mc.gameSettings.keyRight.keyCode(), UtilGui.tabScrollRepeatDelay, UtilGui.tabScrollInitialDelay) || KeyboardHelper.repeatInput(mc.gameSettings.keyLookRight.keyCode(), UtilGui.tabScrollRepeatDelay, UtilGui.tabScrollInitialDelay)){
            if (shifted){
                setActiveTool(activeTool + 1);
            } else {
                setCursorX(cursorX + 1);
            }
        }
        if (KeyboardHelper.repeatInput(mc.gameSettings.keyLeft.keyCode(), UtilGui.tabScrollRepeatDelay, UtilGui.tabScrollInitialDelay) || KeyboardHelper.repeatInput(mc.gameSettings.keyLookLeft.keyCode(), UtilGui.tabScrollRepeatDelay, UtilGui.tabScrollInitialDelay)){
            if (shifted){
                setActiveTool(activeTool - 1);
            } else {
                setCursorX(cursorX - 1);
            }
        }
        if (KeyboardHelper.repeatInput(mc.gameSettings.keyForward.keyCode(), UtilGui.verticalScrollRepeatDelay, UtilGui.verticalScrollInitialDelay) || KeyboardHelper.repeatInput(mc.gameSettings.keyLookUp.keyCode(), UtilGui.verticalScrollRepeatDelay, UtilGui.verticalScrollInitialDelay)){
            selectColor(selectedColor - 1);
        }
        if (KeyboardHelper.repeatInput(mc.gameSettings.keyBack.keyCode(), UtilGui.verticalScrollRepeatDelay, UtilGui.verticalScrollInitialDelay) || KeyboardHelper.repeatInput(mc.gameSettings.keyLookDown.keyCode(), UtilGui.verticalScrollRepeatDelay, UtilGui.verticalScrollInitialDelay)){
            selectColor(selectedColor + 1);
        }
        if (KeyboardHelper.isKeyPressedThisFrame(mc.gameSettings.keyJump.keyCode())){
            selectDye(cursorX);
        }
    }

    @Override
    public void mouseMovedOrUp(int x, int y, int mouseButton) {
        super.mouseMovedOrUp(x, y, mouseButton);
        if (mc.inputType == InputType.CONTROLLER && mc.controllerInput.buttonY.isPressed()){
            return;
        }
        if (this.activeTool >= 0 && this.activeTool < 3) {
            if (this.isDrawing && mouseButton != -1) {
                this.isDrawing = false;
                this.mouseButton = -1;
            } else if (this.isDrawing) {
                int xInCanvas = (x - this.canvasX) / 4;
                int yInCanvas = (y - this.canvasY) / 4;
                byte color = 0;
                if (this.mouseButton == 0 && selectedColor != 3) {
                    color = (byte)(selectedColor + 1);
                }
                if (MathHelper.abs(this.xLast - xInCanvas) <= 1.0f && MathHelper.abs(this.yLast - yInCanvas) <= 1.0f) {
                    this.flagSurfaceEditor.drawLine(xInCanvas, yInCanvas, xInCanvas, yInCanvas, color, this.activeTool + 1);
                } else {
                    this.flagSurfaceEditor.drawLine(this.xLast, this.yLast, xInCanvas, yInCanvas, color, this.activeTool + 1);
                }
                this.xLast = xInCanvas;
                this.yLast = yInCanvas;
            }
        } else if (this.activeTool == 3) {
            if (this.isDrawing && mouseButton != -1) {
                this.isDrawing = false;
                this.mouseButton = -1;
            }
        } else if (this.activeTool == 4) {
            if (this.isDrawing && mouseButton != -1) {
                this.isDrawing = false;
                int xInCanvas = (x - this.canvasX) / 4;
                int yInCanvas = (y - this.canvasY) / 4;
                byte color = 0;
                if (this.mouseButton == 0 && selectedColor != 3) {
                    color = (byte)(selectedColor + 1);
                }
                this.flagSurfaceEditor.drawRectangle(this.xLast, this.yLast, xInCanvas, yInCanvas, color);
                this.mouseButton = -1;
            }
        } else if (this.activeTool == 5 && this.isDrawing && mouseButton != -1) {
            this.isDrawing = false;
            int xInCanvas = (x - this.canvasX) / 4;
            int yInCanvas = (y - this.canvasY) / 4;
            byte color = 0;
            if (this.mouseButton == 0 && selectedColor != 3) {
                color = (byte)(selectedColor + 1);
            }
            this.flagSurfaceEditor.drawEllipse(this.xLast, this.yLast, xInCanvas, yInCanvas, color);
            this.mouseButton = -1;
        }
    }
    protected void drawGuiContainerForegroundLayer(){
        UtilGui.bindTexture("/assets/legacyui/gui/legacyflag.png");
        drawTexturedModalRect(11 + 18 * cursorX, 34, 138, 232, 24,24);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {
        int i = this.mc.renderEngine.getTexture("/assets/legacyui/gui/legacyflag.png");
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.renderEngine.bindTexture(i);
        this.drawTexturedModalRect(GUIx, GUIy, 0, 0, this.xSize, this.ySize);
        if (LegacyContainerFlag.dyesMetaAtSlot.size() > 6){
            drawTexturedModalRect(GUIx + 124, GUIy + 42, 143, 196, 5,9);
            drawTexturedModalRect(GUIx + 7, GUIy + 42, 138, 196, 5,9);
        }
        int textX = 148;
        if (selectedColor == 0) {
            this.drawString(this.fontRenderer, "1", GUIx + textX, GUIy + 66, -1);
        } else {
            this.drawStringNoShadow(this.fontRenderer, "1", GUIx + textX, GUIy + 66, -8421505);
        }
        if (selectedColor == 1) {
            this.drawString(this.fontRenderer, "2", GUIx + textX, GUIy + 85, -1);
        } else {
            this.drawStringNoShadow(this.fontRenderer, "2", GUIx + textX, GUIy + 85, -8421505);
        }
        if (selectedColor == 2) {
            this.drawString(this.fontRenderer, "3", GUIx + textX, GUIy + 104, -1);
        } else {
            this.drawStringNoShadow(this.fontRenderer, "3", GUIx + textX, GUIy + 104, -8421505);
        }
        if (selectedColor == 3) {
            this.drawString(this.fontRenderer, "4", GUIx + textX, GUIy + 123, -1);
        } else {
            this.drawStringNoShadow(this.fontRenderer, "4", GUIx + textX, GUIy + 123, -8421505);
        }
    }

    @Override
    public void drawScreen(int x, int y, float renderPartialTicks) {
        handleInputs();
        super.drawScreen(x, y, renderPartialTicks);
        this.drawOverlaySurface.clear();
        int xInCanvas = (x - this.canvasX) / 4;
        int yInCanvas = (y - this.canvasY) / 4;
        byte color = (byte)(selectedColor + 1);
        if (this.mouseButton == 1) {
            color = 4;
        }
        if (this.activeTool >= 0 && this.activeTool < 3) {
            this.drawOverlaySurfaceEditor.drawLine(xInCanvas, yInCanvas, xInCanvas, yInCanvas, color, this.activeTool + 1);
        } else if (this.activeTool == 3) {
            this.drawOverlaySurfaceEditor.drawLine(xInCanvas, yInCanvas, xInCanvas, yInCanvas, color, 1);
        } else if (this.activeTool == 4) {
            if (this.isDrawing) {
                this.drawOverlaySurfaceEditor.drawRectangle(this.xLast, this.yLast, xInCanvas, yInCanvas, color);
            } else {
                this.drawOverlaySurfaceEditor.drawLine(xInCanvas, yInCanvas, xInCanvas, yInCanvas, color, 1);
            }
        } else if (this.activeTool == 5) {
            if (this.isDrawing) {
                this.drawOverlaySurfaceEditor.drawEllipse(this.xLast, this.yLast, xInCanvas, yInCanvas, color);
            } else {
                this.drawOverlaySurfaceEditor.drawLine(xInCanvas, yInCanvas, xInCanvas, yInCanvas, color, 1);
            }
        }
        this.renderCanvas();
        int regionX = x;
        int regionY = y;
        if (mc.inputType == InputType.CONTROLLER){
            regionX = (int)mc.controllerInput.cursorX;
            regionY = (int) mc.controllerInput.cursorY;
        }


        if (flagRegion.isHovered(regionX, regionY)){
            for (GuiButtonPrompt prompt: promptsDraw) {
                prompt.drawPrompt(mc, x, y);
            }
        }
        else {
            for (GuiButtonPrompt prompt: promptsSelect) {
                prompt.drawPrompt(mc, x, y);
            }
        }
    }

    @Override
    public void keyTyped(char c, int keyCode, int mouseX, int mouseY) {
        super.keyTyped(c, keyCode, mouseX, mouseY);
        if (keyCode == 1 || this.mc.gameSettings.keyInventory.isKey(keyCode) || keyCode == 14) {
            this.mc.thePlayer.closeScreen();
        }
        if (keyCode == 2) {
            selectColor(0);
        }
        if (keyCode == 3) {
            selectColor(1);
        }
        if (keyCode == 4) {
            selectColor(2);
        }
        if (keyCode == 5) {
            selectColor(3);
        }
    }
    private void selectDye(int index){
        if (index >= LegacyContainerFlag.dyesMetaAtSlot.size()){return;}
        int dye = ArrayHelper.wrapAroundIndex(index + dyeScroll, LegacyContainerFlag.dyesMetaAtSlot.size());
        containerFlag.swapDye(dye);
        LegacySoundManager.play.craft(false);
    }
    private void selectDyeOffset(int newDyeScroll){
        if (newDyeScroll != dyeScroll){
            LegacySoundManager.play.scroll(true);
        }
        if (LegacyContainerFlag.dyesMetaAtSlot.size() <= 6) {return;}
        dyeScroll = newDyeScroll;
        if (dyeScroll >= LegacyContainerFlag.dyesMetaAtSlot.size()){
            dyeScroll -= LegacyContainerFlag.dyesMetaAtSlot.size();
        } else if (dyeScroll < 0){
            dyeScroll += LegacyContainerFlag.dyesMetaAtSlot.size();
        }

        setSlots();
    }
    private void setSlots(){
        containerFlag.setSlots();
        buttonLeft.enabled = LegacyContainerFlag.dyesMetaAtSlot.size() > 6;
        buttonRight.enabled = LegacyContainerFlag.dyesMetaAtSlot.size() > 6;
        for (int i = 0; i < dyeButtons.length; i++) {
            dyeButtons[i].enabled = i < (LegacyContainerFlag.dyesMetaAtSlot.size());
        }
    }
    private void setActiveTool(int value){
        LegacySoundManager.play.focus(true);
        this.toolBtns[this.activeTool].enabled = true;
        activeTool = ArrayHelper.wrapAroundIndex(value, 6);
        this.toolBtns[this.activeTool].enabled = false;
    }
    private void setCursorX(int value){
        cursorX = value;
        if (cursorX > 5){
            cursorX = 5;
            selectDyeOffset(dyeScroll + 1);
        } else if (cursorX < 0) {
            cursorX = 0;
            selectDyeOffset(dyeScroll - 1);
        }
        if (value == cursorX){
            LegacySoundManager.play.scroll(true);
        }
        if (mc.inputType == InputType.CONTROLLER){
            mc.controllerInput.snapToSlot(this, 39 + cursorX);
        }
    }
    private void selectColor(int color){
        if (color != selectedColor){
            LegacySoundManager.play.focus(true);
        }
        selectedColor = color;
        if (selectedColor > 3){
            selectedColor -= 4;
        } else if (selectedColor < 0){
            selectedColor += 4;
        }
        if (selectedColor == 3){
            eraseButton.enabled = false;
        } else {
            eraseButton.enabled = true;
        }

    }
    @Override
    public int getWidth() {
        return 24;
    }

    @Override
    public int getHeight() {
        return 16;
    }

    @Override
    public Byte getPixelValue(int x, int y) {
        if (x < 0 || x >= 24) {
            return (byte)0;
        }
        if (y < 0 || y >= 16) {
            return (byte)0;
        }
        return this.tileEntity.flagColors[x + 24 * y];
    }

    @Override
    public boolean setPixelValue(int x, int y, Byte value) {
        if (x < 0 || x >= 24) {
            return false;
        }
        if (y < 0 || y >= 16) {
            return false;
        }
        this.tileEntity.flagColors[x + 24 * y] = value;
        return true;
    }

    @Override
    public void onGuiClosed() {
        if (this.mc.theWorld.isClientSide) {
            this.mc.getSendQueue().addToSendQueue(new Packet141UpdateFlag(this.tileEntity.xCoord, this.tileEntity.yCoord, this.tileEntity.zCoord, this.tileEntity.flagColors, this.tileEntity.owner));
        }
    }

    @Override
    public void GuiControls(ControllerInput controllerInput) {
        if (controllerInput.buttonZL.pressedThisFrame()){
            setActiveTool(activeTool - 1);
        }
        if (controllerInput.buttonZR.pressedThisFrame()){
            setActiveTool(activeTool + 1);
        }
        if (controllerInput.buttonL.pressedThisFrame()){
            selectColor(selectedColor - 1);
        }
        if (controllerInput.buttonR.pressedThisFrame()){
            selectColor(selectedColor + 1);
        }
        if (flagRegion.isHovered((int)mc.controllerInput.cursorX, (int) mc.controllerInput.cursorY)){
            if (controllerInput.digitalPad.right.pressedThisFrame() || (controllerInput.digitalPad.right.isPressed() && RepeatInputHandler.doRepeatInput(-1, 1000/15))){
                RepeatInputHandler.manualSuccess(-1);
                snapToPixel(1, 0);
            }
            if (controllerInput.digitalPad.left.pressedThisFrame() || (controllerInput.digitalPad.left.isPressed() && RepeatInputHandler.doRepeatInput(-1, 1000/15))){
                RepeatInputHandler.manualSuccess(-1);
                snapToPixel(- 1, 0);
            }
            if (controllerInput.digitalPad.up.pressedThisFrame() || (controllerInput.digitalPad.up.isPressed() && RepeatInputHandler.doRepeatInput(-1, 1000/15))){
                RepeatInputHandler.manualSuccess(-1);
                snapToPixel(0, -1);
            }
            if (controllerInput.digitalPad.down.pressedThisFrame() || (controllerInput.digitalPad.down.isPressed() && RepeatInputHandler.doRepeatInput(-1, 1000/15))){
                RepeatInputHandler.manualSuccess(-1);
                snapToPixel(0, 1);
            }
            if (controllerInput.buttonA.isPressed()){
                mouseMovedOrUp((int) controllerInput.cursorX, (int) controllerInput.cursorY, -1);
            }
            if (controllerInput.buttonY.pressedThisFrame()){
                controllerInput.snapToSlot(this, 39 + cursorX);
            }
        } else {
            if (controllerInput.digitalPad.right.pressedThisFrame()){
                setCursorX(cursorX + 1);
            }
            if (controllerInput.digitalPad.left.pressedThisFrame()){
                setCursorX(cursorX - 1);
            }
            if (controllerInput.digitalPad.up.pressedThisFrame()){
                selectColor(selectedColor - 1);
            }
            if (controllerInput.digitalPad.down.pressedThisFrame()){
                selectColor(selectedColor + 1);
            }
            if (controllerInput.buttonY.pressedThisFrame()){
                controllerInput.cursorX = canvasX + pixelX * CANVAS_SCALE + (double) CANVAS_SCALE /2;
                controllerInput.cursorY = canvasY + pixelY * CANVAS_SCALE + (double) CANVAS_SCALE /2;
            }
            if (controllerInput.buttonA.pressedThisFrame()){
                selectDye(cursorX);
            }
        }
    }
    private void snapToPixel(int x, int y){
        pixelX = (int) ((mc.controllerInput.cursorX - canvasX)/CANVAS_SCALE);
        pixelY = (int) ((mc.controllerInput.cursorY - canvasY)/CANVAS_SCALE);
        pixelX += x;
        pixelY += y;
        if (pixelX > CANVAS_WIDTH-1){
            pixelX -= CANVAS_WIDTH;
        } else if (pixelX < 0){
            pixelX += CANVAS_WIDTH;
        }
        if (pixelY > CANVAS_HEIGHT-1){
            pixelY -= CANVAS_HEIGHT;
        } else if (pixelY < 0){
            pixelY += CANVAS_HEIGHT;
        }
        mc.controllerInput.cursorX = canvasX + pixelX * CANVAS_SCALE + (double) CANVAS_SCALE /2;
        mc.controllerInput.cursorY = canvasY + pixelY * CANVAS_SCALE + (double) CANVAS_SCALE /2;
    }

    @Override
    public boolean playDefaultPressSound() {
        return false;
    }

    @Override
    public boolean enableDefaultSnapping() {
        return false;
    }
}
