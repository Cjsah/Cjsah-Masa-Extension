package net.cjsah.mod.masaextension;

import net.cjsah.mod.masaextension.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.CompoundTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//#if MC >= 12105
//$$ import java.util.Optional;
//#endif

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
            Minecraft.getInstance().getChatListener().handleSystemMessage(
                Component.translatable("cjsah_masa_extension.server.changed", name),
                false
            );
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

    public static void parseMetadata(CompoundTag metadata) {
        String key = "extra:" + ModInfo.MOD_ID;
        //#if MC < 12105
        if (metadata.contains(key)) {
            CompoundTag compound = metadata.getCompound(key);
            CjsahMasaExtension.setCachedServerName(compound.getString("server_name"));
        }
        //#else
        //$$ Optional<CompoundTag> compound = metadata.getCompound(key);
        //$$ compound.ifPresent(tag ->
        //$$     CjsahMasaExtension.setCachedServerName(tag.getStringOr("server_name", "default"))
        //$$ );
        //#endif
    }
}
