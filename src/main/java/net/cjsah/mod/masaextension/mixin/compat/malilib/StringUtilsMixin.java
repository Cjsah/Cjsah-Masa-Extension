package net.cjsah.mod.masaextension.mixin.compat.malilib;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fi.dy.masa.malilib.util.StringUtils;
import net.cjsah.mod.masaextension.handler.CrossServerHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = StringUtils.class, remap = false)
public class StringUtilsMixin {

    @WrapOperation(method = "getStorageFileName", at = @At(value = "INVOKE", target = "Lfi/dy/masa/malilib/util/StringUtils;getWorldOrServerName()Ljava/lang/String;"))
    private static String modifyName(Operation<String> original) {
        String name = original.call();
        return CrossServerHandler.resolveConfigName(name);
    }
}
