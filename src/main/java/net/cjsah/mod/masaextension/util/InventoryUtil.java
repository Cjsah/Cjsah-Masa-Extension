package net.cjsah.mod.masaextension.util;

import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InventoryUtil {
    @Nullable
    public static List<ItemStack> serializeItems(CompoundTag nbt, RegistryAccess access) {
        if (!nbt.contains("Items")) return null;
        return serializeItems(nbt.getList("Items", Tag.TAG_COMPOUND), access);
    }

    public static List<ItemStack> serializeItems(ListTag items, RegistryAccess access) {
        return items.stream()
            .map(tag -> ItemStack.parse(access, tag).orElse(ItemStack.EMPTY))
            .filter(stack -> !stack.isEmpty())
            .toList();

    }

}
