package useless.legacyui.Gui.GuiElements.Buttons;

import net.minecraft.client.gui.GuiTexturedButton;

public class GuiAudioTextureButton extends GuiTexturedButton implements IButtonSounds {
    private boolean muted = false;
    private String soundDir = "random.click";
    public GuiAudioTextureButton(int id, String texturePath, int xPosition, int yPosition, int u, int v, int width, int height) {
        super(id, texturePath, xPosition, yPosition, u, v, width, height);
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
