package net.cjsah.mod.masaextension.config.io;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.util.FileUtils;
import fi.dy.masa.malilib.util.JsonUtils;
import net.cjsah.mod.masaextension.CjsahMasaExtension;
import net.cjsah.mod.masaextension.ModInfo;

import java.nio.file.Files;
import java.nio.file.Path;

public abstract class ConfigIO {

    protected final String filename;

    public ConfigIO(String filename) {
        this.filename = filename + ".json";
    }

    public void load() {
        Path configFile = FileUtils.getConfigDirectoryAsPath().resolve(ModInfo.MOD_ID).resolve(this.filename);

        if (Files.exists(configFile) && Files.isReadable(configFile)) {
            JsonElement element = JsonUtils.parseJsonFileAsPath(configFile);

            if (element != null && element.isJsonObject()) {
                JsonObject root = element.getAsJsonObject();
                this.loadJson(root);
            }
        } else {
            CjsahMasaExtension.LOGGER.error("initConfig(): Failed to load config file '{}'.", configFile.toAbsolutePath());
        }
    }

    public void save() {
        Path dir = FileUtils.getConfigDirectoryAsPath().resolve(ModInfo.MOD_ID);

        if (!Files.exists(dir)) {
            FileUtils.createDirectoriesIfMissing(dir);
        }

        if (Files.isDirectory(dir)) {
            JsonObject root = new JsonObject();

            this.saveJson(root);

            JsonUtils.writeJsonToFileAsPath(root, dir.resolve(this.filename));
        } else {
            CjsahMasaExtension.LOGGER.error("saveConfig(): Config Folder '{}' does not exist!", dir.toAbsolutePath());
        }
    }

    protected abstract void loadJson(JsonObject root);

    protected abstract void saveJson(JsonObject root);


    public static ConfigIO client() {
        return new ConfigClientIO();
    }

    public static ConfigIO server() {
        return new ConfigServerIO();
    }
}
