package net.cjsah.mod.masaextension.util;

import net.fabricmc.loader.api.FabricLoader;

public class ModUtil {
    public static boolean isLitematicaLoaded() {
        return isModLoaded("litematica");
    }

    public static boolean isMinihudLoaded() {
        return isModLoaded("minihud");
    }

    public static boolean isCheckTrackerLoaded() {
        return isModLoaded("checktracker");
    }

    public static boolean isModLoaded(String modid) {
        return FabricLoader.getInstance().isModLoaded(modid);
    }

}
