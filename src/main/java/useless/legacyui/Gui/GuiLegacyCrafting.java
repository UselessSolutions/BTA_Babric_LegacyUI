package useless.legacyui.Gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiContainer;
import net.minecraft.client.gui.GuiGuidebook;
import net.minecraft.core.block.Block;
import net.minecraft.core.crafting.CraftingManager;
import net.minecraft.core.crafting.recipe.*;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.ContainerGuidebook;
import net.minecraft.core.player.inventory.ContainerGuidebookRecipeBase;
import net.minecraft.core.player.inventory.ContainerGuidebookRecipeCrafting;
import net.minecraft.core.player.inventory.ContainerGuidebookRecipeFurnace;
import net.minecraft.core.player.inventory.slot.Slot;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import useless.legacyui.LegacyUI;

import java.util.ArrayList;
import java.util.List;

public class GuiLegacyCrafting extends GuiContainer {
    protected static int tab; // Current page of tabs
    protected static int maxTab; // Total amount of tab pages, zero index
    protected static int currentSlot;
    protected static int totalDisplaySlots;
    protected GuiButton lastTabButton;
    protected GuiButton nextTabButton;
    protected String slotString = "1/1";
    protected GuiButton lastSlotButton;
    protected GuiButton nextSlotButton;
    protected String tabString = "1/1"; // Indicator of what tab page you are on
    protected ContainerGuidebookRecipeBase[] recipes;
    private static Object[] storedRecipes;
    private static int totalRecipes;
    public GuiLegacyCrafting(EntityPlayer player, int i, int j, int k) {
        super(new ContainerWorkbenchLegacy(player.inventory, player.world, i, j, k));
    }

    public void initGui() {
        super.initGui();
        this.xSize = 256+17; // width of texture plus the 17px strip that was cut off
        this.ySize = 175; // height of gui window
        this.lastTabButton = new GuiButton(0, (this.width - this.xSize)/2  + this.xSize - 20 - 20 - 5 - 6, (this.height - this.ySize) / 2 + 28, 20, 20, "<");
        this.nextTabButton = new GuiButton(1, (this.width - this.xSize)/2  + this.xSize - 20 - 6, (this.height - this.ySize) / 2 + 28, 20, 20, ">");
        this.lastSlotButton = new GuiButton(0, (this.width - this.xSize)/2  + 6, (this.height - this.ySize) / 2 + 28, 20, 20, "<");
        this.nextSlotButton = new GuiButton(1, (this.width - this.xSize)/2  + 20 + 5 + 6, (this.height - this.ySize) / 2 + 28, 20, 20, ">");
        this.controlList.add(this.lastTabButton);
        this.controlList.add(this.nextTabButton);
        this.controlList.add(this.lastSlotButton);
        this.controlList.add(this.nextSlotButton);
        this.updatePages();
        this.selectDisplaySlot(currentSlot);
    }
    protected void buttonPressed(GuiButton guibutton) {
        if (!guibutton.enabled) {
            return;
        }
        if (guibutton == this.lastTabButton) {
            this.scroll(1);
        }
        if (guibutton == this.nextTabButton) {
            this.scroll(-1);
        }
        if (guibutton == this.lastSlotButton){
            selectDisplaySlot(currentSlot-1);
        }
        if (guibutton == this.nextSlotButton){
            selectDisplaySlot(currentSlot+1);
        }
    }

    public void selectDisplaySlot(int slotIndex){
        if (slotIndex < 0){
            slotIndex = 0;
        } else if (slotIndex > totalDisplaySlots-1) {
            slotIndex = totalDisplaySlots-1;
        }
        currentSlot = slotIndex;
        slotString = "" + (currentSlot+1) + "/" + (totalDisplaySlots);
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
        this.updateRecipesByPage(tab);
        this.updatePageSwitcher();
    }

    protected void updatePageSwitcher() {
        this.lastTabButton.enabled = tab != 0;
        this.nextTabButton.enabled = tab != maxTab;
        this.tabString = "" + (tab + 1) + "/" + (maxTab + 1);
    }

