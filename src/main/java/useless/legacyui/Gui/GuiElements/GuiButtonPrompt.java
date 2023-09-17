package useless.legacyui.Gui.GuiElements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiElement;
import net.minecraft.client.render.FontRenderer;
import useless.legacyui.Gui.GuiScreens.UtilGui;
import useless.legacyui.ModSettings;

public class GuiButtonPrompt extends GuiRegion implements GuiElement {
    private static final int buttonAtlasWidth = 256;
    private final FontRenderer fontRenderer;
    public String prompt;
    public int buttonCoordinates;
    public int buttonCooridnate2 = -1;
    public int spacing;
    public GuiButtonPrompt(int id, int xPosition, int yPosition, int buttonCoordinate, int spacing, String prompt) {
        super(id, xPosition, yPosition, 0, 0);
        this.fontRenderer = Minecraft.getMinecraft(this).fontRenderer;
        this.prompt = prompt;
        this.buttonCoordinates = buttonCoordinate;
        this.spacing = spacing;
        this.width = 13 + spacing + fontRenderer.getStringWidth(prompt);
        this.height = 13;
    }
    public GuiButtonPrompt(int id, int xPosition, int yPosition, int buttonCoordinate, int buttonCoordinates2, int spacing, String prompt) {
        super(id, xPosition, yPosition, 0, 0);
        this.fontRenderer = Minecraft.getMinecraft(this).fontRenderer;
        this.prompt = prompt;
        this.buttonCoordinates = buttonCoordinate;
        this.buttonCooridnate2 = buttonCoordinates2;
        this.spacing = spacing;
        this.width = 13 + spacing + 13 + spacing + fontRenderer.getStringWidth(prompt);
        this.height = 13;
    }
    public void drawPrompt(Minecraft minecraft, int mouseX, int mouseY){
        UtilGui.bindTexture("/assets/legacyui/gui/Controller/buttons.png");
        int u = buttonCoordinates * 13;
        int v = ModSettings.Gui.GuiControllerType()  * 13;
        UtilGui.drawTexturedModalRect(this, xPosition, yPosition, u, v, 13, 13, 1f/buttonAtlasWidth);
        if (buttonCooridnate2 > -1){
            u = buttonCooridnate2 * 13;
            v = ModSettings.Gui.GuiControllerType() * 13;
            UtilGui.drawTexturedModalRect(this, xPosition + 13 + spacing, yPosition, u, v, 13, 13, 1f/buttonAtlasWidth);
            fontRenderer.drawString(prompt, xPosition + 26 + spacing * 2, yPosition+2, ModSettings.Colors.GuiPromptColor());
        } else {
            fontRenderer.drawString(prompt, xPosition + 13 + spacing, yPosition+2, ModSettings.Colors.GuiPromptColor());
        }
    }
}
