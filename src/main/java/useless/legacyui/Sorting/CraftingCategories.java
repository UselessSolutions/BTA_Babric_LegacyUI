package useless.legacyui.Sorting;

import net.minecraft.core.block.Block;
import net.minecraft.core.crafting.CraftingManager;
import net.minecraft.core.crafting.recipe.IRecipe;
import net.minecraft.core.crafting.recipe.RecipeShaped;
import net.minecraft.core.crafting.recipe.RecipeShapeless;
import net.minecraft.core.item.*;
import net.minecraft.core.item.tool.*;
import useless.legacyui.LegacyUI;

import java.util.ArrayList;
import java.util.List;

public class CraftingCategories {
    private static final CraftingCategories instance = new CraftingCategories();

    public static CraftingCategories getInstance() {
        return instance;
    }

    private List<SortingCategory> categories = new ArrayList<SortingCategory>();

    private CraftingCategories() {
        CraftingManager manager = CraftingManager.getInstance();

        int j = 0;

        // Blocks
        List<Object> _resourceBlocks = new ArrayList<Object>();
        List<Object> _fencesBlocks = new ArrayList<Object>();
        List<Object> _gatesBlocks = new ArrayList<Object>();
        List<Object> _doors = new ArrayList<Object>();
        List<Object> _woodenStairsBlocks = new ArrayList<Object>();
        List<Object> _woodenSlabsBlocks = new ArrayList<Object>();
        List<Object> _stairsBlocks = new ArrayList<Object>();
        List<Object> _slabsBlocks = new ArrayList<Object>();
        List<Object> _woodenBlocks = new ArrayList<Object>();
        List<Object> _chest = new ArrayList<Object>();
        List<Object> _guiBlocks = new ArrayList<Object>();
        List<Object> _woolBlocks = new ArrayList<Object>();
        List<Object> _brickBlocks = new ArrayList<Object>();
        List<Object> _naturalBlocks = new ArrayList<Object>();
        List<Object> _blocks = new ArrayList<Object>();
        List<Object> _stickTorches = new ArrayList<Object>();
        List<Object> _bed = new ArrayList<Object>();
        List<Object> _ladders = new ArrayList<Object>();
        List<Object> _polishedStones = new ArrayList<Object>();
        List<Object> _layers = new ArrayList<Object>();
        List<Object> _books = new ArrayList<Object>();
        List<Object> _decoBlocks = new ArrayList<Object>();

        // Tools
        List<Object> _picks = new ArrayList<Object>();
        List<Object> _shovels = new ArrayList<Object>();
        List<Object> _axes = new ArrayList<Object>();
        List<Object> _hoes = new ArrayList<Object>();
        List<Object> _shears = new ArrayList<Object>();
        List<Object> _information = new ArrayList<Object>();
        List<Object> _fishing = new ArrayList<Object>();
        List<Object> _bucket = new ArrayList<Object>();
        List<Object> _fireStarts = new ArrayList<Object>();
        List<Object> _label = new ArrayList<Object>();

        // Combat
        List<Object> _swords = new ArrayList<Object>();
        List<Object> _ranged = new ArrayList<Object>();
        List<Object> _ammo = new ArrayList<Object>();
        List<Object> _helmets = new ArrayList<Object>();
        List<Object> _chestPlates = new ArrayList<Object>();
        List<Object> _leggings = new ArrayList<Object>();
        List<Object> _boots = new ArrayList<Object>();

        // Food
        List<Object> _bread = new ArrayList<Object>();
        List<Object> _stew = new ArrayList<Object>();
        List<Object> _cake = new ArrayList<Object>();
        List<Object> _cookies = new ArrayList<Object>();
        List<Object> _icecream = new ArrayList<Object>();
        List<Object> _gapple = new ArrayList<Object>();
        List<Object> _sugar = new ArrayList<Object>();
        List<Object> _food = new ArrayList<Object>();

        // Redstone
        List<Object> _pistonBlocks = new ArrayList<Object>();
        List<Object> _dispenser = new ArrayList<Object>();
        List<Object> _pressurePlates = new ArrayList<Object>();
        List<Object> _buttonLevers = new ArrayList<Object>();
        List<Object> _baseRedstone = new ArrayList<Object>();
        List<Object> _lampBlocks = new ArrayList<Object>();
        List<Object> _redstoneBlock = new ArrayList<Object>();

        // Transit
        List<Object> _rails = new ArrayList<Object>();
        List<Object> _boat = new ArrayList<Object>();
        List<Object> _carts = new ArrayList<Object>();

        // Misc
        List<Object> _dyes = new ArrayList<Object>();
        List<Object> _decompacts = new ArrayList<Object>();
        List<Object> _others = new ArrayList<Object>();


        for (IRecipe recipe : manager.getRecipeList()) {
            if (recipe instanceof RecipeShaped || recipe instanceof RecipeShapeless) {
                LegacyUI.LOGGER.debug("" + j + " | " + recipe.getRecipeOutput().getItemName());
                Item recipeItem = recipe.getRecipeOutput().getItem();
                if (recipeItem instanceof ItemDye) {
                    if (recipe.getRecipeOutput().getMetadata() == 4){ // Lapis
                        _decompacts.add(recipe);
                    }
                    _dyes.add(recipe);
                } else if (recipeItem instanceof ItemToolPickaxe) {
                    _picks.add(recipe);
                } else if (recipeItem instanceof ItemToolAxe) {
                    _axes.add(recipe);
                } else if (recipeItem instanceof ItemToolHoe) {
                    _hoes.add(recipe);
                } else if (recipeItem instanceof ItemToolShovel) {
                    _shovels.add(recipe);
                } else if (recipeItem instanceof ItemToolSword) {
                    _swords.add(recipe);
                } else if (recipeItem instanceof ItemArmor) {
                    int armorPiece = ((ItemArmor) recipeItem).armorPiece;
                    switch (armorPiece) {
                        case 0:
                            _helmets.add(recipe);
                            break;
                        case 1:
                            _chestPlates.add(recipe);
                            break;
                        case 2:
                            _leggings.add(recipe);
                            break;
                        case 3:
                            _boots.add(recipe);
                            break;
                    }
                } else if (recipeItem instanceof ItemQuiver) {
                    _chestPlates.add(recipe);
                } else if (recipeItem instanceof ItemFood || recipeItem instanceof ItemFoodStackable) {
                    if (recipeItem == Item.foodBread) {
                        _bread.add(recipe);
                    } else if (recipeItem == Item.foodStewMushroom) {
                        _stew.add(recipe);
                    } else if (recipeItem == Item.foodCookie) {
                        _cookies.add(recipe);
                    } else if (recipeItem == Item.foodAppleGold) {
                        _gapple.add(recipe);
                    } else {
                        //_food.add(recipe);
                    }
                } else if (recipeItem instanceof ItemPlaceable) {
                    if (recipeItem == Item.foodCake) {
                        _cake.add(recipe);
                    }
                    else if (recipeItem == Item.dustRedstone) {
                        _decompacts.add(recipe);
                        _baseRedstone.add(recipe);
                    }
                    else if (recipeItem == Item.repeater) {
                        _baseRedstone.add(recipe);
                    }

                } else if (recipeItem instanceof ItemDoor) {
                    _doors.add(recipe);

                }else if (recipeItem instanceof ItemBucketIceCream) {
                    _icecream.add(recipe);

                } else if (recipeItem == Item.toolCompass || recipeItem == Item.toolClock || recipeItem == Item.toolCalendar || recipeItem == Item.map) {
                    _information.add(recipe);
                } else if (recipeItem instanceof ItemBucketEmpty) {
                    _bucket.add(recipe);
                } else if (recipeItem instanceof ItemFishingRod) {
                    _fishing.add(recipe);
                } else if (recipeItem instanceof ItemFirestriker) {
                    _fireStarts.add(recipe);
                } else if (recipeItem instanceof ItemToolShears) {
                    _shears.add(recipe);
                } else if (recipeItem instanceof ItemBow || recipeItem instanceof ItemHandCannonUnloaded || recipeItem instanceof ItemHandCannonLoaded) {
                    _ranged.add(recipe);
                } else if (recipeItem == Item.ammoArrow || recipeItem == Item.ammoSnowball || recipeItem == Item.ammoArrowGold || recipeItem == Item.ammoArrowPurple || recipeItem == Item.ammoChargeExplosive || recipeItem == Item.ammoPebble || recipeItem == Item.ammoFireball) {
                    _ammo.add(recipe);
                } else if (recipeItem == Item.dustSugar) {
                    _sugar.add(recipe);
                } else if (recipeItem == Item.bowl) {
                    _stew.add(recipe);
                } else if (recipeItem instanceof ItemBoat) {
                    _boat.add(recipe);
                } else if (recipeItem instanceof ItemMinecart) {
                    _carts.add(recipe);
                } else if (recipeItem == Item.stick) {
                    _stickTorches.add(recipe);
                } else if (recipeItem instanceof  ItemBed) {
                    _bed.add(recipe);
                } else if (recipeItem == Item.diamond || recipeItem == Item.ingotIron || recipeItem == Item.ingotGold || recipeItem == Item.ingotSteel || recipeItem == Item.ingotSteelCrude || recipeItem == Item.quartz || recipeItem == Item.olivine || recipeItem == Item.coal || recipeItem == Item.nethercoal) {
                    _decompacts.add(recipe);
                } else if (recipeItem instanceof  ItemLabel) {
                    _label.add(recipe);
                } else if (recipeItem == Item.paper || recipeItem == Item.book || recipeItem == Block.bookshelfPlanksOak.asItem()) {
                    _books.add(recipe);
                } else if (recipeItem == Item.sign || recipeItem == Item.painting || recipeItem == Item.flag) {
                    _decoBlocks.add(recipe);
                }

                // Blocks
                else if (recipeItem.id < Block.blocksList.length) {
                    if (recipeItem.id == Block.blockCharcoal.id || recipeItem.id == Block.blockCoal.id || recipeItem.id == Block.blockGold.id || recipeItem.id == Block.blockDiamond.id || recipeItem.id == Block.blockIron.id || recipeItem.id == Block.blockLapis.id || recipeItem.id == Block.blockNetherCoal.id || recipeItem.id == Block.blockOlivine.id || recipeItem.id == Block.blockQuartz.id || recipeItem.id == Block.blockRedstone.id || recipeItem.id == Block.blockSteel.id) {
                        if (recipeItem.id == Block.blockRedstone.id) {_redstoneBlock.add(recipe);}
                        _resourceBlocks.add(recipe);
                    } else if (recipeItem.getKey().toLowerCase().contains("ladder") || recipeItem.getKey().toLowerCase().contains("chain")){
                        _ladders.add(recipe);
                    } else if (recipeItem.getKey().toLowerCase().contains("gate")){
                        _gatesBlocks.add(recipe);
                    } else if (recipeItem.getKey().toLowerCase().contains("fence")){
                        _fencesBlocks.add(recipe);
                    } else if (recipeItem.getKey().toLowerCase().contains("door")){
                        _doors.add(recipe);
                    } else if (recipeItem.getKey().toLowerCase().contains("stairs")){
                        if (recipeItem.getKey().toLowerCase().contains("oak")) {
                            _woodenStairsBlocks.add(recipe);
                        }
                        else {
                            _stairsBlocks.add(recipe);
                        }
                    } else if (recipeItem.getKey().toLowerCase().contains("slab")){
                        if (recipeItem.getKey().toLowerCase().contains("oak")) {
                            _woodenSlabsBlocks.add(recipe);
                        }
                        else {
                            _slabsBlocks.add(recipe);
                        }
                    } else if (recipeItem.getKey().toLowerCase().contains("chest")){
                        _chest.add(recipe);
                    }  else if (recipeItem.getKey().toLowerCase().contains("lamp")){
                        _lampBlocks.add(recipe);
                    } else if (recipeItem.getKey().toLowerCase().contains("piston")){
                        _pistonBlocks.add(recipe);
                    } else if (recipeItem.id == Block.dispenserCobbleStone.id || recipeItem.id == Block.noteblock.id || recipeItem.id == Block.tnt.id || recipeItem.id == Block.spikes.id || recipeItem.id == Block.mesh.id){
                        _dispenser.add(recipe);
                    } else if (recipeItem.getKey().toLowerCase().contains("pressureplate")){
                        _pressurePlates.add(recipe);
                    } else if (recipeItem.getKey().toLowerCase().contains("button") || recipeItem.getKey().toLowerCase().contains("lever")){
                        _buttonLevers.add(recipe);
                    }  else if (recipeItem.id == Block.torchRedstoneActive.id){
                        _baseRedstone.add(recipe);
                    } else if (recipeItem.id == Block.furnaceBlastIdle.id || recipeItem.id == Block.furnaceStoneIdle.id || recipeItem.id == Block.workbench.id || recipeItem.id == Block.trommelIdle.id || recipeItem.id == Block.jukebox.id){
                        _guiBlocks.add(recipe);
                    } else if (recipeItem.getKey().toLowerCase().contains("wool")){
                        _woolBlocks.add(recipe);
                    } else if (recipeItem.getKey().toLowerCase().contains("rail")){
                        _rails.add(recipe);
                    } else if (recipeItem.getKey().toLowerCase().contains("brick")) {
                        _brickBlocks.add(recipe);
                    } else if (recipeItem.getKey().toLowerCase().contains("cobble") || recipeItem.getKey().toLowerCase().contains("sand") || recipeItem.getKey().toLowerCase().contains("sponge") || recipeItem.getKey().toLowerCase().contains("pumpkin") || recipeItem.getKey().toLowerCase().contains("clay") || recipeItem.getKey().toLowerCase().contains("ice") || recipeItem.id == Block.blockSnow.id || recipeItem.getKey().toLowerCase().contains("gravel")){
                        _naturalBlocks.add(recipe);
                    } else if (recipeItem.getKey().toLowerCase().contains("torch")){
                        _stickTorches.add(recipe);
                    } else if (recipeItem.getKey().toLowerCase().contains("polished") || recipeItem.getKey().toLowerCase().contains("pillar")){
                        _polishedStones.add(recipe);
                    } else if (recipeItem.getKey().toLowerCase().contains("layer")){
                        _layers.add(recipe);
                    } else if (recipeItem.getKey().toLowerCase().contains("oak") && !recipeItem.getKey().toLowerCase().contains("shelf")){
                        _woodenBlocks.add(recipe);
                    } else {
                        _blocks.add(recipe);
                    }

                } else {
                    _others.add(recipe);
                }
                j++;
            }
        }


        // Blocks
        RecipeGroup blocksGroup = new RecipeGroup(_blocks.toArray());

        RecipeGroup woodenBlocksGroup = new RecipeGroup(_woodenBlocks.toArray());
        RecipeGroup fencesGroup = new RecipeGroup(_fencesBlocks.toArray());
        RecipeGroup gatesGroup = new RecipeGroup(_gatesBlocks.toArray());
        RecipeGroup doorsGroup = new RecipeGroup(_doors.toArray());
        RecipeGroup chestsGroup = new RecipeGroup(_chest.toArray());
        RecipeGroup guiGroup = new RecipeGroup(_guiBlocks.toArray());
        RecipeGroup woodenSlabsGroup = new RecipeGroup(_woodenSlabsBlocks.toArray());
        RecipeGroup woodenStairsGroup = new RecipeGroup(_woodenStairsBlocks.toArray());
        RecipeGroup stickTorchGroup = new RecipeGroup(_stickTorches.toArray());
        RecipeGroup bedGroup = new RecipeGroup(_bed.toArray());
        RecipeGroup ladderGroup = new RecipeGroup(_ladders.toArray());
        RecipeGroup bookGroup = new RecipeGroup(_books.toArray());
        RecipeGroup decoBlockGroup = new RecipeGroup(_decoBlocks.toArray());
        RecipeGroup foodGroup = new RecipeGroup(_food.toArray());

        // Blocks 2
        RecipeGroup slabsGroup = new RecipeGroup(_slabsBlocks.toArray());
        RecipeGroup stairsGroup = new RecipeGroup(_stairsBlocks.toArray());
        RecipeGroup brickGroup = new RecipeGroup(_brickBlocks.toArray());
        RecipeGroup polishedGroup = new RecipeGroup(_polishedStones.toArray());
        RecipeGroup woolGroup = new RecipeGroup(_woolBlocks.toArray());
        RecipeGroup naturalGroup = new RecipeGroup(_naturalBlocks.toArray());
        RecipeGroup layerGroup = new RecipeGroup(_layers.toArray());

        // Equipment
        List<Object> _miscTools = new ArrayList<Object>();
        _miscTools.addAll(_bucket);
        _miscTools.addAll(_shears);
        _miscTools.addAll(_fireStarts);
        _miscTools.addAll(_label);

        RecipeGroup pickaxeGroup = new RecipeGroup(_picks.toArray());
        RecipeGroup shovelGroup = new RecipeGroup(_shovels.toArray());
        RecipeGroup hoeGroup = new RecipeGroup(_hoes.toArray());
        RecipeGroup axeGroup = new RecipeGroup(_axes.toArray());
        /*RecipeGroup shearsGroup = new RecipeGroup(_shears.toArray());
        RecipeGroup bucketGroup = new RecipeGroup(_bucket.toArray());
        RecipeGroup fireStarterGroup = new RecipeGroup(_fireStarts.toArray());*/
        RecipeGroup fishingGroup = new RecipeGroup(_fishing.toArray());
        RecipeGroup informationGroup = new RecipeGroup(_information.toArray());
        RecipeGroup helmetsGroup = new RecipeGroup(_helmets.toArray());
        RecipeGroup chestPlatesGroup = new RecipeGroup(_chestPlates.toArray());
        RecipeGroup leggingsGroup = new RecipeGroup(_leggings.toArray());
        RecipeGroup bootsGroup = new RecipeGroup(_boots.toArray());
        RecipeGroup miscToolsGroup = new RecipeGroup(_miscTools.toArray());
        RecipeGroup swordGroup = new RecipeGroup(_swords.toArray());
        RecipeGroup rangedGroup = new RecipeGroup(_ranged.toArray());
        RecipeGroup ammoGroup = new RecipeGroup(_ammo.toArray());

        // Food
        RecipeGroup breadGroup = new RecipeGroup(_bread.toArray());
        RecipeGroup stewGroup = new RecipeGroup(_stew.toArray());
        RecipeGroup cakeGroup = new RecipeGroup(_cake.toArray());
        RecipeGroup cookieGroup = new RecipeGroup(_cookies.toArray());
        RecipeGroup icecreamGroup = new RecipeGroup(_icecream.toArray());
        RecipeGroup goldAppleGroup = new RecipeGroup(_gapple.toArray());
        RecipeGroup sugarGroup = new RecipeGroup(_sugar.toArray());

        // Redstone
        RecipeGroup miscRedstoneGroup = new RecipeGroup(_dispenser.toArray());
        RecipeGroup lampsGroup = new RecipeGroup(_lampBlocks.toArray());
        RecipeGroup baseRedstoneGroup = new RecipeGroup(_baseRedstone.toArray());
        RecipeGroup pistonGroup = new RecipeGroup(_pistonBlocks.toArray());
        RecipeGroup pressurePlateGroup = new RecipeGroup(_pressurePlates.toArray());
        RecipeGroup buttonLeversGroup = new RecipeGroup(_buttonLevers.toArray());
        RecipeGroup redstoneBlockGroup = new RecipeGroup(_redstoneBlock.toArray());

        // Transit
        RecipeGroup railGroup = new RecipeGroup(_rails.toArray());
        RecipeGroup cartGroup = new RecipeGroup(_carts.toArray());
        RecipeGroup boatGroup = new RecipeGroup(_boat.toArray());


        // Misc
        RecipeGroup dyesGroup = new RecipeGroup(_dyes.toArray());
        RecipeGroup resourceBlocksGroup = new RecipeGroup(_resourceBlocks.toArray());
        RecipeGroup unresourceItemsGroup = new RecipeGroup(_decompacts.toArray());
        RecipeGroup othersGroup = new RecipeGroup(_others.toArray());


        SortingCategory blocks = new SortingCategory(new RecipeGroup[]{ woodenBlocksGroup, stickTorchGroup, guiGroup, chestsGroup, bedGroup, fencesGroup, gatesGroup, ladderGroup, doorsGroup, woodenStairsGroup, woodenSlabsGroup, bookGroup, decoBlockGroup});
        addCategory(blocks);

        SortingCategory blocks2 = new SortingCategory(new RecipeGroup[]{brickGroup, polishedGroup, stairsGroup, slabsGroup, woolGroup, naturalGroup, layerGroup, resourceBlocksGroup, unresourceItemsGroup});
        addCategory(blocks2);

        SortingCategory tools = new SortingCategory(new RecipeGroup[]{pickaxeGroup, shovelGroup, axeGroup, hoeGroup, swordGroup, fishingGroup, rangedGroup, ammoGroup, miscToolsGroup, informationGroup, helmetsGroup, chestPlatesGroup, leggingsGroup, bootsGroup});
        addCategory(tools);

        SortingCategory food = new SortingCategory(new RecipeGroup[]{breadGroup, stewGroup, cakeGroup, cookieGroup, icecreamGroup, goldAppleGroup, sugarGroup, foodGroup});
        addCategory(food);

        SortingCategory redstone = new SortingCategory(new RecipeGroup[]{baseRedstoneGroup, redstoneBlockGroup, buttonLeversGroup, pressurePlateGroup, pistonGroup, lampsGroup, miscRedstoneGroup});
        addCategory(redstone);

        SortingCategory transit = new SortingCategory(new RecipeGroup[]{railGroup, cartGroup, boatGroup});
        addCategory(transit);

        SortingCategory misc = new SortingCategory(new RecipeGroup[]{dyesGroup, blocksGroup, othersGroup});
        addCategory(misc);

    }

    public int addCategory(SortingCategory category) {
        int index = categories.size();
        categories.add(category);
        return index;
    }


    public List<SortingCategory> getCategories() {
        return categories;
    }
}
