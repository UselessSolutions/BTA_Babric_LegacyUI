package useless.legacyui.mixin;

import net.minecraft.client.render.FontRenderer;
import org.lwjgl.opengl.GL11;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Random;
import java.util.regex.Pattern;

@Mixin(value = FontRenderer.class, remap = false)
public class FontMixin {
    @Shadow public int fontHeight;
    @Final
    @Shadow
    private final byte[] charWidth = new byte[65536];
    @Final
    @Shadow
    private final int[] fontTextureNames = new int[256];
    @Final
    @Shadow
    private final byte[] glyphWidth = new byte[65536];
    @Final
    @Shadow
    private final int[] glyphTextureNames = new int[256];
    @Shadow
    private int boundTextureName;

    @Shadow
    private float posX;
    @Shadow
    private float posY;
    /**
     * @author Useless
     * @reason Scalable font rendering
     */
    @Overwrite
    public float renderDefaultChar(char c, boolean italic) {
        if (this.charWidth[c] == 0 && this.glyphWidth[c] == 0) {
            return 0.0f;
        }
        int charWrapped = c / 256;
        boolean success = true;
        if (this.fontTextureNames[charWrapped] == 0) {
            success = this.loadDefaultTexture(charWrapped);
        }
        if (success) {
            if (this.boundTextureName != this.fontTextureNames[charWrapped]) {
                GL11.glBindTexture(3553, this.fontTextureNames[charWrapped]);
                this.boundTextureName = this.fontTextureNames[charWrapped];
            }
        } else if (this.boundTextureName != this.glyphTextureNames[charWrapped]) {
            GL11.glBindTexture(3553, this.glyphTextureNames[charWrapped]);
            this.boundTextureName = this.glyphTextureNames[charWrapped];
        }
        int var4 = success ? this.charWidth[c] >>> 4 : this.glyphWidth[c] >>> 4;
        int var5 = success ? this.charWidth[c] & 0xF : this.glyphWidth[c] & 0xF;
        float var6 = var4;
        float var7 = var5 + 1;
        float var8 = (float)(c % 16 * 16) + var6;
        float var9 = (c & 0xFF) / 16 * 16;
        float var10 = var7 - var6 - 0.02f;
        float var11 = italic ? 1.0f : 0.0f;
        float fontScale = this.fontHeight/9f;
        GL11.glBegin(5);
        GL11.glTexCoord2f(var8 / 256.0f, var9 / 256.0f);
        GL11.glVertex3f(this.posX + var11, this.posY, 0.0f);
        GL11.glTexCoord2f(var8 / 256.0f, (var9 + 15.98f) / 256.0f);
        GL11.glVertex3f(this.posX - var11, this.posY + 7.99f * fontScale, 0.0f);
        GL11.glTexCoord2f((var8 + var10) / 256.0f, var9 / 256.0f);
        GL11.glVertex3f(this.posX + var10 * fontScale / 2.0f + var11, this.posY, 0.0f);
        GL11.glTexCoord2f((var8 + var10) / 256.0f, (var9 + 15.98f) / 256.0f);
        GL11.glVertex3f(this.posX + var10  * fontScale / 2.0f - var11, this.posY + 7.99f * fontScale, 0.0f);
        GL11.glEnd();
        return (var7 - var6) / 2.0f + 1.0f;
    }

    @Redirect(method = "Lnet/minecraft/client/render/FontRenderer;renderStringAtPos(Ljava/lang/String;Z)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/FontRenderer;posX:F", opcode = Opcodes.PUTFIELD))
    private void stringPosInject(FontRenderer fontRenderer, float newPosX) {
            this.posX += (newPosX - posX) * (fontHeight/9.0f); // Adjusts char offset by its fontHeight
    }

    @Shadow
    private float renderCharAtPos(int formatCode, char c, boolean italicStyle) {
        return 0.0f;
    }

    @Shadow
    private boolean loadDefaultTexture(int charWrapped) {
        return false;
    }
}
