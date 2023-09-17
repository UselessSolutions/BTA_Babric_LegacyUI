package useless.legacyui.Helper;

public class ArrayHelper {
    public static int wrapAroundIndex(int index, int arrayLength) {
        while (index > arrayLength - 1) {
            index -= arrayLength;
        }
        while (index < 0) {
            index += arrayLength;
        }
        return index;
    }
}
