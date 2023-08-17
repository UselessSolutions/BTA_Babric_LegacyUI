package useless.legacyui.mixin;

import net.minecraft.client.option.GameSettings;
import net.minecraft.client.render.FontRenderer;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.Tessellator;
import net.minecraft.core.util.helper.ChatAllowedCharacters;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;
import java.util.regex.Pattern;

@Mixin(value = FontRenderer.class, remap = false)
public class FontMixin {
    @Shadow public int fontHeight;
    @Final
    @Shadow
    private static final Pattern FORMATTING_CODE_REGEX = Pattern.compile("(?i)\\u00A7[0-9A-FK-OR]");
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

    @Shadow
    private boolean obfuscatedStyle;
    @Shadow
    private boolean boldStyle;
    @Shadow
    private boolean italicStyle;
    @Shadow
    private boolean underlineStyle;
    @Shadow
    private boolean strikethroughStyle;
    @Shadow
    private float red;
    @Shadow
    private float blue;
    @Shadow
    private float green;
    @Shadow
    private float alpha;
    @Shadow
    private int textColor;
    @Shadow
    private final int[] colorCode = new int[32];
    @Shadow
    public Random random;
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

    /**
     * @author Useless
     * @reason Scalable font positioning
     */
    @Overwrite
    private void renderStringAtPos(String text, boolean flag) {
        for (int i = 0; i < text.length(); ++i) {
            Tessellator var7;
            int var6;
            int formatCode;
            char c = text.charAt(i);
            if (c == '\u00a7' && i + 1 < text.length()) {
                formatCode = "0123456789abcdefklmnor".indexOf(text.toLowerCase().charAt(i + 1));
                if (formatCode < 16) {
                    this.obfuscatedStyle = false;
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;
                    if (formatCode < 0) {
                        formatCode = 15;
                    }
                    if (flag) {
                        formatCode += 16;
                    }
                    this.textColor = var6 = this.colorCode[formatCode];
                    GL11.glColor4f((float) (var6 >> 16) / 255.0f, (float) (var6 >> 8 & 0xFF) / 255.0f, (float) (var6 & 0xFF) / 255.0f, this.alpha);
                } else if (formatCode == 16) {
                    this.obfuscatedStyle = true;
                } else if (formatCode == 17) {
                    this.boldStyle = true;
                } else if (formatCode == 18) {
                    this.strikethroughStyle = true;
                } else if (formatCode == 19) {
                    this.underlineStyle = true;
                } else if (formatCode == 20) {
                    this.italicStyle = true;
                } else if (formatCode == 21) {
                    this.obfuscatedStyle = false;
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;
                    GL11.glColor4f(this.red, this.blue, this.green, this.alpha);
                }
                ++i;
                continue;
            }
            formatCode = ChatAllowedCharacters.ALLOWED_CHARACTERS.indexOf(c);
            if (this.obfuscatedStyle && formatCode > 0) {
                while (this.charWidth[formatCode + 32] != this.charWidth[(var6 = this.random.nextInt(ChatAllowedCharacters.ALLOWED_CHARACTERS.length())) + 32]) {
                }
                formatCode = var6;
            }
            float var9 = this.renderCharAtPos(formatCode, c, this.italicStyle);
            if (this.boldStyle) {
                this.posX += 1.0f;
                this.renderCharAtPos(formatCode, c, this.italicStyle);
                this.posX -= 1.0f;
                var9 += 1.0f;
            }
            if (this.strikethroughStyle) {
                var7 = Tessellator.instance;
                GL11.glDisable(3553);
                var7.startDrawingQuads();
                var7.addVertex(this.posX, this.posY + (float) (this.fontHeight / 2), 0.0);
                var7.addVertex(this.posX + var9, this.posY + (float) (this.fontHeight / 2), 0.0);
                var7.addVertex(this.posX + var9, this.posY + (float) (this.fontHeight / 2) - 1.0f, 0.0);
                var7.addVertex(this.posX, this.posY + (float) (this.fontHeight / 2) - 1.0f, 0.0);
                var7.draw();
                GL11.glEnable(3553);
            }
            if (this.underlineStyle) {
                var7 = Tessellator.instance;
                GL11.glDisable(3553);
                var7.startDrawingQuads();
                int var8 = this.underlineStyle ? -1 : 0;
                var7.addVertex(this.posX + (float) var8, this.posY + (float) this.fontHeight, 0.0);
                var7.addVertex(this.posX + var9, this.posY + (float) this.fontHeight, 0.0);
                var7.addVertex(this.posX + var9, this.posY + (float) this.fontHeight - 1.0f, 0.0);
                var7.addVertex(this.posX + (float) var8, this.posY + (float) this.fontHeight - 1.0f, 0.0);
                var7.draw();
                GL11.glEnable(3553);
            }
            this.posX += (float) ((int) var9) * (fontHeight/9.0f); // Only part of code I care about
        }
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
