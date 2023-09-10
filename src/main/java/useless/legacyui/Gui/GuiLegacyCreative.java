package useless.legacyui.Gui;

import net.minecraft.client.gui.GuiInventory;
import net.minecraft.client.input.InputType;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.player.inventory.ContainerPlayer;
import net.minecraft.core.player.inventory.ContainerPlayerCreative;
import net.minecraft.core.sound.SoundType;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import useless.legacyui.ConfigTranslations;
import useless.legacyui.Gui.Container.ContainerCreativeLegacy;
import useless.legacyui.LegacyUI;
import useless.legacyui.Utils.KeyboardUtil;

public class GuiLegacyCreative extends GuiInventory {
    protected ContainerCreativeLegacy container;
    protected int tab; // Current page of tabs
    protected final int maxDisplayedTabs = 8; // Total amount of tab pages, zero index
    protected Object[] categories = new Object[8];
    public GuiLegacyCreative(EntityPlayer player) {
        super(player);
        this.container = (ContainerCreativeLegacy)inventorySlots;
    }
    public void initGui() {
        this.xSize = 273; // width of texture plus the 17px strip that was cut off
        this.ySize = 175; // height of Gui window
    }
    public void setControllerCursorPosition() {
        if (this.mc.inputType == InputType.CONTROLLER) {
            this.mc.controllerInput.cursorX = 50;
            this.mc.controllerInput.cursorY = 50;
        }
    }
    public void selectTab(int tabIndex) {
        uiSound("legacyui.ui.focus");
        if (tabIndex < 0) {
            tabIndex += categories.length;
        } else if (tabIndex > categories.length - 1) {
            tabIndex -= categories.length;
        }
        tab = tabIndex;
        setControllerCursorPosition();
        updatePages();
    }

    public void scrollTab(int direction) {
        if (direction > 0) {
            while (direction > 0) {
                selectTab(tab + 1);
                direction--;
            }
        } else if (direction < 0) {
            while (direction < 0) {
                selectTab(tab - 1);
                direction++;
            }
        }
    }
    protected void checkInputs(){
        /*if (KeyboardUtil.isKeyPressed(mc.gameSettings.keyForward.keyCode()) || KeyboardUtil.isKeyPressed(mc.gameSettings.keyLookUp.keyCode())) {
            scrollSlot(-1);
        }
        if (KeyboardUtil.isKeyPressed(mc.gameSettings.keyBack.keyCode()) || KeyboardUtil.isKeyPressed(mc.gameSettings.keyLookDown.keyCode())) {
            scrollSlot(1);
        }*/
        if (KeyboardUtil.isKeyPressed(mc.gameSettings.keyRight.keyCode()) || KeyboardUtil.isKeyPressed(mc.gameSettings.keyLookRight.keyCode())) {
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                scrollTab(1);
            } else {
                //scrollDisplaySlot(1);
            }
        }
        if (KeyboardUtil.isKeyPressed(mc.gameSettings.keyLeft.keyCode()) || KeyboardUtil.isKeyPressed(mc.gameSettings.keyLookLeft.keyCode())) {
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                scrollTab(-1);
            } else {
                //scrollDisplaySlot(-1);
            }
        }

        int mouseScroll = Mouse.getDWheel();
        ContainerCreativeLegacy.scrollRow(-mouseScroll/10);
    }
    protected void updatePages(){
        ((ContainerCreativeLegacy)inventorySlots).updatePage();
    }
    public void drawGuiContainerForegroundLayer() {
        int i = this.mc.renderEngine.getTexture("/assets/legacyui/gui/legacycreative.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(i);

        // Scroll Bar
        int scrollTopY = 36;
        int scrollBottomY = 143 - 15;
        int scrollYPos = (int)((scrollBottomY-scrollTopY) * ((float)ContainerCreativeLegacy.currentRow/ContainerCreativeLegacy.totalRows));
        this.drawTexturedModalRect(251, scrollTopY + scrollYPos, 131, 175, 15, 15);
    }
    public void drawGuiContainerBackgroundLayer(float f) {
        checkInputs();

        int i = this.mc.renderEngine.getTexture("/assets/legacyui/gui/legacycreative.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(i);
        int j = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;

        // Draws base Gui background
        this.drawTexturedModalRect(j, k, 0, 0, 256, this.ySize);
        this.drawTexturedModalRect(j + 256, k + this.ySize - 13, 205, 175, 17, 13);
        this.drawTexturedModalRect(j + 256, k + this.ySize - 81 - 13, 222, 175, 17, 81);
        this.drawTexturedModalRect(j + 256, k + this.ySize - 81 - 81 - 13, 239, 175, 17, 81);
    }
    private void uiSound(String soundDir){
        if (LegacyUI.config.getBoolean(ConfigTranslations.USE_LEGACY_SOUNDS.getKey())){
            mc.sndManager.playSound(soundDir, SoundType.GUI_SOUNDS, 1, 1);
        }
        else {
            mc.sndManager.playSound("random.click", SoundType.GUI_SOUNDS, 1, 1);
        }
    }
}
