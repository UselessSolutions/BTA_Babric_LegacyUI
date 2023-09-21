package useless.legacyui.Sorting;

import net.minecraft.core.block.*;
import net.minecraft.core.item.*;
import net.minecraft.core.item.tool.*;
import useless.legacyui.Helper.IconHelper;
import useless.legacyui.LegacyUI;
import useless.legacyui.Sorting.Item.ItemCategory;
import useless.legacyui.Sorting.Item.ItemCategoryBuilder;
import useless.legacyui.Sorting.Recipe.RecipeCategory;
import useless.legacyui.Sorting.Recipe.RecipeCategoryBuilder;
import useless.legacyui.Sorting.Recipe.RecipeGroup;
import useless.legacyui.Sorting.Recipe.RecipeGroupBuilder;

import java.util.ArrayList;
import java.util.List;

public class LegacyCategoryManager {
    private static List<RecipeCategory> recipeCategories = new ArrayList<>();
    private static List<ItemCategory> creativeCategories = new ArrayList<>();
    public static List<ItemCategoryBuilder> creativeCategoriesBuilders = new ArrayList<>();
    public static List<RecipeCategoryBuilder> recipeCategoryBuilders = new ArrayList<>();
    public static String MOD_ID = LegacyUI.MOD_ID;
    public static void register(){
        recipeCategoryBuilders.add(recipeBasics.category);
        recipeCategoryBuilders.add(recipeBricks.category);
        recipeCategoryBuilders.add(recipeTools.category);
        recipeCategoryBuilders.add(recipeFood.category);
        recipeCategoryBuilders.add(recipeRedstone.category);
        recipeCategoryBuilders.add(recipeTransit.category);
        recipeCategoryBuilders.add(recipeMisc.category);
        //recipeCategoryBuilders.add(new RecipeCategory(MOD_ID, "modded", IconHelper.getOrCreateIconTexture(MOD_ID, "modded.png"), recipeMisc.category.getRecipeGroups(false)));

        creativeCategoriesBuilders.add(creative.natural);
        creativeCategoriesBuilders.add(creative.otherBlocks);
        creativeCategoriesBuilders.add(creative.equipment);
        creativeCategoriesBuilders.add(creative.food);
        creativeCategoriesBuilders.add(creative.redstoneTransit);
        creativeCategoriesBuilders.add(creative.misc);
        creativeCategoriesBuilders.add(creative.modded);
    }
    public static List<RecipeCategory> getRecipeCategories(){
        return recipeCategories;
    }
    public static List<ItemCategory> getCreativeCategories(){
        return creativeCategories;
    }
    public static void build(){
        for (ItemCategoryBuilder builder: creativeCategoriesBuilders) {
            creativeCategories.add(builder.build());
        }
        for (RecipeCategoryBuilder builder: recipeCategoryBuilders) {
            recipeCategories.add(builder.build());
        }
    }
    public static class creative{
        public static ItemCategoryBuilder equipment = new ItemCategoryBuilder(MOD_ID)
                .addClass(ItemTool.class)
                .addClass(ItemToolSword.class)
                .addClass(ItemFishingRod.class)
                .addClass(ItemBow.class)
                .addItem(Item.ammoArrow)
                .addItem(Item.ammoArrowGold)
                .addItem(Item.ammoArrowPurple)
                .addClass(ItemHandCannonLoaded.class)
                .addClass(ItemHandCannonUnloaded.class)
                .addItem(Item.ammoChargeExplosive)
                .addClass(ItemFirestriker.class)
                .addClass(ItemBucket.class)
                .addClass(ItemBucketEmpty.class)
                .excludeItem(Item.bucketMilk)
                .addClass(ItemLabel.class)
                .addClass(ItemToolShears.class)
                .addItem(Item.toolCompass)
                .addItem(Item.toolClock)
                .addItem(Item.toolCalendar)
                .addItem(Item.map)
                .addClass(ItemArmor.class)
                .addClass(ItemQuiver.class)
                .addClass(ItemQuiverEndless.class)
                .setIcon("tools.png")
                .setTranslationKey("equipment");
        public static ItemCategoryBuilder food = new ItemCategoryBuilder(MOD_ID)
                .addClass(ItemFood.class)
                .addClass(ItemBucketIceCream.class)
                .addItem(Item.foodCake)
                .addItem(Item.bucketMilk)
                .setIcon("health.png")
                .setTranslationKey("food");
        public static ItemCategoryBuilder redstoneTransit = new ItemCategoryBuilder(MOD_ID)
                .addClass(BlockRail.class)
                .addClass(BlockRedstone.class)
                .addItem(Item.dustRedstone)
                .addClass(BlockRedstoneTorch.class)
                .addClass(BlockTNT.class)
                .addClass(BlockDispenser.class)
                .addClass(BlockNote.class)
                .addClass(BlockSpikes.class)
                .addClass(BlockMesh.class)
                .addItem(Item.repeater)
                .addClass(ItemMinecart.class)
                .addClass(ItemBoat.class)
                .addClass(ItemSaddle.class)
                .addClass(BlockPistonBase.class)
                .addClass(BlockMotionSensor.class)
                .addClass(BlockDoor.class)
                .addClass(ItemDoor.class)
                .addClass(BlockTrapDoor.class)
                .addClass(BlockLamp.class)
                .addClass(BlockLever.class)
                .addClass(BlockButton.class)
                .addClass(BlockPressurePlate.class)
                .addItem(Item.basket)
                .setIcon("redstonerail.png")
                .setTranslationKey("redstone");
        public static ItemCategoryBuilder natural = new ItemCategoryBuilder(MOD_ID)
                .addClass(BlockLeavesBase.class)
                .addClass(BlockDirtPath.class)
                .addClass(BlockLog.class)
                .addClass(BlockMud.class)
                .addClass(BlockFlower.class)
                .addClass(BlockStone.class)
                .addClass(BlockLayerBase.class)
                .addClass(BlockMoss.class)
                .addClass(BlockAlgae.class)
                .addClass(BlockSand.class)
                .addClass(BlockSoulSand.class)
                .addItem(Block.netherrack)
                .addItem(Block.pumpkin)
                .addClass(BlockIce.class)
                .addItem(Block.obsidian)
                .addClass(BlockGlowStone.class)
                .addClass(BlockBedrock.class)
                .addClass(BlockClay.class)
                .addClass(BlockSnow.class)
                .addClass(BlockSponge.class)
                .addClass(BlockCactus.class)
                .addClass(BlockCobweb.class)
                .addItem(Block.marble)
                .addItem(Block.slate)
                .addItem(Block.sandstone)
                .addClass(BlockGrass.class)
                .addClass(BlockGrassScorched.class)
                .addItem(Block.dirt)
                .addItem(Block.dirtScorched)
                .addItem(Block.dirtScorchedRich)
                .addClass(BlockFarmland.class)
                .addKeyword("tile.ore.")
                .setIcon("grass.png")
                .setTranslationKey("natural");
        public static ItemCategoryBuilder otherBlocks = new ItemCategoryBuilder(MOD_ID)
                .addClass(BlockWool.class)
                .addClass(BlockFence.class)
                .addClass(BlockFenceGate.class)
                .addClass(BlockFenceChainlink.class)
                .addClass(BlockTorch.class)
                .addClass(BlockLadder.class)
                .addClass(BlockSlab.class)
                .addClass(BlockStairs.class)
                .addClass(BlockGlass.class)
                .addClass(BlockPumpkin.class)
                .addClass(BlockTrommel.class)
                .addClass(BlockWorkbench.class)
                .addClass(BlockChest.class)
                .addClass(BlockSign.class)
                .addClass(BlockFurnace.class)
                .addClass(BlockJukeBox.class)
                .addClass(ItemSign.class)
                .addClass(ItemPainting.class)
                .addClass(ItemFlag.class)
                .addClass(BlockIgneousNetherrack.class)
                .addClass(BlockMobSpawner.class)
                .addItem(Block.mobspawnerDeactivated)
                .addItem(Block.permaice)
                .addItem(Block.pillarMarble)
                .addItem(Item.bed)
                .addItem(Item.lanternFireflyRed)
                .addItem(Item.lanternFireflyBlue)
                .addItem(Item.lanternFireflyGreen)
                .addItem(Item.lanternFireflyOrange)
                .addKeyword("tile.block.")
                .addKeyword(".polished")
                .addKeyword(".cobble.")
                .addKeyword(".planks.")
                .addKeyword("tile.brick.")
                .setIcon("bricks.png")
                .setTranslationKey("construction");
        public static ItemCategoryBuilder misc = new ItemCategoryBuilder(MOD_ID)
                .addClass(Block.class)
                .addClass(Item.class)
                .excludeModdedItems()
                .setIcon("painting.png")
                .setTranslationKey("misc");
        public static ItemCategoryBuilder modded = new ItemCategoryBuilder(MOD_ID)
                .isDebug()
                .setIcon("modded.png")
                .setTranslationKey("modded")
                .isModded();
    }
    public static class recipeBasics {
        public static RecipeGroupBuilder planks = new RecipeGroupBuilder()
                .addItem(Block.planksOak)
                .addItemsWithMetaRange(Block.planksOakPainted.asItem(), 0, 16, false);
        public static RecipeGroupBuilder torches = new RecipeGroupBuilder()
                .addClass(BlockTorch.class)
                .addItem(Item.stick)
                .excludeItem(Block.torchRedstoneActive.asItem());
        public static RecipeGroupBuilder utilityBlocks = new RecipeGroupBuilder()
                .addItem(Block.jukebox)
                .addItem(Block.workbench)
                .addItem(Block.furnaceBlastIdle)
                .addItem(Block.furnaceStoneIdle)
                .addItem(Block.trommelIdle);
        public static RecipeGroupBuilder chest = new RecipeGroupBuilder()
                .addClass(BlockChest.class);
        public static RecipeGroupBuilder bed = new RecipeGroupBuilder()
                .addItem(Item.bed);
        public static RecipeGroupBuilder fences = new RecipeGroupBuilder()
                .addClass(BlockFence.class);
        public static RecipeGroupBuilder fencegates = new RecipeGroupBuilder()
                .addClass(BlockFenceGate.class);
        public static RecipeGroupBuilder woodStairs = new RecipeGroupBuilder()
                .addKeyword("stairs.planks");
        public static RecipeGroupBuilder woodSlabs = new RecipeGroupBuilder()
                .addKeyword("slab.planks");
        public static RecipeGroupBuilder ladders = new RecipeGroupBuilder()
                .addClass(BlockLadder.class)
                .addClass(BlockFenceChainlink.class);
        public static RecipeGroupBuilder doors = new RecipeGroupBuilder()
                .addKeyword("door");
        public static RecipeGroupBuilder books = new RecipeGroupBuilder()
                .addItem(Block.bookshelfPlanksOak)
                .addItem(Item.book)
                .addItem(Item.paper);
        public static RecipeGroupBuilder display = new RecipeGroupBuilder()
                .addClass(ItemSign.class)
                .addClass(ItemPainting.class)
                .addClass(ItemFlag.class);
        public static RecipeCategoryBuilder category = new RecipeCategoryBuilder(MOD_ID)
                .setTranslationKey("basics")
                .setIcon("planks.png")
                .addRecipeGroupBuilders(new RecipeGroupBuilder[]{planks, torches, utilityBlocks, chest, bed, fences, fencegates, woodStairs, woodSlabs, ladders, doors, books, display});
    }
    public static class recipeBricks {
        public static RecipeGroupBuilder stoneStairs = new RecipeGroupBuilder()
                .addKeyword(".stairs.");
        public static RecipeGroupBuilder stoneSlabs = new RecipeGroupBuilder()
                .addKeyword(".slab.");
        public static RecipeGroupBuilder bricks = new RecipeGroupBuilder()
                .addKeyword(".brick.");
        public static RecipeGroupBuilder polished = new RecipeGroupBuilder()
                .addKeyword(".polished")
                .addKeyword(".pillar")
                .addKeyword(".carved")
                .excludeItem(Block.pumpkin.asItem())
                .excludeItem(Block.pumpkinCarvedActive.asItem())
                .excludeItem(Block.pumpkinCarvedIdle.asItem());
        public static RecipeGroupBuilder wool = new RecipeGroupBuilder()
                .addClass(BlockWool.class);
        public static RecipeGroupBuilder natural = new RecipeGroupBuilder()
                .addItem(Block.sandstone)
                .addItem(Block.gravel)
                .addItem(Block.blockSnow)
                .addItem(Block.blockClay)
                .addKeyword(".cobble.")
                .excludeItem(Block.dispenserCobbleStone.asItem())
                .addClass(BlockIce.class)
                .addItem(Block.permaice)
                .addClass(BlockPumpkin.class)
                .addClass(BlockSponge.class);
        public static RecipeGroupBuilder layers = new RecipeGroupBuilder()
                .addClass(BlockLayerBase.class);
        public static RecipeGroupBuilder resourceBlocks = new RecipeGroupBuilder()
                .addItem(Block.blockGold)
                .addItem(Block.blockIron)
                .addItem(Block.blockDiamond)
                .addItem(Block.blockLapis)
                .addItem(Block.blockSteel)
                .addItem(Block.blockRedstone)
                .addItem(Block.blockQuartz)
                .addItem(Block.blockCoal)
                .addItem(Block.blockCharcoal)
                .addItem(Block.blockNetherCoal)
                .addItem(Block.blockOlivine);
        public static RecipeGroupBuilder resourceBlocksUncompacts = new RecipeGroupBuilder()
                .addItem(Item.ingotGold)
                .addItem(Item.ingotIron)
                .addItem(Item.diamond)
                .addItem(Item.dye, 4, true)
                .addItem(Item.ingotSteel)
                .addItem(Item.dustRedstone)
                .addItem(Item.quartz)
                .addItem(Item.coal)
                .addItem(Item.nethercoal)
                .addItem(Item.olivine);
        public static RecipeCategoryBuilder category = new RecipeCategoryBuilder(MOD_ID)
                .setTranslationKey("bricks")
                .setIcon("bricks.png")
                .addRecipeGroupBuilders(new RecipeGroupBuilder[]{bricks, polished, stoneStairs, stoneSlabs, wool, natural, layers, resourceBlocks, resourceBlocksUncompacts});
    }
    public static class recipeTools {
        public static RecipeGroupBuilder pickaxe = new RecipeGroupBuilder()
                .addClass(ItemToolPickaxe.class);
        public static RecipeGroupBuilder shovel = new RecipeGroupBuilder()
                .addClass(ItemToolShovel.class);
        public static RecipeGroupBuilder axe = new RecipeGroupBuilder()
                .addClass(ItemToolAxe.class);
        public static RecipeGroupBuilder hoe = new RecipeGroupBuilder()
                .addClass(ItemToolHoe.class);
        public static RecipeGroupBuilder sword = new RecipeGroupBuilder()
                .addClass(ItemToolSword.class);
        public static RecipeGroupBuilder fishing = new RecipeGroupBuilder()
                .addClass(ItemFishingRod.class);
        public static RecipeGroupBuilder bow = new RecipeGroupBuilder()
                .addClass(ItemBow.class)
                .addItem(Item.ammoArrow)
                .addItem(Item.ammoArrowGold)
                .addItem(Item.ammoArrowPurple);
        public static RecipeGroupBuilder handcannon = new RecipeGroupBuilder()
                .addClass(ItemHandCannonLoaded.class)
                .addClass(ItemHandCannonUnloaded.class)
                .addItem(Item.ammoChargeExplosive);
        public static RecipeGroupBuilder miscTools = new RecipeGroupBuilder()
                .addClass(ItemFirestriker.class)
                .addClass(ItemBucket.class)
                .addClass(ItemBucketEmpty.class)
                .addClass(ItemLabel.class)
                .addClass(ItemToolShears.class);
        public static RecipeGroupBuilder toolInfo = new RecipeGroupBuilder()
                .addItem(Item.toolCompass)
                .addItem(Item.toolClock)
                .addItem(Item.toolCalendar)
                .addItem(Item.map);
        public static RecipeGroupBuilder helmet = new RecipeGroupBuilder()
                .addKeyword(".helmet.");
        public static RecipeGroupBuilder chestplate = new RecipeGroupBuilder()
                .addKeyword(".chestplate.")
                .addClass(ItemQuiver.class)
                .addClass(ItemQuiverEndless.class);
        public static RecipeGroupBuilder leggings = new RecipeGroupBuilder()
                .addKeyword(".leggings.");
        public static RecipeGroupBuilder boots = new RecipeGroupBuilder()
                .addKeyword(".boots.");
        public static RecipeCategoryBuilder category = new RecipeCategoryBuilder(MOD_ID)
                .setTranslationKey("equipment")
                .setIcon("tools.png")
                .addRecipeGroupBuilders(new RecipeGroupBuilder[]{pickaxe,shovel,axe,hoe,sword,fishing,bow,handcannon,miscTools,toolInfo,helmet,chestplate,leggings,boots});
    }
    public static class recipeFood {
        public static RecipeGroupBuilder bread = new RecipeGroupBuilder()
                .addItem(Item.foodBread);
        public static RecipeGroupBuilder stew = new RecipeGroupBuilder()
                .addClass(ItemSoup.class)
                .addItem(Item.bowl);
        public static RecipeGroupBuilder cake = new RecipeGroupBuilder()
                .addItem(Item.foodCake);
        public static RecipeGroupBuilder cookies = new RecipeGroupBuilder()
                .addItem(Item.foodCookie);
        public static RecipeGroupBuilder icecream = new RecipeGroupBuilder()
                .addClass(ItemBucketIceCream.class);
        public static RecipeGroupBuilder apple = new RecipeGroupBuilder()
                .addItem(Item.foodApple)
                .addItem(Item.foodAppleGold);
        public static RecipeGroupBuilder sugar = new RecipeGroupBuilder()
                .addItem(Item.dustSugar);
        public static RecipeGroupBuilder foodGeneral = new RecipeGroupBuilder()
                .addClass(ItemFood.class);
        public static RecipeCategoryBuilder category = new RecipeCategoryBuilder(MOD_ID)
                .setTranslationKey("food")
                .setIcon("health.png")
                .addRecipeGroupBuilders(new RecipeGroupBuilder[]{bread,stew,cake,cookies,icecream,apple,sugar, foodGeneral});
    }
    public static class recipeRedstone {
        public static RecipeGroupBuilder basicRedstone = new RecipeGroupBuilder()
                .addItem(Item.repeater)
                .addItem(Item.dustRedstone, true)
                .addItem(Block.torchRedstoneActive);
        public static RecipeGroupBuilder redstoneBlock = new RecipeGroupBuilder()
                .addClass(BlockRedstone.class, true);
        public static RecipeGroupBuilder buttonLever = new RecipeGroupBuilder()
                .addClass(BlockButton.class)
                .addClass(BlockLever.class);
        public static RecipeGroupBuilder pressureplates = new RecipeGroupBuilder()
                .addClass(BlockPressurePlate.class);
        public static RecipeGroupBuilder pistons = new RecipeGroupBuilder()
                .addClass(BlockPistonBase.class);
        public static RecipeGroupBuilder lamps = new RecipeGroupBuilder()
                .addClass(BlockLamp.class);
        public static RecipeGroupBuilder noteBlock = new RecipeGroupBuilder()
                .addClass(BlockNote.class);
        public static RecipeGroupBuilder dispensers = new RecipeGroupBuilder()
                .addClass(BlockDispenser.class);
        public static RecipeGroupBuilder traps = new RecipeGroupBuilder()
                .addClass(BlockTNT.class)
                .addClass(BlockSpikes.class)
                .addClass(BlockMesh.class);

