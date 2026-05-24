package net.cjsah.mod.masaextension.mixin.compat.litematica;

import fi.dy.masa.litematica.materials.MaterialCache;
import fi.dy.masa.litematica.materials.MaterialListUtils;
import fi.dy.masa.malilib.util.ItemType;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = MaterialListUtils.class, remap = false)
public interface MaterialListUtilsInvoker {
    @Invoker
    static void invokeConvertStatesToStacks(Object2IntOpenHashMap<BlockState> blockStatesIn, Object2IntOpenHashMap<ItemType> itemTypesOut, MaterialCache cache) {
        throw new AssertionError();
    }
}
