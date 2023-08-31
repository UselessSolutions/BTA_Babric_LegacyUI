package useless.legacyui.Gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiContainer;
import net.minecraft.client.input.InputType;
import net.minecraft.core.crafting.CraftingManager;
import net.minecraft.core.crafting.recipe.*;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.player.inventory.slot.Slot;
import net.minecraft.core.sound.SoundType;
import net.minecraft.core.util.helper.Time;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import useless.legacyui.ConfigTranslations;
import useless.legacyui.Gui.Container.ContainerWorkbenchLegacy;
import useless.prismaticlibe.gui.slot.SlotResizable;
import useless.legacyui.LegacyUI;
import useless.legacyui.Sorting.CraftingCategories;
import useless.legacyui.Sorting.SortingCategory;
import useless.prismaticlibe.gui.GuiAuditoryButtons;

import java.util.List;

public class GuiLegacyCrafting extends GuiContainer {
    protected  int tab; // Current page of tabs
    protected final int maxDisplayedTabs = 8; // Total amount of tab pages, zero index
    protected  int currentSlot;
    protected final int totalDisplaySlots = 14;
    protected String slotString = "1/1";
    protected String tabString = "1/1"; // Indicator of what tab page you are on

    // Button Hell
    protected GuiAuditoryButtons[] slotButtons = new GuiAuditoryButtons[totalDisplaySlots];
    protected GuiAuditoryButtons[] tabButtons = new GuiAuditoryButtons[maxDisplayedTabs];

    protected GuiAuditoryButtons scrollUp;
    protected GuiAuditoryButtons scrollDown;
    protected SortingCategory[] categories;
    protected  int currentScroll = 0;
    protected  int currentCategory = 0;

    protected static Object[] storedCategories;

    private boolean lastHeldItemWasNull = false;
    private boolean lastCheckPassed = false;

    private long timeStart = 0;

    private boolean[] keysPressed = new boolean[65536];
    private boolean isInInventory;


    public GuiLegacyCrafting(EntityPlayer player, int i, int j, int k) {
        super(new ContainerWorkbenchLegacy(player.inventory, player.world, i, j, k));
        this.mc = Minecraft.getMinecraft(this);
        this.isInInventory = false;
    }
    public GuiLegacyCrafting(EntityPlayer player) {
        super(new ContainerWorkbenchLegacy(player.inventory));
        this.mc = Minecraft.getMinecraft(this);
        this.isInInventory = true;
    }

    public void initGui() {
        super.initGui();
        this.xSize = 256 + 17; // width of texture plus the 17px strip that was cut off
        this.ySize = 175; // height of gui window


        // Setup Invisible buttons
        for (int i = 0; i < slotButtons.length; i++) {
            slotButtons[i] = new GuiAuditoryButtons(i + 4, (this.width - this.xSize) / 2 + 11 + 18 * i, (this.height - this.ySize) / 2 + 55, 18, 18, "");
            slotButtons[i].visible = false;
            slotButtons[i].setMuted(true);
            this.controlList.add(slotButtons[i]);
        }
        for (int i = 0; i < tabButtons.length; i++) {
            tabButtons[i] = new GuiAuditoryButtons(i + 4 + slotButtons.length, (this.width - this.xSize) / 2 + 34 * i, (this.height - this.ySize) / 2, 34, 24, "");
            tabButtons[i].visible = false;
            tabButtons[i].setMuted(true);
            this.controlList.add(tabButtons[i]);
        }

        scrollUp = new GuiAuditoryButtons(4 + slotButtons.length + tabButtons.length + 1, (this.width - this.xSize) / 2 + 11, (this.height - this.ySize) / 2 + 26, 18, 26, "");
        scrollUp.visible = false;
        scrollUp.setMuted(true);

        scrollDown = new GuiAuditoryButtons(4 + slotButtons.length + tabButtons.length + 2, (this.width - this.xSize) / 2 + 11, (this.height - this.ySize) / 2 + 76, 18, 26, "");
        scrollDown.visible = false;
        scrollDown.setMuted(true);
        this.controlList.add(scrollUp);
        this.controlList.add(scrollDown);


        this.updatePages();
        this.selectDisplaySlot(currentSlot, false);
    }

