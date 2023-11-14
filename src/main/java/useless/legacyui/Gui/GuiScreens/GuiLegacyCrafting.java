package useless.legacyui.Gui.GuiScreens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiContainer;
import net.minecraft.client.input.InputType;
import net.minecraft.client.input.controller.ControllerInput;
import net.minecraft.core.crafting.recipe.IRecipe;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.lang.I18n;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import useless.legacyui.Gui.GuiElements.Buttons.GuiAuditoryButton;
import useless.legacyui.Gui.Containers.LegacyContainerCrafting;
import useless.legacyui.Gui.GuiElements.GuiButtonPrompt;
import useless.legacyui.Gui.GuiElements.GuiRegion;
import useless.legacyui.Gui.IGuiController;
import useless.legacyui.Helper.IconHelper;
import useless.legacyui.Helper.KeyboardHelper;
import useless.legacyui.Helper.RepeatInputHandler;
import useless.legacyui.LegacySoundManager;
import useless.legacyui.LegacyUI;
import useless.legacyui.Sorting.LegacyCategoryManager;
import useless.legacyui.Sorting.Recipe.RecipeCategory;
import useless.legacyui.Sorting.Recipe.RecipeGroup;

import java.util.ArrayList;
import java.util.List;

public class GuiLegacyCrafting extends GuiContainer implements IGuiController {
    protected int craftingSize;
    private static int GUIx;
    private static int GUIy;
    private EntityPlayer player;
    public static int currentTab = 0;
    public static int currentScroll = 0;
    public static int currentSlot = 0;
    private static final int guiTextureWidth = 512;
    private static final int tabWidth = 35;
    public GuiAuditoryButton[] tabButtons = new GuiAuditoryButton[8];
    public GuiAuditoryButton[] slotButtons = new GuiAuditoryButton[14];
    public GuiAuditoryButton scrollUp;
    public GuiAuditoryButton scrollDown;
    public GuiAuditoryButton craftingButton;
    protected GuiAuditoryButton lastPageButton;
    protected GuiAuditoryButton nextPageButton;
    public GuiRegion inventoryRegion;
    public GuiRegion craftingRegion;
    public List<GuiButtonPrompt> prompts = new ArrayList<>();
    private static boolean showCraftDisplay = false;
    private static boolean previousShowDisplay = false;
    public GuiLegacyCrafting(EntityPlayer player, int craftingSize){
        super((new LegacyContainerCrafting(player.inventory, craftingSize)));
        this.craftingSize = craftingSize;
        this.player = player;
        this.mc = Minecraft.getMinecraft(this);
        initGui();
    }
    public GuiLegacyCrafting(EntityPlayer player, int x, int y, int z, int craftingSize) {
        super(new LegacyContainerCrafting(player.inventory, player.world, x, y, z, craftingSize));
        this.craftingSize = craftingSize;
        this.player = player;
        this.mc = Minecraft.getMinecraft(this);
        initGui();
    }
    public void scrollSlot(int direction){
        if (direction > 0){
            while (direction > 0){
                selectSlot(currentSlot + 1);
                direction--;
            }
        } else if (direction < 0){
            while (direction < 0){
                selectSlot(currentSlot - 1);
                direction++;
            }
        }
    }
    public void selectSlot(int value){
        if (value == currentSlot){
            craft(true); // Craft if clicking on currently selected slot
            return; // Dont reset scroll
        }
        LegacySoundManager.play.focus(true);
        currentSlot = value;
        int groupSize = currentCategory().getRecipeGroups(isSmall()).length;
        if (currentSlot > groupSize-1){
            currentSlot -= groupSize;
        } else if (currentSlot < 0){
            currentSlot += groupSize;
        }
        currentScroll = 0;
        setContainerRecipes();
    }
    public void scrollGroup(int direction){
        if (direction > 0){
            while (direction > 0){
                selectScrollGroup(currentScroll + 1);
                direction--;
            }
        } else if (direction < 0){
            while (direction < 0){
                selectScrollGroup(currentScroll - 1);
                direction++;
            }
        }
    }
    public void selectScrollGroup(int value){
        int initialScroll = currentScroll;
        currentScroll = value;
        int groupSize = currentCategory().getRecipeGroups(isSmall())[currentSlot].getRecipes(isSmall()).length;
        if (currentScroll > groupSize-1){
            currentScroll -= groupSize;
        } else if (currentScroll < 0){
            currentScroll += groupSize;
        }
        if (initialScroll != currentScroll){
            LegacySoundManager.play.scroll(true);
        }
        setContainerRecipes();
    }
    public void scrollTab(int direction){
        if (direction > 0){
            while (direction > 0){
                selectTab(currentTab + 1);
                direction--;
            }
        } else if (direction < 0){
            while (direction < 0){
                selectTab(currentTab - 1);
                direction++;
            }
        }
    }
    public void selectTab(int value){
        if (currentTab != value){
            LegacySoundManager.play.focus(true);
        }
        currentTab = value;
        int tabAmount = LegacyCategoryManager.getRecipeCategories().size();
        if (currentTab > tabAmount-1){
            currentTab -= tabAmount;
        } else if (currentTab < 0){
            currentTab += tabAmount;
        }
        currentTab = Math.min(currentTab, tabAmount-1);
        currentScroll = 0;
        currentSlot = 0;

        setContainerRecipes();
    }
    protected void buttonPressed(GuiButton guibutton) {
        super.buttonPressed(guibutton);
        for (int i = 0; i < tabButtons.length; i++) {
            if (tabButtons[i] == guibutton){
                selectTab(getPageNumber()*8+i);
            }
        }
        for (int i = 0; i < slotButtons.length; i++) {
            if (guibutton == slotButtons[i]){
                selectSlot(i);
            }
        }
        if (guibutton == scrollUp){
            scrollGroup(-1);
        }
        if (guibutton == scrollDown){
            scrollGroup(1);
        }
        if (guibutton == craftingButton){
            craft(true);
        }
        if (guibutton == nextPageButton){
            selectPage(getPageNumber() + 1);
        }
        if (guibutton == lastPageButton){
            selectPage(getPageNumber() - 1);
        }
    }
    public void handleInputs(){
        boolean shifted = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
        if (KeyboardHelper.isKeyPressedThisFrame(mc.gameSettings.keyForward.keyCode()) || KeyboardHelper.isKeyPressedThisFrame(mc.gameSettings.keyLookUp.keyCode())){
            scrollGroup(-1);
        }
        if (KeyboardHelper.isKeyPressedThisFrame(mc.gameSettings.keyBack.keyCode()) || KeyboardHelper.isKeyPressedThisFrame(mc.gameSettings.keyLookDown.keyCode())){
            scrollGroup(1);
        }
        if (KeyboardHelper.isKeyPressedThisFrame(mc.gameSettings.keyRight.keyCode()) || KeyboardHelper.isKeyPressedThisFrame(mc.gameSettings.keyLookRight.keyCode())){
            if (shifted){
                scrollTab(1);
            } else {
                scrollSlot(1);
            }
        }
        if (KeyboardHelper.isKeyPressedThisFrame(mc.gameSettings.keyLeft.keyCode()) || KeyboardHelper.isKeyPressedThisFrame(mc.gameSettings.keyLookLeft.keyCode())){
            if (shifted){
                scrollTab(-1);
            } else {
                scrollSlot(-1);
            }
        }
        if (KeyboardHelper.isKeyPressedThisFrame(mc.gameSettings.keyJump.keyCode())){
            craft(true);
        }
    }
    public RecipeCategory currentCategory(){
        return LegacyCategoryManager.getRecipeCategories().get(currentTab);
    }
    public boolean isSmall(){
        return craftingSize <= 4;
    }
    public void initGui() {
        super.initGui();

        // Setup size variables
        this.xSize = 273; // width of Gui window
        this.ySize = 175; // height of Gui window
        GUIx = (this.width - this.xSize) / 2;
        GUIy = (this.height - this.ySize) / 2;

        for (int i = 0; i < tabButtons.length; i++) {
            tabButtons[i] = new GuiAuditoryButton(controlList.size(), GUIx + (tabWidth-1)*i, GUIy, tabWidth-1, 24, "");
            tabButtons[i].setMuted(true);
            tabButtons[i].visible = false;
            controlList.add(tabButtons[i]);
        }
        int slotWidth = 18;
        for (int i = 0; i < slotButtons.length; i++) {
            slotButtons[i] = new GuiAuditoryButton(controlList.size(), GUIx + 11 + (slotWidth)*i, GUIy + 55, slotWidth, slotWidth, "");
            slotButtons[i].setMuted(true);
            slotButtons[i].visible = false;
            controlList.add(slotButtons[i]);
        }
        scrollUp = new GuiAuditoryButton(controlList.size(), GUIx + 11, GUIy + 55 - 32, slotWidth, 29, "");
        scrollUp.visible = false;
        scrollUp.setMuted(true);
        controlList.add(scrollUp);

        scrollDown = new GuiAuditoryButton(controlList.size(), GUIx + 11, GUIy + 55 + 21, slotWidth, 29, "");
        scrollDown.visible = false;
        scrollDown.setMuted(true);
        controlList.add(scrollDown);

        craftingButton = new GuiAuditoryButton(controlList.size(), GUIx + 102, GUIy + 122, 26, 26, "");
        craftingButton.visible = false;
        craftingButton.setMuted(true);
        controlList.add(craftingButton);

        nextPageButton = new GuiAuditoryButton(controlList.size() + 1, GUIx + xSize + 2, GUIy + 4, 20, 20, ">");
        nextPageButton.visible = LegacyCategoryManager.getCreativeCategories().size() > 8;
        controlList.add(nextPageButton);

        lastPageButton = new GuiAuditoryButton(controlList.size() + 1, GUIx - 22, GUIy + 4, 20, 20, "<");
        lastPageButton.visible = LegacyCategoryManager.getCreativeCategories().size() > 8;
        controlList.add(lastPageButton);

        inventoryRegion = new GuiRegion(100,GUIx + 147, GUIy + 93, 116, 75);
        craftingRegion = new GuiRegion(100,GUIx + 16, GUIy + 105, 60, 60);

        I18n translator = I18n.getInstance();
        prompts.clear();
        prompts.add(new GuiButtonPrompt( 101, 50, this.height-30, 3,translator.translateKey("legacyui.prompt.craft"), new int[]{0}));
        prompts.add(new GuiButtonPrompt( 102, prompts.get(0).xPosition + prompts.get(0).width + 3, this.height-30,  3,translator.translateKey("legacyui.prompt.back"), new int[]{1}));
        prompts.add(new GuiButtonPrompt( 102, prompts.get(1).xPosition + prompts.get(1).width + 3, this.height-30,  3,translator.translateKey("legacyui.prompt.tabselect"), new int[]{9,10}));

        // Static Initialization
        currentTab = 0;
        currentScroll = 0;
        currentSlot = 0;
        setContainerRecipes();
    }
    public void setContainerRecipes(){
        RecipeGroup[] recipeGroups = currentCategory().getRecipeGroups(isSmall());
        if (recipeGroups[currentSlot].getRecipes(isSmall()).length > 1){ // If scroll bar active
            scrollUp.enabled = true;
            scrollDown.enabled = true;
        } else {
            scrollUp.enabled = false;
            scrollDown.enabled = false;
        }
        scrollUp.xPosition = GUIx + 11 + 18 * currentSlot;
        scrollDown.xPosition = GUIx + 11 + 18 * currentSlot;

        for (int i = 0; i < slotButtons.length; i++) { // Only enable buttons if there is a corresponding recipe group
            slotButtons[i].enabled = i < recipeGroups.length;
        }
        for (int i = 0; i < tabButtons.length; i++) { // Only enable buttons if there is a corresponding recipe group
            tabButtons[i].enabled = (getPageNumber() * 8 + i) < LegacyCategoryManager.getRecipeCategories().size();
        }

        ((LegacyContainerCrafting)inventorySlots).setRecipes(player, mc.statFileWriter, showCraftDisplay);
    }
    public void craft(boolean isPressed){
        if(((LegacyContainerCrafting)inventorySlots).craft(mc, inventorySlots.windowId)){
            LegacySoundManager.play.craft(false);
        } else if (isPressed) {
            LegacySoundManager.play.craftfail(false);
        }
        setContainerRecipes();
    }
    public void onGuiClosed() {
        super.onGuiClosed();
        this.inventorySlots.onCraftGuiClosed(this.mc.thePlayer);
    }
    public void drawScreen(int x, int y, float renderPartialTicks) {
        craftingButton.enabled = mc.inputType == InputType.KEYBOARD;
        handleInputs();
        previousShowDisplay = showCraftDisplay;
        renderCraftingDisplay(x, y);
        if (previousShowDisplay != showCraftDisplay){
            previousShowDisplay = showCraftDisplay;
            setContainerRecipes();
        }
        super.drawScreen(x, y, renderPartialTicks);
        for (GuiButtonPrompt prompt: prompts) {
            prompt.drawPrompt(mc, x, y);
        }
    }
    protected void drawGuiContainerForegroundLayer(){
        UtilGui.bindTexture("/assets/legacyui/gui/legacycrafting.png");
        drawSelectionCursorForeground();
    }
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick) {
        UtilGui.bindTexture("/assets/legacyui/gui/legacycrafting.png");
        UtilGui.drawTexturedModalRect(this, GUIx, GUIy, 0,0, this.xSize, this.ySize, 1f/guiTextureWidth); // Render Background


        UtilGui.drawTexturedModalRect(this, GUIx + (tabWidth - 1) * (currentTab % 8), GUIy - 2, (tabWidth-1) * (currentTab % 8),229, tabWidth, 30, 1f/guiTextureWidth); // Render Selected Tab

        IRecipe currentRecipe = (IRecipe) currentCategory().getRecipeGroups(isSmall())[currentSlot].getRecipes(isSmall())[currentScroll];
        if ((currentCategory().getRecipeGroups(isSmall())[currentSlot].getContainer(currentScroll, isSmall()).inventorySlots.size() <= 5 && showCraftDisplay) || isSmall()){ // 2x2 Crafting overlay
            UtilGui.drawTexturedModalRect(this, GUIx + 19, GUIy + 108, 61, 175, 54, 54, 1f/guiTextureWidth);
        }

        drawStringCenteredNoShadow(fontRenderer, I18n.getInstance().translateKey("legacyui.guilabel.inventory"),GUIx + 204, GUIy + 97, LegacyUI.modSettings.getGuiLabelColor().value.value);

        String craftingString; // Text above crafting table
        if (LegacyUI.modSettings.getShowCraftingItemNamePreview().value && showCraftDisplay){ // If crafting display rendered and render item names enabled
            craftingString = currentRecipe.getRecipeOutput().getDisplayName(); // Get Item name
            if (!LegacyContainerCrafting.isDicovered(currentRecipe.getRecipeOutput(), mc.statFileWriter, mc.thePlayer)){ // If undiscovered obscure it
                craftingString = craftingString.replaceAll("[^ ]", "?");
            }
            if (craftingString.length() > 21){ // If too long then cap to 21 characters
                craftingString = craftingString.substring(0, 18) + "...";
            }
        } else { // Render default "Crafting" Text
            craftingString = I18n.getInstance().translateKey("legacyui.guilabel.crafting");
        }

        drawStringCenteredNoShadow(fontRenderer, craftingString,GUIx + 73, GUIy + 97, LegacyUI.modSettings.getGuiLabelColor().value.value);
        drawStringCenteredNoShadow(fontRenderer, LegacyCategoryManager.getRecipeCategories().get(currentTab).getTranslatedKey(),GUIx + (xSize/2), GUIy + 36, LegacyUI.modSettings.getGuiLabelColor().value.value);

        UtilGui.bindTexture("/assets/legacyui/gui/legacycrafting.png");
        drawSelectionCursorBackground();

        UtilGui.bindTexture(IconHelper.ICON_TEXTURE);
        int iconAmountToDraw = Math.min(LegacyCategoryManager.getRecipeCategories().size() - (getPageNumber() * 8), 8);
        for (int i = 0; i < iconAmountToDraw; i++) {
            int vertoffset = (currentTab % 8) == i? -2 : 0;
            UtilGui.drawIconTexture(this, GUIx + 5 + (tabWidth - 1) * i, GUIy + 2 + vertoffset, LegacyCategoryManager.getRecipeCategories().get(getPageNumber()*8 + i).iconCoordinate, 0.75f); // Render Icon
        }
    }
    private void drawSelectionCursorForeground(){
        int x = 8 + 18*currentSlot;
        int y = 52;
        if (currentCategory().getRecipeGroups(isSmall())[currentSlot].getRecipes(isSmall()).length > 1){
            UtilGui.drawTexturedModalRect(this, x - 1,y,35, 175, 26, 24, 1f/guiTextureWidth);
            UtilGui.drawTexturedModalRect(this, x - 1,y - 31, 115, 175, 26,31, 1f/guiTextureWidth);
            UtilGui.drawTexturedModalRect(this, x - 1,y + 24, 141, 175, 26,31, 1f/guiTextureWidth);
        } else {
            UtilGui.drawTexturedModalRect(this, x,y, 36, 175, 24, 24, 1f/guiTextureWidth);
        }
    }
    private void drawSelectionCursorBackground(){
        int x = 12 + 18*currentSlot;
        int y = 51;
        if (currentCategory().getRecipeGroups(isSmall())[currentSlot].getRecipes(isSmall()).length > 1){
            UtilGui.drawTexturedModalRect(this,GUIx + x - 1,GUIy + y - 17,167, 175, 18, 18, 1f/guiTextureWidth);
            UtilGui.drawTexturedModalRect(this,GUIx + x - 1,GUIy + y + 25, 167, 175, 18,18, 1f/guiTextureWidth);
        }
    }
    public void renderCraftingDisplay(int mouseX, int mouseY) {
        boolean holdingItem = mc.thePlayer.inventory.getHeldItemStack() != null;

        boolean isItem = false;
        for (int i = 1; i < ((craftingSize<=4) ? 5:10); i++) {
            isItem = isItem || (inventorySlots.getSlot(i) != null && inventorySlots.getSlot(i).getStack() != null);
        }
        boolean result = (!holdingItem || inventoryRegion.isHovered(mouseX, mouseY)) && !isItem;

        if (result){
            craftingButton.enabled = true;
        } else {
            craftingButton.enabled = false;
        }

        showCraftDisplay = result;
    }

    @Override
    public void GuiControls(ControllerInput controllerInput) {
        if (controllerInput.buttonR.pressedThisFrame()){
            scrollTab(1);
        }
        if (controllerInput.buttonL.pressedThisFrame()){
            scrollTab(-1);
        }
        if (controllerInput.buttonZL.pressedThisFrame()){
            controllerInput.snapToSlot(this, 0);
        }
        if (controllerInput.buttonZR.pressedThisFrame()){
            controllerInput.snapToSlot(this, LegacyContainerCrafting.inventorySlotsStart);
        }
        if (!inventoryRegion.isHovered((int)mc.controllerInput.cursorX, (int) mc.controllerInput.cursorY)){
            if (controllerInput.digitalPad.right.pressedThisFrame()){
                scrollSlot(1);
            }
            if (controllerInput.digitalPad.left.pressedThisFrame()){
                scrollSlot(-1);
            }
            if (controllerInput.digitalPad.up.pressedThisFrame()){
                scrollGroup(-1);
            }
            if (controllerInput.digitalPad.down.pressedThisFrame()){
                scrollGroup(1);
            }
            if (!craftingRegion.isHovered((int)mc.controllerInput.cursorX, (int) mc.controllerInput.cursorY) && (controllerInput.buttonA.pressedThisFrame() || (controllerInput.buttonA.getHoldTime() >= 10 && RepeatInputHandler.doRepeatInput(-10, 50)))){
                craft(controllerInput.buttonA.pressedThisFrame());
            }
        }
    }

    @Override
    public boolean playDefaultPressSound() {
        return false;
    }

    @Override
    public boolean enableDefaultSnapping() {
        if (inventoryRegion.isHovered((int)mc.controllerInput.cursorX, (int) mc.controllerInput.cursorY)){
            return true;
        }
        return false;
    }
    public static int getPageNumber(){
        return currentTab/8;
    }
    public void selectPage(int pageNumber){
        int desiredPage = pageNumber;
        if (desiredPage < 0){
            desiredPage = LegacyCategoryManager.getRecipeCategories().size()/8;
        }
        if (desiredPage > LegacyCategoryManager.getRecipeCategories().size()/8){
            desiredPage = 0;
        }
        selectTab(desiredPage * 8);
    }
}
