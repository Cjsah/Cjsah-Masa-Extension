package net.cjsah.mod.masaextension.mixin.compat.litematica;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fi.dy.masa.litematica.materials.MaterialListEntry;
import fi.dy.masa.litematica.materials.MaterialListUtils;
import fi.dy.masa.litematica.schematic.LitematicaSchematic;
import fi.dy.masa.litematica.schematic.container.LitematicaBlockStateContainer;
import fi.dy.masa.malilib.util.ItemType;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.cjsah.mod.masaextension.handler.LitematicaHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mixin(MaterialListUtils.class)
public class MaterialListUtilsMixin {
//    @Inject(method = "createMaterialListFor(Lfi/dy/masa/litematica/schematic/LitematicaSchematic;Ljava/util/Collection;)Ljava/util/List;", at = @At(value = "INVOKE", target = "Lfi/dy/masa/litematica/materials/MaterialListUtils;getMaterialList(Lit/unimi/dsi/fastutil/objects/Object2IntOpenHashMap;Lit/unimi/dsi/fastutil/objects/Object2IntOpenHashMap;Lit/unimi/dsi/fastutil/objects/Object2IntOpenHashMap;Lnet/minecraft/world/entity/player/Player;)Ljava/util/List;"))
//    private static void test(LitematicaSchematic schematic, Collection<String> subRegions, CallbackInfoReturnable<List<MaterialListEntry>> cir) {
//        for (String name : schematic.getAreas().keySet()) {
//
//            Map<BlockPos, CompoundTag> region = schematic.getBlockEntityMapForRegion(name);
//            if (region == null) continue;
//
//            for (CompoundTag nbt : region.values()) {
//                if (!nbt.contains("Items")) continue;
//
//            }
//
//        }
//
//    }

    @WrapOperation(method = "getMaterialList", at = @At(value = "INVOKE", target = "Lfi/dy/masa/litematica/materials/MaterialListUtils;getInventoryItemCounts(Lnet/minecraft/world/Container;)Lit/unimi/dsi/fastutil/objects/Object2IntOpenHashMap;"))
    private static Object2IntOpenHashMap<ItemType> initApplySelectionArea(Container inv, Operation<Object2IntOpenHashMap<ItemType>> original) {
        Object2IntOpenHashMap<ItemType> result = original.call(inv);
        LitematicaHandler.applySelectionArea(result);
        return result;
    }

    @WrapOperation(method = "updateAvailableCounts", at = @At(value = "INVOKE", target = "Lfi/dy/masa/litematica/materials/MaterialListUtils;getInventoryItemCounts(Lnet/minecraft/world/Container;)Lit/unimi/dsi/fastutil/objects/Object2IntOpenHashMap;"))
    private static Object2IntOpenHashMap<ItemType> updateApplySelectionArea(Container inv, Operation<Object2IntOpenHashMap<ItemType>> original) {
        Object2IntOpenHashMap<ItemType> result = original.call(inv);
        LitematicaHandler.applySelectionArea(result);
        return result;
    }
}