    protected void buttonPressed(GuiButton guibutton) {
        //LegacyUI.LOGGER.info("" + currentScroll);
        if (!guibutton.enabled) {
            return;
        }
        int i = 0;
        for (GuiButton button : slotButtons) {
            if (guibutton == button) {
                selectDisplaySlot(i);
            }
            i++;
        }

        i = 0;
        for (GuiButton button : tabButtons) {
            if (guibutton == button) {
                selectTab(i);
                currentScroll = 0;
            }
            i++;
        }
        if (guibutton == scrollUp) {
            scrollSlot(-1);
        }
        if (guibutton == scrollDown) {
            scrollSlot(1);
        }

    }

    public void scrollSlot(int direction) {
        uiSound("legacyui.ui.scroll");
        int count = 1;
        while (this.scrollUp.enabled && direction > 0 && count > 0) {
            currentScroll += 1;
            --count;
        }
        while (this.scrollDown.enabled && direction < 0 && count > 0) {
            currentScroll -= 1;
            --count;
        }
        updatePages();
    }

    public void selectDisplaySlot(int slotIndex) {
        selectDisplaySlot(slotIndex, true);
    }

    public void selectDisplaySlot(int slotIndex, boolean craft) {
        if (currentSlot != slotIndex) {
            uiSound("legacyui.ui.focus");
            currentScroll = 0; // reset scroll
        } else if (craft) {
            craft();
        }
        if (slotIndex < 0) {
            slotIndex += categories[tab].getRecipeGroups(isInInventory).length;
        } else if (slotIndex > categories[tab].getRecipeGroups(isInInventory).length - 1) {
            slotIndex -= categories[tab].getRecipeGroups(isInInventory).length;
        }
        currentSlot = slotIndex;
        slotString = "" + (currentSlot + 1) + "/" + (totalDisplaySlots);
        setControllerCursorPosition();
        updatePages();
    }

    public void setControllerCursorPosition() {
        if (this.mc.inputType == InputType.CONTROLLER) {
            this.mc.controllerInput.cursorX = slotButtons[currentSlot].xPosition + 9;
            this.mc.controllerInput.cursorY = slotButtons[currentSlot].yPosition + 9;
        }
    }

    public void scrollDisplaySlot(int direction) {
        if (direction > 0) {
            while (direction > 0) {
                selectDisplaySlot(currentSlot + 1, false);
                direction--;
            }
        } else if (direction < 0) {
            while (direction < 0) {
                selectDisplaySlot(currentSlot - 1, false);
                direction++;
            }
        }
    }

    public void craft() {
        if (((ContainerWorkbenchLegacy) this.inventorySlots).craft(this.mc, this.inventorySlots.windowId, categories[tab], currentSlot, currentScroll)){
            uiSound("legacyui.ui.craft");
        } else {
            uiSound("legacyui.ui.craftfail");
        }

    }

