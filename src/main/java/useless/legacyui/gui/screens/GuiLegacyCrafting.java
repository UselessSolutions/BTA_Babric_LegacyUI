package useless.legacyui.gui.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.container.ScreenContainerAbstract;
import net.minecraft.client.input.InputType;
import net.minecraft.client.input.controller.ControllerInput;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.core.data.registry.recipe.entry.RecipeEntryCrafting;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import useless.legacyui.gui.containers.LegacyContainerCrafting;
import useless.legacyui.gui.elements.GuiButtonPrompt;
import useless.legacyui.gui.elements.GuiRegion;
import useless.legacyui.gui.IGuiController;
import useless.legacyui.helper.InventoryHelper;
import useless.legacyui.helper.RepeatInputHandler;
import useless.legacyui.LegacySoundManager;
import useless.legacyui.LegacyUI;
import useless.legacyui.sorting.LegacyCategoryManager;
import useless.legacyui.sorting.recipe.RecipeCategory;
import useless.legacyui.sorting.recipe.RecipeGroup;

import java.util.ArrayList;
import java.util.List;

import static useless.legacyui.helper.KeyboardHelper.*;


public class GuiLegacyCrafting extends ScreenContainerAbstract implements IGuiController {
    protected int craftingSize;
    private int GUIx;
    private int GUIy;
    private final Player player;
    public static int currentTab = 0;
    public static int currentScroll = 0;
    public static int currentSlot = 0;
    private static final int guiTextureWidth = 512;
    private static final int tabWidth = 35;
    public ButtonElement[] tabButtons = new ButtonElement[8];
    public ButtonElement[] slotButtons = new ButtonElement[14];
    public ButtonElement scrollUp;
    public ButtonElement scrollDown;
    public ButtonElement craftingButton;
    protected ButtonElement lastPageButton;
    protected ButtonElement nextPageButton;
    public GuiRegion inventoryRegion;
    public GuiRegion craftingRegion;
    public List<GuiButtonPrompt> prompts = new ArrayList<>();
    private static boolean showCraftDisplay = false;
    private static boolean previousShowDisplay = false;
    public GuiLegacyCrafting(final Player player, final int craftingSize){
        super((new LegacyContainerCrafting(player.inventory, craftingSize)));
        this.craftingSize = craftingSize;
        this.player = player;
        this.mc = Minecraft.getMinecraft();
        init();
    }
    public GuiLegacyCrafting(final Player player, final int x, final int y, final int z, final int craftingSize) {
        super(new LegacyContainerCrafting(player.inventory, player.world, x, y, z, craftingSize));
        this.craftingSize = craftingSize;
        this.player = player;
        this.mc = Minecraft.getMinecraft();
        init();
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
    public void selectSlot(final int value){
        if (value == currentSlot){
            craft(true); // Craft if clicking on currently selected slot
            return; // Dont reset scroll
        }
        LegacySoundManager.play.focus(true);
        currentSlot = value;
        final int groupSize = currentCategory().getRecipeGroups(isSmall()).length;
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
    public void selectScrollGroup(final int value){
        final int initialScroll = currentScroll;
        currentScroll = value;
        final int groupSize = currentCategory().getRecipeGroups(isSmall())[currentSlot].getRecipes(isSmall()).size();
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
    public void selectTab(final int value){
        if (currentTab != value){
            LegacySoundManager.play.focus(true);
        }
        currentTab = value;
        final int tabAmount = LegacyCategoryManager.getRecipeCategories().size();
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
    protected void buttonClicked(final ButtonElement guibutton) {
        super.buttonClicked(guibutton);
        for (int i = 0; i < this.tabButtons.length; i++) {
            if (this.tabButtons[i] == guibutton){
                selectTab(getPageNumber()*8+i);
            }
        }
        for (int i = 0; i < this.slotButtons.length; i++) {
            if (guibutton == this.slotButtons[i]){
                selectSlot(i);
            }
        }
        if (guibutton == this.scrollUp){
            scrollGroup(-1);
        }
        if (guibutton == this.scrollDown){
            scrollGroup(1);
        }
        if (guibutton == this.craftingButton){
            craft(showCraftDisplay);
        }
        if (guibutton == this.nextPageButton){
            selectPage(getPageNumber() + 1);
        }
        if (guibutton == this.lastPageButton){
            selectPage(getPageNumber() - 1);
        }
    }
    private static boolean shiftedPrev = false;
    public void handleInputs(){
        final boolean shifted = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
        if (repeatInput(this.mc.gameSettings.keyForward.getKeyCode(), UtilGui.verticalScrollRepeatDelay, UtilGui.verticalScrollInitialDelay) || repeatInput(this.mc.gameSettings.keyLookUp.getKeyCode(), UtilGui.verticalScrollRepeatDelay, UtilGui.verticalScrollInitialDelay)){
            scrollGroup(-1);
        }
        if (repeatInput(this.mc.gameSettings.keyBack.getKeyCode(), UtilGui.verticalScrollRepeatDelay, UtilGui.verticalScrollInitialDelay) || repeatInput(this.mc.gameSettings.keyLookDown.getKeyCode(), UtilGui.verticalScrollRepeatDelay, UtilGui.verticalScrollInitialDelay)){
            scrollGroup(1);
        }
        if (repeatInput(this.mc.gameSettings.keyRight.getKeyCode(), UtilGui.tabScrollRepeatDelay, UtilGui.tabScrollInitialDelay) || repeatInput(this.mc.gameSettings.keyLookRight.getKeyCode(), UtilGui.tabScrollRepeatDelay, UtilGui.tabScrollInitialDelay)){
            if (shifted){
                scrollTab(1);
            } else {
                scrollSlot(1);
            }
        }
        if (repeatInput(this.mc.gameSettings.keyLeft.getKeyCode(), UtilGui.tabScrollRepeatDelay, UtilGui.tabScrollInitialDelay) || repeatInput(this.mc.gameSettings.keyLookLeft.getKeyCode(), UtilGui.tabScrollRepeatDelay, UtilGui.tabScrollInitialDelay)){
            if (shifted){
                scrollTab(-1);
            } else {
                scrollSlot(-1);
            }
        }
        if (shiftedPrev != shifted){
            resetKey(this.mc.gameSettings.keyJump.getKeyCode());
        }
        if (shifted){
            if (repeatInput(this.mc.gameSettings.keyJump.getKeyCode(), (int) (UtilGui.repeatCraftDelay * 0.5f), (int) (UtilGui.initialCraftDelay * 0.5f))){
                craft(isKeyPressedThisFrame(this.mc.gameSettings.keyJump.getKeyCode()));
            }
        } else {
            if (repeatInput(this.mc.gameSettings.keyJump.getKeyCode(), UtilGui.repeatCraftDelay, UtilGui.initialCraftDelay)){
                craft(isKeyPressedThisFrame(this.mc.gameSettings.keyJump.getKeyCode()));
            }
        }
        shiftedPrev = shifted;
    }
    public RecipeCategory currentCategory(){
        return LegacyCategoryManager.getRecipeCategories().get(currentTab);
    }
    public boolean isSmall(){
        return this.craftingSize <= 4;
    }

    @Override
    public void init() {
        super.init();
        this.buttons.clear();

        // Setup size variables
        this.xSize = 273; // width of Gui window
        this.ySize = 175; // height of Gui window
        GUIx = (this.width - this.xSize) / 2;
        GUIy = (this.height - this.ySize) / 2;

        for (int i = 0; i < this.tabButtons.length; i++) {
            this.tabButtons[i] = new ButtonElement(this.buttons.size(), GUIx + (tabWidth-1)*i, GUIy, tabWidth-1, 24, "");
            this.tabButtons[i].mute();
            this.tabButtons[i].visible = false;
            this.buttons.add(this.tabButtons[i]);
        }
        final int slotWidth = 18;
        for (int i = 0; i < this.slotButtons.length; i++) {
            this.slotButtons[i] = new ButtonElement(this.buttons.size(), GUIx + 11 + (slotWidth)*i, GUIy + 55, slotWidth, slotWidth, "");
            this.slotButtons[i].mute();
            this.slotButtons[i].visible = false;
            this.buttons.add(this.slotButtons[i]);
        }
        this.scrollUp = new ButtonElement(this.buttons.size(), GUIx + 11, GUIy + 55 - 32, slotWidth, 29, "");
        this.scrollUp.visible = false;
        this.scrollUp.mute();
        this.buttons.add(this.scrollUp);

        this.scrollDown = new ButtonElement(this.buttons.size(), GUIx + 11, GUIy + 55 + 21, slotWidth, 29, "");
        this.scrollDown.visible = false;
        this.scrollDown.mute();
        this.buttons.add(this.scrollDown);

        this.craftingButton = new ButtonElement(this.buttons.size(), GUIx + 102, GUIy + 122, 26, 26, "");
        this.craftingButton.visible = false;
        this.craftingButton.mute();
        this.buttons.add(this.craftingButton);

        this.nextPageButton = new ButtonElement(this.buttons.size() + 1, GUIx + this.xSize + 2, GUIy + 4, 20, 20, ">");
        this.nextPageButton.visible = LegacyCategoryManager.getCreativeCategories().size() > 8;
        this.buttons.add(this.nextPageButton);

        this.lastPageButton = new ButtonElement(this.buttons.size() + 1, GUIx - 22, GUIy + 4, 20, 20, "<");
        this.lastPageButton.visible = LegacyCategoryManager.getCreativeCategories().size() > 8;
        this.buttons.add(this.lastPageButton);

        this.inventoryRegion = new GuiRegion(100,GUIx + 147, GUIy + 93, 116, 75);
        this.craftingRegion = new GuiRegion(100,GUIx + 16, GUIy + 105, 60, 60);

        final I18n translator = I18n.getInstance();
        this.prompts.clear();
        this.prompts.add(new GuiButtonPrompt( 101, 50, this.height-30, 3,translator.translateKey("legacyui.prompt.craft"), new int[]{0}));
        this.prompts.add(new GuiButtonPrompt( 102, this.prompts.get(0).xPosition + this.prompts.get(0).width + 3, this.height-30,  3,translator.translateKey("legacyui.prompt.back"), new int[]{1}));
        this.prompts.add(new GuiButtonPrompt( 102, this.prompts.get(1).xPosition + this.prompts.get(1).width + 3, this.height-30,  3,translator.translateKey("legacyui.prompt.tabselect"), new int[]{9,10}));

        // Static Initialization
        currentTab = 0;
        currentScroll = 0;
        currentSlot = 0;
        setContainerRecipes();
    }
    public void setContainerRecipes(){
        final RecipeGroup[] recipeGroups = currentCategory().getRecipeGroups(isSmall());
        if (recipeGroups[currentSlot].getRecipes(isSmall()).size() > 1){ // If scroll bar active
            this.scrollUp.enabled = true;
            this.scrollDown.enabled = true;
        } else {
            this.scrollUp.enabled = false;
            this.scrollDown.enabled = false;
        }
        this.scrollUp.xPosition = GUIx + 11 + 18 * currentSlot;
        this.scrollDown.xPosition = GUIx + 11 + 18 * currentSlot;

        for (int i = 0; i < this.slotButtons.length; i++) { // Only enable buttons if there is a corresponding recipe group
            this.slotButtons[i].enabled = i < recipeGroups.length;
        }
        for (int i = 0; i < this.tabButtons.length; i++) { // Only enable buttons if there is a corresponding recipe group
            this.tabButtons[i].enabled = (getPageNumber() * 8 + i) < LegacyCategoryManager.getRecipeCategories().size();
        }

        ((LegacyContainerCrafting) this.inventorySlots).setRecipes(this.player, this.mc.statsCounter, showCraftDisplay);
    }
    public void craft(final boolean isPressed){
        if(((LegacyContainerCrafting) this.inventorySlots).craft(this.mc, this.inventorySlots.containerId)){
            LegacySoundManager.play.craft(false);
        } else if (isPressed) {
            LegacySoundManager.play.craftfail(false);
        }
        setContainerRecipes();
    }
    @Override
    public void removed() {
        super.removed();
        this.inventorySlots.onCraftGuiClosed(this.mc.thePlayer);
    }

    @Override
    public void render(final int mx, final int my, final float partialTick) {
        this.craftingButton.enabled = this.mc.inputType == InputType.KEYBOARD;
        handleInputs();
        previousShowDisplay = showCraftDisplay;
        renderCraftingDisplay(mx, my);
        if (previousShowDisplay != showCraftDisplay){
            previousShowDisplay = showCraftDisplay;
            setContainerRecipes();
        }
        super.render(mx, my, partialTick);
        for (final GuiButtonPrompt prompt: this.prompts) {
            prompt.drawPrompt(this.mc, mx, my);
        }
    }
    protected void drawGuiContainerForegroundLayer(){
        GL11.glColor4f(1, 1, 1, 1);
        mc.textureManager.loadTexture("/assets/legacyui/textures/gui/legacycrafting.png").bind();
        drawSelectionCursorForeground();
    }
    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTick) {
        GL11.glColor4f(1, 1, 1, 1);
        mc.textureManager.loadTexture("/assets/legacyui/textures/gui/legacycrafting.png").bind();
        UtilGui.drawTexturedModalRect(this, GUIx, GUIy, 0,0, this.xSize, this.ySize, 1f/guiTextureWidth); // Render Background


        UtilGui.drawTexturedModalRect(this, GUIx + (tabWidth-1) * (currentTab % 8), GUIy - 2, (tabWidth) * (currentTab % 8),229, tabWidth, 30, 1f/guiTextureWidth); // Render Selected Tab


        final RecipeEntryCrafting<?, ?> currentRecipe = currentCategory().getRecipeGroups(isSmall())[currentSlot].getRecipes(isSmall()).get(currentScroll);
        if ((InventoryHelper.getRecipeInput(currentRecipe).length <= 5 && showCraftDisplay) || isSmall()){ // 2x2 Crafting overlay
            UtilGui.drawTexturedModalRect(this, GUIx + 19, GUIy + 108, 61, 175, 54, 54, 1f/guiTextureWidth);
        }

        drawStringCenteredNoShadow(font, I18n.getInstance().translateKey("legacyui.guilabel.inventory"),GUIx + 204, GUIy + 97, 0x404040);

        String craftingString; // Text above crafting table
        if (LegacyUI.modSettings.legacyui$getShowCraftingItemNamePreview().value && showCraftDisplay){ // If crafting display rendered and render item names enabled
            craftingString = ((ItemStack)currentRecipe.getOutput()).getDisplayName(); // Get Item name
            if (!LegacyContainerCrafting.isDicovered((ItemStack) currentRecipe.getOutput(), this.mc.statsCounter, this.mc.thePlayer)){ // If undiscovered obscure it
                craftingString = craftingString.replaceAll("[^ ]", "?");
            }
            if (craftingString.length() > 21){ // If too long then cap to 21 characters
                craftingString = craftingString.substring(0, 18) + "...";
            }
        } else { // Render default "Crafting" Text
            craftingString = I18n.getInstance().translateKey("legacyui.guilabel.crafting");
        }

        drawStringCenteredNoShadow(font, craftingString,GUIx + 73, GUIy + 97, 0x404040);
        drawStringCenteredNoShadow(font, LegacyCategoryManager.getRecipeCategories().get(currentTab).getTranslatedKey(),GUIx + (this.xSize /2), GUIy + 36, 0x404040);

        GL11.glColor4f(1, 1, 1, 1);
        mc.textureManager.loadTexture("/assets/legacyui/textures/gui/legacycrafting.png").bind();
        drawSelectionCursorBackground();

        final int iconAmountToDraw = Math.min(LegacyCategoryManager.getRecipeCategories().size() - (getPageNumber() * 8), 8);
        for (int i = 0; i < iconAmountToDraw; i++) {
            final boolean isSelected = (currentTab % 8) == i;
            if (isSelected){
                final double x0 = GUIx + 3 + (tabWidth - 1) * i;
                final double y0 = GUIy - 1;
                final IconCoordinate coordinate = LegacyCategoryManager.getCreativeCategories().get(getPageNumber()*8 + i).iconCoordinate;
                drawIconTextureDouble(x0, y0, x0 + 32 * 0.9f, y0 + 32 * 0.9f, 0, 0, coordinate.width, coordinate.height, coordinate);
            } else {
                final double x0 = GUIx + 5.5 + (tabWidth - 1) * i;
                final double y0 = GUIy + 2;
                final IconCoordinate coordinate = LegacyCategoryManager.getCreativeCategories().get(getPageNumber()*8 + i).iconCoordinate;
                drawIconTextureDouble(x0, y0, x0 + 32 * 0.75f, y0 + 32 * 0.75f, 0, 0, coordinate.width, coordinate.height, coordinate);
            }
        }
    }
    private void drawSelectionCursorForeground(){
        final int x = 8 + 18*currentSlot;
        final int y = 52;
        if (currentCategory().getRecipeGroups(isSmall())[currentSlot].getRecipes(isSmall()).size() > 1){
            UtilGui.drawTexturedModalRect(this, x - 1,y,35, 175, 26, 24, 1f/guiTextureWidth);
            UtilGui.drawTexturedModalRect(this, x - 1,y - 31, 115, 175, 26,31, 1f/guiTextureWidth);
            UtilGui.drawTexturedModalRect(this, x - 1,y + 24, 141, 175, 26,31, 1f/guiTextureWidth);
        } else {
            UtilGui.drawTexturedModalRect(this, x,y, 36, 175, 24, 24, 1f/guiTextureWidth);
        }
    }
    private void drawSelectionCursorBackground(){
        final int x = 12 + 18*currentSlot;
        final int y = 51;
        if (currentCategory().getRecipeGroups(isSmall())[currentSlot].getRecipes(isSmall()).size() > 1){
            UtilGui.drawTexturedModalRect(this,GUIx + x - 1,GUIy + y - 17,167, 175, 18, 18, 1f/guiTextureWidth);
            UtilGui.drawTexturedModalRect(this,GUIx + x - 1,GUIy + y + 25, 167, 175, 18,18, 1f/guiTextureWidth);
        }
    }
    public void renderCraftingDisplay(final int mouseX, final int mouseY) {
        final boolean holdingItem = this.mc.thePlayer.inventory.getHeldItemStack() != null;

        boolean isItem = false;
        for (int i = 1; i < ((this.craftingSize <=4) ? 5:10); i++) {
            isItem = isItem || (this.inventorySlots.getSlot(i) != null && this.inventorySlots.getSlot(i).getItemStack() != null);
        }
        final boolean result = (!holdingItem || this.inventoryRegion.isHovered(mouseX, mouseY)) && !isItem;

        this.craftingButton.enabled = result;

        showCraftDisplay = result;
    }

    @Override
    public void guiSpecificControllerInput(final ControllerInput controllerInput) {
        if (controllerInput.buttonRightShoulder.pressedThisFrame() || controllerInput.buttonRightShoulder.isPressed() && RepeatInputHandler.doRepeatInput(-2, UtilGui.tabScrollRepeatDelay) && controllerInput.buttonRightShoulder.getHoldTime() > 3){
            RepeatInputHandler.manualSuccess(-2);
            scrollTab(1);
        }
        if (controllerInput.buttonLeftShoulder.pressedThisFrame() || controllerInput.buttonLeftShoulder.isPressed() && RepeatInputHandler.doRepeatInput(-2, UtilGui.tabScrollRepeatDelay) && controllerInput.buttonLeftShoulder.getHoldTime() > 3){
            RepeatInputHandler.manualSuccess(-2);
            scrollTab(-1);
        }
        if (controllerInput.buttonLeftTrigger.pressedThisFrame()){
            controllerInput.snapToSlot(this, 0);
        }
        if (controllerInput.buttonRightTrigger.pressedThisFrame()){
            controllerInput.snapToSlot(this, LegacyContainerCrafting.inventorySlotsStart);
        }
        if (!this.inventoryRegion.isHovered((int) this.mc.controllerInput.cursorX, (int) this.mc.controllerInput.cursorY) && !(this.craftingRegion.isHovered((int) this.mc.controllerInput.cursorX, (int) this.mc.controllerInput.cursorY) && !showCraftDisplay)){
            if (controllerInput.digitalPad.right.pressedThisFrame() || controllerInput.digitalPad.right.isPressed() && RepeatInputHandler.doRepeatInput(-1, UtilGui.tabScrollRepeatDelay) && controllerInput.digitalPad.right.getHoldTime() > 3){
                RepeatInputHandler.manualSuccess(-1);
                scrollSlot(1);
            }
            if (controllerInput.digitalPad.left.pressedThisFrame() || controllerInput.digitalPad.left.isPressed() && RepeatInputHandler.doRepeatInput(-1, UtilGui.tabScrollRepeatDelay) && controllerInput.digitalPad.left.getHoldTime() > 3){
                RepeatInputHandler.manualSuccess(-1);
                scrollSlot(-1);
            }
            if (controllerInput.digitalPad.up.pressedThisFrame() || controllerInput.digitalPad.up.isPressed() && RepeatInputHandler.doRepeatInput(-1, UtilGui.verticalScrollRepeatDelay) && controllerInput.digitalPad.up.getHoldTime() > 3){
                RepeatInputHandler.manualSuccess(-1);
                scrollGroup(-1);
            }
            if (controllerInput.digitalPad.down.pressedThisFrame() || controllerInput.digitalPad.down.isPressed() && RepeatInputHandler.doRepeatInput(-1, UtilGui.verticalScrollRepeatDelay) && controllerInput.digitalPad.down.getHoldTime() > 3){
                RepeatInputHandler.manualSuccess(-1);
                scrollGroup(1);
            }
            if ((controllerInput.buttonA.pressedThisFrame() || (controllerInput.buttonA.getHoldTime() >= 10 && RepeatInputHandler.doRepeatInput(-10, 50)))){
                craft(controllerInput.buttonA.pressedThisFrame() && showCraftDisplay);
            }
        }
    }

    @Override
    public boolean playDefaultPressSound() {
        return false;
    }

    @Override
    public boolean enableDefaultSnapping() {
        return this.inventoryRegion.isHovered((int) this.mc.controllerInput.cursorX, (int) this.mc.controllerInput.cursorY) || (this.craftingRegion.isHovered((int) this.mc.controllerInput.cursorX, (int) this.mc.controllerInput.cursorY) && !showCraftDisplay);
    }
    public static int getPageNumber(){
        return currentTab/8;
    }
    public void selectPage(final int pageNumber){
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
