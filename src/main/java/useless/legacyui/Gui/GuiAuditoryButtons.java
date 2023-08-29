package useless.legacyui.Gui;

import net.minecraft.client.gui.GuiButton;

public class GuiAuditoryButtons extends GuiButton implements IButtonSounds{
    private boolean muted = false;
    private String soundDir = "random.click";
    public GuiAuditoryButtons(int id, int xPosition, int yPosition, String text) {
        super(id, xPosition, yPosition, text);
    }

    public GuiAuditoryButtons(int id, int xPosition, int yPosition, int width, int height, String text) {
        super(id, xPosition, yPosition, width, height, text);
    }

    @Override
    public boolean isMuted() {
        return muted;
    }

    public void setMuted(Boolean flag){
        muted = flag;
    }

    @Override
    public String getSound() {
        return soundDir;
    }
    public void setSound(String sound){
        soundDir = sound;
    }
}
