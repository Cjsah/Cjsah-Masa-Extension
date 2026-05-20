package net.cjsah.mod.masaextension.config;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.util.StringUtils;
import net.cjsah.mod.masaextension.ModInfo;

import java.util.List;
import java.util.Optional;

public enum ConfigTab {
    ALL("All"),
    GENERIC("Generic"),
    BUNGEE("Bungee");

    private final String name;
    private final String key;
    private final String translateKey;

    ConfigTab(String name) {
        this.name = name;
        this.key = name.toLowerCase();
        this.translateKey = ModInfo.MOD_ID + ".config.category." + this.key + ".name";
    }

    public List<IConfigBase> getConfigs() {
        return Optional.ofNullable(Configs.OPTIONS.get(this))
            .orElseGet(ImmutableList::of);
    }

    public String getName() {
        return this.name;
    }

    public String getKey() {
        return this.key;
    }

    public String getDisplayName() {
        return StringUtils.translate(this.translateKey);
    }

}
