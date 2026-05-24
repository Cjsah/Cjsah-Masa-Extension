package net.cjsah.mod.masaextension.mixin.compat.litematica;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import fi.dy.masa.litematica.materials.MaterialListEntry;
import fi.dy.masa.litematica.materials.MaterialListUtils;
import fi.dy.masa.litematica.schematic.LitematicaSchematic;
import fi.dy.masa.malilib.util.ItemType;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.cjsah.mod.masaextension.handler.LitematicaHandler;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Collection;
import java.util.List;

@Mixin(MaterialListUtils.class)
public class MaterialListUtilsMixin {
    @WrapOperation(method = "createMaterialListFor(Lfi/dy/masa/litematica/schematic/LitematicaSchematic;Ljava/util/Collection;)Ljava/util/List;", at = @At(value = "INVOKE", target = "Lfi/dy/masa/litematica/materials/MaterialListUtils;getMaterialList(Lit/unimi/dsi/fastutil/objects/Object2IntOpenHashMap;Lit/unimi/dsi/fastutil/objects/Object2IntOpenHashMap;Lit/unimi/dsi/fastutil/objects/Object2IntOpenHashMap;Lnet/minecraft/world/entity/player/Player;)Ljava/util/List;"))
    private static List<MaterialListEntry> test(Object2IntOpenHashMap<BlockState> countsTotal, Object2IntOpenHashMap<BlockState> countsMissing, Object2IntOpenHashMap<BlockState> countsMismatch, Player player, Operation<List<MaterialListEntry>> original, @Local(argsOnly = true) LitematicaSchematic schematic, @Local(argsOnly = true) Collection<String> subRegions) {
        return LitematicaHandler.getMaterials(schematic, subRegions, countsTotal, player);
    }

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
