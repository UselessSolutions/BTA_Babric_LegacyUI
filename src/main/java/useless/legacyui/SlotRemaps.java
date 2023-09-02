package useless.legacyui;

public class SlotRemaps {
    public static int remapSurvivalInventory(int id, boolean fromServer){
        if (fromServer){
            return id - 5;
        }
        return id + 5;
    }
    public static int remapInventoryCrafting(int id, boolean fromServer){
        if (id < 5){ // [0,4] are crafting slots
            return id;
        }
        if (fromServer){
            // skip over the 4 armor slots
            return id - 4;
        }
        return id + 4;
    }
}
