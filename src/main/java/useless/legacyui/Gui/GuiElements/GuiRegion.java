package useless.legacyui.Gui.GuiElements;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiElement;

public class GuiRegion extends Gui implements GuiElement {
    public int width;
    public int height;
    public int xPosition;
    public int yPosition;
    public int id;
    public GuiRegion(int id, int xPosition, int yPosition, int width, int height) {
        this.id = id;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.width = width;
        this.height = height;
    }
    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
    }
    @Override
    public void setX(int newX) {
        this.xPosition = newX;
    }
    @Override
    public void setY(int newY) {
        this.yPosition = newY;
    }
    @Override
    public void setWidth(int newWidth) {
        this.width = newWidth;
    }
    @Override
    public void setHeight(int newHeight) {
        this.height = newHeight;
    }
    @Override
    public int getX() {
        return xPosition;
    }
    @Override
    public int getY() {
        return yPosition;
    }
    @Override
    public int getWidth() {
        return width;
    }
    @Override
    public int getHeight() {
        return height;
    }
}