    public void scroll(int direction) {
        int count = 1;
        if (Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157)) {
            count = 10;
            if (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54)) {
                count = 100;
            }
        }
        while (this.lastTabButton.enabled && direction > 0 && count > 0) {
            this.lastPage();
            this.updatePageSwitcher();
            --count;
        }
        while (this.nextTabButton.enabled && direction < 0 && count > 0) {
            this.nextPage();
            this.updatePageSwitcher();
            --count;
        }
    }

    public void updateRecipesByPage(int page) {
        int startIndex = page * totalDisplaySlots;
        this.recipes = new ContainerGuidebookRecipeBase[totalDisplaySlots];
        for (int i = 0; i < totalDisplaySlots && startIndex + i < totalRecipes; ++i) {
            if (storedRecipes[startIndex + i] instanceof IRecipe) {
                this.recipes[i] = new ContainerGuidebookRecipeCrafting((IRecipe)storedRecipes[startIndex + i]);
                continue;
            }
            if (storedRecipes[startIndex + i] instanceof ItemStack[]) {
                this.recipes[i] = new ContainerGuidebookRecipeFurnace(((ItemStack[])storedRecipes[startIndex + i])[0], ((ItemStack[])storedRecipes[startIndex + i])[1], ((ItemStack[])storedRecipes[startIndex + i])[2]);
                continue;
            }
            System.out.println("Unknown guidebook recipe!");
        }
        ((ContainerWorkbenchLegacy)this.inventorySlots).setRecipes(this.mc.thePlayer, this.recipes, this.mc.statFileWriter);
    }


    public void onGuiClosed() {
        super.onGuiClosed();
        this.inventorySlots.onCraftGuiClosed(this.mc.thePlayer);
    }

    public void drawGuiContainerForegroundLayer() {
        this.fontRenderer.drawCenteredString("Inventory", 205, this.ySize - 78, 0XFFFFFF);
        this.fontRenderer.drawCenteredString("Crafting", 72, this.ySize - 78, 0XFFFFFF);
        this.drawStringNoShadow(this.fontRenderer, this.tabString, this.xSize - this.fontRenderer.getStringWidth(this.tabString) - 52, 36, 0x404040);
        this.drawStringNoShadow(this.fontRenderer, this.slotString,+ 52, 36, 0x404040);

        int i = this.mc.renderEngine.getTexture("assets/gui/legacycrafting.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(i);

        // Render Selection texture ontop of currently selected slot, does not require offset like bg layer
        this.drawTexturedModalRect(7 + 18 * currentSlot,52,35,175, 26, 24);
    }

    public void drawGuiContainerBackgroundLayer(float f) {
        this.scroll(Mouse.getDWheel()); // Scroll through tabs
        int i = this.mc.renderEngine.getTexture("assets/gui/legacycrafting.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(i);
        int j = (this.width - this.xSize) / 2;
        int k = (this.height - this.ySize) / 2;

        // Draws base gui background
        this.drawTexturedModalRect(j, k, 0, 0, 256, this.ySize); // TODO make gui texture 512x512
        this.drawTexturedModalRect(j + 256, k+this.ySize-81, 205, 175, 17, 81);
        this.drawTexturedModalRect(j + 256, k+this.ySize-81-81 , 222, 175, 17, 81);
        this.drawTexturedModalRect(j + 256, k+this.ySize-81-81-13, 239, 175, 17, 13);

        // Renders selected bookmark
        int bookMarkWidth = 34;
        this.drawTexturedModalRect(j + bookMarkWidth * tab, k - 2, 0, 175, bookMarkWidth, 30);





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
        maxTab = 7;
        currentSlot = 0;
        totalDisplaySlots = 14;
        totalRecipes = 0;
        List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
        for (int i2 = 0; i2 < recipes.size(); ++i2) {
            if (!(recipes.get(i2) instanceof RecipeShaped) && !(recipes.get(i2) instanceof RecipeShapeless)) continue;
            ++totalRecipes;
        }
        //maxPage = (totalRecipes += RecipesBlastFurnace.smelting().getSmeltingList().size()) / 6 - 1;
        int index = 0;
        storedRecipes = new Object[totalRecipes];
        for (int i3 = 0; i3 < recipes.size(); ++i3) {
            if (!(recipes.get(i3) instanceof RecipeShaped) && !(recipes.get(i3) instanceof RecipeShapeless)) continue;
            GuiLegacyCrafting.storedRecipes[index++] = recipes.get(i3);
        }
    }
}
