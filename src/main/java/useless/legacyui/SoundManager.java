package useless.legacyui;

import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
    public void addSound(String MOD_ID, String soundSource){
        //String source = ("\\assets\\" + MOD_ID + "\\sound\\" + soundSource.replace("/", "\\")).replace("\\\\", "\\");
        String destination = ("\\" + MOD_ID + "\\" + soundSource.replace("/", "\\")).replace("\\\\", "\\");

        String source = ("/assets/" + MOD_ID + "/sound/" + soundSource).replace("//", "/").trim();

        try {
            JarFile jar = new JarFile("legacyui.jar");
            JarEntry entry = jar.getJarEntry("META-INF/file.txt");
            if (entry != null) {
                // META-INF/file.txt exists in foo.jar
                LegacyUI.LOGGER.info("Source in jar");
            }
        }
        catch (IOException ioException){
            LegacyUI.LOGGER.warn(ioException.toString());
        }

        File sourceFile = new File(mc.getMinecraftDir(),source);
        File destFile = new File(soundDirectory, destination);
        LegacyUI.LOGGER.info(sourceFile.getPath() + " | " + destFile.getAbsolutePath());

        if (sourceFile.isFile()){
            LegacyUI.LOGGER.info("source file exists");
        }
        if (destFile.isFile()){
            LegacyUI.LOGGER.info("Destination file already exists!");
        }

    }
}
