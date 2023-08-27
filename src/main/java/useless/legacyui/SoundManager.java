package useless.legacyui;

import net.minecraft.client.Minecraft;

import java.io.File;

public class SoundManager {
    public static File appDirectory = Minecraft.getAppDir("minecraft-bta");
    public static File soundDirectory = new File(appDirectory.getAbsolutePath() + "\\resources\\mod\\sound");
    private static SoundManager soundManager;
    private Minecraft mc;

    public SoundManager(){
        mc = Minecraft.getMinecraft(this);
        LegacyUI.LOGGER.info(soundDirectory.getAbsolutePath());
        soundManager = this;
    }
    public static SoundManager getInstance(){
        if (soundManager == null){
            return new SoundManager();
        }
        return soundManager;
    }
    public static void addSound(String MOD_ID, String soundSource){
        String source = ("\\assets\\" + MOD_ID + "\\sound\\" + soundSource.replace("/", "\\")).replace("\\\\", "\\");
        String destination = (soundDirectory.getAbsolutePath() + "\\" + MOD_ID + "\\" + soundSource.replace("/", "\\")).replace("\\\\", "\\");
        File sourceFile = new File(source);
        File destFile = new File()

    }
}
