package useless.legacyui.Gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiContainer;
import net.minecraft.core.crafting.CraftingManager;
import net.minecraft.core.crafting.recipe.*;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.player.inventory.ContainerGuidebookRecipeBase;
import net.minecraft.core.player.inventory.ContainerGuidebookRecipeCrafting;
import net.minecraft.core.player.inventory.slot.Slot;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import useless.legacyui.LegacyUI;
import useless.legacyui.Sorting.CraftingCategories;
import useless.legacyui.Sorting.SortingCategory;

import java.util.List;

public class GuiLegacyCrafting extends GuiContainer {
    protected static int tab; // Current page of tabs
    protected final int maxDisplayedTabs = 8; // Total amount of tab pages, zero index
    protected static int currentSlot;
    protected final int totalDisplaySlots = 14;
    protected String slotString = "1/1";
    protected String tabString = "1/1"; // Indicator of what tab page you are on

    // Button Hell
    protected GuiButtonTransparent[] slotButtons = new GuiButtonTransparent[totalDisplaySlots];
    protected GuiButtonTransparent[] tabButtons = new GuiButtonTransparent[maxDisplayedTabs];

    protected GuiButtonTransparent scrollUp;
    protected GuiButtonTransparent scrollDown;
    protected SortingCategory[] categories;
    protected static int currentScroll = 0;
    protected static int currentCategory = 0;

    protected static Object[] storedCategories;



    public GuiLegacyCrafting(EntityPlayer player, int i, int j, int k) {
        super(new ContainerWorkbenchLegacy(player.inventory, player.world, i, j, k));
    }

    public void initGui() {
        super.initGui();
        this.xSize = 256+17; // width of texture plus the 17px strip that was cut off
        this.ySize = 175; // height of gui window


        // Setup Invisible buttons
        for (int i = 0; i < slotButtons.length; i++){
            slotButtons[i] = new GuiButtonTransparent(i + 4, (this.width - this.xSize)/2 + 11 + 18*i, (this.height - this.ySize) / 2 + 55, 18,18, "");
            this.controlList.add(slotButtons[i]);
        }
        for (int i = 0; i < tabButtons.length; i++){
            tabButtons[i] = new GuiButtonTransparent(i + 4 + slotButtons.length, (this.width - this.xSize)/2 + 34 * i, (this.height - this.ySize)/2, 34,24, "");
            this.controlList.add(tabButtons[i]);
        }

        scrollUp = new GuiButtonTransparent(4 + slotButtons.length + tabButtons.length + 1, (this.width - this.xSize)/2 + 11, (this.height - this.ySize) / 2 + 26, 18, 26,"");
        scrollDown = new GuiButtonTransparent(4 + slotButtons.length + tabButtons.length + 2, (this.width - this.xSize)/2 + 11, (this.height - this.ySize) / 2 + 76, 18, 26,"");
        this.controlList.add(scrollUp);
        this.controlList.add(scrollDown);

        this.updatePages();
        this.selectDisplaySlot(currentSlot);
    }
    protected void buttonPressed(GuiButton guibutton) {
        LegacyUI.LOGGER.info("" + currentScroll);
        if (!guibutton.enabled) {
            return;
        }
        int i = 0;
        for (GuiButtonTransparent button : slotButtons){
            if (guibutton == button){
                selectDisplaySlot(i);
                currentScroll = 0;
            }
            i++;
        }

        i = 0;
        for (GuiButtonTransparent button : tabButtons){
            if (guibutton == button){
                selectTab(i);
                currentScroll = 0;
            }
            i++;
        }
        if (guibutton == scrollUp){
            scroll(-1);
        }
        if (guibutton == scrollDown){
            scroll(1);
        }

    }

