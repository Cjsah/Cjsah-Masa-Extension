package net.cjsah.mod.masaextension.mixin.compat.minihud;

import fi.dy.masa.minihud.data.EntitiesDataManager;
import net.cjsah.mod.masaextension.handler.CrossServerHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EntitiesDataManager.class, remap = false)
public class EntitiesDataManagerMixin {

    @Inject(method = "receiveServuxMetadata", at = @At(value = "INVOKE", target = "Lfi/dy/masa/minihud/data/EntitiesDataManager;setServuxVersion(Ljava/lang/String;)V"))
    private void parseMetadata(CompoundTag data, CallbackInfoReturnable<Boolean> cir) {
        CrossServerHandler.parseMetadata(data);
    }

    @Inject(method = "handleBlockEntityData", at = @At(value = "INVOKE", target = "Ljava/util/concurrent/ConcurrentHashMap;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
    private void receiveBlockEntityData(BlockPos pos, CompoundTag nbt, ResourceLocation type, CallbackInfoReturnable<BlockEntity> cir) {
        if (!nbt.contains("Items")) return;
        ListTag items = nbt.getList("Items", Tag.TAG_COMPOUND);

    }
}
