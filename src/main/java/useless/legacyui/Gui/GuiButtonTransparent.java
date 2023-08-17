package useless.legacyui.Gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiButtonTransparent extends GuiButton {
    public GuiButtonTransparent(int id, int xPosition, int yPosition, String text) {
        super(id, xPosition, yPosition, text);
    }

    public GuiButtonTransparent(int id, int xPosition, int yPosition, int width, int height, String text) {
        super(id, xPosition, yPosition, width, height, text);
    }

    // Makes buttons invisible
    @Override
    public void drawButton(Minecraft minecraft, int i, int j) {
        return;
    }
}
