package net.cjsah.mod.masaextension.mixin.compat.litematica;

import fi.dy.masa.litematica.data.EntitiesDataStorage;
import net.cjsah.mod.masaextension.CjsahMasaExtension;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EntitiesDataStorage.class, remap = false)
public class EntitiesDataStorageMixin {

    @Inject(method = "receiveServuxMetadata", at = @At(value = "INVOKE", target = "Lfi/dy/masa/litematica/data/EntitiesDataStorage;setServuxVersion(Ljava/lang/String;)V"))
    private void parseMetadata(CompoundTag data, CallbackInfoReturnable<Boolean> cir) {
        CjsahMasaExtension.parseMetadata(data);
    }
}
