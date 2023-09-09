package useless.legacyui.Utils;

import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.packet.Packet104WindowItems;

public class PacketUtil {
    public static Packet104WindowItems packet104Converter(Packet104WindowItems packet){
        ItemStack[] newStackList = new ItemStack[packet.itemStack.length-5];
        System.arraycopy(packet.itemStack, 5, newStackList, 0, packet.itemStack.length - 5);
        packet.itemStack = newStackList;
        return packet;
    }
}
