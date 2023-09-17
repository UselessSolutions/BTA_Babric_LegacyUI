package useless.legacyui.Gui;

import net.minecraft.client.input.controller.ControllerInput;

public interface IGuiController {
    void GuiControls(ControllerInput controllerInput);
    boolean playDefaultPressSound();
    boolean enableDefaultSnapping();
}
