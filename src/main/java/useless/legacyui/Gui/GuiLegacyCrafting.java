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
    protected final int maxTab = 8; // Total amount of tab pages, zero index
    protected static int currentSlot;
    protected final int totalDisplaySlots = 14;
    protected String slotString = "1/1";
    protected String tabString = "1/1"; // Indicator of what tab page you are on
    protected ContainerGuidebookRecipeBase[] recipes;
    private static Object[] storedRecipes;
    private static int totalRecipes;

    // Button Hell
    protected GuiButtonTransparent[] slotButtons = new GuiButtonTransparent[totalDisplaySlots];
    protected GuiButtonTransparent[] tabButtons = new GuiButtonTransparent[maxTab];



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
            tabButtons[i] = new GuiButtonTransparent(i + 4, (this.width - this.xSize)/2 + 34 * i, (this.height - this.ySize)/2, 34,24, "");
            this.controlList.add(tabButtons[i]);
        }

        this.updatePages();
        this.selectDisplaySlot(currentSlot);
    }
    protected void buttonPressed(GuiButton guibutton) {
        if (!guibutton.enabled) {
            return;
        }
        int i = 0;
        for (GuiButtonTransparent button : slotButtons){
            if (guibutton == button){
                selectDisplaySlot(i);
            }
            i++;
        }

        i = 0;
        for (GuiButtonTransparent button : tabButtons){
            if (guibutton == button){
                selectTab(i);
            }
            i++;
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
        updatePages();
    }
    public void selectTab(int tabIndex){
        if (tabIndex < 0){
            tabIndex = 0;
        } else if (tabIndex > maxTab-1) {
            tabIndex = maxTab-1;
        }
        tab = tabIndex;
        tabString = "" + (tab+1) + "/" + (maxTab);
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
        this.updateRecipesByPage(tab);
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
        ((ContainerWorkbenchLegacy)this.inventorySlots).setRecipes(this.mc.thePlayer, this.recipes, this.mc.statFileWriter, currentSlot);
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

        // Render Selection texture ontop of currently selected slot, does not require offset like bg layer
        this.drawTexturedModalRect(7 + 18 * currentSlot,52,35,175, 26, 24);
    }

    public void drawGuiContainerBackgroundLayer(float f) {
        //this.scroll(Mouse.getDWheel()); // Scroll through tabs
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
        currentSlot = 0;
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
