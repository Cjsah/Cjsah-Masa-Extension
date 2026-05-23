package net.cjsah.mod.masaextension.mixin.compat.minihud;

import com.llamalad7.mixinextras.sugar.Local;
import fi.dy.masa.malilib.render.InventoryOverlay;
import fi.dy.masa.minihud.renderer.InventoryOverlayHandler;
import net.cjsah.mod.masaextension.handler.ChestTrackerHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InventoryOverlayHandler.class)
public class InventoryOverlayHandlerMixin {
    @Inject(method = "getTargetInventory", at = @At(value = "INVOKE", target = "Lfi/dy/masa/minihud/renderer/InventoryOverlayHandler;getTargetInventoryFromBlock(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/nbt/CompoundTag;)Lfi/dy/masa/malilib/render/InventoryOverlay$Context;"))
    private void syncToChestTracker(Minecraft mc, CallbackInfoReturnable<InventoryOverlay.Context> cir, @Local Level world, @Local BlockPos pos, @Local CompoundTag nbt) {
        if (!(world instanceof ServerLevel) || !nbt.contains("Items")) return;
        ListTag items = nbt.getList("Items", Tag.TAG_COMPOUND);
        ChestTrackerHandler.saveItems(pos, items);
    }
}
