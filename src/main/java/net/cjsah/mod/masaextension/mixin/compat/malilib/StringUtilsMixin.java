package net.cjsah.mod.masaextension.mixin.compat.malilib;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fi.dy.masa.malilib.util.StringUtils;
import net.cjsah.mod.masaextension.CjsahMasaExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = StringUtils.class, remap = false)
public class StringUtilsMixin {

    @WrapOperation(method = "getStorageFileName", at = @At(value = "INVOKE", target = "Lfi/dy/masa/malilib/util/StringUtils;getWorldOrServerName()Ljava/lang/String;"))
    private static String modifyName(Operation<String> original) {
        String name = original.call();

        String serverName = CjsahMasaExtension.getCachedServerName();

        if (name == null || "default".equals(serverName)) {
            return name;
        }

        return name + "_" + serverName;
    }
}
