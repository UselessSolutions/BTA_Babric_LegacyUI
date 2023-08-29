package useless.legacyui;


import com.b100.utils.FileUtils;
import com.google.common.io.Files;
import net.minecraft.client.Minecraft;

import java.io.*;
import java.net.URI;
import java.util.Hashtable;


public class SoundManager {
    private static Hashtable<String, String> fileCache = new Hashtable<String, String>();
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
        String destination = ("\\" + MOD_ID + "\\" + soundSource.replace("/", "\\")).replace("\\\\", "\\");
        String source = ("/assets/" + MOD_ID + "/sound/" + soundSource).replace("//", "/").trim();

        String tempfile = extract(source);
        FileUtils.copy(new File(tempfile), new File(soundDirectory, destination));
    }
    private static String extract(String jarFilePath){

        if(jarFilePath == null)
            return null;

        // See if we already have the file
        if(fileCache.contains(jarFilePath))
            return fileCache.get(jarFilePath);

        // Alright, we don't have the file, let's extract it
        try {
            // Read the file we're looking for
            InputStream fileStream = SoundManager.class.getResourceAsStream(jarFilePath);

            // Was the resource found?
            if(fileStream == null)
                return null;

            // Grab the file name
            String[] chopped = jarFilePath.split("\\/");
            String fileName = chopped[chopped.length-1];

            // Create our temp file (first param is just random bits)
            File tempFile = File.createTempFile("asdf", fileName);

            // Set this file to be deleted on VM exit
            tempFile.deleteOnExit();

            // Create an output stream to barf to the temp file
            OutputStream out = new FileOutputStream(tempFile);

            // Write the file to the temp file
            byte[] buffer = new byte[1024];
            int len = fileStream.read(buffer);
            while (len != -1) {
                out.write(buffer, 0, len);
                len = fileStream.read(buffer);
            }

            // Store this file in the cache list
            fileCache.put(jarFilePath, tempFile.getAbsolutePath());

            // Close the streams
            fileStream.close();
            out.close();

            // Return the path of this sweet new file
            return tempFile.getAbsolutePath();

        } catch (IOException e) {
            return null;
        }
    }
}


