package useless.legacyui.gui.screens;

import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.DrawableSurfaceElement;
import net.minecraft.client.gui.TexturedButtonElement;
import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.client.gui.drawing.DrawableEditor;
import net.minecraft.client.gui.drawing.IDrawableSurface;
import net.minecraft.client.input.InputType;
import net.minecraft.client.input.controller.ControllerInput;
import net.minecraft.client.util.helper.Colors;
import net.minecraft.core.block.entity.TileEntityFlag;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.net.packet.PacketCustomPayload;
import net.minecraft.core.util.helper.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import useless.legacyui.gui.containers.LegacyContainerFlag;
import useless.legacyui.gui.elements.GuiButtonPrompt;
import useless.legacyui.gui.elements.GuiRegion;
import useless.legacyui.gui.IGuiController;
import useless.legacyui.helper.ArrayHelper;
import useless.legacyui.helper.KeyboardHelper;
import useless.legacyui.helper.RepeatInputHandler;
import useless.legacyui.LegacySoundManager;

import java.util.ArrayList;
import java.util.List;

import static useless.legacyui.helper.KeyboardHelper.*;

public class GuiLegacyFlag extends ScreenContainerAbstract
        implements IDrawableSurface<Byte>, IGuiController {
    private final TileEntityFlag tileEntity;
    private final int CANVAS_SCALE = 4;
    private final int CANVAS_WIDTH = 24;
    private final int CANVAS_HEIGHT = 16;
    private int xLast = 0;
    private int yLast = 0;
    private int mouseButton;
    private boolean isDrawing = false;
    private int canvasX = 0;
    private int canvasY = 0;
    public static int selectedColor = 0;
    public static int dyeScroll = 0;
    public static int cursorX = 0;
    private final DrawableEditor<Byte> flagSurfaceEditor;
    private final DrawableEditor<Byte> drawOverlaySurfaceEditor;
    private TexturedButtonElement[] toolBtns;
    private TexturedButtonElement eraseButton;
    private ButtonElement buttonRight;
    private ButtonElement buttonLeft;
    private int activeTool = 0;
    DrawableSurfaceElement flagSurface;
    DrawableSurfaceElement drawOverlaySurface;
    private final LegacyContainerFlag containerFlag;
    private int GUIx;
    private int GUIy;
    protected ButtonElement[] dyeButtons;
    protected GuiRegion flagRegion;
    private int pixelX = 0;
    private int pixelY = 0;
    public List<GuiButtonPrompt> promptsDraw = new ArrayList<>();
    public List<GuiButtonPrompt> promptsSelect = new ArrayList<>();
    public I18n translator = I18n.getInstance();
    public GuiLegacyFlag(final Player player, final TileEntityFlag flagTileEntity) {
        super(new LegacyContainerFlag(player.inventory, flagTileEntity));
        this.containerFlag = (LegacyContainerFlag) this.inventorySlots;
        this.tileEntity = flagTileEntity;
        this.xSize = 159;
        this.ySize = 148;
        flagTileEntity.owner = player.uuid;
        this.flagSurface = new DrawableSurfaceElement(24, 16, 4, flagTileEntity.flagColors);
        this.flagSurfaceEditor = new DrawableEditor<>(this.flagSurface);
        this.drawOverlaySurface = new DrawableSurfaceElement(24, 16, 4);
        this.drawOverlaySurfaceEditor = new DrawableEditor<>(this.drawOverlaySurface);
    }

    @Override
    public void init() {
        this.GUIx = (this.width - this.xSize) / 2;
        this.GUIy = (this.height - this.ySize) / 2;
        this.canvasX = this.GUIx + 20;
        this.canvasY = this.GUIy + 66;
        super.init();
        buttons.clear();
        this.toolBtns = new TexturedButtonElement[6];
        for (int i = 0; i < 6; ++i) {
            this.toolBtns[i] = new TexturedButtonElement(i, "/assets/legacyui/gui/legacyflag.png", this.GUIx + 12 + 23 * i, this.GUIy + 12, 20 * i, 196, 20, 20);
            this.toolBtns[i].mute();
            if (i == this.activeTool) {
                this.toolBtns[i].enabled = false;
            }
            buttons.add(this.toolBtns[i]);
        }
        this.eraseButton = new TexturedButtonElement(6, "/assets/legacyui/gui/legacyflag.png", this.GUIx + 128, this.GUIy + 118, 120, 196, 18, 18);
        this.eraseButton.mute();
        buttons.add(this.eraseButton);
        this.dyeButtons = new ButtonElement[6];
        for (int i = 0; i < this.dyeButtons.length; i++) {
            this.dyeButtons[i] = new ButtonElement(10 + i, this.GUIx + 14 + 18 * i, this.GUIy + 37, 18, 18, "");
            this.dyeButtons[i].mute();
            this.dyeButtons[i].visible = false;
            buttons.add(this.dyeButtons[i]);
        }
        this.buttonRight = new ButtonElement(30, this.GUIx + 122, this.GUIy + 37, 11, 18, "");
        this.buttonRight.visible = false;
        buttons.add(this.buttonRight);
        this.buttonLeft = new ButtonElement(31, this.GUIx + 3, this.GUIy + 37, 11, 18, "");
        this.buttonLeft.visible = false;
        buttons.add(this.buttonLeft);
        this.flagRegion = new GuiRegion(200, this.canvasX - this.CANVAS_SCALE, this.canvasY - this.CANVAS_SCALE, this.CANVAS_WIDTH * this.CANVAS_SCALE + 2 * this.CANVAS_SCALE, this.CANVAS_HEIGHT * this.CANVAS_SCALE + 2 * this.CANVAS_SCALE);

        this.promptsDraw.clear();
        this.promptsDraw.add(new GuiButtonPrompt( 101, 50, this.height-30, 3, this.translator.translateKey("legacyui.prompt.draw"), new int[]{GuiButtonPrompt.A}));
        this.promptsDraw.add(new GuiButtonPrompt( 102, this.promptsDraw.get(0).xPosition + this.promptsDraw.get(0).width + 3, this.height-30,  3, this.translator.translateKey("legacyui.prompt.back"), new int[]{GuiButtonPrompt.B}));
        this.promptsDraw.add(new GuiButtonPrompt( 103, this.promptsDraw.get(1).xPosition + this.promptsDraw.get(1).width + 3, this.height-30,  3, this.translator.translateKey("legacyui.prompt.pickcolor"), new int[]{GuiButtonPrompt.Y}));
        this.promptsDraw.add(new GuiButtonPrompt( 104, this.promptsDraw.get(2).xPosition + this.promptsDraw.get(2).width + 3, this.height-30,  3, this.translator.translateKey("legacyui.prompt.colorselect"), new int[]{GuiButtonPrompt.LEFT_BUMPER,GuiButtonPrompt.RIGHT_BUMPER}));
        this.promptsDraw.add(new GuiButtonPrompt( 105, this.promptsDraw.get(3).xPosition + this.promptsDraw.get(3).width + 3, this.height-30,  3, this.translator.translateKey("legacyui.prompt.toolselect"), new int[]{GuiButtonPrompt.LEFT_TRIGGER,GuiButtonPrompt.RIGHT_TRIGGER}));

        this.promptsSelect.clear();
        this.promptsSelect.add(new GuiButtonPrompt( 101, 50, this.height-30, 3, this.translator.translateKey("legacyui.prompt.select"), new int[]{GuiButtonPrompt.A}));
        this.promptsSelect.add(new GuiButtonPrompt( 102, this.promptsSelect.get(0).xPosition + this.promptsSelect.get(0).width + 3, this.height-30,  3, this.translator.translateKey("legacyui.prompt.back"), new int[]{GuiButtonPrompt.B}));
        this.promptsSelect.add(new GuiButtonPrompt( 103, this.promptsSelect.get(1).xPosition + this.promptsSelect.get(1).width + 3, this.height-30,  3, this.translator.translateKey("legacyui.prompt.pickdraw"), new int[]{GuiButtonPrompt.Y}));
        this.promptsSelect.add(new GuiButtonPrompt( 104, this.promptsSelect.get(2).xPosition + this.promptsSelect.get(2).width + 3, this.height-30,  3, this.translator.translateKey("legacyui.prompt.colorselect"), new int[]{GuiButtonPrompt.LEFT_BUMPER,GuiButtonPrompt.RIGHT_BUMPER}));
        this.promptsSelect.add(new GuiButtonPrompt( 105, this.promptsSelect.get(3).xPosition + this.promptsSelect.get(3).width + 3, this.height-30,  3, this.translator.translateKey("legacyui.prompt.toolselect"), new int[]{GuiButtonPrompt.LEFT_TRIGGER,GuiButtonPrompt.RIGHT_TRIGGER}));
        setSlots();
    }

    private void renderCanvas() {
        final int[] colors = new int[5];
        for (int i = 1; i < 4; ++i) {
            final ItemStack stack = this.tileEntity.getItem(35 + i);
            if (stack == null || stack.getItem() != Items.DYE) continue;
            colors[i] = Colors.allFlagColors[TextFormatting.get(15 - stack.getMetadata()).id].getARGB();
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
    public void mouseClicked(final int x, final int y, final int mouseButton) {
        super.mouseClicked(x, y, mouseButton);
        if (this.mc.inputType == InputType.CONTROLLER && this.mc.controllerInput.buttonY.isPressed()){
            return;
        }
        if (this.tileEntity.getItem(36 + selectedColor) == null && selectedColor != 3) {
            return;
        }
        if (this.activeTool >= 0 && this.activeTool < 3) {
            if (!this.isDrawing) {
                final int xInCanvas = (x - this.canvasX) / 4;
                final int yInCanvas = (y - this.canvasY) / 4;
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
                final int xInCanvas = (x - this.canvasX) / 4;
                final int yInCanvas = (y - this.canvasY) / 4;
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
            final int xInCanvas = (x - this.canvasX) / 4;
            final int yInCanvas = (y - this.canvasY) / 4;
            if (xInCanvas >= 0 && xInCanvas < 24 && yInCanvas >= 0 && yInCanvas < 16) {
                this.isDrawing = true;
                this.mouseButton = mouseButton;
                this.xLast = xInCanvas;
                this.yLast = yInCanvas;
            }
        }
    }

    @Override
    protected void buttonClicked(final ButtonElement guibutton) {
        super.buttonClicked(guibutton);
        if (guibutton.id >= 0 && guibutton.id < 6) {
            setActiveTool(guibutton.id);
        }
        if (guibutton == this.eraseButton){
            selectColor(3);
        }
        for (int i = 0; i < this.dyeButtons.length; i++) {
            if (guibutton == this.dyeButtons[i]){
                selectDye(i);
            }
        }
        if (guibutton == this.buttonRight){
            selectDyeOffset(dyeScroll + 1);
        }
        if (guibutton == this.buttonLeft){
            selectDyeOffset(dyeScroll - 1);
        }
    }
    public void handleInputs(){
        final boolean shifted = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
        if (repeatInput(this.mc.gameSettings.keyRight.getKeyCode(), UtilGui.tabScrollRepeatDelay, UtilGui.tabScrollInitialDelay) || repeatInput(this.mc.gameSettings.keyLookRight.getKeyCode(), UtilGui.tabScrollRepeatDelay, UtilGui.tabScrollInitialDelay)){
            if (shifted){
                setActiveTool(this.activeTool + 1);
            } else {
                setCursorX(cursorX + 1);
            }
        }
        if (repeatInput(this.mc.gameSettings.keyLeft.getKeyCode(), UtilGui.tabScrollRepeatDelay, UtilGui.tabScrollInitialDelay) || repeatInput(this.mc.gameSettings.keyLookLeft.getKeyCode(), UtilGui.tabScrollRepeatDelay, UtilGui.tabScrollInitialDelay)){
            if (shifted){
                setActiveTool(this.activeTool - 1);
            } else {
                setCursorX(cursorX - 1);
            }
        }
        if (repeatInput(this.mc.gameSettings.keyForward.getKeyCode(), UtilGui.verticalScrollRepeatDelay, UtilGui.verticalScrollInitialDelay) || repeatInput(this.mc.gameSettings.keyLookUp.getKeyCode(), UtilGui.verticalScrollRepeatDelay, UtilGui.verticalScrollInitialDelay)){
            selectColor(selectedColor - 1);
        }
        if (repeatInput(this.mc.gameSettings.keyBack.getKeyCode(), UtilGui.verticalScrollRepeatDelay, UtilGui.verticalScrollInitialDelay) || repeatInput(this.mc.gameSettings.keyLookDown.getKeyCode(), UtilGui.verticalScrollRepeatDelay, UtilGui.verticalScrollInitialDelay)){
            selectColor(selectedColor + 1);
        }
        if (KeyboardHelper.isKeyPressedThisFrame(this.mc.gameSettings.keyJump.getKeyCode())){
            selectDye(cursorX);
        }
    }

    @Override
    public void mouseReleased(final int mx, final int my, final int mouseButton) {
        super.mouseReleased(mx, my, mouseButton);
        if (this.mc.inputType == InputType.CONTROLLER && this.mc.controllerInput.buttonY.isPressed()){
            return;
        }
        if (this.activeTool >= 0 && this.activeTool < 3) {
            if (this.isDrawing && mouseButton != -1) {
                this.isDrawing = false;
                this.mouseButton = -1;
            } else if (this.isDrawing) {
                final int xInCanvas = (mx - this.canvasX) / 4;
                final int yInCanvas = (my - this.canvasY) / 4;
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
                final int xInCanvas = (mx - this.canvasX) / 4;
                final int yInCanvas = (my - this.canvasY) / 4;
                byte color = 0;
                if (this.mouseButton == 0 && selectedColor != 3) {
                    color = (byte)(selectedColor + 1);
                }
                this.flagSurfaceEditor.drawRectangle(this.xLast, this.yLast, xInCanvas, yInCanvas, color);
                this.mouseButton = -1;
            }
        } else if (this.activeTool == 5 && this.isDrawing && mouseButton != -1) {
            this.isDrawing = false;
            final int xInCanvas = (mx - this.canvasX) / 4;
            final int yInCanvas = (my - this.canvasY) / 4;
            byte color = 0;
            if (this.mouseButton == 0 && selectedColor != 3) {
                color = (byte)(selectedColor + 1);
            }
            this.flagSurfaceEditor.drawEllipse(this.xLast, this.yLast, xInCanvas, yInCanvas, color);
            this.mouseButton = -1;
        }
    }
    protected void drawGuiContainerForegroundLayer(){
        mc.textureManager.loadTexture("/assets/legacyui/gui/legacyflag.png").bind();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        drawTexturedModalRect(11 + 18 * cursorX, 34, 138, 232, 24,24);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float f) {
        this.mc.textureManager.loadTexture("/assets/legacyui/gui/legacyflag.png").bind();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.drawTexturedModalRect(this.GUIx, this.GUIy, 0, 0, this.xSize, this.ySize);
        if (LegacyContainerFlag.dyesMetaAtSlot.size() > 6){
            drawTexturedModalRect(this.GUIx + 124, this.GUIy + 42, 143, 196, 5,9);
            drawTexturedModalRect(this.GUIx + 7, this.GUIy + 42, 138, 196, 5,9);
        }
        final int textX = 148;
        if (selectedColor == 0) {
            this.drawString(this.font, "1", this.GUIx + textX, this.GUIy + 66, -1);
        } else {
            this.drawStringNoShadow(this.font, "1", this.GUIx + textX, this.GUIy + 66, -8421505);
        }
        if (selectedColor == 1) {
            this.drawString(this.font, "2", this.GUIx + textX, this.GUIy + 85, -1);
        } else {
            this.drawStringNoShadow(this.font, "2", this.GUIx + textX, this.GUIy + 85, -8421505);
        }
        if (selectedColor == 2) {
            this.drawString(this.font, "3", this.GUIx + textX, this.GUIy + 104, -1);
        } else {
            this.drawStringNoShadow(this.font, "3", this.GUIx + textX, this.GUIy + 104, -8421505);
        }
        if (selectedColor == 3) {
            this.drawString(this.font, "4", this.GUIx + textX, this.GUIy + 123, -1);
        } else {
            this.drawStringNoShadow(this.font, "4", this.GUIx + textX, this.GUIy + 123, -8421505);
        }
    }

    @Override
    public void render(final int mx, final int my, final float renderPartialTicks) {
        handleInputs();
        super.render(mx, my, renderPartialTicks);
        this.drawOverlaySurface.clear();
        final int xInCanvas = (mx - this.canvasX) / 4;
        final int yInCanvas = (my - this.canvasY) / 4;
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
        int regionX = mx;
        int regionY = my;
        if (this.mc.inputType == InputType.CONTROLLER){
            regionX = (int) this.mc.controllerInput.cursorX;
            regionY = (int) this.mc.controllerInput.cursorY;
        }


        if (this.flagRegion.isHovered(regionX, regionY)){
            for (final GuiButtonPrompt prompt: this.promptsDraw) {
                prompt.drawPrompt(this.mc, mx, my);
            }
        }
        else {
            for (final GuiButtonPrompt prompt: this.promptsSelect) {
                prompt.drawPrompt(this.mc, mx, my);
            }
        }
    }

    @Override
    public void keyPressed(final char eventCharacter, final int eventKey, final int mx, final int my) {
        super.keyPressed(eventCharacter, eventKey, mx, my);
        if (eventKey == Keyboard.KEY_ESCAPE || this.mc.gameSettings.keyInventory.isKeyboardKey(eventKey) || eventKey == Keyboard.KEY_BACK) {
            this.mc.thePlayer.closeScreen();
        }
        if (eventKey == Keyboard.KEY_1) {
            selectColor(0);
        }
        if (eventKey == Keyboard.KEY_2) {
            selectColor(1);
        }
        if (eventKey == Keyboard.KEY_3) {
            selectColor(2);
        }
        if (eventKey == Keyboard.KEY_4) {
            selectColor(3);
        }
    }
    private void selectDye(final int index){
        if (index >= LegacyContainerFlag.dyesMetaAtSlot.size()){return;}
        final int dye = ArrayHelper.wrapAroundIndex(index + dyeScroll, LegacyContainerFlag.dyesMetaAtSlot.size());
        this.containerFlag.swapDye(dye);
        LegacySoundManager.play.craft(false);
    }
    private void selectDyeOffset(final int newDyeScroll){
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
        this.containerFlag.setSlots();
        this.buttonLeft.enabled = LegacyContainerFlag.dyesMetaAtSlot.size() > 6;
        this.buttonRight.enabled = LegacyContainerFlag.dyesMetaAtSlot.size() > 6;
        for (int i = 0; i < this.dyeButtons.length; i++) {
            this.dyeButtons[i].enabled = i < (LegacyContainerFlag.dyesMetaAtSlot.size());
        }
    }
    private void setActiveTool(final int value){
        LegacySoundManager.play.focus(true);
        this.toolBtns[this.activeTool].enabled = true;
        this.activeTool = ArrayHelper.wrapAroundIndex(value, 6);
        this.toolBtns[this.activeTool].enabled = false;
    }
    private void setCursorX(final int value){
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
        if (this.mc.inputType == InputType.CONTROLLER){
            this.mc.controllerInput.snapToSlot(this, 39 + cursorX);
        }
    }
    private void selectColor(final int color){
        if (color != selectedColor){
            LegacySoundManager.play.focus(true);
        }
        selectedColor = color;
        if (selectedColor > 3){
            selectedColor -= 4;
        } else if (selectedColor < 0){
            selectedColor += 4;
        }
        this.eraseButton.enabled = selectedColor != 3;

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
    public Byte getPixelValue(final int x, final int y) {
        if (x < 0 || x >= 24) {
            return (byte)0;
        }
        if (y < 0 || y >= 16) {
            return (byte)0;
        }
        return this.tileEntity.flagColors[x + 24 * y];
    }

    @Override
    public boolean setPixelValue(final int x, final int y, final Byte value) {
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
    public void removed() {
        if (this.mc.currentWorld.isClientSide) {
            this.mc.getSendQueue().addToSendQueue(new PacketCustomPayload("BTA|Flag", this.tileEntity.flagColors));
        }
    }

    @Override
    public void guiSpecificControllerInput(final ControllerInput controllerInput) {
        final int dpadDelay = 1000/15;
        final int dyeSelectDelay = 1000/7;
        final int toolDelay = UtilGui.tabScrollRepeatDelay;
        if (controllerInput.buttonLeftTrigger.pressedThisFrame() || controllerInput.buttonLeftTrigger.isPressed() && RepeatInputHandler.doRepeatInput(-2, toolDelay) && controllerInput.buttonLeftTrigger.getHoldTime() > 3){
            RepeatInputHandler.manualSuccess(-2);
            setActiveTool(this.activeTool - 1);
        }
        if (controllerInput.buttonRightTrigger.pressedThisFrame() || controllerInput.buttonRightTrigger.isPressed() && RepeatInputHandler.doRepeatInput(-2, toolDelay) && controllerInput.buttonRightTrigger.getHoldTime() > 3){
            RepeatInputHandler.manualSuccess(-2);
            setActiveTool(this.activeTool + 1);
        }
        if (controllerInput.buttonLeftShoulder.pressedThisFrame() || controllerInput.buttonLeftShoulder.isPressed() && RepeatInputHandler.doRepeatInput(-2, toolDelay) && controllerInput.buttonLeftShoulder.getHoldTime() > 3){
            RepeatInputHandler.manualSuccess(-2);
            selectColor(selectedColor - 1);
        }
        if (controllerInput.buttonRightShoulder.pressedThisFrame() || controllerInput.buttonRightShoulder.isPressed() && RepeatInputHandler.doRepeatInput(-2, toolDelay) && controllerInput.buttonRightShoulder.getHoldTime() > 3){
            RepeatInputHandler.manualSuccess(-2);
            selectColor(selectedColor + 1);
        }
        if (this.flagRegion.isHovered((int) this.mc.controllerInput.cursorX, (int) this.mc.controllerInput.cursorY)){
            if (controllerInput.digitalPad.right.pressedThisFrame() || (controllerInput.digitalPad.right.isPressed() && RepeatInputHandler.doRepeatInput(-1, dpadDelay))){
                RepeatInputHandler.manualSuccess(-1);
                snapToPixel(1, 0);
            }
            if (controllerInput.digitalPad.left.pressedThisFrame() || (controllerInput.digitalPad.left.isPressed() && RepeatInputHandler.doRepeatInput(-1, dpadDelay))){
                RepeatInputHandler.manualSuccess(-1);
                snapToPixel(- 1, 0);
            }
            if (controllerInput.digitalPad.up.pressedThisFrame() || (controllerInput.digitalPad.up.isPressed() && RepeatInputHandler.doRepeatInput(-1, dpadDelay))){
                RepeatInputHandler.manualSuccess(-1);
                snapToPixel(0, -1);
            }
            if (controllerInput.digitalPad.down.pressedThisFrame() || (controllerInput.digitalPad.down.isPressed() && RepeatInputHandler.doRepeatInput(-1, dpadDelay))){
                RepeatInputHandler.manualSuccess(-1);
                snapToPixel(0, 1);
            }
            if (controllerInput.buttonA.isPressed()){
                mouseReleased((int) controllerInput.cursorX, (int) controllerInput.cursorY, -1);
            }
            if (controllerInput.buttonY.pressedThisFrame()){
                controllerInput.snapToSlot(this, 39 + cursorX);
            }
        } else {
            if (controllerInput.digitalPad.right.pressedThisFrame() || (controllerInput.digitalPad.right.isPressed() && RepeatInputHandler.doRepeatInput(-1, dyeSelectDelay))){
                RepeatInputHandler.manualSuccess(-1);
                setCursorX(cursorX + 1);
            }
            if (controllerInput.digitalPad.left.pressedThisFrame() || (controllerInput.digitalPad.left.isPressed() && RepeatInputHandler.doRepeatInput(-1, dyeSelectDelay))){
                RepeatInputHandler.manualSuccess(-1);
                setCursorX(cursorX - 1);
            }
            if (controllerInput.digitalPad.up.pressedThisFrame() || (controllerInput.digitalPad.up.isPressed() && RepeatInputHandler.doRepeatInput(-1, toolDelay))){
                RepeatInputHandler.manualSuccess(-1);
                selectColor(selectedColor - 1);
            }
            if (controllerInput.digitalPad.down.pressedThisFrame() || (controllerInput.digitalPad.down.isPressed() && RepeatInputHandler.doRepeatInput(-1, toolDelay))){
                RepeatInputHandler.manualSuccess(-1);
                selectColor(selectedColor + 1);
            }
            if (controllerInput.buttonY.pressedThisFrame()){
                controllerInput.cursorX = this.canvasX + this.pixelX * this.CANVAS_SCALE + (double) this.CANVAS_SCALE /2;
                controllerInput.cursorY = this.canvasY + this.pixelY * this.CANVAS_SCALE + (double) this.CANVAS_SCALE /2;
            }
            if (controllerInput.buttonA.pressedThisFrame()){
                selectDye(cursorX);
            }
        }
    }
    private void snapToPixel(final int x, final int y){
        this.pixelX = (int) ((this.mc.controllerInput.cursorX - this.canvasX)/ this.CANVAS_SCALE);
        this.pixelY = (int) ((this.mc.controllerInput.cursorY - this.canvasY)/ this.CANVAS_SCALE);
        this.pixelX += x;
        this.pixelY += y;
        if (this.pixelX > this.CANVAS_WIDTH -1){
            this.pixelX -= this.CANVAS_WIDTH;
        } else if (this.pixelX < 0){
            this.pixelX += this.CANVAS_WIDTH;
        }
        if (this.pixelY > this.CANVAS_HEIGHT -1){
            this.pixelY -= this.CANVAS_HEIGHT;
        } else if (this.pixelY < 0){
            this.pixelY += this.CANVAS_HEIGHT;
        }
        this.mc.controllerInput.cursorX = this.canvasX + this.pixelX * this.CANVAS_SCALE + (double) this.CANVAS_SCALE /2;
        this.mc.controllerInput.cursorY = this.canvasY + this.pixelY * this.CANVAS_SCALE + (double) this.CANVAS_SCALE /2;
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
