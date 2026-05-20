package net.cjsah.mod.masaextension.config;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.IHotkeyTogglable;
import fi.dy.masa.malilib.config.options.ConfigBase;
import fi.dy.masa.malilib.config.options.ConfigBooleanHotkeyed;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.util.FileUtils;
import fi.dy.masa.malilib.util.JsonUtils;
import fi.dy.masa.minihud.Reference;
import net.cjsah.mod.masaextension.CjsahMasaExtension;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Configs implements IConfigHandler {
    private static final String CONFIG_FILE_NAME = Reference.MOD_ID + ".json";

    public static final ConfigHotkey OPEN_CONFIG_SCREEN = register(ConfigGui.Tab.GENERIC, new ConfigHotkey("openConfigScreen", "P,C"));

    public static final ConfigHotkey SYNC_AREA_CONTAINER = register(ConfigGui.Tab.GENERIC, new ConfigHotkey("syncAreaContainer", ""));
    public static final ConfigBooleanHotkeyed HUD_DATA_SYNC = register(ConfigGui.Tab.GENERIC, new ConfigBooleanHotkeyed("hudDataSync", false, ""));
    public static final ConfigBooleanHotkeyed ENTITY_MATERIALS = register(ConfigGui.Tab.GENERIC, new ConfigBooleanHotkeyed("entityMaterials", false, ""));

    public static final List<IConfigBase> OPTIONS = ImmutableList.of(
        OPEN_CONFIG_SCREEN,
        SYNC_AREA_CONTAINER,
        HUD_DATA_SYNC,
        ENTITY_MATERIALS
    );

    public static final List<IHotkeyTogglable> HOTKEYS = ImmutableList.of(
        HUD_DATA_SYNC,
        ENTITY_MATERIALS
    );

    private static <T extends ConfigBase<?>> T register(ConfigGui.Tab tab, T config) {
        config.apply(tab.key);
        return config;
    }

    @Override
    public void load() {
        Path configFile = FileUtils.getConfigDirectoryAsPath().resolve(CONFIG_FILE_NAME);
        if (Files.exists(configFile) && Files.isReadable(configFile)) {
            JsonElement element = JsonUtils.parseJsonFileAsPath(configFile);

            if (element != null && element.isJsonObject()) {
                JsonObject root = element.getAsJsonObject();
                ConfigUtils.readConfigBase(root, "Generic", OPTIONS);
            } else {
                CjsahMasaExtension.LOGGER.error("loadFromFile(): Failed to load config file '{}'.", configFile.toAbsolutePath());
            }
        }

    }

    @Override
    public void save() {
        Path dir = FileUtils.getConfigDirectoryAsPath();

        if (!Files.exists(dir)) {
            FileUtils.createDirectoriesIfMissing(dir);
        }

        if (Files.isDirectory(dir)) {
            JsonObject root = new JsonObject();
            ConfigUtils.writeConfigBase(root, "Generic", fi.dy.masa.minihud.config.Configs.Generic.OPTIONS);
            JsonUtils.writeJsonToFileAsPath(root, dir.resolve(CONFIG_FILE_NAME));
        } else {
            CjsahMasaExtension.LOGGER.error("saveToFile(): Config Folder '{}' does not exist!", dir.toAbsolutePath());
        }

    }
}
