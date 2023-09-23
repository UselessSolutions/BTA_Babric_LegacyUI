package useless.legacyui.Gui.GuiScreens;

import net.minecraft.client.gui.*;
import net.minecraft.client.gui.drawing.DrawableEditor;
import net.minecraft.client.gui.drawing.IDrawableSurface;
import net.minecraft.client.render.FlagRenderer;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.core.block.entity.TileEntityFlag;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.net.packet.Packet141UpdateFlag;
import net.minecraft.core.player.inventory.ContainerFlag;
import net.minecraft.core.util.helper.Colors;
import net.minecraft.core.util.helper.MathHelper;
import org.lwjgl.opengl.GL11;
import useless.legacyui.Gui.Containers.LegacyContainerFlag;
import useless.legacyui.Gui.GuiElements.Buttons.GuiAuditoryButton;
import useless.legacyui.Gui.GuiElements.GuiRegion;
import useless.legacyui.Helper.ArrayHelper;

public class GuiLegacyFlag extends GuiContainer
        implements IDrawableSurface<Byte> {
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
    private FlagRenderer flagRenderer;
    private final DrawableEditor<Byte> flagSurfaceEditor;
    private final DrawableEditor<Byte> drawOverlaySurfaceEditor;
    private GuiTexturedButton[] toolBtns;
    private GuiTexturedButton eraseButton;
    private GuiAuditoryButton buttonRight;
    private GuiAuditoryButton buttonLeft;
    private int activeTool = 0;
    GuiSurface flagSurface;
    GuiSurface drawOverlaySurface;
    private final LegacyContainerFlag containerFlag;
    private int GUIx;
    private int GUIy;
    protected GuiAuditoryButton[] dyeButtons;

    public GuiLegacyFlag(EntityPlayer player, TileEntityFlag flagTileEntity, RenderEngine renderEngine) {
        super(new LegacyContainerFlag(player.inventory, flagTileEntity));
        containerFlag = (LegacyContainerFlag)inventorySlots;
        this.renderEngineInstance = renderEngine;
        this.tileEntity = flagTileEntity;
        this.xSize = 148;
        this.ySize = 138;
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
        super.initGui();
        controlList.clear();
        toolBtns = new GuiTexturedButton[6];
        for (int i = 0; i < 6; ++i) {
            toolBtns[i] = new GuiTexturedButton(i, "/assets/legacyui/gui/legacyflag.png", GUIx + 9 + 22 * i, GUIy + 8, 20 * i, 138, 20, 20);
            if (i == activeTool) {
                toolBtns[i].enabled = false;
            }
            controlList.add(toolBtns[i]);
        }
        eraseButton = new GuiTexturedButton(6, "/assets/legacyui/gui/legacyflag.png", GUIx + 119, GUIy + 113, 120, 138, 18, 18);
        controlList.add(eraseButton);
        dyeButtons = new GuiAuditoryButton[7];
        for (int i = 0; i < dyeButtons.length; i++) {
            dyeButtons[i] = new GuiAuditoryButton(10 + i, GUIx + 11 + 18 * i, GUIy + 33, 18, 18, "");
            dyeButtons[i].setMuted(true);
            dyeButtons[i].visible = false;
            controlList.add(dyeButtons[i]);
        }
        buttonRight = new GuiAuditoryButton(30, GUIx + 137, GUIy + 33, 10, 18, "");
        buttonRight.setMuted(false);
        controlList.add(buttonRight);
        buttonLeft = new GuiAuditoryButton(31, GUIx + 1, GUIy + 33, 10, 18, "");
        buttonLeft.setMuted(false);
        controlList.add(buttonLeft);
        setSlots();
    }

    private void renderCanvas() {
        this.canvasX = GUIx + 16;
        this.canvasY = GUIy + 61;
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
            this.toolBtns[this.activeTool].enabled = true;
            this.activeTool = guibutton.id;
            guibutton.enabled = false;
        }
        if (guibutton == eraseButton){
            eraserButton();
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

    @Override
    public void mouseMovedOrUp(int x, int y, int mouseButton) {
        super.mouseMovedOrUp(x, y, mouseButton);
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

    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {
        int i = this.mc.renderEngine.getTexture("/assets/legacyui/gui/legacyflag.png");
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.renderEngine.bindTexture(i);
        this.drawTexturedModalRect(GUIx, GUIy, 0, 0, this.xSize, this.ySize);
    }

    @Override
    public void drawScreen(int x, int y, float renderPartialTicks) {
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
        int textX = 138;
        if (selectedColor == 0) {
            this.drawString(this.fontRenderer, "1", GUIx + textX, GUIy + 60, -1);
        } else {
            this.drawStringNoShadow(this.fontRenderer, "1", GUIx + textX, GUIy + 60, -8421505);
        }
        if (selectedColor == 1) {
            this.drawString(this.fontRenderer, "2", GUIx + textX, GUIy + 79, -1);
        } else {
            this.drawStringNoShadow(this.fontRenderer, "2", GUIx + textX, GUIy + 79, -8421505);
        }
        if (selectedColor == 2) {
            this.drawString(this.fontRenderer, "3", GUIx + textX, GUIy + 98, -1);
        } else {
            this.drawStringNoShadow(this.fontRenderer, "3", GUIx + textX, GUIy + 98, -8421505);
        }
        if (selectedColor == 3) {
            this.drawString(this.fontRenderer, "4", GUIx + textX, GUIy + 117, -1);
        } else {
            this.drawStringNoShadow(this.fontRenderer, "4", GUIx + textX, GUIy + 117, -8421505);
        }
    }

    @Override
    public void keyTyped(char c, int keyCode, int mouseX, int mouseY) {
        super.keyTyped(c, keyCode, mouseX, mouseY);
        if (keyCode == 1 || this.mc.gameSettings.keyInventory.isKey(keyCode) || keyCode == 14) {
            this.mc.thePlayer.closeScreen();
        }
        if (keyCode == 2) {
            selectedColor = 0;
            eraseButton.enabled = true;
        }
        if (keyCode == 3) {
            selectedColor = 1;
            eraseButton.enabled = true;
        }
        if (keyCode == 4) {
            selectedColor = 2;
            eraseButton.enabled = true;
        }
        if (keyCode == 5) {
            eraserButton();
        }
    }
    private void eraserButton(){
        selectedColor = 3;
        eraseButton.enabled = false;
    }
    private void selectDye(int index){
        int dye = ArrayHelper.wrapAroundIndex(index + dyeScroll, LegacyContainerFlag.dyesMetaAtSlot.size());
        containerFlag.swapDye(dye);
    }
    private void selectDyeOffset(int newDyeScroll){
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
        buttonLeft.enabled = LegacyContainerFlag.dyesMetaAtSlot.size() > 7;
        buttonRight.enabled = LegacyContainerFlag.dyesMetaAtSlot.size() > 7;
        for (int i = 0; i < dyeButtons.length; i++) {
            dyeButtons[i].enabled = i < (LegacyContainerFlag.dyesMetaAtSlot.size());
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
}
