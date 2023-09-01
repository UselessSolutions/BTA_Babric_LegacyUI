package useless.legacyui;

public class SlotRemaps {
    public static int remapSurvivalInventory(int id, boolean fromServer){
        if (fromServer){
            return id - 5;
        }
        return id + 5;
    }
}