        public static RecipeCategoryBuilder category = new RecipeCategoryBuilder(MOD_ID)
                .setIcon("lever.png")
                .setTranslationKey("restone")
                .addRecipeGroupBuilders(new RecipeGroupBuilder[]{basicRedstone, redstoneBlock, buttonLever, pressureplates,pistons,lamps,noteBlock,dispensers,traps});
    }
    public static class recipeTransit {
        public static RecipeGroupBuilder baseRail = new RecipeGroupBuilder()
                .addItem(Block.rail);
        public static RecipeGroupBuilder detectRail = new RecipeGroupBuilder()
                .addItem(Block.railDetector);
        public static RecipeGroupBuilder powerRail = new RecipeGroupBuilder()
                .addItem(Block.railPowered);
        public static RecipeGroupBuilder allRail = new RecipeGroupBuilder()
                .addClass(BlockRail.class);
        public static RecipeGroupBuilder baseCart = new RecipeGroupBuilder()
                .addItem(Item.minecart);
        public static RecipeGroupBuilder chestCart = new RecipeGroupBuilder()
                .addItem(Item.minecartChest);
        public static RecipeGroupBuilder furnaceCart = new RecipeGroupBuilder()
                .addItem(Item.minecartFurnace);
        public static RecipeGroupBuilder allCart = new RecipeGroupBuilder()
                .addClass(ItemMinecart.class);
        public static RecipeGroupBuilder boat = new RecipeGroupBuilder()
                .addClass(ItemBoat.class);
        public static RecipeCategoryBuilder category = new RecipeCategoryBuilder(MOD_ID)
                .setIcon("rail.png")
                .setTranslationKey("travel")
                .addRecipeGroupBuilders(new RecipeGroupBuilder[]{baseRail,powerRail,detectRail,allRail,baseCart,chestCart,furnaceCart,allCart,boat});
    }
    public static class recipeMisc{
        public static RecipeGroupBuilder dyes = new RecipeGroupBuilder()
                .addClass(ItemDye.class);
        public static RecipeGroupBuilder allBlocks = new RecipeGroupBuilder()
                .addClass(Block.class);
        public static RecipeGroupBuilder allItems = new RecipeGroupBuilder()
                .addClass(Item.class);
        public static RecipeGroupBuilder all = new RecipeGroupBuilder()
                .addKeyword(".");
        public static RecipeCategoryBuilder category = new RecipeCategoryBuilder(MOD_ID)
                .setTranslationKey("misc")
                .setIcon("painting.png")
                .addRecipeGroupBuilders(new RecipeGroupBuilder[]{dyes,allBlocks,allItems,all});
    }
}
