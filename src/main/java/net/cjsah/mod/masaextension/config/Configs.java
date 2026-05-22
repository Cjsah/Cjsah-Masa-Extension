package net.cjsah.mod.masaextension.config;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.options.ConfigBase;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigBooleanHotkeyed;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.hotkeys.IHotkey;
import fi.dy.masa.malilib.util.FileUtils;
import fi.dy.masa.malilib.util.JsonUtils;
import net.cjsah.mod.masaextension.CjsahMasaExtension;
import net.cjsah.mod.masaextension.ModInfo;
import net.cjsah.mod.masaextension.handler.CrossServerHandler;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Configs implements IConfigHandler {
    private static final List<ConfigInfo<?>> OPTIONS_INNER = new ArrayList<>(50);

    private static final String PREFIX = ModInfo.MOD_ID + ".config.option";

    public static final ConfigHotkey OPEN_CONFIG_SCREEN = register(ConfigTab.GENERIC, new ConfigHotkey("openConfigScreen", "P,C"));

    public static final ConfigHotkey SYNC_AREA_CONTAINER = register(ConfigTab.GENERIC, new ConfigHotkey("syncAreaContainer", ""));
    public static final ConfigBoolean HUD_DATA_SYNC = register(ConfigTab.GENERIC, new ConfigBoolean("hudDataSync", false));
    public static final ConfigBoolean SELECTION_MATERIALS = register(ConfigTab.GENERIC, new ConfigBooleanHotkeyed("selectionMaterials", false, ""));
    public static final ConfigBoolean CONTAINER_MATERIALS = register(ConfigTab.GENERIC, new ConfigBooleanHotkeyed("containerMaterials", false, ""));
    public static final ConfigBoolean ENTITY_MATERIALS = register(ConfigTab.GENERIC, new ConfigBooleanHotkeyed("entityMaterials", false, ""));

    public static final ConfigBoolean CROSS_SERVER_SUPPORT = register(ConfigTab.BUNGEE, new ConfigBoolean("crossServerSupport", false));


    public static final ImmutableMap<ConfigTab, ImmutableList<IConfigBase>> OPTIONS;
    public static final ImmutableList<IHotkey> HOTKEYS;

    static {
        List<IConfigBase> total = OPTIONS_INNER.stream().map(it -> it.config).collect(Collectors.toList());
        Map<ConfigTab, List<IConfigBase>> group = OPTIONS_INNER
            .stream()
            .collect(Collectors.groupingBy(
                it -> it.tab,
                Collectors.mapping(it -> it.config, Collectors.toList())
            ));
        ImmutableMap.Builder<ConfigTab, ImmutableList<IConfigBase>> builder = ImmutableMap.builder();
        for (Map.Entry<ConfigTab, List<IConfigBase>> entry : group.entrySet()) {
            builder.put(entry.getKey(), ImmutableList.copyOf(entry.getValue()));
        }
        builder.put(ConfigTab.ALL, ImmutableList.copyOf(total));
        OPTIONS = builder.build();
        List<IHotkey> hotkeys = total.stream().filter(it -> it instanceof IHotkey).map(it -> (IHotkey) it).toList();
        HOTKEYS = ImmutableList.copyOf(hotkeys);
    }

    private static <T extends ConfigBase<?>> T register(ConfigTab tab, T option) {
        option.apply(PREFIX);
        option.setTranslatedName(PREFIX + "." + option.getCleanName() + "." + ConfigBase.TRANSLATED_NAME_KEY);
        option.setComment       (PREFIX + "." + option.getCleanName() + "." + ConfigBase.COMMENT_KEY);
        option.setPrettyName    (PREFIX + "." + option.getCleanName() + "." + ConfigBase.TRANSLATED_NAME_KEY);
        OPTIONS_INNER.add(new ConfigInfo<>(option, tab));
        return option;
    }

    @Override
    public void load() {
        Path configFile = FileUtils.getConfigDirectoryAsPath().resolve(ModInfo.MOD_ID + ".json");

        if (Files.exists(configFile) && Files.isReadable(configFile)) {
            JsonElement element = JsonUtils.parseJsonFileAsPath(configFile);

            if (element != null && element.isJsonObject()) {
                JsonObject root = element.getAsJsonObject();

                for (Map.Entry<ConfigTab, ImmutableList<IConfigBase>> entry : OPTIONS.entrySet()) {
                    ConfigTab key = entry.getKey();
                    if (key == ConfigTab.ALL) continue;
                    ConfigUtils.readConfigBase(root, key.getName(), entry.getValue());
                }

                CrossServerHandler.load(root);
            }
        } else {
            CjsahMasaExtension.LOGGER.error("initConfig(): Failed to load config file '{}'.", configFile.toAbsolutePath());
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
            for (Map.Entry<ConfigTab, ImmutableList<IConfigBase>> entry : OPTIONS.entrySet()) {
                ConfigTab key = entry.getKey();
                if (key == ConfigTab.ALL) continue;
                ConfigUtils.writeConfigBase(root, key.getName(), entry.getValue());
            }

            CrossServerHandler.save(root);

            JsonUtils.writeJsonToFileAsPath(root, dir.resolve(ModInfo.MOD_ID + ".json"));
        } else {
            CjsahMasaExtension.LOGGER.error("saveConfig(): Config Folder '{}' does not exist!", dir.toAbsolutePath());
        }
    }

    private record ConfigInfo<T extends IConfigBase>(T config, ConfigTab tab) {
    }

}
