package useless.legacyui.Sorting.Items;

import java.util.ArrayList;

public class CategoryManager {
    private static ArrayList<ItemCategory> itemCategories = new ArrayList<ItemCategory>();

    public static ItemCategory get(int index){
        if (index < itemCategories.size()){
            return itemCategories.get(index);
        }
        return null;
    }

    public static int size(){
        return itemCategories.size();
    }

    static {
        for (int i = 0; i < 7; i++){
            itemCategories.add(new ItemCategory(String.valueOf(i), new int[]{i * 16,0}));
        }
    }
}
