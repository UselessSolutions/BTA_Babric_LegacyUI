package useless.legacyui.utils;

public class ArrayUtil {
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
