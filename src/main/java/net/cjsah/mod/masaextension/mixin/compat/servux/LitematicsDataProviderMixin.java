package net.cjsah.mod.masaextension.mixin.compat.servux;

import fi.dy.masa.servux.dataproviders.LitematicsDataProvider;
import net.cjsah.mod.masaextension.handler.CrossServerHandler;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LitematicsDataProvider.class, remap = false)
public class LitematicsDataProviderMixin {

    @Shadow
    @Final
    protected CompoundTag metadata;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void test(CallbackInfo ci) {
        CrossServerHandler.applyExtension(this.metadata);
    }
}
