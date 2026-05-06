package net.cjsah.mod.masaextension;

public final class ModInfo {
    // 避免编译器优化
    public static final String MOD_ID = of("{MOD_ID}");
    public static final String MOD_NAME = of("{MOD_NAME}");

    private static String of(String value) {
        return value;
    }

}
