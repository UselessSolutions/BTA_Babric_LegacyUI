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
import useless.legacyui.Gui.GuiElements.GuiRegion;

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
    private int selectedColor = 0;
    private FlagRenderer flagRenderer;
    private DrawableEditor<Byte> flagSurfaceEditor;
    private DrawableEditor<Byte> drawOverlaySurfaceEditor;
    private GuiTexturedButton[] toolBtns;
    private int activeTool = 0;
    GuiSurface flagSurface;
    GuiSurface drawOverlaySurface;
    private int GUIx;
    private int GUIy;
    public static int selectedDyeSlot = -1;
    public GuiRegion dyeSlotRegion;
    public LegacyContainerFlag containerFlag;
    public GuiLegacyFlag(EntityPlayer player, TileEntityFlag flagTileEntity, RenderEngine renderEngine) {
        super(new LegacyContainerFlag(player.inventory, flagTileEntity));
        containerFlag = (LegacyContainerFlag)inventorySlots;
        this.renderEngineInstance = renderEngine;
        this.tileEntity = flagTileEntity;
        this.xSize = 170;
        this.ySize = 151;
        flagTileEntity.owner = player.username;
        this.flagRenderer = new FlagRenderer(renderEngine);
        this.flagSurface = new GuiSurface(24, 16, 4, flagTileEntity.flagColors);
        this.flagSurfaceEditor = new DrawableEditor<Byte>(this.flagSurface);
        this.drawOverlaySurface = new GuiSurface(24, 16, 4);
        this.drawOverlaySurfaceEditor = new DrawableEditor<Byte>(this.drawOverlaySurface);
    }

    @Override
    public void initGui() {
        GUIx = (this.width - this.xSize) / 2;
        GUIy = (this.height - this.ySize) / 2;
        dyeSlotRegion = new GuiRegion(200, GUIx + 3, GUIy + 37, 164, 22);
        super.initGui();
        this.controlList.clear();
        this.canvasX = GUIx + 37;
        this.canvasY = GUIy + 74;
        this.toolBtns = new GuiTexturedButton[7];
        for (int i = 0; i < 7; ++i) {
            this.toolBtns[i] = new GuiTexturedButton(i, "/assets/legacyui/gui/legacyflag.png", GUIx + 9 + 22 * i, GUIy + 8, 20 * i, 151, 20, 20);
            if (i == this.activeTool) {
                this.toolBtns[i].enabled = false;
            }
            this.controlList.add(this.toolBtns[i]);
        }
    }

    private void renderCanvas() {
        this.canvasX = GUIx + 37;
        this.canvasY = GUIy + 74;
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
        if (this.tileEntity.getStackInSlot(36 + this.selectedColor) == null) {
            return;
        }
        if (this.activeTool >= 0 && this.activeTool < 4) { // Drawing
            if (!this.isDrawing) {
                int xInCanvas = (x - this.canvasX) / 4;
                int yInCanvas = (y - this.canvasY) / 4;
                if (xInCanvas >= 0 && xInCanvas < 24 && yInCanvas >= 0 && yInCanvas < 16) {
                    this.isDrawing = true;
                    this.xLast = xInCanvas;
                    this.yLast = yInCanvas;
                    this.mouseButton = mouseButton;
                    byte color = 0;
                    int size = activeTool + 1;
                    if (this.mouseButton == 0 && activeTool != 3) {
                        color = (byte)(this.selectedColor + 1);
                    }
                    if (activeTool == 3){
                        size = 1;
                    }
                    this.flagSurfaceEditor.drawLine(xInCanvas, yInCanvas, xInCanvas, yInCanvas, color, size);
                }
            }
        } else if (this.activeTool == 4) { // Bucket
            if (!this.isDrawing) {
                int xInCanvas = (x - this.canvasX) / 4;
                int yInCanvas = (y - this.canvasY) / 4;
                if (xInCanvas >= 0 && xInCanvas < 24 && yInCanvas >= 0 && yInCanvas < 16) {
                    this.isDrawing = true;
                    this.mouseButton = mouseButton;
                    byte color = 0;
                    if (this.mouseButton == 0) {
                        color = (byte)(this.selectedColor + 1);
                    }
                    this.flagSurfaceEditor.floodFill(xInCanvas, yInCanvas, color);
                }
            }
        } else if (!(this.activeTool != 5 && this.activeTool != 6 || this.isDrawing)) { // Rectangle circle
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
        if (guibutton.id >= 0 && guibutton.id < 7) {
            this.toolBtns[this.activeTool].enabled = true;
            this.activeTool = guibutton.id;
            guibutton.enabled = false;
        }
    }

    @Override
    public void mouseMovedOrUp(int x, int y, int mouseButton) {
        super.mouseMovedOrUp(x, y, mouseButton);
        if (this.activeTool >= 0 && this.activeTool < 4) {
            if (this.isDrawing && mouseButton != -1) {
                this.isDrawing = false;
                this.mouseButton = -1;
            } else if (this.isDrawing) {
                int xInCanvas = (x - this.canvasX) / 4;
                int yInCanvas = (y - this.canvasY) / 4;
                byte color = 0;
                int size = activeTool + 1;
                if (this.mouseButton == 0 && activeTool != 3) {
                    color = (byte)(this.selectedColor + 1);
                }
                if (activeTool == 3){
                    size = 1;
                }
                if (MathHelper.abs(this.xLast - xInCanvas) <= 1.0f && MathHelper.abs(this.yLast - yInCanvas) <= 1.0f) {
                    this.flagSurfaceEditor.drawLine(xInCanvas, yInCanvas, xInCanvas, yInCanvas, color, size);
                } else {
                    this.flagSurfaceEditor.drawLine(this.xLast, this.yLast, xInCanvas, yInCanvas, color, size);
                }
                this.xLast = xInCanvas;
                this.yLast = yInCanvas;
            }
        } else if (this.activeTool == 4) { // Bucket
            if (this.isDrawing && mouseButton != -1) {
                this.isDrawing = false;
                this.mouseButton = -1;
            }
        } else if (this.activeTool == 5) { // Rectangle
            if (this.isDrawing && mouseButton != -1) {
                this.isDrawing = false;
                int xInCanvas = (x - this.canvasX) / 4;
                int yInCanvas = (y - this.canvasY) / 4;
                byte color = 0;
                if (this.mouseButton == 0) {
                    color = (byte)(this.selectedColor + 1);
                }
                this.flagSurfaceEditor.drawRectangle(this.xLast, this.yLast, xInCanvas, yInCanvas, color);
                this.mouseButton = -1;
            }
        } else if (this.activeTool == 6 && this.isDrawing && mouseButton != -1) { // Ellipse
            this.isDrawing = false;
            int xInCanvas = (x - this.canvasX) / 4;
            int yInCanvas = (y - this.canvasY) / 4;
            byte color = 0;
            if (this.mouseButton == 0) {
                color = (byte)(this.selectedColor + 1);
            }
            this.flagSurfaceEditor.drawEllipse(this.xLast, this.yLast, xInCanvas, yInCanvas, color);
            this.mouseButton = -1;
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {
        UtilGui.bindTexture("/assets/legacyui/gui/legacyflag.png");
        drawTexturedModalRect(GUIx, GUIy, 0, 0, this.xSize, this.ySize);

        if (selectedDyeSlot > 0){
            drawTexturedModalRect(GUIx + 7 + (33 * (selectedDyeSlot-1)), GUIy + 39, 0, 237, 90, 18);
        }

        if (selectedColor == 0) {
            drawString(fontRenderer, "1", GUIx + 49, GUIy + 59, -1);
        } else {
            drawStringNoShadow(fontRenderer, "1", GUIx + 49, GUIy + 59, -8421505);
        }
        if (selectedColor == 1) {
            drawString(fontRenderer, "2", GUIx + 82, GUIy + 59, -1);
        } else {
            drawStringNoShadow(fontRenderer, "2", GUIx + 82, GUIy + 59, -8421505);
        }
        if (selectedColor == 2) {
            drawString(fontRenderer, "3", GUIx + 115, GUIy + 59, -1);
        } else {
            drawStringNoShadow(fontRenderer, "3", GUIx + 115, GUIy + 59, -8421505);
        }
    }
    protected void drawGuiContainerForegroundLayer() {
        if (selectedDyeSlot > 0){
            UtilGui.bindTexture("/assets/legacyui/gui/legacyflag.png");
            drawTexturedModalRect( - 6  + (33 * (selectedDyeSlot - 1)), 35, 0, 211, 116, 26);
        }
    }

    @Override
    public void drawScreen(int x, int y, float renderPartialTicks) {
        if (dyeSlotRegion.isHovered(x, y) && selectedColor < 3){
            setSelectedDye(selectedColor + 1);
        } else {
            setSelectedDye(-1);
        }
        containerFlag.setSlots();

        super.drawScreen(x, y, renderPartialTicks);

        this.drawOverlaySurface.clear();
        int xInCanvas = (x - this.canvasX) / 4;
        int yInCanvas = (y - this.canvasY) / 4;
        byte color = (byte)(this.selectedColor + 1);
        if (this.mouseButton == 1 || activeTool == 3) {
            color = 4;
        }
        if (this.activeTool >= 0 && this.activeTool < 4) { // Regular Draw
            int size = activeTool + 1;
            if (activeTool == 3){
                size = 1;
            }
            this.drawOverlaySurfaceEditor.drawLine(xInCanvas, yInCanvas, xInCanvas, yInCanvas, color, size);
        } else if (this.activeTool == 4) { // Bucket
            this.drawOverlaySurfaceEditor.drawLine(xInCanvas, yInCanvas, xInCanvas, yInCanvas, color, 1);
        } else if (this.activeTool == 5) { // Rectangle
            if (this.isDrawing) {
                this.drawOverlaySurfaceEditor.drawRectangle(this.xLast, this.yLast, xInCanvas, yInCanvas, color);
            } else {
                this.drawOverlaySurfaceEditor.drawLine(xInCanvas, yInCanvas, xInCanvas, yInCanvas, color, 1);
            }
        } else if (this.activeTool == 6) { // Ellipses
            if (this.isDrawing) {
                this.drawOverlaySurfaceEditor.drawEllipse(this.xLast, this.yLast, xInCanvas, yInCanvas, color);
            } else {
                this.drawOverlaySurfaceEditor.drawLine(xInCanvas, yInCanvas, xInCanvas, yInCanvas, color, 1);
            }
        }
        this.renderCanvas();
    }

    @Override
    public void keyTyped(char c, int keyCode, int mouseX, int mouseY) {
        super.keyTyped(c, keyCode, mouseX, mouseY);
        if (keyCode == 1 || this.mc.gameSettings.keyInventory.isKey(keyCode) || keyCode == 14) {
            this.mc.thePlayer.closeScreen();
        }
        if (keyCode == 2) {
            this.selectedColor = 0;
        }
        if (keyCode == 3) {
            this.selectedColor = 1;
        }
        if (keyCode == 4) {
            this.selectedColor = 2;
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
    public void setSelectedDye(int newDye){
        selectedDyeSlot = newDye;
    }
}
