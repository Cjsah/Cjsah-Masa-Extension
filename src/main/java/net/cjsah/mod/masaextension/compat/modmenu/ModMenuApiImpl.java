package net.cjsah.mod.masaextension.compat.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.cjsah.mod.masaextension.config.ConfigGui;

public class ModMenuApiImpl implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return screen -> {
            ConfigGui configGui = new ConfigGui();
            configGui.setParent(screen);
            return configGui;
        };
    }
}
