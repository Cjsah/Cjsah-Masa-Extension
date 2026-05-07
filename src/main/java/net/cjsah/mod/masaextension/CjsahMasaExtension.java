package net.cjsah.mod.masaextension;

import net.cjsah.mod.masaextension.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.CompoundTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CjsahMasaExtension {
    public static final Logger LOGGER = LoggerFactory.getLogger(ModInfo.MOD_NAME);

    private static String cachedServerName = "default";

    public static String getCachedServerName() {
        return cachedServerName;
    }

    public static void setCachedServerName(String name) {
        if (cachedServerName.equals(name)) return;
        cachedServerName = name;

        if (Minecraft.getInstance().player != null) {
            Minecraft.getInstance().player.sendSystemMessage(Component.literal("Server is changed to: " + name));
        }

        LOGGER.info("Server is changed to: {}", name);
    }

    public static void reset() {
        cachedServerName = "default";
    }

    public static void applyExtension(CompoundTag metadata) {
        CompoundTag extension = new CompoundTag();
        extension.putString("server_name", ModConfig.getInstance().name());
        metadata.put("extra:" + ModInfo.MOD_ID, extension);
    }
}