    public void selectTab(int tabIndex) {
        uiSound("legacyui.ui.focus");
        currentSlot = 0; //Reset to start on tab change
        if (tabIndex < 0) {
            tabIndex += categories.length;
        } else if (tabIndex > categories.length - 1) {
            tabIndex -= categories.length;
        }
        tab = tabIndex;
        tabString = "" + (tab + 1) + "/" + (maxDisplayedTabs);
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

    public boolean getIsMouseOverSlot(Slot slot, int i, int j) {
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        i -= k;
        j -= l;
        int slotSize = 16;
        if (slot instanceof SlotResizable) {
            slotSize = ((SlotResizable) slot).width;
        }
        return i >= slot.xDisplayPosition - 1 && i < slot.xDisplayPosition + slotSize - 2 + 1 && j >= slot.yDisplayPosition - 1 && j < slot.yDisplayPosition + slotSize - 2 + 1;
    }

    protected void updatePages() {
        // update scrollbar position
        scrollUp.xPosition = (this.width - this.xSize) / 2 + 11 + 18 * currentSlot;
        scrollDown.xPosition = (this.width - this.xSize) / 2 + 11 + 18 * currentSlot;

        this.categories = new SortingCategory[storedCategories.length];
        for (int i = 0; i < categories.length; ++i) {
            categories[i] = (SortingCategory) storedCategories[i];
        }
        ((ContainerWorkbenchLegacy) this.inventorySlots).setRecipes(this.mc.thePlayer, categories[tab], this.mc.statFileWriter, currentSlot, currentScroll, renderCraftingDisplay());
    }

    public void onGuiClosed() {
        super.onGuiClosed();
        this.inventorySlots.onCraftGuiClosed(this.mc.thePlayer);
    }

    public void drawGuiContainerForegroundLayer() {
        try {
            this.drawStringCenteredNoShadow(fontRenderer, "Inventory", 205, this.ySize - 78, LegacyUI.getGuiLabelColor());
            this.drawStringCenteredNoShadow(fontRenderer, "Crafting", 72, this.ySize - 78, LegacyUI.getGuiLabelColor());

            int i = this.mc.renderEngine.getTexture("/assets/legacyui/gui/legacycrafting.png");
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.mc.renderEngine.bindTexture(i);

            // Render Selector Scrollbar when applicable
            if (categories[tab].getRecipeGroups(isInInventory)[currentSlot].getRecipes(isInInventory).length > 1) {
                this.drawTexturedModalRect(7 + 18 * currentSlot, 21, 115, 175, 26, 31);
                this.drawTexturedModalRect(7 + 18 * currentSlot, 76, 141, 175, 26, 31);

                // Draw slightly wider marker
                this.drawTexturedModalRect(7 + 18 * currentSlot, 52, 35, 175, 26, 24);
            } else {
                // Render Selection texture ontop of currently selected slot, does not require offset like bg layer
                this.drawTexturedModalRect(8 + 18 * currentSlot, 52, 36, 175, 24, 24);
            }
        }
        catch (Exception e){
            onGuiClosed();
        }

    }

    private boolean isKeyPressed(int keyCode) {
        if (Keyboard.isKeyDown(keyCode)) {
            if (keysPressed[keyCode]) {
                return false;
            } else {
                keysPressed[keyCode] = true;
                return true;
            }
        } else {
            keysPressed[keyCode] = false;
            return false;
        }
    }

    public void drawGuiContainerBackgroundLayer(float f) {
        try {
            shouldUpdateThisFrame();

            //this.scrollSlot(-Mouse.getDWheel()); // Scroll through tabs

            if (isKeyPressed(mc.gameSettings.keyForward.keyCode()) || isKeyPressed(mc.gameSettings.keyLookUp.keyCode())) {
                scrollSlot(-1);
            }
            if (isKeyPressed(mc.gameSettings.keyBack.keyCode()) || isKeyPressed(mc.gameSettings.keyLookDown.keyCode())) {
                scrollSlot(1);
            }
            if (isKeyPressed(mc.gameSettings.keyRight.keyCode()) || isKeyPressed(mc.gameSettings.keyLookRight.keyCode())) {
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    scrollTab(1);
                } else {
                    scrollDisplaySlot(1);
                }
            }
            if (isKeyPressed(mc.gameSettings.keyLeft.keyCode()) || isKeyPressed(mc.gameSettings.keyLookLeft.keyCode())) {
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    scrollTab(-1);
                } else {
                    scrollDisplaySlot(-1);
                }
            }
            if (isKeyPressed(mc.gameSettings.keyJump.keyCode())) {
                craft();
            }

            int i = this.mc.renderEngine.getTexture("/assets/legacyui/gui/legacycrafting.png");
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.mc.renderEngine.bindTexture(i);
            int j = (this.width - this.xSize) / 2;
            int k = (this.height - this.ySize) / 2;

            // Draws base gui background
            this.drawTexturedModalRect(j, k, 0, 0, 256, this.ySize); // TODO make gui texture 512x512
            this.drawTexturedModalRect(j + 256, k + this.ySize - 81, 205, 175, 17, 81);
            this.drawTexturedModalRect(j + 256, k + this.ySize - 81 - 81, 222, 175, 17, 81);
            this.drawTexturedModalRect(j + 256, k + this.ySize - 81 - 81 - 13, 239, 175, 17, 13);

            // Renders selected bookmark
            int bookMarkWidth = 34;
            this.drawTexturedModalRect(j + bookMarkWidth * tab, k - 2, 0, 175, bookMarkWidth, 30);

            // If 2x2
            if (isInInventory || categories[tab].getRecipeGroups(isInInventory)[currentSlot].getContainer(currentScroll, isInInventory).inventorySlots.size() < 6 && renderCraftingDisplay()) {
                this.drawTexturedModalRect(j + 19, k + 108, 61, 175, 54, 54);
            }

            // Render Selector Scrollbar background when applicable
            if (categories[tab].getRecipeGroups(isInInventory)[currentSlot].getRecipes(isInInventory).length > 1) {
                this.drawTexturedModalRect(j + 12 + 18 * currentSlot, k + 34, 168, 175, 18, 18);
                this.drawTexturedModalRect(j + 12 + 18 * currentSlot, k + 76, 168, 175, 18, 18);
            }

            // Category icons TODO Make icons render based of pages selected
            int item = 0;
            this.drawTexturedModalRect(j + 9 + 34 * item, k + 6, 16 * item++, 256 - 16, 16, 16);
            this.drawTexturedModalRect(j + 9 + 34 * item, k + 6, 16 * item++, 256 - 16, 16, 16);
            this.drawTexturedModalRect(j + 9 + 34 * item, k + 6, 16 * item++, 256 - 16, 16, 16);
            this.drawTexturedModalRect(j + 9 + 34 * item, k + 6, 16 * item++, 256 - 16, 16, 16);
            this.drawTexturedModalRect(j + 9 + 34 * item, k + 6, 16 * item++, 256 - 16, 16, 16);
            this.drawTexturedModalRect(j + 9 + 34 * item, k + 6, 16 * item++, 256 - 16, 16, 16);
            this.drawTexturedModalRect(j + 9 + 34 * item, k + 6, 16 * item++, 256 - 16, 16, 16);
            this.drawTexturedModalRect(j + 9 + 34 * item, k + 6, 16 * item++, 256 - 16, 16, 16);

            if (this.mc.inputType == InputType.CONTROLLER) {
                // Controller Prompts
                int craftX = 50;
                int spacing = 14;
                int buttonsOffset = 2;
                drawStringNoShadow(fontRenderer, "Craft", craftX + spacing, this.height - 24, 0xFFFFFFFF);
                int exitX = craftX + spacing + fontRenderer.getStringWidth("Craft") + spacing;
                drawStringNoShadow(fontRenderer, "Exit", exitX + spacing, this.height - 24, 0xFFFFFFFF);
                int tabX = exitX + spacing + fontRenderer.getStringWidth("Exit") + spacing;
                drawStringNoShadow(fontRenderer, "Select Tab", tabX + spacing + 25, this.height - 24, 0xFFFFFFFF);


                i = this.mc.renderEngine.getTexture("/assets/legacyui/gui/xbox360.png");
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                this.mc.renderEngine.bindTexture(i);
                GL11.glScaled(.5D, .5D, .5D);

                this.drawTexturedModalRect((craftX + buttonsOffset) * 2, (this.height - 26) * 2, 0, 0, 20, 20);
                this.drawTexturedModalRect((exitX + buttonsOffset) * 2, (this.height - 26) * 2, 20, 0, 20, 20);
                this.drawTexturedModalRect((tabX + buttonsOffset) * 2, (this.height - 26) * 2, 90, 0, 71, 21);

                GL11.glScaled(2, 2, 2);
            }
        } catch (Exception e) {
            onGuiClosed();
        }
    }

    public boolean renderCraftingDisplay() {
        boolean holdingItem = mc.thePlayer.inventory.getHeldItemStack() != null;

        boolean isItem = false;
        for (int i = 1; i < (isInInventory ? 5:10); i++) {
            isItem = isItem || (inventorySlots.getSlot(i) != null && inventorySlots.getSlot(i).getStack() != null);
        }
        return !isItem && !holdingItem;
    }

    public void shouldUpdateThisFrame() {
        if (lastHeldItemWasNull != (mc.thePlayer.inventory.getHeldItemStack() == null) || lastCheckPassed) {
            lastHeldItemWasNull = (mc.thePlayer.inventory.getHeldItemStack() == null);
            if (!LegacyUI.config.getBoolean(ConfigTranslations.EXPERIMENTAL_STACK_FIX.getKey())) {
                updatePages();
            } else if (lastCheckPassed && System.currentTimeMillis() - timeStart > LegacyUI.config.getInt(ConfigTranslations.EXPERIMENTAL_STACK_DELAY.getKey())) {
                //timeStart = Time.now();
                updatePages();
                lastCheckPassed = false;
            } else if (!lastCheckPassed) {
                timeStart = Time.now();
                lastCheckPassed = true;
            }
        }
    }

    private void uiSound(String soundDir){
        if (LegacyUI.config.getBoolean(ConfigTranslations.USE_LEGACY_SOUNDS.getKey())){
            mc.sndManager.playSound(soundDir, SoundType.GUI_SOUNDS, 1, 1);
        }
        else {
            mc.sndManager.playSound("random.click", SoundType.GUI_SOUNDS, 1, 1);
        }
    }

    static {
        List<SortingCategory> categories = CraftingCategories.getInstance().getCategories();
        GuiLegacyCrafting.storedCategories = categories.toArray();
    }
}
