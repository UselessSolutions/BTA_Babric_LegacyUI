package useless.legacyui.gui.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiElement;
import net.minecraft.client.input.InputType;
import net.minecraft.client.render.Font;
import org.lwjgl.opengl.GL11;
import useless.legacyui.gui.screens.UtilGui;
import useless.legacyui.LegacyUI;

public class GuiButtonPrompt extends GuiRegion implements GuiElement {
    public static final int A = 0;
    public static final int B = 1;
    public static final int X = 2;
    public static final int Y = 3;
    public static final int SELECT = 4;
    public static final int HOME = 5;
    public static final int START = 6;
    public static final int STICK_LEFT = 7;
    public static final int STICK_RIGHT = 8;
    public static final int LEFT_BUMPER = 9;
    public static final int RIGHT_BUMPER = 10;
    public static final int LEFT_TRIGGER = 11;
    public static final int RIGHT_TRIGGER = 12;
    public static final int DPAD_UP = 13;
    public static final int DPAD_DOWN = 14;
    public static final int DPAD_LEFT = 15;
    public static final int DPAD_RIGHT = 16;
    public static final int TOUCHPAD = 17;

    private static final int buttonAtlasWidth = 256;
    private final Font fontRenderer;
    public String prompt;
    public int[] buttonCoordinates;
    public int spacing;
    public GuiButtonPrompt(int id, int xPosition, int yPosition, int spacing, String prompt, int[] buttonCoordinates) {
        super(id, xPosition, yPosition, 0, 0);
        this.fontRenderer = Minecraft.getMinecraft().font;
        this.prompt = prompt;
        this.buttonCoordinates = buttonCoordinates;
        this.spacing = spacing;
        this.width = (buttonCoordinates.length * 13) + (spacing * buttonCoordinates.length) + fontRenderer.getStringWidth(prompt);
        this.height = 13;
    }
    public void drawPrompt(Minecraft minecraft, int mouseX, int mouseY){
        if (!LegacyUI.modSettings.legacyui$getForceButtonPrompts().value && minecraft.inputType != InputType.CONTROLLER) {return;}
        GL11.glColor4f(1, 1, 1, 1);
        minecraft.textureManager.loadTexture("/assets/legacyui/textures/gui/controller/buttons.png").bind();
        int v = LegacyUI.modSettings.legacyui$getGuiControllerType().value.index()  * 13;
        for (int i = 0; i < buttonCoordinates.length; i++) {
            int u = buttonCoordinates[i] * 13;
            UtilGui.drawTexturedModalRect(this, xPosition + ((13 + spacing) * i), yPosition, u, v, 13, 13, 1f/buttonAtlasWidth);
        }
        fontRenderer.drawString(prompt, xPosition + ((13 + spacing) * buttonCoordinates.length), yPosition+2, LegacyUI.modSettings.legacyui$getGuiPromptColor().value.value);
    }
}
