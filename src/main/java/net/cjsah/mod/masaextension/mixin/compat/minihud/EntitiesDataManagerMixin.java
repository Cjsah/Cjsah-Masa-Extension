package net.cjsah.mod.masaextension.mixin.compat.minihud;

import fi.dy.masa.minihud.data.EntitiesDataManager;
import net.cjsah.mod.masaextension.CjsahMasaExtension;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EntitiesDataManager.class, remap = false)
public class EntitiesDataManagerMixin {

    @Inject(method = "receiveServuxMetadata", at = @At(value = "INVOKE", target = "Lfi/dy/masa/minihud/data/EntitiesDataManager;setServuxVersion(Ljava/lang/String;)V"))
    private void parseMetadata(CompoundTag data, CallbackInfoReturnable<Boolean> cir) {
        CjsahMasaExtension.parseMetadata(data);
    }

}