    public void scroll(int direction) {
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

    public void selectDisplaySlot(int slotIndex){
        if (slotIndex < 0){
            slotIndex = 0;
        } else if (slotIndex > totalDisplaySlots-1) {
            slotIndex = totalDisplaySlots-1;
        }
        currentSlot = slotIndex;
        slotString = "" + (currentSlot+1) + "/" + (totalDisplaySlots);
        updatePages();
    }
    public void selectTab(int tabIndex){
        currentSlot = 0; //Reset to start on tab change
        if (tabIndex < 0){
            tabIndex = 0;
        } else if (tabIndex > maxDisplayedTabs -1) {
            tabIndex = maxDisplayedTabs -1;
        } else if (tabIndex > categories.length - 1){ // Prevents crashing if trying to move to tab which does not exist
            tabIndex = categories.length - 1;
        }
        tab = tabIndex;
        tabString = "" + (tab+1) + "/" + (maxDisplayedTabs);
        updatePages();
    }
    public void lastPage() {
        --tab;
        this.updatePages();
    }

    public void nextPage() {
        ++tab;
        this.updatePages();
    }

    protected void updatePages() {
        // update scrollbar position
        scrollUp.xPosition = (this.width - this.xSize)/2 + 11 + 18 * currentSlot;
        scrollDown.xPosition = (this.width - this.xSize)/2 + 11 + 18 * currentSlot;

        this.updateRecipesByPage(tab);
    }


    public void updateRecipesByPage(int page) {
        int startIndex = page * totalDisplaySlots;
        this.categories = new SortingCategory[storedCategories.length];
        for (int i = 0; i < categories.length; ++i) {
            categories[i] = (SortingCategory) storedCategories[i];
        }
        ((ContainerWorkbenchLegacy)this.inventorySlots).setRecipes(this.mc.thePlayer, categories[tab], this.mc.statFileWriter, currentSlot, currentScroll);
    }


    public void onGuiClosed() {
        super.onGuiClosed();
        this.inventorySlots.onCraftGuiClosed(this.mc.thePlayer);
    }

    public void drawGuiContainerForegroundLayer() {
        this.fontRenderer.drawCenteredString("Inventory", 205, this.ySize - 78, 0XFFFFFF);
        this.fontRenderer.drawCenteredString("Crafting", 72, this.ySize - 78, 0XFFFFFF);
        //this.drawStringNoShadow(this.fontRenderer, this.tabString, this.xSize - this.fontRenderer.getStringWidth(this.tabString) - 52, 36, 0x404040);
        //this.drawStringNoShadow(this.fontRenderer, this.slotString,+ 52, 36, 0x404040);

        int i = this.mc.renderEngine.getTexture("assets/gui/legacycrafting.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(i);




        // Render Selector Scrollbar when applicable
        if (categories[tab].recipeGroups[currentSlot].recipes.length > 0){
            this.drawTexturedModalRect(7 + 18 * currentSlot,21,115,175, 26, 31);
            this.drawTexturedModalRect(7 + 18 * currentSlot,76,141,175, 26, 31);

            // Draw slightly wider marker
            this.drawTexturedModalRect(7 + 18 * currentSlot,52,35,175, 26, 24);
        }
        else {
            // Render Selection texture ontop of currently selected slot, does not require offset like bg layer
            this.drawTexturedModalRect(8 + 18 * currentSlot,52,36,175, 24, 24);
        }

    }

    public void drawGuiContainerBackgroundLayer(float f) {
        //this.scroll(Mouse.getDWheel()); // Scroll through tabs
        int i = this.mc.renderEngine.getTexture("assets/gui/legacycrafting.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(i);
        int j = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;

        // Draws base gui background
        this.drawTexturedModalRect(j, k, 0, 0, this.xSize, this.ySize); // TODO make gui texture 512x512
        this.drawTexturedModalRect(j + 256, k+this.ySize-81, 205, 175, 17, 81);
        this.drawTexturedModalRect(j + 256, k+this.ySize-81-81 , 222, 175, 17, 81);
        this.drawTexturedModalRect(j + 256, k+this.ySize-81-81-13, 239, 175, 17, 13);

        // Renders selected bookmark
        int bookMarkWidth = 34;
        this.drawTexturedModalRect(j + bookMarkWidth * tab, k - 2, 0, 175, bookMarkWidth, 30);

        // Render Selector Scrollbar background when applicable
        if (categories[tab].recipeGroups[currentSlot].recipes.length > 0){
            this.drawTexturedModalRect(j + 12 + 18 * currentSlot,k + 34,168,175, 18, 18);
            this.drawTexturedModalRect(j + 12 + 18 * currentSlot,k + 76,168,175, 18, 18);
        }




        // Category icons TODO Make icons render based of pages selected
        int item = 0;
        this.drawTexturedModalRect(j + 9 + 34*item,k + 6, 16 * item++, 256-16, 16, 16);
        this.drawTexturedModalRect(j + 9 + 34*item,k + 6, 16 * item++, 256-16, 16, 16);
        this.drawTexturedModalRect(j + 9 + 34*item,k + 6, 16 * item++, 256-16, 16, 16);
        this.drawTexturedModalRect(j + 9 + 34*item,k + 6, 16 * item++, 256-16, 16, 16);
        this.drawTexturedModalRect(j + 9 + 34*item,k + 6, 16 * item++, 256-16, 16, 16);
        this.drawTexturedModalRect(j + 9 + 34*item,k + 6, 16 * item++, 256-16, 16, 16);
        this.drawTexturedModalRect(j + 9 + 34*item,k + 6, 16 * item++, 256-16, 16, 16);
        this.drawTexturedModalRect(j + 9 + 34*item,k + 6, 16 * item++, 256-16, 16, 16);
    }

    public boolean getIsMouseOverSlot(Slot slot, int i, int j) {
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        i -= k;
        j -= l;
        int slotSize = 16;
        if (slot instanceof SlotResizable){
            slotSize = ((SlotResizable) slot).width;
        }
        return i >= slot.xDisplayPosition - 1 && i < slot.xDisplayPosition + slotSize -2 + 1 && j >= slot.yDisplayPosition - 1 && j < slot.yDisplayPosition + slotSize -2 + 1;
    }
    static {
        int i;
        tab = 0;
        currentSlot = 0;
        currentScroll = 0;
        currentCategory = 0;


        List<SortingCategory> categories = CraftingCategories.getInstance().getCategories();
        List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();

        GuiLegacyCrafting.storedCategories = categories.toArray();

    }
}
