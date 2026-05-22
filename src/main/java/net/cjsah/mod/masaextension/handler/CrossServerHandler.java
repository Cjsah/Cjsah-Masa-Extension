package net.cjsah.mod.masaextension.handler;

import com.google.gson.JsonObject;
import net.cjsah.mod.masaextension.CjsahMasaExtension;
import net.cjsah.mod.masaextension.ModInfo;
import net.cjsah.mod.masaextension.config.Configs;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;

//#if MC >= 12105
//$$ import java.util.Optional;
//#endif

public class CrossServerHandler {
    private static String cachedServerName = "default";

    public static String resolveConfigName(String origin) {
        if (!Configs.CROSS_SERVER_SUPPORT.getBooleanValue() || origin == null || "default".equals(cachedServerName)) {
            return origin;
        }

        return origin + "_" + cachedServerName;
    }

    public static void setCachedServerName(String name) {
        if (cachedServerName.equals(name)) return;
        cachedServerName = name;

        if (Minecraft.getInstance().player != null) {
            Minecraft.getInstance().getChatListener().handleSystemMessage(
                Component.translatable("cjsah_masa_extension.server.changed", name),
                false
            );
        }

        CjsahMasaExtension.LOGGER.info("Server is changed to: {}", name);
    }

    public static void reset() {
        cachedServerName = "default";
    }

    public static void applyExtension(CompoundTag metadata) {
        CompoundTag extension = new CompoundTag();
        extension.putString("server_name", cachedServerName);
        metadata.put("extra:" + ModInfo.MOD_ID, extension);
    }

    public static void parseMetadata(CompoundTag metadata) {
        String key = "extra:" + ModInfo.MOD_ID;
        //#if MC < 12105
        if (metadata.contains(key)) {
            CompoundTag compound = metadata.getCompound(key);
            setCachedServerName(compound.getString("server_name"));
        }
        //#else
        //$$ Optional<CompoundTag> compound = metadata.getCompound(key);
        //$$ compound.ifPresent(tag ->
        //$$     setCachedServerName(tag.getStringOr("server_name", "default"))
        //$$ );
        //#endif
    }

    public static void load(JsonObject root) {
        String name = "default";

        if (root.has("server_name")) {
            String serverName = root.get("server_name").getAsString();
            if (serverName != null && !serverName.trim().isEmpty()) {
                name = serverName;
            }
        }

        cachedServerName = name;
    }

    public static void save(JsonObject root) {
        root.addProperty("server_name", cachedServerName);
    }


}
