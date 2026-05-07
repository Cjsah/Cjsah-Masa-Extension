package net.cjsah.mod.masaextension;

import net.minecraft.resources.ResourceLocation;

public final class ModInfo {
    // 避免编译器优化
    public static final String MOD_ID = val("@MOD_ID@");
    public static final String MOD_NAME = val("@MOD_NAME@");
    public static final String MOD_VERSION = val("@MOD_VERSION@");

    private static String val(String value) {
        return value;
    }

    public static ResourceLocation of(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

}
