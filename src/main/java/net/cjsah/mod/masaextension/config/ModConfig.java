package net.cjsah.mod.masaextension.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.cjsah.mod.masaextension.CjsahMasaExtension;
import net.cjsah.mod.masaextension.ModInfo;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public record ModConfig(String name) {
    public static final Codec<ModConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.fieldOf("server_name").forGetter(ModConfig::name)
    ).apply(instance, ModConfig::new));
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private static ModConfig INSTANCE;

    public static ModConfig getInstance() {
        if (INSTANCE == null) {
            INSTANCE = loadConfig();
            CjsahMasaExtension.LOGGER.info("Current Server Name: {}", INSTANCE.name());
        }
        return INSTANCE;
    }

    private static ModConfig loadConfig() {
        Path path = FabricLoader.getInstance().getConfigDir().resolve(ModInfo.MOD_ID + ".json");
        File file = path.toFile();
        try {
            if (!file.exists() || !file.isFile()) {
                file.getParentFile().mkdirs();
                return createDefaultConfig(path);
            }
            String s = Files.readString(file.toPath(), StandardCharsets.UTF_8);
            Optional<ModConfig> result = CODEC.parse(JsonOps.INSTANCE, JsonParser.parseString(s)).result();
            if (result.isEmpty()) {
                CjsahMasaExtension.LOGGER.warn("Failed to parse config file, using default config");
                return createDefaultConfig(path);
            }
            return result.get();
        }catch (IOException e) {
            CjsahMasaExtension.LOGGER.warn("Failed to read config file, using default config", e);
            return createDefaultConfig(path);
        }
    }

    private static ModConfig createDefaultConfig(Path path) {
        ModConfig config = new ModConfig("default");
        JsonElement result = CODEC.encodeStart(JsonOps.INSTANCE, config).result().orElseThrow();
        try {
            Files.writeString(path, GSON.toJson(result), StandardCharsets.UTF_8);
        } catch (IOException ignored) {}
        return config;
    }



}
