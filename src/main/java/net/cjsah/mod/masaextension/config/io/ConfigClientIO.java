package net.cjsah.mod.masaextension.config.io;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigBase;
import net.cjsah.mod.masaextension.config.ConfigTab;
import net.cjsah.mod.masaextension.config.Configs;

import java.util.Map;

public class ConfigClientIO extends ConfigIO {

    protected ConfigClientIO() {
        super("client");
    }

    @Override
    protected void loadJson(JsonObject root) {
        for (Map.Entry<ConfigTab, ImmutableList<IConfigBase>> entry : Configs.OPTIONS.entrySet()) {
            ConfigTab key = entry.getKey();
            if (key == ConfigTab.ALL) continue;
            ConfigUtils.readConfigBase(root, key.getName(), entry.getValue());
        }
    }

    @Override
    protected void saveJson(JsonObject root) {
        for (Map.Entry<ConfigTab, ImmutableList<IConfigBase>> entry : Configs.OPTIONS.entrySet()) {
            ConfigTab key = entry.getKey();
            if (key == ConfigTab.ALL) continue;
            ConfigUtils.writeConfigBase(root, key.getName(), entry.getValue());
        }
    }

}
