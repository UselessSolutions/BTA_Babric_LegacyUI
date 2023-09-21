package useless.legacyui.Gui.GuiElements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiElement;
import net.minecraft.client.render.FontRenderer;
import useless.legacyui.Gui.GuiScreens.UtilGui;
import useless.legacyui.Settings.ModSettings;

public class GuiButtonPrompt extends GuiRegion implements GuiElement {
    private static final int buttonAtlasWidth = 256;
    private final FontRenderer fontRenderer;
    public String prompt;
    public int[] buttonCoordinates;
    public int spacing;
    public GuiButtonPrompt(int id, int xPosition, int yPosition, int spacing, String prompt, int[] buttonCoordinates) {
        super(id, xPosition, yPosition, 0, 0);
        this.fontRenderer = Minecraft.getMinecraft(this).fontRenderer;
        this.prompt = prompt;
        this.buttonCoordinates = buttonCoordinates;
        this.spacing = spacing;
        this.width = (buttonCoordinates.length * 13) + (spacing * buttonCoordinates.length) + fontRenderer.getStringWidth(prompt);
        this.height = 13;
    }
    public void drawPrompt(Minecraft minecraft, int mouseX, int mouseY){
        UtilGui.bindTexture("/assets/legacyui/gui/Controller/buttons.png");
        int v = ModSettings.legacyOptions.getGuiControllerType().value.index()  * 13;
        for (int i = 0; i < buttonCoordinates.length; i++) {
            int u = buttonCoordinates[i] * 13;
            UtilGui.drawTexturedModalRect(this, xPosition + ((13 + spacing) * i), yPosition, u, v, 13, 13, 1f/buttonAtlasWidth);
        }
        fontRenderer.drawString(prompt, xPosition + ((13 + spacing) * buttonCoordinates.length), yPosition+2, ModSettings.legacyOptions.getGuiPromptColor().value.value);
    }
}
